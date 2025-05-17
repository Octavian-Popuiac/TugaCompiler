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

public class TypeChecker extends TugaBaseVisitor<Type> {
    private FunctionSymbol currentFunction = null;
    private Type voidType = Type.VOID;
    private SymbolTable symbolTable = new SymbolTable();

    // Lista para armazenar todos os erros encontrados
    private List<String> errors = new ArrayList<>();
    private Set<String> reportedErrors = new HashSet<>();

    // Metodo para verificar se existe erros
    public boolean hasErrors(){
        return !errors.isEmpty();
    }

    // Metodo para obter todos os erros
    public List<String> getErrors(){
        return errors;
    }

    // Em vez de lancar excecao, o erro e adicionado a lista
    private void reportError(String message){
        if (!reportedErrors.contains(message)){
            errors.add(message);
            reportedErrors.add(message);
        }
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    /*
    @Override
    public Type visitProgram(TugaParser.ProgramContext ctx){
        System.out.println("| TYPECHECKER ERRORS |");
        for (TugaParser.FunctionDeclContext funcDecl : ctx.functionDecl()){
            registerFunctionSignature(funcDecl);
        }
        // Processar as declaracoes de variaveis globais
        if (ctx.globals() != null){
            visit(ctx.globals());
        }

        // Verificar se existe funcao 'principal'
        boolean hasPrincipal = false;

        // Processar todas as declaracoes de funcoes
        for (TugaParser.FunctionDeclContext funcDecl : ctx.functionDecl()){
            visit(funcDecl);
            if (funcDecl.IDENTIFIER().getText().equals("principal")){
                hasPrincipal = true;
            }
        }

        if (!hasPrincipal){
            int lastLine = ctx.stop.getLine();

            reportError(
                    "erro na linha " + lastLine + ": falta funcao principal()"

            );
        }
        System.out.println("| END TYPECHECKER |");

        return null;
    }

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

    private int extractLineNumber(String errorMsg){
        try {
            int startIndex = errorMsg.indexOf("linha") + 6;
            int endIndex = errorMsg.indexOf(":", startIndex);
            return Integer.parseInt(errorMsg.substring(startIndex,endIndex));
        }catch (Exception e){
            return Integer.MAX_VALUE;
        }
    }

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

        try {
            symbolTable.declare(functionSymbol, ctx.IDENTIFIER().getSymbol());
        }catch (TypeCheckingException e){
            reportError("erro na linha " + ctx.IDENTIFIER().getSymbol().getLine() + ": '" + funcName + "' ja foi declarado");
        }
    }

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

            // Declarar a funcao no escopo atual
            //symbolTable.declare(functionSymbol, ctx.IDENTIFIER().getSymbol());

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

    @Override
    public Type visitFunctionalCallExpr(TugaParser.FunctionalCallExprContext ctx){
        /*
        Type type = visit(ctx.functionCall());
        if (type == voidType){
            reportError(
                    String.format(
                            "erro na linha %d: funcao '%s' nao retorna um valor",
                            ctx.getStart().getLine(),
                            ctx.functionCall().IDENTIFIER().getText()
                    )
            );
            return Type.ERROR;
        }
         */

        //return type;
        return visit(ctx.functionCall());
    }

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

    @Override
    public Type visitParamList(TugaParser.ParamListContext ctx){
        for (TugaParser.ParamContext param : ctx.param()){
            visit(param);
        }

        return null;
    }

    @Override
    public Type visitParam(TugaParser.ParamContext ctx){
        String paramName = ctx.IDENTIFIER().getText();
        Type paramType = getTypeFromString(ctx.type().getText());

        try {
            // Criar um VarSymbol com flag a indicar que e um parametro
            VarSymbol paramSymbol = new VarSymbol(ctx.IDENTIFIER().getSymbol(), paramType, true);
            symbolTable.declare(paramSymbol, ctx.IDENTIFIER().getSymbol());
        } catch (TypeCheckingException e) {
            reportError(e.getMessage());
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
            try {
                // Criar um VarSymbol para cada variavel
                VarSymbol varSymbol = new VarSymbol(id.getSymbol(), type, false);
                symbolTable.declare(varSymbol, id.getSymbol());
            }catch (TypeCheckingException e){
                reportError(String.format(
                        "erro na linha %d: '%s' ja foi declarado",
                        id.getSymbol().getLine(),
                        varName
                ));
            }
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

    @Override
    public Type visitBlockInstr(TugaParser.BlockInstrContext ctx){
        return visit(ctx.block());
        /*
        String blockId = "bloco_" + ctx.hashCode() + "_" + ctx.getStart().getLine();
        symbolTable.enterScope(blockId);

        // Processar declaracoes locais se houver
        if (ctx.declarations() != null){
            visit(ctx.declarations());
        }

        // Visitar todas as instrucoes do bloco
        for (TugaParser.InstructionContext instr : ctx.instruction()){
            visit(instr);
        }

        symbolTable.exitScope();

        return null;
         */
    }

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

    @Override
    public Type visitEmptyInstr(TugaParser.EmptyInstrContext ctx){
        // Nao faz nada
        return  null;
    }

    @Override
    public Type visitVarExpr(TugaParser.VarExprContext ctx){
        String varName = ctx.IDENTIFIER().getText();

        try {
           Symbol symbol = symbolTable.lookup(varName, ctx.IDENTIFIER().getSymbol());

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
       }catch (TypeCheckingException e){
           reportError(e.getMessage());
           return Type.ERROR;
       }
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
            case "void" -> Type.VOID;
            default -> throw new TypeCheckingException(
                    "Tipo desconhecido: " + typeStr
            );
        };
    }

    private void reportTypeError(Token token, String message){
        String errorMsg = String.format(
                "erro na linha %d: %s",
                token.getLine(),
                message
        );
        reportError(errorMsg);
    }
}
