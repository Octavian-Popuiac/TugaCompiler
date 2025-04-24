package Tuga.semantic;

import org.antlr.v4.runtime.Token;
import Tuga.parser.TugaBaseVisitor;
import Tuga.parser.TugaParser;
import org.antlr.v4.runtime.tree.TerminalNode;

public class TypeChecker extends TugaBaseVisitor<Type> {
    private SymbolTable symbolTable = new SymbolTable();

    @Override
    public Type visitProgram(TugaParser.ProgramContext ctx){
        // Primeiro processar as declaracoes de variaveis
        if (ctx.declarations() != null){
            visit(ctx.declarations());
        }

        // Visita todas as instrucoes do programa
        for(TugaParser.InstructionContext instrucion : ctx.instruction()){
            visit(instrucion);
        }
        return null;
    }

    @Override
    public Type visitDeclarations(TugaParser.DeclarationsContext ctx){
        for (TugaParser.DeclarationContext decl : ctx.declaration()){
            visit(decl);
        }

        return null;
    }

    @Override
    public Type visitDeclaration(TugaParser.DeclarationContext ctx){
        Type type = getTypeFromString(ctx.type().getText());

        for (TerminalNode id : ctx.variableList().IDENTIFIER()){
            String varName = id.getText();
            symbolTable.declare(varName, type, id.getSymbol());
        }

        return null;
    }

    @Override
    public Type visitWriteInstr(TugaParser.WriteInstrContext ctx){
        // Qualquer tipo de ser escrito
        visit(ctx.expression());
        return null;
    }

    @Override
    public Type visitAssignInstr(TugaParser.AssignInstrContext ctx){
        String varName = ctx.IDENTIFIER().getText();

        // Verificar se a variavel existe
        Type varType = symbolTable.lookup(varName, ctx.IDENTIFIER().getSymbol());

        // Verificar o tipo de expressao
        Type exprType = visit(ctx.expression());

        // Verificar conpatibilidade de tipos
        if (!isAssignable(varType, exprType)){
            reportTypeError(ctx.IDENTIFIER().getSymbol(),
                    "Nao e possivel atribuir " + exprType + " a uma variavel de tipo " + varType);
        }

        return null;
    }

    @Override
    public Type visitBlockInstr(TugaParser.BlockInstrContext ctx){
        // Visitar todas as instrucoes dentro do bloco
        for (TugaParser.InstructionContext instr : ctx.instruction()){
            visit(instr);
        }

        return null;
    }

    @Override
    public Type visitWhileInstr(TugaParser.WhileInstrContext ctx){
        // Verificar se a expressao de controlo e booleana
        Type condType = visit(ctx.expression());

        if (condType != Type.BOOLEAN){
            reportTypeError(ctx.expression().getStart(),
                    "Expressao de controlo deve ser do tipo booleana, nao " + condType
            );
        }

        // Visitar o corpo do loop
        visit(ctx.instruction());
        return null;
    }

    @Override
    public Type visitIfElseInstr(TugaParser.IfElseInstrContext ctx){
        Type condType = visit(ctx.expression());

        if (condType != Type.BOOLEAN){
            reportTypeError(ctx.expression().getStart(),
                    "Expressao de controlo deve ser do tipo booleana, nao " + condType
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

    @Override
    public Type visitEmptyInstr(TugaParser.EmptyInstrContext ctx){
        // Nao faz nada
        return  null;
    }

    @Override
    public Type visitVarExpr(TugaParser.VarExprContext ctx){
        String varName = ctx.IDENTIFIER().getText();
        return symbolTable.lookup(varName, ctx.IDENTIFIER().getSymbol());
    }

    @Override
    public Type visitLiteralExpr(TugaParser.LiteralExprContext ctx){
        return visit(ctx.literal());
    }

    @Override
    public Type visitIntLiteral(TugaParser.IntLiteralContext ctx){
        return Type.INTEGER;
    }

    @Override
    public Type visitRealLiteral(TugaParser.RealLiteralContext ctx){
        return Type.REAL;
    }

    @Override
    public Type visitStringLiteral(TugaParser.StringLiteralContext ctx){
        return Type.STRING;
    }

    @Override
    public Type visitBoolLiteral(TugaParser.BoolLiteralContext ctx){
        return Type.BOOLEAN;
    }

    @Override
    public Type visitParenExpr(TugaParser.ParenExprContext ctx){
        return visit(ctx.expression());
    }

    @Override
    public Type visitUnaryExpr(TugaParser.UnaryExprContext ctx){
        String op = ctx.op.getText();
        Type exprType = visit(ctx.expression());

        if(op.equals("-")){
            if(exprType == Type.INTEGER || exprType == Type.REAL){
                return exprType; // O tipo resultante e o mesmo do -- operando --
            }else{
                reportTypeError(ctx.op, "Operador '-' nao pode ser aplicado a "+ exprType);
            }
        } else if (op.equals("nao")) {
            if (exprType == Type.BOOLEAN){
                return Type.BOOLEAN;
            }else {
                reportTypeError(ctx.op, "Operador 'nao' so pode ser aplicado a booleano, nao a " + exprType);
            }
        }

        return null; // Nunca deve chegar aqui
    }

    @Override
    public Type visitBinaryExpr(TugaParser.BinaryExprContext ctx){
        String op = ctx.op.getText();
        Type leftType = visit(ctx.expression(0));
        Type rightType = visit(ctx.expression(1));

        // Regras de operadores aritméticos: +, -, *, /, %
        if(op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/")){
            // Adicionar a regra para a concatenacao de strings
            if (op.equals("+") && (leftType == Type.STRING || rightType == Type.STRING)){
                return Type.STRING;
            }

            // Verificacao para operadores numericos
            if (leftType == Type.INTEGER && rightType == Type.INTEGER){
                return Type.INTEGER;
            } else if ((leftType == Type.INTEGER || leftType == Type.REAL) && (rightType == Type.INTEGER || rightType == Type.REAL)) {
                return Type.REAL; // PElo menos um dos operadores e real
            }else{
                reportTypeError(ctx.op, "Operador'" + op + "'nao pode ser aplicado entre " + leftType + " e " + rightType);
            }
        } else if (op.equals("%")) {
            // Operador % so funciona com inteiros
            if (leftType == Type.INTEGER && rightType == Type.INTEGER){
                return Type.INTEGER;
            }else {
                reportTypeError(ctx.op, "Operador '%' so pode ser aplicado entre inteiros, nao entre " + leftType + " e " + rightType);
            }
        }

        return null; // Nunca deve chegar aqui
    }

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

    private boolean isAssignable(Type varType, Type exprType){
        if (varType == exprType){
            return true;
        }

        // Promocao de INTEGER para REAL
        return varType == Type.REAL && exprType == Type.INTEGER;
    }

    private Type getTypeFromString(String typeStr){
        return switch (typeStr){
            case "inteiro" -> Type.INTEGER;
            case "real" -> Type.REAL;
            case "booleano" -> Type.BOOLEAN;
            case "string" -> Type.STRING;
            default -> throw new TypeCheckingException(
                    "Tipo desconhecido: " + typeStr
            );
        };
    }

    private void reportTypeError(Token token, String message){
        String errorMsg = String.format("Erro de tipo na linha %d:%d - %s", token.getLine(), token.getCharPositionInLine(), message);
        throw new TypeCheckingException(errorMsg);
    }
}
