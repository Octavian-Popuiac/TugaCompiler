grammar Tuga;

// PARSER RULES
program :   instruction+ EOF;

instruction :   'escreve' expression ';';

// EXPRESSOES
expression
    :   literal                                                 # LiteralExpr
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
    |   BOOLEAN     # BoolLiteral
    ;

// LEXER RULES
INTEGER:    [0-9]+ ;
REAL:   [0-9]+ '.' [0-9]* | '.' [0-9]+ ;
STRING: '"' (~["\r\n] | '\\"')* '"' ;
BOOLEAN:    'verdadeiro' | 'falso';

// IGNORE WHITESPACE AND COMMENTS
WS : [ \t\r\n]+ -> skip ;
SL_COMMENT : '//' .*? (EOF|'\n') -> skip ; // single-line comment
ML_COMMENT : '/*' .*? '*/' -> skip ; // multi-line comment
