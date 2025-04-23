grammar Tuga;

// PARSER RULES
program :   declarations? instruction* EOF;

// DECLARATIONS
declarations : declaration+ ;
declaration : variableList ':' type ';' ;
variableList : IDENTIFIER (',' IDENTIFIER)* ;
type         : 'inteiro' | 'real' | 'booleano' | 'string' ;

instruction
    :   'escreve' expression ';'                                        # WriteInstr
    |   IDENTIFIER '<-' expression ';'                                  # AssignInstr
    |   'inicio' instruction* 'fim'                                      # BlockInstr
    |   'enquanto' '(' expression ')' instruction                       # WhileInstr
    |   'se' '(' expression ')' instruction ('senao' instruction)?      # IfElseInstr
    |   ';'                                                             # EmptyInstr
    ;
// EXPRESSOES
expression
    :   literal                                                 # LiteralExpr
    |   IDENTIFIER                                              # VarExpr
    |   '(' expression ')'                                      # ParenExpr
    |   op=('-' | 'nao') expression                             # UnaryExpr
    |   expression op=('*' | '/' | '%') expression              # BinaryExpr
    |   expression op=('+' | '-') expression                    # BinaryExpr
    |   expression op=('<' | '<=' | '>' | '>=') expression      # ComparisonExpr
    |   expression op=('igual' | 'diferente') expression        # EqualityExpr
    |   expression 'e' expression                               # AndExpr
    |   expression 'ou' expression                              # OrExpr
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

// IDENTIFIER
IDENTIFIER: [a-zA-Z_] [a-zA-Z0-9_]* ;

// IGNORE WHITESPACE AND COMMENTS
WS : [ \t\r\n]+ -> skip ;
SL_COMMENT : '//' .*? (EOF|'\n') -> skip ; // single-line comment
ML_COMMENT : '/*' .*? '*/' -> skip ; // multi-line comment
