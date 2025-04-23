// Generated from /Users/octavianpopuiac/Documents/Universidade/2024:2025/Semestre2/Compiladores/Projeto/Tuga/src/Tuga.g4 by ANTLR 4.13.2
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
	 * Enter a parse tree produced by {@link TugaParser#instruction}.
	 * @param ctx the parse tree
	 */
	void enterInstruction(TugaParser.InstructionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TugaParser#instruction}.
	 * @param ctx the parse tree
	 */
	void exitInstruction(TugaParser.InstructionContext ctx);
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