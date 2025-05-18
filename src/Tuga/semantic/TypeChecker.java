package Tuga.semantic;

import Tuga.semantic.symbols.FunctionSymbol;
import Tuga.semantic.symbols.Symbol;
import Tuga.semantic.symbols.VarSymbol;
import org.antlr.v4.runtime.Token;
import Tuga.parser.TugaBaseVisitor;
import Tuga.parser.TugaParser;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementa a verificacao semantica de tipos para a linguagem Tuga.
 * Este visitante percorre a arvore sintatica e verifica a validade
 * dos tipos nas expressoes, instrucoes e declaracoes.
 * Tambem valida chamadas de funcoes, parametros e instrucoes de retorno.
 */
public class TypeChecker extends TugaBaseVisitor<Type> {
    /** Funcao atual em analise, usada para verificar declaracoes 'retorna' */
    private FunctionSymbol currentFunction = null;
    /** Tipo vazio usado para funcoes sem retorno */
    private Type voidType = Type.VOID;
    /** Tabela de simbolos para rastrear variaveis e funcoes */
    private SymbolTable symbolTable = new SymbolTable();
    /** Lista para armazenar todos os erros encontrados durante a analise */
    private List<String> errors = new ArrayList<>();
    /** Conjunto para evitar reportar o mesmo erro multiplas vezes */
    private Set<String> reportedErrors = new HashSet<>();

    /**
     * Processa o programa completo, analisando declaracoes globais e funcoes.
     * Verifica se existe uma funcao 'principal'.
     *
     * @param ctx O contexto do programa
     * @return null
     */
    @Override
    public Type visitProgram(TugaParser.ProgramContext ctx){
        if (ctx.globals() != null){
            visit(ctx.globals());
        }

        for (TugaParser.FunctionDeclContext funcDecl : ctx.functionDecl()){
            registerFunctionSignature(funcDecl);
        }

        boolean hasPrincipal =false;
        for (TugaParser.FunctionDeclContext funcDecl : ctx.functionDecl()){
            visit(funcDecl);
            if (funcDecl.IDENTIFIER().getText().equals("principal")){
                hasPrincipal = true;
            }
        }

        if (!hasPrincipal){
            int lastLine = ctx.stop.getLine();
            reportError("erro na linha " + lastLine + ": falta funcao principal()");
        }

        errors.sort((error1, error2) ->{
            int line1 = extractLineNumber(error1);
            int line2 = extractLineNumber(error2);
            return Integer.compare(line1,line2);
        });

        return null;
    }

    /**
     * Processa a declaracao de uma funcao, verificando parametros e corpo.
     * Valida se funcoes nao-void contem instrucoes de retorno.
     *
     * @param ctx O contexto da declaracao de funcao
     * @return null
     */
    @Override
    public Type visitFunctionDecl(TugaParser.FunctionDeclContext ctx){
        String funcName = ctx.IDENTIFIER().getText();

        try {

            Symbol symbol = symbolTable.lookupSymbol(funcName);

            if (symbol == null){
                reportError(
                        String.format(
                                "erro na linha %d: funcao '%s' nao foi declarado corretamente",
                                ctx.IDENTIFIER().getSymbol().getLine(),
                                funcName
                        )
                );
                return Type.ERROR;
            }

            if (!(symbol instanceof FunctionSymbol)){
                return Type.ERROR;
            }

            // Determinar o tipo de retorno
            Type returnType = ctx.type() != null ? getTypeFromString(ctx.type().getText()) : voidType;

            // Criar o simbolo de funcao
            FunctionSymbol functionSymbol = (FunctionSymbol) symbol;

            // Entrar no escopo da funcao
            symbolTable.enterScope();

            // Armazenar a funcao atual para validar retornos
            FunctionSymbol prevFunction = currentFunction;
            currentFunction = functionSymbol;

            // Processar os parametros
            if (ctx.paramList() != null) {
                visit(ctx.paramList());
            }

            // Salvar o escopo da funcao para uso futuro
            symbolTable.saveScope(funcName);

            // Visitar o corpo da funcao
            visit(ctx.block());

            // Verificar se a funcao non-void tem retorno
            if (returnType != voidType && !functionSymbol.hasReturn()) {
                reportError(
                        String.format(
                                "erro na linha %d: funcao '%s' deve retornar um valor do tipo %s",
                                ctx.IDENTIFIER().getSymbol().getLine(),
                                funcName,
                                returnType
                        )
                );
            }

            // Restaurar a funcao anterior
            currentFunction = prevFunction;

            // Sair do escopo da funcao
            symbolTable.exitScope();
        } catch (TypeCheckingException e) {
            reportError(e.getMessage());
        }

        return null;
    }

    /**
     * Processa chamadas de funcao como expressoes.
     *
     * @param ctx O contexto da expressao de chamada de funcao
     * @return O tipo de retorno da funcao chamada
     */
    @Override
    public Type visitFunctionalCallExpr(TugaParser.FunctionalCallExprContext ctx){
        return visit(ctx.functionCall());
    }

    /**
     * Processa uma chamada de funcao, verificando existencia da funcao,
     * numero e tipos dos argumentos.
     *
     * @param ctx O contexto da chamada de funcao
     * @return O tipo de retorno da funcao ou Type.ERROR em caso de erro
     */
    @Override
    public Type visitFunctionCall(TugaParser.FunctionCallContext ctx){
        String funcName = ctx.IDENTIFIER().getText();

        Symbol symbol = symbolTable.lookupSymbol(funcName);

        if (symbol == null){
            reportError(
                    String.format(
                            "erro na linha %d: '%s' nao foi declarado",
                            ctx.IDENTIFIER().getSymbol().getLine(),
                            funcName
                    )
            );

            return Type.ERROR;
        }

        if (!(symbol instanceof FunctionSymbol func)) {
            reportError(
                    String.format(
                            "erro na linha %d: '%s' nao e uma funcao",
                            ctx.IDENTIFIER().getSymbol().getLine(),
                            funcName
                    )
            );
            return Type.ERROR;
        }

        List<Type> argTypes = new ArrayList<>();
        List<TugaParser.ExpressionContext> args = ctx.exprList() != null ? ctx.exprList().expression() : new ArrayList<>();

        for (TugaParser.ExpressionContext arg : args){
            argTypes.add(visit(arg));
        }

        // Verificar argumentos
        List<VarSymbol> params = func.getParameters();


        if (params.size() != args.size()) {
            reportError(
                    String.format(
                            "erro na linha %d: '%s' requer %d argumentos",
                            ctx.IDENTIFIER().getSymbol().getLine(),
                            funcName,
                            params.size()
                    )
            );
        }

        // Verificar compatibilidade de tipos de argumentos
        for (int i = 0; i < Math.min(params.size(), argTypes.size()); i++) {
            Type paramType = params.get(i).type;
            Type argType = argTypes.get(i);

            if (!isAssignable(paramType, argType)) {
                reportError(
                        String.format(
                                "erro na linha %d: '%s' devia ser do tipo %s",
                                args.get(i).getStart().getLine(),
                                args.get(i).getText(),
                                paramType
                        )
                );
            }
        }

        return func.type;
    }

    /**
     * Processa chamadas de funcao como instrucoes.
     * Verifica se funcoes que retornam valores nao sao utilizadas como instrucoes.
     *
     * @param ctx O contexto da instrucao de chamada de funcao
     * @return null
     */
    @Override
    public Type visitFunctionCallInstr(TugaParser.FunctionCallInstrContext ctx){
        Type returnType = visit(ctx.functionCall());

        // Para instrucoes de chamada de funcao, o tipo tem de ser void
        if (returnType != voidType && returnType != Type.ERROR){
            reportError(
                    String.format(
                            "erro na linha %d: valor de '%s' tem de ser atribuido a uma variavel",
                            ctx.getStart().getLine(),
                            ctx.functionCall().IDENTIFIER().getText()
                    )
            );
        }

        return null;
    }

    /**
     * Processa instrucoes de retorno.
     * Verifica se o tipo retornado coincide com o tipo de retorno da funcao.
     *
     * @param ctx O contexto da instrucao de retorno
     * @return null
     */
    @Override
    public Type visitReturnInstr(TugaParser.ReturnInstrContext ctx){
        // Verificar se estamos dentro de uma funcao
        if (currentFunction == null){
            reportError(
                    String.format(
                            "erro na linha %d: 'retorna' fora de uma funcao",
                            ctx.getStart().getLine()
                    )
            );

            return null;
        }

        // Marcar que esta dentro de uma funcao
        currentFunction.setHasReturn(true);

        // Verificar tipo de retorno
        if (ctx.expression() != null){
            Type exprType = visit(ctx.expression());

            if (currentFunction.type == Type.VOID){
                reportError(
                        String.format(
                                "erro na linha %d: funcao nao deve retornar valor",
                                ctx.getStart().getLine()
                        )
                );
            } else if (!isAssignable(currentFunction.type, exprType)) {
                reportError(
                        String.format(
                                "erro na linha %d: tipo incompativel no retorno: esperado %s, encontrado %s",
                                ctx.getStart().getLine(),
                                currentFunction.type,
                                exprType
                        )
                );
            }
        }else {
            // Sem expressao de retorno
            if (currentFunction.type != voidType){
                reportError(
                        String.format(
                                "erro na linha %d: funcao deve retornar um valor do tipo %s",
                                ctx.getStart().getLine(),
                                currentFunction.type
                        )
                );
            }
        }

        return null;
    }

    /**
     * Processa a lista de parametros de uma funcao.
     *
     * @param ctx O contexto da lista de parametros
     * @return null
     */
    @Override
    public Type visitParamList(TugaParser.ParamListContext ctx){
        for (TugaParser.ParamContext param : ctx.param()){
            visit(param);
        }

        return null;
    }

    /**
     * Processa um parametro de funcao, declarando-o no escopo atual.
     *
     * @param ctx O contexto do parametro
     * @return null
     */
    @Override
    public Type visitParam(TugaParser.ParamContext ctx){
        String paramName = ctx.IDENTIFIER().getText();
        Type paramType = getTypeFromString(ctx.type().getText());

        VarSymbol paramSymbol = new VarSymbol(ctx.IDENTIFIER().getSymbol(), paramType, true);
        if (!symbolTable.declare(paramSymbol)){
            reportError(
                    String.format(
                            "erro na linha %d: '%s' ja foi declarado",
                            ctx.IDENTIFIER().getSymbol().getLine(),
                            paramName
                    )
            );
        }

        return null;
    }

    /**
     * Processa um bloco de declaracoes de variaveis.
     *
     * @param ctx O contexto das declaracoes
     * @return null
     */
    @Override
    public Type visitDeclarations(TugaParser.DeclarationsContext ctx){
        for (TugaParser.DeclarationContext decl : ctx.declaration()){
            visit(decl);
        }

        return null;
    }

    /**
     * Processa uma declaracao de variavel, registando cada variavel
     * na tabela de simbolos com o tipo especificado.
     *
     * @param ctx O contexto da declaracao
     * @return null
     */
    @Override
    public Type visitDeclaration(TugaParser.DeclarationContext ctx){
        Type type = getTypeFromString(ctx.type().getText());

        for (TerminalNode id : ctx.variableList().IDENTIFIER()){
            String varName = id.getText();
            VarSymbol varSymbol = new VarSymbol(id.getSymbol(), type, false);
            if(!symbolTable.declare(varSymbol)){
                reportError(
                        String.format(
                                "erro na linha %d: '%s' ja foi declarado",
                                ctx.getStart().getLine(),
                                varName
                        )
                );
            }
        }

        return null;
    }

    /**
     * Processa uma instrucao de escrita.
     * Qualquer tipo pode ser escrito.
     *
     * @param ctx O contexto da instrucao de escrita
     * @return null
     */
    @Override
    public Type visitWriteInstr(TugaParser.WriteInstrContext ctx){
        visit(ctx.expression());
        return null;
    }

    /**
     * Processa uma instrucao de atribuicao.
     * Verifica se o tipo da expressao e compativel com o tipo da variavel.
     *
     * @param ctx O contexto da instrucao de atribuicao
     * @return null
     */
    @Override
    public Type visitAssignInstr(TugaParser.AssignInstrContext ctx){
        String varName = ctx.IDENTIFIER().getText();
        Symbol symbol = symbolTable.lookupSymbol(varName);
        if (symbol instanceof FunctionSymbol) {
            reportError(String.format(
                    "erro na linha %d: '%s' nao eh variavel",
                    ctx.IDENTIFIER().getSymbol().getLine(),
                    varName
            ));
            return null;
        }

        try {
            Type varType = symbol.type;
            Type exprType = visit(ctx.expression());

            if (exprType != Type.ERROR && !isAssignable(varType,exprType)){
                reportError(String.format(
                        "erro na linha %d: operador '<-' eh invalido entre %s e %s",
                        ctx.IDENTIFIER().getSymbol().getLine(),
                        varType,
                        exprType
                ));
            }
        }catch (TypeCheckingException e){
            reportError(e.getMessage());
        }
        return null;
    }

    /**
     * Processa uma instrucao de bloco.
     *
     * @param ctx O contexto da instrucao de bloco
     * @return null
     */
    @Override
    public Type visitBlockInstr(TugaParser.BlockInstrContext ctx){
        return visit(ctx.block());
    }

    /**
     * Processa um bloco de codigo.
     * Cria um novo escopo para o bloco (exceto para blocos de funcao).
     *
     * @param ctx O contexto do bloco
     * @return null
     */
    @Override
    public Type visitBlock(TugaParser.BlockContext ctx){

        boolean isFunctionBlock = ctx.getParent() instanceof TugaParser.FunctionDeclContext;

        if (!isFunctionBlock){
            symbolTable.enterScope("bloco_" + ctx.hashCode() + "_" + ctx.getStart().getLine());
        }

        if (ctx.declarations() != null){
            visit(ctx.declarations());
        }

        for (TugaParser.InstructionContext instr : ctx.instruction()){
            visit(instr);
        }

        if (!isFunctionBlock){
            symbolTable.exitScope();
        }

        return null;
    }

    /**
     * Processa uma instrucao while (enquanto).
     * Verifica se a expressao de condicao e do tipo booleano.
     *
     * @param ctx O contexto da instrucao while
     * @return null
     */
    @Override
    public Type visitWhileInstr(TugaParser.WhileInstrContext ctx){
        // Verificar se a expressao de controlo e booleana
        Type condType = visit(ctx.expression());

        if (condType != Type.BOOLEAN){
            reportTypeError(ctx.expression().getStart(),
                    "expressao de 'enquanto' nao eh do tipo booleano"
            );
        }

        // Visitar o corpo do loop
        visit(ctx.instruction());
        return null;
    }

    /**
     * Processa uma instrucao if-else (se-senao).
     * Verifica se a expressao de condicao e do tipo booleano.
     *
     * @param ctx O contexto da instrucao if-else
     * @return null
     */
    @Override
    public Type visitIfElseInstr(TugaParser.IfElseInstrContext ctx){
        Type condType = visit(ctx.expression());

        if (condType != Type.BOOLEAN){
            reportTypeError(ctx.expression().getStart(),
                    "expressao de 'se' nao eh do tipo booleano"
            );
        }

        // Visitar o bloco 'if'
        visit(ctx.instruction(0));

        // Visitar bloco 'else' se existir
        if (ctx.instruction().size() > 1){
            visit(ctx.instruction(1));
        }

        return null;
    }

    /**
     * Processa uma instrucao vazia.
     *
     * @param ctx O contexto da instrucao vazia
     * @return null
     */
    @Override
    public Type visitEmptyInstr(TugaParser.EmptyInstrContext ctx){
        // Nao faz nada
        return  null;
    }

    /**
     * Processa uma expressao de variavel.
     * Verifica se a variavel esta declarada.
     *
     * @param ctx O contexto da expressao de variavel
     * @return O tipo da variavel ou Type.ERROR em caso de erro
     */
    @Override
    public Type visitVarExpr(TugaParser.VarExprContext ctx){
        String varName = ctx.IDENTIFIER().getText();

        Symbol symbol = symbolTable.lookupSymbol(varName);

        if (symbol == null){
            reportError(
                    String.format(
                            "erro na linha %d: '%s' nao foi declarado",
                            ctx.IDENTIFIER().getSymbol().getLine(),
                            varName
                    )
            );
            return Type.ERROR;
        }

        if (symbol instanceof FunctionSymbol){
            reportError(
                    String.format(
                            "erro na linha %d: '%s' nao eh variavel",
                            ctx.IDENTIFIER().getSymbol().getLine(),
                            varName
                    )
            );
            return Type.ERROR;
        }
        return symbol.type;
    }

    /**
     * Processa uma expressao literal.
     *
     * @param ctx O contexto da expressao literal
     * @return O tipo do literal
     */
    @Override
    public Type visitLiteralExpr(TugaParser.LiteralExprContext ctx){
        return visit(ctx.literal());
    }

    /**
     * Processa um literal inteiro.
     *
     * @param ctx O contexto do literal inteiro
     * @return Type.INTEGER
     */
    @Override
    public Type visitIntLiteral(TugaParser.IntLiteralContext ctx){
        return Type.INTEGER;
    }

    /**
     * Processa um literal real.
     *
     * @param ctx O contexto do literal real
     * @return Type.REAL
     */
    @Override
    public Type visitRealLiteral(TugaParser.RealLiteralContext ctx){
        return Type.REAL;
    }

    /**
     * Processa um literal string.
     *
     * @param ctx O contexto do literal string
     * @return Type.STRING
     */
    @Override
    public Type visitStringLiteral(TugaParser.StringLiteralContext ctx){
        return Type.STRING;
    }

    /**
     * Processa um literal booleano.
     *
     * @param ctx O contexto do literal booleano
     * @return Type.BOOLEAN
     */
    @Override
    public Type visitBoolLiteral(TugaParser.BoolLiteralContext ctx){
        return Type.BOOLEAN;
    }

    /**
     * Processa uma expressao entre parenteses.
     *
     * @param ctx O contexto da expressao entre parenteses
     * @return O tipo da expressao contida nos parenteses
     */
    @Override
    public Type visitParenExpr(TugaParser.ParenExprContext ctx){
        return visit(ctx.expression());
    }

    /**
     * Processa uma expressao unaria (- ou nao).
     * Verifica se o operador e compativel com o tipo da expressao.
     *
     * @param ctx O contexto da expressao unaria
     * @return O tipo resultante da operacao unaria
     */
    @Override
    public Type visitUnaryExpr(TugaParser.UnaryExprContext ctx){
        String op = ctx.op.getText();
        Type exprType = visit(ctx.expression());

        if(op.equals("-")){
            if(exprType == Type.INTEGER || exprType == Type.REAL){
                return exprType; // O tipo resultante e o mesmo do -- operando --
            }else{
                reportTypeError(ctx.op, "operador '-' nao pode ser aplicado a "+ exprType);
            }
        } else if (op.equals("nao")) {
            if (exprType == Type.BOOLEAN){
                return Type.BOOLEAN;
            }else {
                reportTypeError(ctx.op, "operador 'nao' so pode ser aplicado a booleano, nao a " + exprType);
            }
        }

        return null; // Nunca deve chegar aqui
    }

    /**
     * Processa uma expressao binaria (+, -, *, /, %).
     * Verifica se os operadores sao compativeis com os tipos.
     *
     * @param ctx O contexto da expressao binaria
     * @return O tipo resultante da operacao binaria
     */
    @Override
    public Type visitBinaryExpr(TugaParser.BinaryExprContext ctx){
        String op = ctx.op.getText();
        Type leftType = visit(ctx.expression(0));
        Type rightType = visit(ctx.expression(1));

        if (leftType == Type.VOID || rightType == Type.VOID){
            reportTypeError(ctx.op, "operador '" + op + "' eh invalido entre " + leftType + " e " + rightType);
            return Type.ERROR;
        }

        // Regras de operadores aritmeticos: +, -, *, /, %
        if(op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/")){
            // Adicionar a regra para a concatenacao de strings
            if (op.equals("+") && (leftType == Type.STRING || rightType == Type.STRING)){
                return Type.STRING;
            }

            // Verificacao para operadores numericos
            if (leftType == Type.INTEGER && rightType == Type.INTEGER){
                return Type.INTEGER;
            } else if ((leftType == Type.INTEGER || leftType == Type.REAL) && (rightType == Type.INTEGER || rightType == Type.REAL)) {
                return Type.REAL; // Pelo menos um dos operadores e real
            }else{
                reportTypeError(ctx.op, "operador '" + op + "' eh invalido entre " + leftType + " e " + rightType);
            }
        } else if (op.equals("%")) {
            // Operador % so funciona com inteiros
            if (leftType == Type.INTEGER && rightType == Type.INTEGER){
                return Type.INTEGER;
            }else {
                reportTypeError(ctx.op, "operador '%' so pode ser aplicado entre inteiros, nao entre " + leftType + " e " + rightType);
            }
        }

        return null; // Nunca deve chegar aqui
    }

    /**
     * Processa uma expressao de comparacao (<, >, <=, >=).
     * Verifica se os operandos sao valores numericos.
     *
     * @param ctx O contexto da expressao de comparacao
     * @return Type.BOOLEAN se valido, Type.ERROR caso contrario
     */
    @Override
    public Type visitComparisonExpr(TugaParser.ComparisonExprContext ctx){
        String op = ctx.op.getText();
        Type leftType = visit(ctx.expression(0));
        Type rightType = visit(ctx.expression(1));

        // Regras para operador de comparacao: <, >, <=, >=
        // Importante: So funciona com valores numericos
        if ((leftType == Type.INTEGER || leftType == Type.REAL) && (rightType == Type.INTEGER || rightType == Type.REAL)){
            return Type.BOOLEAN;
        }else {
            reportTypeError(ctx.op, "Operador '" + op + "' so pode ser aplicado entre valores numericos, nao entre " + leftType + " e " + rightType);
        }

        return null; // Nunca deve chegar aqui
    }

    /**
     * Processa uma expressao de igualdade (=, !=).
     * Verifica se os operandos sao do mesmo tipo.
     *
     * @param ctx O contexto da expressao de igualdade
     * @return Type.BOOLEAN se valido, Type.ERROR caso contrario
     */
    @Override
    public Type visitEqualityExpr(TugaParser.EqualityExprContext ctx){
        String op = ctx.op.getText();
        Type leftType = visit(ctx.expression(0));
        Type rightType = visit(ctx.expression(1));

        // Regras para operadores de igualdade: igual, diferente
        // Importante: Podem ser aplicados entre booleanos, strings e valores numericos
        if ((leftType == Type.BOOLEAN && rightType == Type.BOOLEAN) || (leftType == Type.STRING && rightType == Type.STRING) || ((leftType == Type.INTEGER || leftType == Type.REAL) && (rightType == Type.INTEGER || rightType == Type.REAL))){
            return Type.BOOLEAN;
        }else {
            reportTypeError(ctx.op, "Operador'" + op + "' nao pode ser aplicado entre " + leftType + " e " + rightType);
        }

        return null; // Nunca deve chegar aqui
    }

    /**
     * Processa uma expressao de conjuncao logica (e).
     * Verifica se ambos os operandos sao valores booleanos.
     *
     * @param ctx O contexto da expressao 'e'
     * @return Type.BOOLEAN se valido, Type.ERROR caso contrario
     */
    @Override
    public Type visitAndExpr(TugaParser.AndExprContext ctx){
        Type leftType = visit(ctx.expression(0));
        Type rightType = visit(ctx.expression(1));

        // Regra para operador: e
        // Importante: Ambos os operadores devem ser booleanos
        if (leftType == Type.BOOLEAN && rightType == Type.BOOLEAN){
            return Type.BOOLEAN;
        }else {
            reportTypeError(ctx.getStart(), "Operador 'e' so pode ser aplicado entre valores booleanos, nao entre " + leftType + " e " + rightType);
        }

        return null; // Nunca deve chegar aqui
    }

    /**
     * Processa uma expressao de disjuncao logica (ou).
     * Verifica se ambos os operandos sao valores booleanos.
     *
     * @param ctx O contexto da expressao 'ou'
     * @return Type.BOOLEAN se valido, Type.ERROR caso contrario
     */
    @Override
    public Type visitOrExpr(TugaParser.OrExprContext ctx){
        Type leftType = visit(ctx.expression(0));
        Type rightType = visit(ctx.expression(1));

        // Regra para operador: ou
        // Importante: Ambos os operandos devem ser booleanos
        if (leftType == Type.BOOLEAN && rightType == Type.BOOLEAN){
            return Type.BOOLEAN;
        }else {
            reportTypeError(ctx.getStart(), "Operador 'ou' so pode ser aplicado entre valores booleanos, nao entre " + leftType + " e " + rightType);
        }

        return null; // Nunca deve chegar aqui
    }

    /*
     * Utilitarios para gerar o typeChecker
     */

    /**
     * Verifica se um tipo pode ser atribuido a outro.
     * Suporta atribuicao direta e promocao de INTEGER para REAL.
     *
     * @param varType O tipo da variavel de destino
     * @param exprType O tipo da expressao a atribuir
     * @return true se a atribuicao for valida, false caso contrario
     */
    private boolean isAssignable(Type varType, Type exprType){
        if (varType == exprType){
            return true;
        }

        // Promocao de INTEGER para REAL
        return varType == Type.REAL && exprType == Type.INTEGER;
    }

    /**
     * Converte uma string de tipo na linguagem Tuga para um valor Type.
     *
     * @param typeStr O nome do tipo em portugues
     * @return O valor Type correspondente
     * @throws TypeCheckingException se o tipo for desconhecido
     */
    private Type getTypeFromString(String typeStr){
        return switch (typeStr){
            case "inteiro" -> Type.INTEGER;
            case "real" -> Type.REAL;
            case "booleano" -> Type.BOOLEAN;
            case "string" -> Type.STRING;
            case "void" -> Type.VOID;
            default -> throw new TypeCheckingException(
                    "tipo desconhecido: " + typeStr
            );
        };
    }

    /**
     * Reporta um erro de tipo com informacoes de linha.
     *
     * @param token O token onde ocorreu o erro
     * @param message A mensagem de erro
     */
    private void reportTypeError(Token token, String message){
        String errorMsg = String.format(
                "erro na linha %d: %s",
                token.getLine(),
                message
        );
        reportError(errorMsg);
    }

    /**
     * Adiciona uma mensagem de erro a lista de erros.
     * Evita adicionar erros duplicados.
     *
     * @param message A mensagem de erro
     */
    private void reportError(String message){
        if (!reportedErrors.contains(message)){
            errors.add(message);
            reportedErrors.add(message);
        }
    }

    /**
     * Verifica se foram encontrados erros durante a analise.
     *
     * @return true se existirem erros, false caso contrario
     */
    public boolean hasErrors(){
        return !errors.isEmpty();
    }

    /**
     * Obtem a lista de todos os erros encontrados.
     *
     * @return Lista de mensagens de erro
     */
    public List<String> getErrors(){
        return errors;
    }

    /**
     * Obtem a tabela de simbolos usada durante a analise.
     *
     * @return A tabela de simbolos
     */
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    /**
     * Extrai o numero da linha de uma mensagem de erro.
     *
     * @param errorMsg A mensagem de erro
     * @return O numero da linha ou Integer.MAX_VALUE se nao for possivel extrair
     */
    private int extractLineNumber(String errorMsg){
        try {
            int startIndex = errorMsg.indexOf("linha") + 6;
            int endIndex = errorMsg.indexOf(":", startIndex);
            return Integer.parseInt(errorMsg.substring(startIndex,endIndex));
        }catch (Exception e){
            return Integer.MAX_VALUE;
        }
    }

    /**
     * Regista a assinatura de uma funcao na tabela de simbolos.
     * Este passo e realizado antes de processar o corpo das funcoes.
     *
     * @param ctx O contexto da declaracao de funcao
     */
    private void registerFunctionSignature(TugaParser.FunctionDeclContext ctx){
        String funcName = ctx.IDENTIFIER().getText();
        Type returnType = ctx.type() != null ? getTypeFromString(ctx.type().getText()) : voidType;

        Symbol existingSymbol = symbolTable.lookupSymbol(funcName);

        if (existingSymbol != null && !(existingSymbol instanceof FunctionSymbol)){
            reportError(
                    String.format(
                            "erro na linha %d: '%s' ja foi declarado",
                            ctx.IDENTIFIER().getSymbol().getLine(),
                            funcName
                    )
            );
            return;
        }

        FunctionSymbol functionSymbol = new FunctionSymbol(funcName, returnType);

        if (ctx.paramList() != null){
            for (TugaParser.ParamContext param : ctx.paramList().param()){
                String paramName = param.IDENTIFIER().getText();
                Type paramType = getTypeFromString(param.type().getText());

                VarSymbol paramSymbol = new VarSymbol(param.IDENTIFIER().getSymbol(), paramType, true);

                functionSymbol.addParameter(paramSymbol);
            }
        }

        if (!symbolTable.declare(functionSymbol)){
            reportError(
                    String.format(
                            "erro na linha %d: '%s' ja foi declarado",
                            ctx.IDENTIFIER().getSymbol().getLine(),
                            funcName
                    )
            );
        }
    }
}
