// Generated from /Users/octavianpopuiac/Documents/Universidade/ProjetoCompiladores/Tuga/src/Tuga.g4 by ANTLR 4.13.2
package Tuga.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TugaParser}.
 */
public interface TugaListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TugaParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(TugaParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link TugaParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(TugaParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link TugaParser#declarations}.
	 * @param ctx the parse tree
	 */
	void enterDeclarations(TugaParser.DeclarationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TugaParser#declarations}.
	 * @param ctx the parse tree
	 */
	void exitDeclarations(TugaParser.DeclarationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TugaParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(TugaParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TugaParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(TugaParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TugaParser#variableList}.
	 * @param ctx the parse tree
	 */
	void enterVariableList(TugaParser.VariableListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TugaParser#variableList}.
	 * @param ctx the parse tree
	 */
	void exitVariableList(TugaParser.VariableListContext ctx);
	/**
	 * Enter a parse tree produced by {@link TugaParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(TugaParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TugaParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(TugaParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WriteInstr}
	 * labeled alternative in {@link TugaParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterWriteInstr(TugaParser.WriteInstrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WriteInstr}
	 * labeled alternative in {@link TugaParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitWriteInstr(TugaParser.WriteInstrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AssignInstr}
	 * labeled alternative in {@link TugaParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterAssignInstr(TugaParser.AssignInstrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AssignInstr}
	 * labeled alternative in {@link TugaParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitAssignInstr(TugaParser.AssignInstrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BlockInstr}
	 * labeled alternative in {@link TugaParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterBlockInstr(TugaParser.BlockInstrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BlockInstr}
	 * labeled alternative in {@link TugaParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitBlockInstr(TugaParser.BlockInstrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WhileInstr}
	 * labeled alternative in {@link TugaParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterWhileInstr(TugaParser.WhileInstrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WhileInstr}
	 * labeled alternative in {@link TugaParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitWhileInstr(TugaParser.WhileInstrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfElseInstr}
	 * labeled alternative in {@link TugaParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterIfElseInstr(TugaParser.IfElseInstrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfElseInstr}
	 * labeled alternative in {@link TugaParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitIfElseInstr(TugaParser.IfElseInstrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EmptyInstr}
	 * labeled alternative in {@link TugaParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterEmptyInstr(TugaParser.EmptyInstrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EmptyInstr}
	 * labeled alternative in {@link TugaParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitEmptyInstr(TugaParser.EmptyInstrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(TugaParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(TugaParser.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EqualityExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpr(TugaParser.EqualityExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EqualityExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpr(TugaParser.EqualityExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ComparisonExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterComparisonExpr(TugaParser.ComparisonExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ComparisonExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitComparisonExpr(TugaParser.ComparisonExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinaryExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExpr(TugaParser.BinaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BinaryExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExpr(TugaParser.BinaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralExpr(TugaParser.LiteralExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralExpr(TugaParser.LiteralExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VarExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterVarExpr(TugaParser.VarExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VarExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitVarExpr(TugaParser.VarExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterParenExpr(TugaParser.ParenExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitParenExpr(TugaParser.ParenExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpr(TugaParser.UnaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpr(TugaParser.UnaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(TugaParser.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(TugaParser.OrExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntLiteral}
	 * labeled alternative in {@link TugaParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterIntLiteral(TugaParser.IntLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntLiteral}
	 * labeled alternative in {@link TugaParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitIntLiteral(TugaParser.IntLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RealLiteral}
	 * labeled alternative in {@link TugaParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterRealLiteral(TugaParser.RealLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RealLiteral}
	 * labeled alternative in {@link TugaParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitRealLiteral(TugaParser.RealLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link TugaParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(TugaParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link TugaParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(TugaParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BoolLiteral}
	 * labeled alternative in {@link TugaParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterBoolLiteral(TugaParser.BoolLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BoolLiteral}
	 * labeled alternative in {@link TugaParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitBoolLiteral(TugaParser.BoolLiteralContext ctx);
}