package Tuga.codegen;

import Tuga.semantic.SymbolTable;
import Tuga.semantic.Type;
import Tuga.semantic.TypeChecker;
import Tuga.semantic.symbols.FunctionSymbol;
import Tuga.semantic.symbols.Scope;
import Tuga.semantic.symbols.Symbol;
import Tuga.vm.ConstantPool;
import org.antlr.v4.runtime.tree.ParseTree;
import Tuga.parser.TugaBaseVisitor;
import Tuga.parser.TugaParser;
import Tuga.vm.OpCode;
import Tuga.vm.instruction.*;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.*;
import java.util.*;

/**
 * Gerador de bytecode para a maquina virtual.
 * Esta classe e responsavel pela ultima fase da compilacao, transformando
 * a arvore sintatica validada em codigo de bytes executavel pela maquina virtual.
 * O gerador visita a arvore sintatica e gera instrucoes correspondentes para
 * cada construcao da linguagem, incluindo:
 * - Declaracoes de variaveis e funcoes
 * - Expressoes aritmeticas e logicas
 * - Estruturas de controle de fluxo (condicionais e repeticoes)
 * - Chamadas de funcao e atribuicoes
 * Tambem gerencia a pool de constantes para valores literais (strings e numeros reais)
 * e mantem informacoes sobre variaveis locais e globais para geracao adequada de
 * instrucoes de acesso a memoria.
 */
public class BytecodeGenerator extends TugaBaseVisitor<Void> {
    // ---- Geracao de Codigo ----
    /** As instrucoes de bytecode geradas */
    private final ArrayList<Instruction> code = new ArrayList<>();
    /** Pool de constantes para valores reais e strings */
    private final ConstantPool constantPool;

    // ---- Informacoes de Simbolos e Tipos ----
    /** TypeChecker para determinar os tipos de expressoes */
    private final TypeChecker typeChecker;
    /** Tabela de simbolos para consultas de variaveis e funcoes */
    private final SymbolTable symbolTable;
    /** Cache para os tipos de expressoes */
    private final Map<ParseTree, Type> expressionTypes = new HashMap<>();

    // ---- Gestao de Memoria ----
    /** Mapeia nomes de variaveis globais para os seus enderecos */
    private final Map<String, Integer> variableAddress = new HashMap<>();
    /** Proximo endereco disponivel para variaveis globais */
    private int nextVarAddress = 0;
    /** Mapeia nomes de variaveis locais para os seus enderecos no ambito actual */
    private Map<String, Integer> currentLocalVars = new HashMap<>();
    /** Proximo endereco disponivel para variaveis locais */
    private int nextLocalAdrress = 2; // Comeca em 2 apos o ponteiro de frame e endereco de retorno

    // ---- Gestao de Funcoes ----
    /** Mapeia nomes de funcoes para os seus enderecos iniciais no bytecode */
    private final Map<String, Integer> functionAddresses = new HashMap<>();
    /** Armazena chamadas de funcao que necessitam de backpatching */
    private Map<String, List<Integer>> callsToBackatch = new HashMap<>();

    // ---- Rastreamento de Estado ----
    /** Flag que indica se estamos actualmente no ambito global */
    private boolean inGlobalScope = true;
    /** Nome da funcao que esta a ser processada actualmente */
    private String currentFunction = null;


    /**
     * Construtor da classe BytecodeGenerator.
     * Inicializa o gerador de bytecode com os componentes necessarios:
     * - typeChecker: usado para verificar e recuperar tipos de expressoes
     * - constantPool: pool de constantes para armazenar literais de string e valores reais
     * - symbolTable: tabela de simbolos com informacoes sobre variaveis e funcoes do programa
     *
     * @param typeChecker verificador de tipos que ja processou o programa
     * @param symbolTable tabela de simbolos com todas as declaracoes
     */
    public BytecodeGenerator(TypeChecker typeChecker, SymbolTable symbolTable){
        this.typeChecker = typeChecker;
        this.constantPool = new ConstantPool();
        this.symbolTable = symbolTable;
    }


    /**
     * Visita o no raiz do programa e gera o codigo bytecode correspondente.
     * A estrutura do bytecode gerado e:
     * 1. Alocacao de espaco para variaveis globais
     * 2. Chamada para a funcao principal
     * 3. Instrucao halt para encerrar o programa
     * 4. Definicoes de todas as funcoes do programa
     *
     * @param ctx O contexto do no programa
     * @return null (o metodo retorna Void)
     */
    @Override
    public Void visitProgram(TugaParser.ProgramContext ctx) {
        // Processa declaracoes de variaveis globais
        if (ctx.globals() != null){
            visit(ctx.globals());
        }

        // Reserva espaco para a chamada da funcao principal
        // O endereco real sera preenchido depois que todas as funcoes forem processadas
        int callToMainPos = code.size();
        emit(OpCode.call, 0);  // Placeholder - sera substituido pelo endereco real

        // Adiciona instrucao para encerrar o programa apos a execucao da funcao principal
        emit(OpCode.halt);

        // Processa todas as funcoes e registra os seus enderecos iniciais
        for (TugaParser.FunctionDeclContext func : ctx.functionDecl()){
            String funcName = func.IDENTIFIER().getText();
            functionAddresses.put(funcName, code.size());  // Armazena a posicao inicial
            visit(func);  // Gera o codigo para a funcao
        }

        // Substitui o placeholder com o endereco real da funcao principal
        if (functionAddresses.containsKey("principal")){
            ((Instruction1Arg)code.get(callToMainPos)).setArg(functionAddresses.get("principal"));
        } else {
            throw new RuntimeException("Funcao 'principal' nao encontrada");
        }

        // Resolve todas as chamadas de funcao adiadas (backpatching)
        backpatchFunctionCalls();

        return null;
    }


    /**
     * Gera codigo bytecode para instrucao de escrita (escreve).
     * Avalia a expressao a ser impressa e emite o opcode adequado
     * com base no tipo da expressao.
     *
     * @param ctx O contexto da instrucao de escrita
     * @return null (o metodo retorna Void)
     */
    @Override
    public Void visitWriteInstr(TugaParser.WriteInstrContext ctx){
        // Gera o codigo para a expressao e coloca o valor na pilha
        visit(ctx.expression());

        // Determina o tipo da expressao
        Type exprType = getExpressionType(ctx.expression());

        // Emite o opcode apropriado para impressao com base no tipo
        if (exprType != null) {
            switch (exprType){
                case INTEGER -> emit(OpCode.iprint);
                case REAL -> emit(OpCode.dprint);
                case BOOLEAN -> emit(OpCode.bprint);
                case STRING -> emit(OpCode.sprint);
                default -> throw new RuntimeException("Nao e possivel imprimir valor do tipo: " + exprType);
            }
        } else {
            throw new RuntimeException("Nao foi possivel determinar o tipo da expressao a ser impressa");
        }

        return null;
    }


    /**
     * Processa declaracoes de variaveis, emitindo instrucoes de bytecode e
     * registrando as variaveis nos escopos apropriados.
     * Para variaveis globais:
     * - Emite instrucao galloc para alocar espaco na memoria global
     * - Registra cada variavel com o seu endereco global
     * Para variaveis locais:
     * - Registra cada variavel no mapa de variaveis locais
     * - Atribui endereco local relativo ao frame atual
     * - Incrementa o contador de enderecos locais para futuras variaveis
     *
     * @param ctx O contexto das declaracoes a serem processadas
     * @return null (o metodo retorna Void)
     */
    @Override
    public Void visitDeclarations(TugaParser.DeclarationsContext ctx) {
        for (TugaParser.DeclarationContext decl : ctx.declaration()){
            if (inGlobalScope){
                // Variaveis globais
                int varCount = decl.variableList().IDENTIFIER().size();
                if (varCount > 0){
                    emit(OpCode.galloc, varCount);

                    for (TerminalNode id : decl.variableList().IDENTIFIER()){
                        registerVariable(id.getText());
                    }
                }
            }else {
                // Variaveis locais
                int localVarsCount = nextLocalAdrress;

                for (TerminalNode id : decl.variableList().IDENTIFIER()){
                    String varName = id.getText();
                    currentLocalVars.put(varName, nextLocalAdrress++);
                }

                int varCount = nextLocalAdrress - localVarsCount;

                if (varCount > 0){
                    emit(OpCode.lalloc, varCount);
                }
            }
        }

        return null;
    }


    /**
     * Gera codigo bytecode para instrucoes de atribuicao (varname <- expression).
     * O metodo segue os seguintes passos:
     * 1. Processa a expressao do lado direito, deixando o seu valor no topo da pilha
     * 2. Localiza o endereco da variavel de destino na memoria
     * 3. Emite a instrucao de armazenamento apropriada (lstore ou gstore)
     * Se a variavel nao foi encontrada nos mapas de endereco atuais, tenta
     * localiza-la na tabela de simbolos. Se encontrada, aloca um novo endereco
     * para ela no escopo apropriado.
     *
     * @param ctx O contexto da instrucao de atribuicao
     * @return null (o metodo retorna Void)
     * @throws RuntimeException se a variavel nao for encontrada
     */
    @Override
    public Void visitAssignInstr(TugaParser.AssignInstrContext ctx) {
        // Processa a primeira expressao
        visit(ctx.expression());

        String varName = ctx.IDENTIFIER().getText();

        Integer varAddress = lookupVariable(varName);

        if (varAddress == null) {
            Symbol symbol = symbolTable.lookupSymbol(varName);
            if (symbol == null) {
                throw new RuntimeException("Variavel nao encontrada: " + varName);
            }

            if (!inGlobalScope) {
                varAddress = nextLocalAdrress++;
                currentLocalVars.put(varName, varAddress);
            } else {
                varAddress = getVariableAddress(varName);
            }
        }

        if (varAddress < 0 || (!inGlobalScope && currentLocalVars.containsKey(varName))) {
            emit(OpCode.lstore, varAddress);
        } else {
            emit(OpCode.gstore, varAddress);
        }

        return null;
    }


    /**
     * Processa instrucoes vazias (apenas um ponto-e-virgula).
     * Este metodo nao gera nenhum bytecode, pois instrucoes vazias
     * nao possuem comportamento em tempo de execucao.
     *
     * @param ctx O contexto da instrucao vazia
     * @return null (o metodo retorna Void)
     */
    @Override
    public Void visitEmptyInstr(TugaParser.EmptyInstrContext ctx){
        return null;
    }


    /**
     * Processa instrucoes de bloco delegando para o metodo visitBlock.
     * Esta e uma funcao intermediaria que simplesmente passa o controle
     * para o metodo que processa o conteudo do bloco.
     *
     * @param ctx O contexto da instrucao de bloco
     * @return null (o metodo retorna Void)
     */
    @Override
    public Void visitBlockInstr(TugaParser.BlockInstrContext ctx){
        return visitBlock(ctx.block());
    }

    /**
     * Gera codigo bytecode para um bloco de instrucoes.
     * Este metodo processa blocos de codigo que podem conter declaracoes de variaveis
     * e instrucoes, gerenciando o escopo de forma adequada. O metodo:
     * 1. Salva o estado atual das variaveis locais
     * 2. Cria um novo escopo na tabela de simbolos (exceto para blocos de funcao)
     * 3. Processa declaracoes de variaveis locais do bloco
     * 4. Aloca memoria para as variaveis locais
     * 5. Processa as instrucoes do bloco
     * 6. Deteta instrucoes de retorno e interrompe o processamento quando encontradas
     * 7. Libera a memoria alocada para variaveis locais (se necessario)
     * 8. Restaura o escopo anterior (exceto para blocos de funcao)
     *
     * @param ctx O contexto do bloco a ser processado
     * @return null (o metodo retorna Void)
     */
    @Override
    public Void visitBlock(TugaParser.BlockContext ctx){
        boolean isFunctionBlock = ctx.getParent() instanceof TugaParser.FunctionDeclContext;

        Map<String, Integer> savedLocalVars =  new HashMap<>(currentLocalVars);
        int savedNextLocalAddr = nextLocalAdrress;

        if (!isFunctionBlock){
            String blockId = "bloco_" + ctx.hashCode() + "_" + ctx.getStart().getLine();
            symbolTable.enterScope(blockId);
        }

        if (ctx.declarations() != null){
            visit(ctx.declarations());
        }

        int blockVarCount = nextLocalAdrress - savedNextLocalAddr;

        boolean hasReturn = false;

        for (TugaParser.InstructionContext instr : ctx.instruction()){
            visit(instr);

            if (containsReturn(instr)){
                hasReturn = true;
                break;
            }
        }

        if (!isFunctionBlock && blockVarCount > 0 && !hasReturn){
            emit(OpCode.pop, blockVarCount);
        }

        if (!isFunctionBlock){
            symbolTable.exitScope();
            currentLocalVars = savedLocalVars;
            nextLocalAdrress = savedNextLocalAddr;
        }

        return null;
    }

    /**
     * Analisa recursivamente se uma instrucao, ou qualquer das suas instrucoes internas,
     * contem um comando 'retorna' explicitamente declarado.
     * Este metodo permite determinar se um bloco de codigo possui uma instrucao de retorno,
     * realizando uma analise recursiva da estrutura sintatica. Verifica:
     * 1. Se a instrucao e diretamente um comando de retorno
     * 2. Se a instrucao e um bloco que contem um comando de retorno
     * 3. Se a instrucao e uma estrutura condicional (se-senao) com retorno em algum ramo
     * Esta funcionalidade e utilizada para determinar se e necessario emitir codigo
     * para libertar espaco de variaveis locais apos blocos de codigo.
     *
     * @param ctx O contexto da instrucao a ser analisada
     * @return verdadeiro se a instrucao contiver um comando de retorno, falso caso contrario
     */
    private boolean containsReturn(TugaParser.InstructionContext ctx){
        if (ctx instanceof TugaParser.ReturnInstrContext){
            return true;
        }

        if (ctx instanceof TugaParser.BlockInstrContext){
            TugaParser.BlockContext blockCtx = ((TugaParser.BlockInstrContext)ctx).block();
            for (TugaParser.InstructionContext instr : blockCtx.instruction()){
                if (containsReturn(instr)){
                    return true;
                }
            }
        }

        if (ctx instanceof TugaParser.IfElseInstrContext){
            TugaParser.IfElseInstrContext ifCtx = (TugaParser.IfElseInstrContext) ctx;
            return containsReturn(ifCtx.instruction(0)) || (ifCtx.instruction().size() > 1 && containsReturn(ifCtx.instruction(1)));
        }

        return false;
    }

    @Override
    public Void visitWhileInstr(TugaParser.WhileInstrContext ctx){
        int startLabel = code.size();

        // Gera codigo para a condicao
        visit(ctx.expression());

        // Posicao onde ficara o jumpf
        int jumpfPos = code.size();
        emit(OpCode.jumpf, 0); // Placeholder, sera ajustado depois

        // Gera o codigo para o corpo do loop
        visit(ctx.instruction());

        // Salto incondicional de volta ao inicio
        emit(OpCode.jump, startLabel);

        // Atualiza o jumpf para saltar para aqui (final loop)
        int endPos = code.size();
        ((Instruction1Arg)code.get(jumpfPos)).setArg(endPos);

        return null;
    }



    /**
     * Gera codigo bytecode para instrucoes condicionais (se-senao).
     * Este metodo implementa a logica de controlo de fluxo para instrucoes
     * condicionais, seguindo os seguintes passos:
     * 1. Avalia a expressao condicional, colocando o resultado na pilha
     * 2. Emite um salto condicional (jumpf) para o caso da condicao ser falsa
     * 3. Gera o codigo para o bloco "se" (primeira instrucao)
     * 4. Trata o bloco "senao" caso exista:
     *    - Emite um salto incondicional para evitar a execucao do bloco "senao"
     *    - Ajusta o salto condicional para apontar para o inicio do bloco "senao"
     *    - Gera o codigo para o bloco "senao" (segunda instrucao)
     *    - Ajusta o salto incondicional para apontar para o fim da estrutura
     * 5. Se nao existir bloco "senao", ajusta o salto condicional para apontar
     *    para o fim da estrutura
     *
     * @param ctx O contexto da instrucao condicional
     * @return null (o metodo retorna Void)
     */
    @Override
    public Void visitIfElseInstr(TugaParser.IfElseInstrContext ctx){
        visit(ctx.expression());

        int jumpfPos = code.size();
        emit(OpCode.jumpf, 0);

        // Gera codigo para o bloco if
        visit(ctx.instruction(0));

        // Se tiver else
        if (ctx.instruction().size() > 1){
            int jumpPos = code.size();
            emit(OpCode.jump, 0);

            // Atualiza o jumpf para saltar para o inicio do else
            int elsePos = code.size();
            ((Instruction1Arg)code.get(jumpfPos)).setArg(elsePos);

            visit(ctx.instruction(1));

            int endPos = code.size();
            ((Instruction1Arg)code.get(jumpPos)).setArg(endPos);
        }else {
            // Se nao tive else, atualiza o jumpf para saltar para aqui
            int endPos = code.size();
            ((Instruction1Arg)code.get(jumpfPos)).setArg(endPos);
        }

        return null;
    }


    /**
     * Processa expressoes literais, delegando para o metodo apropriado
     * com base no tipo do literal.
     *
     * @param ctx O contexto da expressao literal
     * @return null (o metodo retorna Void)
     */
    @Override
    public Void visitLiteralExpr(TugaParser.LiteralExprContext ctx) {
        return visit(ctx.literal());
    }

    /**
     * Gera codigo bytecode para literais inteiros.
     *
     * @param ctx O contexto do literal inteiro
     * @return null (o metodo retorna Void)
     */
    @Override
    public Void visitIntLiteral(TugaParser.IntLiteralContext ctx) {
        int value = Integer.parseInt(ctx.INTEGER().getText());
        emit(OpCode.iconst, value);
        return null;
    }

    /**
     * Gera codigo bytecode para literais reais.
     *
     * @param ctx O contexto do literal real
     * @return null (o metodo retorna Void)
     */
    @Override
    public Void visitRealLiteral(TugaParser.RealLiteralContext ctx) {
        double value = Double.parseDouble(ctx.REAL().getText());
        int index = addRealConstant(value);
        emit(OpCode.dconst, index);
        return null;
    }

    /**
     * Gera codigo bytecode para string.
     * Remove as aspas da string, adiciona-a a constant pool e carrega-a na pilha.
     *
     * @param ctx O contexto do literal string
     * @return null (o metodo retorna Void)
     */
    @Override
    public Void visitStringLiteral(TugaParser.StringLiteralContext ctx) {
        // Remove as aspas da string
        String text = ctx.STRING().getText();
        String value = text.substring(1, text.length() - 1);

        int index = addStringConstant(value);
        emit(OpCode.sconst, index);
        return null;
    }

    /**
     * Gera codigo bytecode para literais booleanos.
     * Carrega true ou false na pilha com base no token presente.
     *
     * @param ctx O contexto do literal booleano
     * @return null (o metodo retorna Void)
     */
    @Override
    public Void visitBoolLiteral(TugaParser.BoolLiteralContext ctx) {
        boolean value = ctx.VERDADEIRO() != null;
        if (value) {
            emit(OpCode.tconst);
        } else {
            emit(OpCode.fconst);
        }
        return null;
    }

    /**
     * Processa expressoes entre parenteses.
     *
     * @param ctx O contexto da expressao entre parenteses
     * @return null (o metodo retorna Void)
     */
    @Override
    public Void visitParenExpr(TugaParser.ParenExprContext ctx) {
        return visit(ctx.expression());
    }

    /**
     * Gera codigo bytecode para expressoes unarias (negacao e operador 'nao').
     * Avalia a expressao e aplica o operador unario apropriado com base no tipo.
     *
     * @param ctx O contexto da expressao unaria
     * @return null (o metodo retorna Void)
     * @throws RuntimeException se o operador for incompativel com o tipo da expressao
     */
    @Override
    public Void visitUnaryExpr(TugaParser.UnaryExprContext ctx) {
        String op = ctx.op.getText();
        Type exprType = getExpressionType(ctx.expression());

        visit(ctx.expression());

        switch (op){
            case "-" -> {
                if (exprType == Type.INTEGER){
                    emit(OpCode.iuminus);
                } else if (exprType == Type.REAL) {
                    emit(OpCode.duminus);
                }else {
                    throw new RuntimeException("Operador '-' nao pode ser aplicado a " + exprType);
                }
            }
            case "nao" -> {
                if (exprType == Type.BOOLEAN){
                    emit(OpCode.not);
                }else {
                    throw new RuntimeException("Operador unario desconhecido: " + op);
                }
            }
        }

        return null;
    }

    /**
     * Gera codigo bytecode para expressoes binarias.
     * Suporta operacoes aritmeticas e concatenacao de strings.
     * Para operacoes entre tipos diferentes (como inteiro e real),
     * realiza as conversoes necessarias automaticamente.
     *
     * @param ctx O contexto da expressao binaria
     * @return null (o metodo retorna Void)
     * @throws RuntimeException se a operacao for incompativel com os tipos dos operandos
     */
    @Override
    public Void visitBinaryExpr(TugaParser.BinaryExprContext ctx) {
        Type leftType = getExpressionType(ctx.expression(0));
        Type rightType = getExpressionType(ctx.expression(1));
        Type resultType = getExpressionType(ctx);
        String op = ctx.op.getText();

        // Operadores aritmeticos
        if ("+".equals(op) || "-".equals(op) || "*".equals(op) || "/".equals(op)){
            // Caso especial: + com string (concatenacao)

            if ("+".equals(op) && (leftType == Type.STRING || rightType == Type.STRING)){

                // Gera codigo para ambos os lados
                visit(ctx.expression(0));

                // Converter para string necessario
                if (leftType != Type.STRING && leftType !=  null){
                    convertToString(leftType);
                }

                visit(ctx.expression(1));

                if (rightType != Type.STRING && rightType != null){
                    convertToString(rightType);
                }

                emit(OpCode.sconcat);

                return null;
            }

            // Operacoes numericas normais
            if (resultType == Type.INTEGER){

                visit(ctx.expression(0));
                visit(ctx.expression(1));

                switch (op){
                    case "+" -> emit(OpCode.iadd);
                    case "-" -> emit(OpCode.isub);
                    case "*" -> emit(OpCode.imult);
                    case "/" -> emit(OpCode.idiv);
                }
            } else if (resultType == Type.REAL) {
                // Gera o codigo para o lado esquerdo
                visit(ctx.expression(0));

                // Converter para real se necessario
                if (leftType == Type.INTEGER){
                    emit(OpCode.itod);
                }

                visit(ctx.expression(1));
                if (rightType == Type.INTEGER){
                    emit(OpCode.itod);
                }

                switch (op){
                    case "+" -> emit(OpCode.dadd);
                    case "-" -> emit(OpCode.dsub);
                    case "*" -> emit(OpCode.dmult);
                    case "/" -> emit(OpCode.ddiv);
                }
            }
        } else if ("%".equals(op)) {
            // Mod so funciona com inteiros
            if (leftType == Type.INTEGER && rightType == Type.INTEGER){
                // Gera o codigo para ambos os lados
                visit(ctx.expression(0));
                visit(ctx.expression(1));

                emit(OpCode.imod);
            }else {
                throw new RuntimeException("Operador '%' so pode ser aplicado entre inteiros");
            }
        }

        return null;
    }

    /**
     * Gera codigo bytecode para expressoes de comparacao (<, <=, >, >=).
     * Avalia os operandos e aplica o operador de comparacao apropriado.
     * Para comparacoes entre inteiros e reais, realiza as conversoes necessarias.
     *
     * @param ctx O contexto da expressao de comparacao
     * @return null (o metodo retorna Void)
     * @throws RuntimeException se a comparacao nao for possivel entre os tipos dados
     */
    @Override
    public Void visitComparisonExpr(TugaParser.ComparisonExprContext ctx) {
        String op = ctx.op.getText();
        Type leftType = getExpressionType(ctx.expression(0));
        Type rightType = getExpressionType(ctx.expression(1));

        if ((leftType == Type.INTEGER || leftType == Type.REAL) && (rightType == Type.INTEGER || rightType == Type.REAL)){
            // Para ">" e ">=", invertemos a logica para usar "<" e "<="
            boolean invertOperands = op.equals(">") || op.equals(">=");

            // Determinar se e necessario converter para real
            boolean needRealComparison = leftType == Type.REAL || rightType == Type.REAL;

            if (invertOperands){
                // Para ">" e ">=", geramos primeiro o lado DIREITO
                visit(ctx.expression(1));

                // Converter imediatamente se necessario
                if (needRealComparison && rightType == Type.INTEGER){
                    emit(OpCode.itod);
                }

                // Depois geramos o lado ESQUERDO
                visit(ctx.expression(0));

                // Converter imediatamente se necessario
                if (needRealComparison && leftType == Type.INTEGER){
                    emit(OpCode.itod);
                }
            }else {
                // Para "<" e "<=", mantemos a ordem normal
                visit(ctx.expression(0));

                // Converter imediatamente se necessario
                if (needRealComparison && leftType == Type.INTEGER){
                    emit(OpCode.itod);
                }

                visit(ctx.expression(1));

                // Converter imediatamente se necessario
                if (needRealComparison && rightType == Type.INTEGER){
                    emit(OpCode.itod);
                }

            }

            // Operador de comparacao
            if (needRealComparison){
                switch (op){
                    case "<", ">" -> emit(OpCode.dlt);
                    case "<=", ">=" -> emit(OpCode.dleq);
                }
            }else {
                // Pelo menos um e real
                switch (op){
                    case "<", ">" -> emit(OpCode.ilt);
                    case "<=", ">=" -> emit(OpCode.ileq);
                }
            }
        } else {
            throw new RuntimeException("Operadores de comparacao so podem ser aplicador entre valor numericos");
        }

        return null;
    }

    /**
     * Gera codigo bytecode para expressoes de igualdade (igual, diferente).
     * Suporta comparacoes entre tipos numericos, strings e booleanos.
     * Realiza conversoes de tipo quando necessario.
     *
     * @param ctx O contexto da expressao de igualdade
     * @return null (o metodo retorna Void)
     * @throws RuntimeException se a comparacao nao for possivel entre os tipos dados
     */
    @Override
    public Void visitEqualityExpr(TugaParser.EqualityExprContext ctx) {
        String op = ctx.op.getText();
        Type leftType = getExpressionType(ctx.expression(0));
        Type rightType = getExpressionType(ctx.expression(1));

        // Operador de igualdade numericas
        if ((leftType == Type.INTEGER || leftType == Type.REAL) && (rightType == Type.INTEGER || rightType == Type.REAL)){

            // Determinar se precisamos de comparacao de reais
            boolean needRealComparison = leftType == Type.REAL || rightType == Type.REAL;

            visit(ctx.expression(0));

            if (needRealComparison && leftType == Type.INTEGER){
                emit(OpCode.itod);
            }

            visit(ctx.expression(1));

            if (needRealComparison && rightType == Type.INTEGER){
                emit(OpCode.itod);
            }

            // Emitir a operacao de igualdade adequada
            if (needRealComparison){
                if ("igual".equals(op)) emit(OpCode.deq);
                else emit(OpCode.dneq);
            }else {
                if ("igual".equals(op)) emit(OpCode.ieq);
                else emit(OpCode.ineq);
            }
        } else if (leftType == Type.STRING && rightType == Type.STRING) {
            visit(ctx.expression(0));
            visit(ctx.expression(1));

            if ("igual".equals(op)) emit(OpCode.seq);
            else emit(OpCode.sneq);
        } else if (leftType == Type.BOOLEAN && rightType == Type.BOOLEAN) {
            visit(ctx.expression(0));
            visit(ctx.expression(1));

            if ("igual".equals(op)) emit(OpCode.beq);
            else emit(OpCode.bneq);
        } else {
            throw new RuntimeException("Operadores de igualdade nao podem ser aplicados entre "+ leftType + " e " + rightType);
        }

        return null;
    }

    /**
     * Gera codigo bytecode para expressoes logicas 'e' (AND).
     * Avalia ambos os operandos e aplica o operador logico 'e'.
     *
     * @param ctx O contexto da expressao AND
     * @return null (o metodo retorna Void)
     * @throws RuntimeException se os operandos nao forem valores booleanos
     */
    @Override
    public Void visitAndExpr(TugaParser.AndExprContext ctx) {
        visit(ctx.expression(0));
        visit(ctx.expression(1));

        Type leftType = getExpressionType(ctx.expression(0));
        Type rightType = getExpressionType(ctx.expression(1));

        if (leftType == Type.BOOLEAN && rightType == Type.BOOLEAN) {
            emit(OpCode.and);
        } else {
            throw new RuntimeException("Operador 'e' so pode ser aplicado entre valores booleanos");
        }

        return null;
    }

    /**
     * Gera codigo bytecode para expressoes logicas 'ou' (OR).
     * Avalia ambos os operandos e aplica o operador logico 'ou'.
     *
     * @param ctx O contexto da expressao OR
     * @return null (o metodo retorna Void)
     * @throws RuntimeException se os operandos nao forem valores booleanos
     */
    @Override
    public Void visitOrExpr(TugaParser.OrExprContext ctx) {
        visit(ctx.expression(0));
        visit(ctx.expression(1));

        Type leftType = getExpressionType(ctx.expression(0));
        Type rightType = getExpressionType(ctx.expression(1));

        if (leftType == Type.BOOLEAN && rightType == Type.BOOLEAN) {
            emit(OpCode.or);
        } else {
            throw new RuntimeException("Operador 'ou' so pode ser aplicado entre valores booleanos");
        }

        return null;
    }

    /**
     * Gera codigo bytecode para declaracoes de funcao.
     * Processa os parametros, variaveis locais e instrucoes do corpo da funcao.
     * Tambem emite instrucoes para gestao da pilha de chamadas e retorno.
     *
     * @param ctx O contexto da declaracao de funcao
     * @return null (o metodo retorna Void)
     */
    @Override
    public Void visitFunctionDecl(TugaParser.FunctionDeclContext ctx){
        String funcName = ctx.IDENTIFIER().getText();

        Scope oldScope = symbolTable.getCurrentScope();

        symbolTable.restoreScope(funcName);

        boolean returnsValue = ctx.type() != null;

        // Salvar o estado atual
        Map<String, Integer> savedLocalVars = currentLocalVars;
        int savedNextLocalAddress = nextLocalAdrress;
        boolean savedInGlobalScope = inGlobalScope;
        String savedCurrentFunction = currentFunction;

        // Configurar o novo estado
        currentLocalVars = new HashMap<>();
        nextLocalAdrress = 2;
        inGlobalScope = false;
        currentFunction = funcName;

        // Registrar o endereco da funcao
        int functionAdress = code.size();
        functionAddresses.put(funcName, functionAdress);

        // Processar parametros
        int paramCount = 0;

        if (ctx.paramList() != null){
            paramCount = ctx.paramList().param().size();
            for (TugaParser.ParamContext param : ctx.paramList().param()){
                String paramName = param.IDENTIFIER().getText();

                // Parametros sao armazenados em posicoes negativas relativas ao FP
                int paramOffset = -(paramCount);
                currentLocalVars.put(paramName, paramOffset);
                paramCount--;
            }
        }

        // Declaracoes de variaveis locais do bloco
        if (ctx.block().declarations() != null){
            visit(ctx.block().declarations());
        }

        // Alcoar espaco para variaveis locais

        int localVarCount = nextLocalAdrress - 2;

        // Processar instrucoes do corpo da funcao
        for (TugaParser.InstructionContext instr : ctx.block().instruction()){
            visit(instr);
        }

        // Se a funcao nao tiver returno explicito e for do tipo void, adicionar return implicito
        if (!returnsValue){
            int localVarsCount = nextLocalAdrress - 2;
            if (localVarsCount > 0){
                emit(OpCode.pop, localVarCount);
            }
            int returnParamCount = 0;
            for (Integer value : currentLocalVars.values()){
                if (value < 0){
                    returnParamCount++;
                }
            }
            emit(OpCode.ret, returnParamCount);
        }

        // Restaurar o estado anterior
        currentLocalVars = savedLocalVars;
        nextLocalAdrress = savedNextLocalAddress;
        inGlobalScope = savedInGlobalScope;
        currentFunction = savedCurrentFunction;
        symbolTable.setCurrentScope(oldScope);

        return null;
    }

    /**
     * Gera codigo bytecode para instrucoes de retorno.
     * Processa a expressao de retorno (se existir) e emite a instrucao
     * de retorno apropriada (retval ou ret).
     *
     * @param ctx O contexto da instrucao de retorno
     * @return null (o metodo retorna Void)
     * @throws RuntimeException se a instrucao de retorno estiver fora de uma funcao
     */
    @Override
    public Void visitReturnInstr(TugaParser.ReturnInstrContext ctx){
        if (inGlobalScope){
            throw new RuntimeException("'retorna' fora de funcao");
        }

        int paramCount = 0;
        for (Integer value : currentLocalVars.values()){
            if (value < 0){
                paramCount++;
            }
        }

        // Processar a expressao de retorno se houver
        if (ctx.expression() != null){
            visit(ctx.expression());

            Symbol funcSymbol = symbolTable.lookupSymbol(currentFunction);
            if (funcSymbol instanceof FunctionSymbol){
                Type returnType = funcSymbol.type;
                Type exprType = getExpressionType(ctx.expression());

                if (returnType != exprType){
                    if (returnType == Type.REAL && exprType == Type.INTEGER){
                        emit(OpCode.itod);
                    }else if (returnType == Type.STRING && exprType != null){
                        convertToString(exprType);
                    }else {
                        throw new RuntimeException("Nao e possivel converter " +exprType + " para " + returnType + " no retorno de funcao " + currentFunction);
                    }
                }

            }
            emit(OpCode.retval, paramCount);
        } else {
            emit(OpCode.ret, paramCount);
        }

        return null;
    }

    /**
     * Processa chamadas de funcao quando usadas em expressoes.
     * Simplesmente delega para o metodo visitFunctionCall.
     *
     * @param ctx O contexto da expressao de chamada de funcao
     * @return null (o metodo retorna Void)
     */
    @Override
    public Void visitFunctionalCallExpr(TugaParser.FunctionalCallExprContext ctx) {
        return visit(ctx.functionCall());
    }

    /**
     * Processa chamadas de funcao quando usadas como instrucoes.
     * Simplesmente delega para o metodo visitFunctionCall.
     *
     * @param ctx O contexto da instrucao de chamada de funcao
     * @return null (o metodo retorna Void)
     */
    @Override
    public Void visitFunctionCallInstr(TugaParser.FunctionCallInstrContext ctx){
        return visit(ctx.functionCall());
    }

    /**
     * Gera codigo bytecode para chamadas de funcao.
     * Empilha os argumentos, emite a instrucao de chamada e
     * opcionalmente descarta o valor de retorno se a chamada for
     * usada como uma instrucao.
     *
     * @param ctx O contexto da chamada de funcao
     * @return null (o metodo retorna Void)
     * @throws RuntimeException se a funcao nao estiver declarada
     */
    @Override
    public Void visitFunctionCall(TugaParser.FunctionCallContext ctx){
        String funcName = ctx.IDENTIFIER().getText();

        Scope oldScope = symbolTable.getCurrentScope();

        // Verifica se existe no symbolTable
        Symbol funcSymbol = symbolTable.lookupSymbol(funcName);
        if (!(funcSymbol instanceof FunctionSymbol functionSymbol)) {
            throw new RuntimeException("Function not declared: " + funcName);
        }

        List<? extends Symbol> parameters = functionSymbol.getParameters();

        if (ctx.exprList() != null){
            List<TugaParser.ExpressionContext> arguments = ctx.exprList().expression();

            for (int i = 0; i < arguments.size(); i++){
                TugaParser.ExpressionContext expr = arguments.get(i);

                visit(expr);

                Type paramType = parameters.get(i).type;
                Type argType = getExpressionType(expr);

                if (paramType != argType){
                    if (paramType == Type.REAL && argType == Type.INTEGER){
                        emit(OpCode.itod);
                    }else if (paramType == Type.STRING && argType != null){
                        convertToString(argType);
                    }else {
                        throw new RuntimeException("Nao e possivel converter " + argType + " para " +
                                paramType + " no argumento " + (i+1) + " da funcao " + funcName
                        );
                    }
                }
            }
        }

        symbolTable.setCurrentScope(functionSymbol.scope);

        int callPos = code.size();

        if (functionAddresses.containsKey(funcName) && functionAddresses.get(funcName) >= 0){
            emit(OpCode.call, functionAddresses.get(funcName));
        }else {
            emit(OpCode.call, 0);

            if (callsToBackatch == null) {
                callsToBackatch = new HashMap<>();
            }

            if (!callsToBackatch.containsKey(funcName)) {
                callsToBackatch.put(funcName, new ArrayList<>());
            }

            callsToBackatch.get(funcName).add(callPos);
        }

        // Para chamadas de funcao usadas como instrucoes, descarta o valor de retorno se existir
        if (ctx.parent instanceof TugaParser.FunctionCallInstrContext) {
            Type returnType = typeChecker.visit(ctx);
            if (returnType != null && returnType != Type.VOID) {
                emit(OpCode.pop, 1); // Descarta o valor de rotorno
            }
        }

        // Restora o escopo original
        symbolTable.setCurrentScope(oldScope);

        return null;
    }

    /**
     * Gera codigo bytecode para referenciar uma variavel.
     * Carrega o valor da variavel na pilha a partir do endereco
     * local ou global, conforme apropriado.
     *
     * @param ctx O contexto da expressao de variavel
     * @return null (o metodo retorna Void)
     * @throws RuntimeException se a variavel nao for encontrada
     */
    @Override
    public Void visitVarExpr(TugaParser.VarExprContext ctx){
        String varName = ctx.IDENTIFIER().getText();

        if (!inGlobalScope && currentLocalVars.containsKey(varName)){
            int address = currentLocalVars.get(varName);
            emit(OpCode.lload, address);
            return null;
        }

        Symbol symbol = symbolTable.lookupSymbol(varName);

        if (symbol == null){
            throw new RuntimeException("Variavel nao encontrada");
        }

        Type varTpe = symbol.type;
        System.out.println("DEBUG: VarType: " + varTpe);


        if (!inGlobalScope){
            int address = nextVarAddress++;
            currentLocalVars.put(varName,address);
            emit(OpCode.lload, address);
        }else {
            int address = getVariableAddress(varName);
            emit(OpCode.gload, address);
        }

        return null;
    }


    /*
     * Utilitarios para gerar codigo
     */

    /**
     * Converte o valor no topo da pilha para string.
     * Esta funcao utilitaria emite o opcode de conversao apropriado
     * com base no tipo do valor.
     *
     * @param type O tipo do valor a ser convertido para string
     */
    private void convertToString(Type type) {
        switch (type) {
            case INTEGER -> emit(OpCode.itos);
            case REAL -> emit(OpCode.dtos);
            case BOOLEAN -> emit(OpCode.btos);
            // String nao precisa de conversao
        }
    }

    /**
     * Obtem o tipo de uma expressao.
     * Primeiro tenta obter o tipo a partir do contexto da expressao,
     * depois tenta inferir a partir da estrutura sintatica e, como ultimo
     * recurso, consulta o TypeChecker.
     *
     * @param ctx O contexto da expressao
     * @return O tipo da expressao, ou null se nao for possivel determinar
     */
    private Type getExpressionType(ParseTree ctx) {
        if (ctx instanceof TugaParser.VarExprContext){
            String varName = ctx.getText();
            Symbol symbol = symbolTable.lookupSymbol(varName);

            if (symbol != null){
                return symbol.type;
            }

        } else if (ctx instanceof TugaParser.LiteralExprContext) {
            TugaParser.LiteralContext literalContext = ((TugaParser.LiteralExprContext) ctx).literal();

            if (literalContext instanceof TugaParser.IntLiteralContext){
                return Type.INTEGER;
            } else if (literalContext instanceof TugaParser.RealLiteralContext) {
                return Type.REAL;
            } else if (literalContext instanceof TugaParser.StringLiteralContext) {
                return Type.STRING;
            } else if (literalContext instanceof TugaParser.BoolLiteralContext) {
                return Type.BOOLEAN;
            }
        }

        try {
            return typeChecker.visit(ctx);
        }catch (Exception e){
            System.out.println("ERRO: Falha ao determinar tipo via TypeChecker: " + e.getMessage());
        }

        return null;
    }

    /**
     * Realiza o backpatching de todas as chamadas de funcao.
     * Este metodo e chamado apos todas as funcoes terem sido processadas
     * para resolver as referencias cruzadas entre funcoes.
     *
     * @throws RuntimeException se uma funcao for referenciada mas nao declarada
     */
    private void backpatchFunctionCalls(){
        for (Map.Entry<String, List<Integer>> entry : callsToBackatch.entrySet()){
            String funcName = entry.getKey();
            if (!functionAddresses.containsKey(funcName)){
                throw new RuntimeException("Function referenced but not declared: " + funcName);
            }

            int actualAdrress = functionAddresses.get(funcName);
            for (int callPos : entry.getValue()){
                ((Instruction1Arg)code.get(callPos)).setArg(actualAdrress);
            }
        }
    }


    /**
     * Adiciona uma instrucao sem argumentos ao codigo gerado.
     *
     * @param opcode O opcode da instrucao
     */
    private void emit(OpCode opcode) {
        code.add(new Instruction(opcode));
    }

    /**
     * Adiciona uma instrucao com um argumento ao codigo gerado.
     *
     * @param opcode O opcode da instrucao
     * @param arg O argumento da instrucao
     */
    private void emit(OpCode opcode, int arg) {
        code.add(new Instruction1Arg(opcode, arg));
    }

    /**
     * Adiciona um valor real a constant pool.
     *
     * @param value O valor real a ser adicionado
     * @return O indice do valor na constant pool
     */
    private int addRealConstant(double value) {
        return constantPool.addReal(value);
    }

    /**
     * Adiciona uma string a constant pool.
     *
     * @param value A string a ser adicionada
     * @return O indice da string na constant pool
     */
    private int addStringConstant(String value) {
        return constantPool.addConstant(value);
    }

    /**
     * Exibe o codigo gerado em formato textual com instrucoes "assembly".
     * Util para depuracao do codigo gerado.
     */
    public void dumpCode() {
        for (int i = 0; i < code.size(); i++) {
            System.out.println(i + ": " + code.get(i).toString().toLowerCase());
        }
    }

    /**
     * Salva os bytecodes gerados em um arquivo binario.
     * Este arquivo pode ser carregado e executado pela maquina virtual Tuga.
     *
     * @param filename O nome do arquivo para salvar os bytecodes
     * @throws IOException Se ocorrer um erro de E/S ao escrever o arquivo
     */
    public void saveBytecodes(String filename) throws IOException {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(filename))) {
            List<Object> allConstants = constantPool.getAllConstants();

            // Escreve o numero de entradas na constant pool
            out.writeInt(allConstants.size());

           for (Object constant: allConstants){
               if (constant instanceof Double){
                   out.writeByte(1); //Tipo Double
                   out.writeDouble((Double) constant);
               } else if (constant instanceof String) {
                   out.writeByte(2); //Tipo String
                   String str = (String) constant;
                   char[] chars = str.toCharArray();
                   out.writeInt(chars.length * 2); //Tamano em bytes (2 bytes por caractere)
                   for (char c : chars){
                       out.writeChar(c);
                   }
               }
           }

            // Escreve as instrucoes
            for (Instruction inst : code) {
                inst.writeTo(out);
            }

        }
    }

    /**
     * Registra uma variavel global na tabela de enderecos.
     * Atribui um endereco sequencial para a variavel.
     *
     * @param name O nome da variavel a ser registrada
     */
    private void registerVariable(String name){
        variableAddress.put(name, nextVarAddress++);
    }

    /**
     * Obtem o endereco de uma variavel global.
     *
     * @param name O nome da variavel
     * @return O endereco da variavel
     * @throws RuntimeException se a variavel nao for encontrada
     */
    private int getVariableAddress(String name) {
        if (!variableAddress.containsKey(name)) {
            throw new RuntimeException("Variavel nao encontrada: " + name);
        }
        return variableAddress.get(name);
    }

    /**
     * Exibe o conteudo da constant pool.
     * Util para depuracao.
     */
    public void dumpConstantPool(){
        List<Object> constants = constantPool.getAllConstants();

        for (int i = 0; i < constants.size(); i++){
            Object value = constants.get(i);
            if (value instanceof Double){
                System.out.println(i + ": " + value );
            } else if (value instanceof String) {
                System.out.println(i + ": \"" + value + "\"");
            }
        }
    }

    /**
     * Procura o endereco de uma variavel, seja no escopo local ou global.
     *
     * @param name O nome da variavel
     * @return O endereco da variavel, ou null se nao for encontrada
     */
    private Integer lookupVariable(String name) {

        if (currentLocalVars.containsKey(name)) {
            return currentLocalVars.get(name);
        }

        if (variableAddress.containsKey(name)) {
            return variableAddress.get(name);
        }

        return null;
    }
}