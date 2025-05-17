grammar Tuga;

// PARSER RULES
program :   globals functionDecl* EOF;

globals : declaration*;

functionDecl : FUNCAO IDENTIFIER '(' paramList? ')' (':' type)?block;

paramList : param(',' param)*;

param : IDENTIFIER ':' type;

block : INICIO declarations? instruction* FIM;

// DECLARATIONS
declarations : declaration+ ;
declaration : variableList ':' type ';' ;
variableList : IDENTIFIER (',' IDENTIFIER)* ;
type         : INTEIRO_KW | REAL_KW | BOOLEANO_KW | STRING_KW ;

instruction
    :   ESCREVE expression ';'                                        # WriteInstr
    |   IDENTIFIER '<-' expression ';'                                  # AssignInstr
    |   block                                                           # BlockInstr
    |   ENQUANTO '(' expression ')' instruction                       # WhileInstr
    |   SE '(' expression ')' instruction (SENAO instruction)?      # IfElseInstr
    |   ';'                                                             # EmptyInstr
    |   functionCall ';'                                                # FunctionCallInstr
    |   RETORNA expression? ';'                                       # ReturnInstr
    ;

functionCall : IDENTIFIER '(' exprList? ')';

exprList : expression (',' expression)*;
// EXPRESSOES
expression
    :   literal                                                 # LiteralExpr
    |   IDENTIFIER                                              # VarExpr
    |   functionCall                                            # FunctionalCallExpr
    |   '(' expression ')'                                      # ParenExpr
    |   op=('-' | NAO) expression                             # UnaryExpr
    |   expression op=('*' | '/' | '%') expression              # BinaryExpr
    |   expression op=('+' | '-') expression                    # BinaryExpr
    |   expression op=('<' | '<=' | '>' | '>=') expression      # ComparisonExpr
    |   expression op=(IGUAL | DIFERENTE) expression        # EqualityExpr
    |   expression E expression                               # AndExpr
    |   expression OU expression                              # OrExpr
    ;

literal
    :   INTEGER     # IntLiteral
    |   REAL        # RealLiteral
    |   STRING      # StringLiteral
    |   VERDADEIRO  # BoolLiteral
    |   FALSO       # BoolLiteral
    ;

// LEXER RULES
INTEGER:    [0-9]+ ;
REAL:   [0-9]+ '.' [0-9]* | '.' [0-9]+ ;
STRING: '"' (~["\r\n] | '\\"')* '"' ;

// RESERVED WORDS
FUNCAO: 'funcao';
ESCREVE: 'escreve';
INICIO: 'inicio';
FIM: 'fim';
ENQUANTO: 'enquanto';
SE: 'se';
SENAO: 'senao';
INTEIRO_KW: 'inteiro';
REAL_KW: 'real';
BOOLEANO_KW: 'booleano';
STRING_KW: 'string';
VERDADEIRO: 'verdadeiro';
FALSO: 'falso';
NAO: 'nao';
E: 'e';
OU: 'ou';
IGUAL: 'igual';
DIFERENTE: 'diferente';
RETORNA: 'retorna';

// IDENTIFIER
IDENTIFIER: [a-zA-Z_] [a-zA-Z0-9_]* ;

// IGNORE WHITESPACE AND COMMENTS
WS : [ \t\r\n]+ -> skip ;
SL_COMMENT : '//' .*? (EOF|'\n') -> skip ; // single-line comment
ML_COMMENT : '/*' .*? '*/' -> skip ; // multi-line comment
