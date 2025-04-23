package Tuga.codegen;

import Tuga.semantic.Type;
import Tuga.semantic.TypeChecker;
import Tuga.vm.ConstantPool;
import org.antlr.v4.runtime.tree.ParseTree;
import Tuga.parser.TugaBaseVisitor;
import Tuga.parser.TugaParser;
import Tuga.vm.OpCode;
import Tuga.vm.instruction.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BytecodeGenerator extends TugaBaseVisitor<Void> {
    // O codigo gerado (instrucoes)
    private final ArrayList<Instruction> code = new ArrayList<>();

    // Constant pools para valores reais e strings
    private final ConstantPool constantPool;
    // TypeChecker para determinar os tipos de expressoes
    private final TypeChecker typeChecker;
    // Mapa para armazenar os tipos de expressoes
    private final Map<ParseTree, Type> expressionTypes = new HashMap<>();

    public BytecodeGenerator(TypeChecker typeChecker){
        this.typeChecker = typeChecker;
        this.constantPool = new ConstantPool();
    }

    // Metodo para obter o tipo de uma expressao
    private Type getExpressionType(ParseTree ctx){
        Type type = expressionTypes.get(ctx);
        if (type == null){
            // Se o tipo ainda nao foi determinado
            type = typeChecker.visit(ctx);
            expressionTypes.put(ctx, type);
        }
        return type;
    }

    // PROGRAM: instruction+ EOF
    @Override
    public Void visitProgram(TugaParser.ProgramContext ctx) {
        // Primeiro, executa o TypeChecker em todo o programa para preencher o mapa de tipos de expressoes
        typeChecker.visitProgram(ctx);

        // Agora gera o codigo
        for (TugaParser.InstructionContext inst : ctx.instruction()) {
            visit(inst);
        }

        // Adiciona a instrucao HALT ao final
        emit(OpCode.halt);
        return null;
    }

    // INSTRUCTION: 'escreve' expression ';'
    @Override
    public Void visitInstruction(TugaParser.InstructionContext ctx) {
        // Gera codigo para a expressao
        visit(ctx.expression());

        // Determina o tipo de expressao e emite a instrucao adequada a impressao
        Type exprType = getExpressionType(ctx.expression());

        switch (exprType){
            case INTEGER -> emit(OpCode.iprint);
            case REAL -> emit(OpCode.dprint);
            case STRING -> emit(OpCode.sprint);
            case BOOLEAN -> emit(OpCode.bprint);
            default -> throw new RuntimeException("Tipo nao suportado para impressao: " + exprType);
        }

        return null;
    }

    // LITERALEXPR: literal
    @Override
    public Void visitLiteralExpr(TugaParser.LiteralExprContext ctx) {
        return visit(ctx.literal());
    }

    // INTLITERAL: INTEGER
    @Override
    public Void visitIntLiteral(TugaParser.IntLiteralContext ctx) {
        int value = Integer.parseInt(ctx.INTEGER().getText());
        emit(OpCode.iconst, value);
        return null;
    }

    // REALLITERAL: REAL
    @Override
    public Void visitRealLiteral(TugaParser.RealLiteralContext ctx) {
        double value = Double.parseDouble(ctx.REAL().getText());
        int index = addRealConstant(value);
        emit(OpCode.dconst, index);
        return null;
    }

    // STRINGLITERAL: STRING
    @Override
    public Void visitStringLiteral(TugaParser.StringLiteralContext ctx) {
        // Remove as aspas da string
        String text = ctx.STRING().getText();
        String value = text.substring(1, text.length() - 1);

        int index = addStringConstant(value);
        emit(OpCode.sconst, index);
        return null;
    }

    // BOOLLITERAL: BOOLEAN
    @Override
    public Void visitBoolLiteral(TugaParser.BoolLiteralContext ctx) {
        boolean value = ctx.BOOLEAN().getText().equals("verdadeiro");
        if (value) {
            emit(OpCode.tconst);
        } else {
            emit(OpCode.fconst);
        }
        return null;
    }

    // PARENEXPR: '(' expression ')'
    @Override
    public Void visitParenExpr(TugaParser.ParenExprContext ctx) {
        Type exprType = typeChecker.visit(ctx.expression());
        visit(ctx.expression());
        return null;
    }

    // UNARYEXPR: op=('-' | 'nao') expression
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

    // BINARYEXPR: expression op=('*' | '/' | '%') expression
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
                if (leftType != Type.STRING){
                    convertToString(leftType);
                }

                visit(ctx.expression(1));
                if (rightType != Type.STRING){
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

    // COMPARISONEXPR: expression op=('<' | '<=' | '>' | '>=') expression
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

                // Converter imediatamente se necessário
                if (needRealComparison && rightType == Type.INTEGER){
                    emit(OpCode.itod);
                }

                // Depois geramos o lado ESQUERDO
                visit(ctx.expression(0));

                // Converter imediatamente se necessário
                if (needRealComparison && leftType == Type.INTEGER){
                    emit(OpCode.itod);
                }
            }else {
                // Para "<" e "<=", mantemos a ordem normal
                visit(ctx.expression(0));

                // Converter imediatamente se necessário
                if (needRealComparison && leftType == Type.INTEGER){
                    emit(OpCode.itod);
                }

                visit(ctx.expression(1));

                // Converter imediatamente se necessário
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

    // EQUALITYEXPR: expression op=('igual' | 'diferente') expression
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

    // ANDEXPR: expression 'e' expression
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

    // OREXPR: expression 'ou' expression
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

    /*
     * Utilitarios para gerar codigo
     */

    // Converte o valor no topo da pilha para string com base no tipo
    private void convertToString(Type type) {
        switch (type) {
            case INTEGER -> emit(OpCode.itos);
            case REAL -> emit(OpCode.dtos);
            case BOOLEAN -> emit(OpCode.btos);
            // String nao precisa de conversao
        }
    }


    // Adiciona uma instrucao sem argumentos
    private void emit(OpCode opcode) {
        code.add(new Instruction(opcode));
    }

    // Adiciona uma instrucao com um argumento
    private void emit(OpCode opcode, int arg) {
        code.add(new Instruction1Arg(opcode, arg));
    }

    // Adiciona um valor real a constant pool
    private int addRealConstant(double value) {
        return constantPool.addReal(value);
    }

    // Adiciona uma string a constant pool
    private int addStringConstant(String value) {
        return constantPool.addConstant(value);
    }

    // Exibe o codigo gerado em formato "assembly"
    public void dumpCode() {
        for (int i = 0; i < code.size(); i++) {
            System.out.println(i + ": " + code.get(i).toString().toLowerCase());
        }
    }

    // Salva os bytecodes gerados em um arquivo
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

            //System.out.println("Bytecodes salvos em " + filename);
        }
    }

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
}