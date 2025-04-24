// Generated from /Users/octavianpopuiac/Documents/Universidade/ProjetoCompiladores/Tuga/src/Tuga.g4 by ANTLR 4.13.2
package Tuga.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TugaParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface TugaVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link TugaParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(TugaParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link TugaParser#declarations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarations(TugaParser.DeclarationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link TugaParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(TugaParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link TugaParser#variableList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableList(TugaParser.VariableListContext ctx);
	/**
	 * Visit a parse tree produced by {@link TugaParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(TugaParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WriteInstr}
	 * labeled alternative in {@link TugaParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWriteInstr(TugaParser.WriteInstrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AssignInstr}
	 * labeled alternative in {@link TugaParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignInstr(TugaParser.AssignInstrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BlockInstr}
	 * labeled alternative in {@link TugaParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockInstr(TugaParser.BlockInstrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WhileInstr}
	 * labeled alternative in {@link TugaParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileInstr(TugaParser.WhileInstrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfElseInstr}
	 * labeled alternative in {@link TugaParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfElseInstr(TugaParser.IfElseInstrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EmptyInstr}
	 * labeled alternative in {@link TugaParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyInstr(TugaParser.EmptyInstrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(TugaParser.AndExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EqualityExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExpr(TugaParser.EqualityExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ComparisonExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonExpr(TugaParser.ComparisonExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BinaryExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryExpr(TugaParser.BinaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LiteralExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralExpr(TugaParser.LiteralExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VarExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarExpr(TugaParser.VarExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpr(TugaParser.ParenExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpr(TugaParser.UnaryExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrExpr}
	 * labeled alternative in {@link TugaParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpr(TugaParser.OrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntLiteral}
	 * labeled alternative in {@link TugaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntLiteral(TugaParser.IntLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RealLiteral}
	 * labeled alternative in {@link TugaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRealLiteral(TugaParser.RealLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringLiteral}
	 * labeled alternative in {@link TugaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(TugaParser.StringLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BoolLiteral}
	 * labeled alternative in {@link TugaParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolLiteral(TugaParser.BoolLiteralContext ctx);
}