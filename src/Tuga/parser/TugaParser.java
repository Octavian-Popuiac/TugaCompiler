// Generated from /Users/octavianpopuiac/Documents/Universidade/ProjetoCompiladores/Tuga/src/Tuga.g4 by ANTLR 4.13.2
package Tuga.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class TugaParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, INTEGER=16, 
		REAL=17, STRING=18, FUNCAO=19, ESCREVE=20, INICIO=21, FIM=22, ENQUANTO=23, 
		SE=24, SENAO=25, INTEIRO_KW=26, REAL_KW=27, BOOLEANO_KW=28, STRING_KW=29, 
		VERDADEIRO=30, FALSO=31, NAO=32, E=33, OU=34, IGUAL=35, DIFERENTE=36, 
		RETORNA=37, IDENTIFIER=38, WS=39, SL_COMMENT=40, ML_COMMENT=41;
	public static final int
		RULE_program = 0, RULE_globals = 1, RULE_functionDecl = 2, RULE_paramList = 3, 
		RULE_param = 4, RULE_block = 5, RULE_declarations = 6, RULE_declaration = 7, 
		RULE_variableList = 8, RULE_type = 9, RULE_instruction = 10, RULE_functionCall = 11, 
		RULE_exprList = 12, RULE_expression = 13, RULE_literal = 14;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "globals", "functionDecl", "paramList", "param", "block", 
			"declarations", "declaration", "variableList", "type", "instruction", 
			"functionCall", "exprList", "expression", "literal"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "':'", "','", "';'", "'<-'", "'-'", "'*'", "'/'", 
			"'%'", "'+'", "'<'", "'<='", "'>'", "'>='", null, null, null, "'funcao'", 
			"'escreve'", "'inicio'", "'fim'", "'enquanto'", "'se'", "'senao'", "'inteiro'", 
			"'real'", "'booleano'", "'string'", "'verdadeiro'", "'falso'", "'nao'", 
			"'e'", "'ou'", "'igual'", "'diferente'", "'retorna'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, "INTEGER", "REAL", "STRING", "FUNCAO", "ESCREVE", 
			"INICIO", "FIM", "ENQUANTO", "SE", "SENAO", "INTEIRO_KW", "REAL_KW", 
			"BOOLEANO_KW", "STRING_KW", "VERDADEIRO", "FALSO", "NAO", "E", "OU", 
			"IGUAL", "DIFERENTE", "RETORNA", "IDENTIFIER", "WS", "SL_COMMENT", "ML_COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Tuga.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TugaParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public GlobalsContext globals() {
			return getRuleContext(GlobalsContext.class,0);
		}
		public TerminalNode EOF() { return getToken(TugaParser.EOF, 0); }
		public List<FunctionDeclContext> functionDecl() {
			return getRuleContexts(FunctionDeclContext.class);
		}
		public FunctionDeclContext functionDecl(int i) {
			return getRuleContext(FunctionDeclContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitProgram(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(30);
			globals();
			setState(34);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FUNCAO) {
				{
				{
				setState(31);
				functionDecl();
				}
				}
				setState(36);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(37);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GlobalsContext extends ParserRuleContext {
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public GlobalsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_globals; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterGlobals(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitGlobals(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitGlobals(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GlobalsContext globals() throws RecognitionException {
		GlobalsContext _localctx = new GlobalsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_globals);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER) {
				{
				{
				setState(39);
				declaration();
				}
				}
				setState(44);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionDeclContext extends ParserRuleContext {
		public TerminalNode FUNCAO() { return getToken(TugaParser.FUNCAO, 0); }
		public TerminalNode IDENTIFIER() { return getToken(TugaParser.IDENTIFIER, 0); }
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ParamListContext paramList() {
			return getRuleContext(ParamListContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public FunctionDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterFunctionDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitFunctionDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitFunctionDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionDeclContext functionDecl() throws RecognitionException {
		FunctionDeclContext _localctx = new FunctionDeclContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_functionDecl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(45);
			match(FUNCAO);
			setState(46);
			match(IDENTIFIER);
			setState(47);
			match(T__0);
			setState(49);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(48);
				paramList();
				}
			}

			setState(51);
			match(T__1);
			setState(54);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(52);
				match(T__2);
				setState(53);
				type();
				}
			}

			setState(56);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParamListContext extends ParserRuleContext {
		public List<ParamContext> param() {
			return getRuleContexts(ParamContext.class);
		}
		public ParamContext param(int i) {
			return getRuleContext(ParamContext.class,i);
		}
		public ParamListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paramList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterParamList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitParamList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitParamList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamListContext paramList() throws RecognitionException {
		ParamListContext _localctx = new ParamListContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			param();
			setState(63);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(59);
				match(T__3);
				setState(60);
				param();
				}
				}
				setState(65);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParamContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(TugaParser.IDENTIFIER, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitParam(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitParam(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamContext param() throws RecognitionException {
		ParamContext _localctx = new ParamContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_param);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(66);
			match(IDENTIFIER);
			setState(67);
			match(T__2);
			setState(68);
			type();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockContext extends ParserRuleContext {
		public TerminalNode INICIO() { return getToken(TugaParser.INICIO, 0); }
		public TerminalNode FIM() { return getToken(TugaParser.FIM, 0); }
		public DeclarationsContext declarations() {
			return getRuleContext(DeclarationsContext.class,0);
		}
		public List<InstructionContext> instruction() {
			return getRuleContexts(InstructionContext.class);
		}
		public InstructionContext instruction(int i) {
			return getRuleContext(InstructionContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(70);
			match(INICIO);
			setState(72);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(71);
				declarations();
				}
				break;
			}
			setState(77);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 412345172000L) != 0)) {
				{
				{
				setState(74);
				instruction();
				}
				}
				setState(79);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(80);
			match(FIM);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeclarationsContext extends ParserRuleContext {
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public DeclarationsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declarations; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterDeclarations(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitDeclarations(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitDeclarations(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationsContext declarations() throws RecognitionException {
		DeclarationsContext _localctx = new DeclarationsContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_declarations);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(83); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(82);
					declaration();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(85); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeclarationContext extends ParserRuleContext {
		public VariableListContext variableList() {
			return getRuleContext(VariableListContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87);
			variableList();
			setState(88);
			match(T__2);
			setState(89);
			type();
			setState(90);
			match(T__4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableListContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(TugaParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(TugaParser.IDENTIFIER, i);
		}
		public VariableListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterVariableList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitVariableList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitVariableList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableListContext variableList() throws RecognitionException {
		VariableListContext _localctx = new VariableListContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_variableList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92);
			match(IDENTIFIER);
			setState(97);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(93);
				match(T__3);
				setState(94);
				match(IDENTIFIER);
				}
				}
				setState(99);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeContext extends ParserRuleContext {
		public TerminalNode INTEIRO_KW() { return getToken(TugaParser.INTEIRO_KW, 0); }
		public TerminalNode REAL_KW() { return getToken(TugaParser.REAL_KW, 0); }
		public TerminalNode BOOLEANO_KW() { return getToken(TugaParser.BOOLEANO_KW, 0); }
		public TerminalNode STRING_KW() { return getToken(TugaParser.STRING_KW, 0); }
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1006632960L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InstructionContext extends ParserRuleContext {
		public InstructionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instruction; }
	 
		public InstructionContext() { }
		public void copyFrom(InstructionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class WriteInstrContext extends InstructionContext {
		public TerminalNode ESCREVE() { return getToken(TugaParser.ESCREVE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public WriteInstrContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterWriteInstr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitWriteInstr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitWriteInstr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ReturnInstrContext extends InstructionContext {
		public TerminalNode RETORNA() { return getToken(TugaParser.RETORNA, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ReturnInstrContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterReturnInstr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitReturnInstr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitReturnInstr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BlockInstrContext extends InstructionContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public BlockInstrContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterBlockInstr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitBlockInstr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitBlockInstr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EmptyInstrContext extends InstructionContext {
		public EmptyInstrContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterEmptyInstr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitEmptyInstr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitEmptyInstr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AssignInstrContext extends InstructionContext {
		public TerminalNode IDENTIFIER() { return getToken(TugaParser.IDENTIFIER, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AssignInstrContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterAssignInstr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitAssignInstr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitAssignInstr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class WhileInstrContext extends InstructionContext {
		public TerminalNode ENQUANTO() { return getToken(TugaParser.ENQUANTO, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public InstructionContext instruction() {
			return getRuleContext(InstructionContext.class,0);
		}
		public WhileInstrContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterWhileInstr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitWhileInstr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitWhileInstr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FunctionCallInstrContext extends InstructionContext {
		public FunctionCallContext functionCall() {
			return getRuleContext(FunctionCallContext.class,0);
		}
		public FunctionCallInstrContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterFunctionCallInstr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitFunctionCallInstr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitFunctionCallInstr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IfElseInstrContext extends InstructionContext {
		public TerminalNode SE() { return getToken(TugaParser.SE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<InstructionContext> instruction() {
			return getRuleContexts(InstructionContext.class);
		}
		public InstructionContext instruction(int i) {
			return getRuleContext(InstructionContext.class,i);
		}
		public TerminalNode SENAO() { return getToken(TugaParser.SENAO, 0); }
		public IfElseInstrContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterIfElseInstr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitIfElseInstr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitIfElseInstr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstructionContext instruction() throws RecognitionException {
		InstructionContext _localctx = new InstructionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_instruction);
		int _la;
		try {
			setState(136);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				_localctx = new WriteInstrContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(102);
				match(ESCREVE);
				setState(103);
				expression(0);
				setState(104);
				match(T__4);
				}
				break;
			case 2:
				_localctx = new AssignInstrContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(106);
				match(IDENTIFIER);
				setState(107);
				match(T__5);
				setState(108);
				expression(0);
				setState(109);
				match(T__4);
				}
				break;
			case 3:
				_localctx = new BlockInstrContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(111);
				block();
				}
				break;
			case 4:
				_localctx = new WhileInstrContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(112);
				match(ENQUANTO);
				setState(113);
				match(T__0);
				setState(114);
				expression(0);
				setState(115);
				match(T__1);
				setState(116);
				instruction();
				}
				break;
			case 5:
				_localctx = new IfElseInstrContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(118);
				match(SE);
				setState(119);
				match(T__0);
				setState(120);
				expression(0);
				setState(121);
				match(T__1);
				setState(122);
				instruction();
				setState(125);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
				case 1:
					{
					setState(123);
					match(SENAO);
					setState(124);
					instruction();
					}
					break;
				}
				}
				break;
			case 6:
				_localctx = new EmptyInstrContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(127);
				match(T__4);
				}
				break;
			case 7:
				_localctx = new FunctionCallInstrContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(128);
				functionCall();
				setState(129);
				match(T__4);
				}
				break;
			case 8:
				_localctx = new ReturnInstrContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(131);
				match(RETORNA);
				setState(133);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 282394558594L) != 0)) {
					{
					setState(132);
					expression(0);
					}
				}

				setState(135);
				match(T__4);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionCallContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(TugaParser.IDENTIFIER, 0); }
		public ExprListContext exprList() {
			return getRuleContext(ExprListContext.class,0);
		}
		public FunctionCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitFunctionCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_functionCall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(138);
			match(IDENTIFIER);
			setState(139);
			match(T__0);
			setState(141);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 282394558594L) != 0)) {
				{
				setState(140);
				exprList();
				}
			}

			setState(143);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ExprListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterExprList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitExprList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitExprList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprListContext exprList() throws RecognitionException {
		ExprListContext _localctx = new ExprListContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_exprList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(145);
			expression(0);
			setState(150);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(146);
				match(T__3);
				setState(147);
				expression(0);
				}
				}
				setState(152);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AndExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode E() { return getToken(TugaParser.E, 0); }
		public AndExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterAndExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitAndExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitAndExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EqualityExprContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode IGUAL() { return getToken(TugaParser.IGUAL, 0); }
		public TerminalNode DIFERENTE() { return getToken(TugaParser.DIFERENTE, 0); }
		public EqualityExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterEqualityExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitEqualityExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitEqualityExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FunctionalCallExprContext extends ExpressionContext {
		public FunctionCallContext functionCall() {
			return getRuleContext(FunctionCallContext.class,0);
		}
		public FunctionalCallExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterFunctionalCallExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitFunctionalCallExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitFunctionalCallExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ComparisonExprContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ComparisonExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterComparisonExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitComparisonExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitComparisonExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BinaryExprContext extends ExpressionContext {
		public Token op;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public BinaryExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterBinaryExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitBinaryExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitBinaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LiteralExprContext extends ExpressionContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public LiteralExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterLiteralExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitLiteralExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitLiteralExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VarExprContext extends ExpressionContext {
		public TerminalNode IDENTIFIER() { return getToken(TugaParser.IDENTIFIER, 0); }
		public VarExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterVarExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitVarExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitVarExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParenExprContext extends ExpressionContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ParenExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterParenExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitParenExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitParenExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnaryExprContext extends ExpressionContext {
		public Token op;
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode NAO() { return getToken(TugaParser.NAO, 0); }
		public UnaryExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterUnaryExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitUnaryExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitUnaryExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class OrExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode OU() { return getToken(TugaParser.OU, 0); }
		public OrExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterOrExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitOrExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitOrExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 26;
		enterRecursionRule(_localctx, 26, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				_localctx = new LiteralExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(154);
				literal();
				}
				break;
			case 2:
				{
				_localctx = new VarExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(155);
				match(IDENTIFIER);
				}
				break;
			case 3:
				{
				_localctx = new FunctionalCallExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(156);
				functionCall();
				}
				break;
			case 4:
				{
				_localctx = new ParenExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(157);
				match(T__0);
				setState(158);
				expression(0);
				setState(159);
				match(T__1);
				}
				break;
			case 5:
				{
				_localctx = new UnaryExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(161);
				((UnaryExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==T__6 || _la==NAO) ) {
					((UnaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(162);
				expression(7);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(185);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(183);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
					case 1:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(165);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(166);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1792L) != 0)) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(167);
						expression(7);
						}
						break;
					case 2:
						{
						_localctx = new BinaryExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(168);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(169);
						((BinaryExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==T__6 || _la==T__10) ) {
							((BinaryExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(170);
						expression(6);
						}
						break;
					case 3:
						{
						_localctx = new ComparisonExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(171);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(172);
						((ComparisonExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 61440L) != 0)) ) {
							((ComparisonExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(173);
						expression(5);
						}
						break;
					case 4:
						{
						_localctx = new EqualityExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(174);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(175);
						((EqualityExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==IGUAL || _la==DIFERENTE) ) {
							((EqualityExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(176);
						expression(4);
						}
						break;
					case 5:
						{
						_localctx = new AndExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(177);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(178);
						match(E);
						setState(179);
						expression(3);
						}
						break;
					case 6:
						{
						_localctx = new OrExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(180);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(181);
						match(OU);
						setState(182);
						expression(2);
						}
						break;
					}
					} 
				}
				setState(187);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralContext extends ParserRuleContext {
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
	 
		public LiteralContext() { }
		public void copyFrom(LiteralContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class RealLiteralContext extends LiteralContext {
		public TerminalNode REAL() { return getToken(TugaParser.REAL, 0); }
		public RealLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterRealLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitRealLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitRealLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StringLiteralContext extends LiteralContext {
		public TerminalNode STRING() { return getToken(TugaParser.STRING, 0); }
		public StringLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterStringLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitStringLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitStringLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BoolLiteralContext extends LiteralContext {
		public TerminalNode VERDADEIRO() { return getToken(TugaParser.VERDADEIRO, 0); }
		public TerminalNode FALSO() { return getToken(TugaParser.FALSO, 0); }
		public BoolLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterBoolLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitBoolLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitBoolLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IntLiteralContext extends LiteralContext {
		public TerminalNode INTEGER() { return getToken(TugaParser.INTEGER, 0); }
		public IntLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).enterIntLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TugaListener ) ((TugaListener)listener).exitIntLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TugaVisitor ) return ((TugaVisitor<? extends T>)visitor).visitIntLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_literal);
		try {
			setState(193);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INTEGER:
				_localctx = new IntLiteralContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(188);
				match(INTEGER);
				}
				break;
			case REAL:
				_localctx = new RealLiteralContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(189);
				match(REAL);
				}
				break;
			case STRING:
				_localctx = new StringLiteralContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(190);
				match(STRING);
				}
				break;
			case VERDADEIRO:
				_localctx = new BoolLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(191);
				match(VERDADEIRO);
				}
				break;
			case FALSO:
				_localctx = new BoolLiteralContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(192);
				match(FALSO);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 13:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 6);
		case 1:
			return precpred(_ctx, 5);
		case 2:
			return precpred(_ctx, 4);
		case 3:
			return precpred(_ctx, 3);
		case 4:
			return precpred(_ctx, 2);
		case 5:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001)\u00c4\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0001\u0000\u0001\u0000"+
		"\u0005\u0000!\b\u0000\n\u0000\f\u0000$\t\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0001\u0005\u0001)\b\u0001\n\u0001\f\u0001,\t\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u00022\b\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0003\u00027\b\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003>\b\u0003\n\u0003\f\u0003"+
		"A\t\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005"+
		"\u0001\u0005\u0003\u0005I\b\u0005\u0001\u0005\u0005\u0005L\b\u0005\n\u0005"+
		"\f\u0005O\t\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0004\u0006T\b\u0006"+
		"\u000b\u0006\f\u0006U\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\b\u0001\b\u0001\b\u0005\b`\b\b\n\b\f\bc\t\b\u0001\t"+
		"\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n~\b\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n\u0086\b\n\u0001\n\u0003"+
		"\n\u0089\b\n\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000b\u008e\b\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0005\f\u0095\b\f\n\f"+
		"\f\f\u0098\t\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r"+
		"\u0001\r\u0001\r\u0001\r\u0003\r\u00a4\b\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0005\r\u00b8\b\r\n\r\f\r\u00bb"+
		"\t\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0003"+
		"\u000e\u00c2\b\u000e\u0001\u000e\u0000\u0001\u001a\u000f\u0000\u0002\u0004"+
		"\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u0000\u0006"+
		"\u0001\u0000\u001a\u001d\u0002\u0000\u0007\u0007  \u0001\u0000\b\n\u0002"+
		"\u0000\u0007\u0007\u000b\u000b\u0001\u0000\f\u000f\u0001\u0000#$\u00d6"+
		"\u0000\u001e\u0001\u0000\u0000\u0000\u0002*\u0001\u0000\u0000\u0000\u0004"+
		"-\u0001\u0000\u0000\u0000\u0006:\u0001\u0000\u0000\u0000\bB\u0001\u0000"+
		"\u0000\u0000\nF\u0001\u0000\u0000\u0000\fS\u0001\u0000\u0000\u0000\u000e"+
		"W\u0001\u0000\u0000\u0000\u0010\\\u0001\u0000\u0000\u0000\u0012d\u0001"+
		"\u0000\u0000\u0000\u0014\u0088\u0001\u0000\u0000\u0000\u0016\u008a\u0001"+
		"\u0000\u0000\u0000\u0018\u0091\u0001\u0000\u0000\u0000\u001a\u00a3\u0001"+
		"\u0000\u0000\u0000\u001c\u00c1\u0001\u0000\u0000\u0000\u001e\"\u0003\u0002"+
		"\u0001\u0000\u001f!\u0003\u0004\u0002\u0000 \u001f\u0001\u0000\u0000\u0000"+
		"!$\u0001\u0000\u0000\u0000\" \u0001\u0000\u0000\u0000\"#\u0001\u0000\u0000"+
		"\u0000#%\u0001\u0000\u0000\u0000$\"\u0001\u0000\u0000\u0000%&\u0005\u0000"+
		"\u0000\u0001&\u0001\u0001\u0000\u0000\u0000\')\u0003\u000e\u0007\u0000"+
		"(\'\u0001\u0000\u0000\u0000),\u0001\u0000\u0000\u0000*(\u0001\u0000\u0000"+
		"\u0000*+\u0001\u0000\u0000\u0000+\u0003\u0001\u0000\u0000\u0000,*\u0001"+
		"\u0000\u0000\u0000-.\u0005\u0013\u0000\u0000./\u0005&\u0000\u0000/1\u0005"+
		"\u0001\u0000\u000002\u0003\u0006\u0003\u000010\u0001\u0000\u0000\u0000"+
		"12\u0001\u0000\u0000\u000023\u0001\u0000\u0000\u000036\u0005\u0002\u0000"+
		"\u000045\u0005\u0003\u0000\u000057\u0003\u0012\t\u000064\u0001\u0000\u0000"+
		"\u000067\u0001\u0000\u0000\u000078\u0001\u0000\u0000\u000089\u0003\n\u0005"+
		"\u00009\u0005\u0001\u0000\u0000\u0000:?\u0003\b\u0004\u0000;<\u0005\u0004"+
		"\u0000\u0000<>\u0003\b\u0004\u0000=;\u0001\u0000\u0000\u0000>A\u0001\u0000"+
		"\u0000\u0000?=\u0001\u0000\u0000\u0000?@\u0001\u0000\u0000\u0000@\u0007"+
		"\u0001\u0000\u0000\u0000A?\u0001\u0000\u0000\u0000BC\u0005&\u0000\u0000"+
		"CD\u0005\u0003\u0000\u0000DE\u0003\u0012\t\u0000E\t\u0001\u0000\u0000"+
		"\u0000FH\u0005\u0015\u0000\u0000GI\u0003\f\u0006\u0000HG\u0001\u0000\u0000"+
		"\u0000HI\u0001\u0000\u0000\u0000IM\u0001\u0000\u0000\u0000JL\u0003\u0014"+
		"\n\u0000KJ\u0001\u0000\u0000\u0000LO\u0001\u0000\u0000\u0000MK\u0001\u0000"+
		"\u0000\u0000MN\u0001\u0000\u0000\u0000NP\u0001\u0000\u0000\u0000OM\u0001"+
		"\u0000\u0000\u0000PQ\u0005\u0016\u0000\u0000Q\u000b\u0001\u0000\u0000"+
		"\u0000RT\u0003\u000e\u0007\u0000SR\u0001\u0000\u0000\u0000TU\u0001\u0000"+
		"\u0000\u0000US\u0001\u0000\u0000\u0000UV\u0001\u0000\u0000\u0000V\r\u0001"+
		"\u0000\u0000\u0000WX\u0003\u0010\b\u0000XY\u0005\u0003\u0000\u0000YZ\u0003"+
		"\u0012\t\u0000Z[\u0005\u0005\u0000\u0000[\u000f\u0001\u0000\u0000\u0000"+
		"\\a\u0005&\u0000\u0000]^\u0005\u0004\u0000\u0000^`\u0005&\u0000\u0000"+
		"_]\u0001\u0000\u0000\u0000`c\u0001\u0000\u0000\u0000a_\u0001\u0000\u0000"+
		"\u0000ab\u0001\u0000\u0000\u0000b\u0011\u0001\u0000\u0000\u0000ca\u0001"+
		"\u0000\u0000\u0000de\u0007\u0000\u0000\u0000e\u0013\u0001\u0000\u0000"+
		"\u0000fg\u0005\u0014\u0000\u0000gh\u0003\u001a\r\u0000hi\u0005\u0005\u0000"+
		"\u0000i\u0089\u0001\u0000\u0000\u0000jk\u0005&\u0000\u0000kl\u0005\u0006"+
		"\u0000\u0000lm\u0003\u001a\r\u0000mn\u0005\u0005\u0000\u0000n\u0089\u0001"+
		"\u0000\u0000\u0000o\u0089\u0003\n\u0005\u0000pq\u0005\u0017\u0000\u0000"+
		"qr\u0005\u0001\u0000\u0000rs\u0003\u001a\r\u0000st\u0005\u0002\u0000\u0000"+
		"tu\u0003\u0014\n\u0000u\u0089\u0001\u0000\u0000\u0000vw\u0005\u0018\u0000"+
		"\u0000wx\u0005\u0001\u0000\u0000xy\u0003\u001a\r\u0000yz\u0005\u0002\u0000"+
		"\u0000z}\u0003\u0014\n\u0000{|\u0005\u0019\u0000\u0000|~\u0003\u0014\n"+
		"\u0000}{\u0001\u0000\u0000\u0000}~\u0001\u0000\u0000\u0000~\u0089\u0001"+
		"\u0000\u0000\u0000\u007f\u0089\u0005\u0005\u0000\u0000\u0080\u0081\u0003"+
		"\u0016\u000b\u0000\u0081\u0082\u0005\u0005\u0000\u0000\u0082\u0089\u0001"+
		"\u0000\u0000\u0000\u0083\u0085\u0005%\u0000\u0000\u0084\u0086\u0003\u001a"+
		"\r\u0000\u0085\u0084\u0001\u0000\u0000\u0000\u0085\u0086\u0001\u0000\u0000"+
		"\u0000\u0086\u0087\u0001\u0000\u0000\u0000\u0087\u0089\u0005\u0005\u0000"+
		"\u0000\u0088f\u0001\u0000\u0000\u0000\u0088j\u0001\u0000\u0000\u0000\u0088"+
		"o\u0001\u0000\u0000\u0000\u0088p\u0001\u0000\u0000\u0000\u0088v\u0001"+
		"\u0000\u0000\u0000\u0088\u007f\u0001\u0000\u0000\u0000\u0088\u0080\u0001"+
		"\u0000\u0000\u0000\u0088\u0083\u0001\u0000\u0000\u0000\u0089\u0015\u0001"+
		"\u0000\u0000\u0000\u008a\u008b\u0005&\u0000\u0000\u008b\u008d\u0005\u0001"+
		"\u0000\u0000\u008c\u008e\u0003\u0018\f\u0000\u008d\u008c\u0001\u0000\u0000"+
		"\u0000\u008d\u008e\u0001\u0000\u0000\u0000\u008e\u008f\u0001\u0000\u0000"+
		"\u0000\u008f\u0090\u0005\u0002\u0000\u0000\u0090\u0017\u0001\u0000\u0000"+
		"\u0000\u0091\u0096\u0003\u001a\r\u0000\u0092\u0093\u0005\u0004\u0000\u0000"+
		"\u0093\u0095\u0003\u001a\r\u0000\u0094\u0092\u0001\u0000\u0000\u0000\u0095"+
		"\u0098\u0001\u0000\u0000\u0000\u0096\u0094\u0001\u0000\u0000\u0000\u0096"+
		"\u0097\u0001\u0000\u0000\u0000\u0097\u0019\u0001\u0000\u0000\u0000\u0098"+
		"\u0096\u0001\u0000\u0000\u0000\u0099\u009a\u0006\r\uffff\uffff\u0000\u009a"+
		"\u00a4\u0003\u001c\u000e\u0000\u009b\u00a4\u0005&\u0000\u0000\u009c\u00a4"+
		"\u0003\u0016\u000b\u0000\u009d\u009e\u0005\u0001\u0000\u0000\u009e\u009f"+
		"\u0003\u001a\r\u0000\u009f\u00a0\u0005\u0002\u0000\u0000\u00a0\u00a4\u0001"+
		"\u0000\u0000\u0000\u00a1\u00a2\u0007\u0001\u0000\u0000\u00a2\u00a4\u0003"+
		"\u001a\r\u0007\u00a3\u0099\u0001\u0000\u0000\u0000\u00a3\u009b\u0001\u0000"+
		"\u0000\u0000\u00a3\u009c\u0001\u0000\u0000\u0000\u00a3\u009d\u0001\u0000"+
		"\u0000\u0000\u00a3\u00a1\u0001\u0000\u0000\u0000\u00a4\u00b9\u0001\u0000"+
		"\u0000\u0000\u00a5\u00a6\n\u0006\u0000\u0000\u00a6\u00a7\u0007\u0002\u0000"+
		"\u0000\u00a7\u00b8\u0003\u001a\r\u0007\u00a8\u00a9\n\u0005\u0000\u0000"+
		"\u00a9\u00aa\u0007\u0003\u0000\u0000\u00aa\u00b8\u0003\u001a\r\u0006\u00ab"+
		"\u00ac\n\u0004\u0000\u0000\u00ac\u00ad\u0007\u0004\u0000\u0000\u00ad\u00b8"+
		"\u0003\u001a\r\u0005\u00ae\u00af\n\u0003\u0000\u0000\u00af\u00b0\u0007"+
		"\u0005\u0000\u0000\u00b0\u00b8\u0003\u001a\r\u0004\u00b1\u00b2\n\u0002"+
		"\u0000\u0000\u00b2\u00b3\u0005!\u0000\u0000\u00b3\u00b8\u0003\u001a\r"+
		"\u0003\u00b4\u00b5\n\u0001\u0000\u0000\u00b5\u00b6\u0005\"\u0000\u0000"+
		"\u00b6\u00b8\u0003\u001a\r\u0002\u00b7\u00a5\u0001\u0000\u0000\u0000\u00b7"+
		"\u00a8\u0001\u0000\u0000\u0000\u00b7\u00ab\u0001\u0000\u0000\u0000\u00b7"+
		"\u00ae\u0001\u0000\u0000\u0000\u00b7\u00b1\u0001\u0000\u0000\u0000\u00b7"+
		"\u00b4\u0001\u0000\u0000\u0000\u00b8\u00bb\u0001\u0000\u0000\u0000\u00b9"+
		"\u00b7\u0001\u0000\u0000\u0000\u00b9\u00ba\u0001\u0000\u0000\u0000\u00ba"+
		"\u001b\u0001\u0000\u0000\u0000\u00bb\u00b9\u0001\u0000\u0000\u0000\u00bc"+
		"\u00c2\u0005\u0010\u0000\u0000\u00bd\u00c2\u0005\u0011\u0000\u0000\u00be"+
		"\u00c2\u0005\u0012\u0000\u0000\u00bf\u00c2\u0005\u001e\u0000\u0000\u00c0"+
		"\u00c2\u0005\u001f\u0000\u0000\u00c1\u00bc\u0001\u0000\u0000\u0000\u00c1"+
		"\u00bd\u0001\u0000\u0000\u0000\u00c1\u00be\u0001\u0000\u0000\u0000\u00c1"+
		"\u00bf\u0001\u0000\u0000\u0000\u00c1\u00c0\u0001\u0000\u0000\u0000\u00c2"+
		"\u001d\u0001\u0000\u0000\u0000\u0012\"*16?HMUa}\u0085\u0088\u008d\u0096"+
		"\u00a3\u00b7\u00b9\u00c1";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}