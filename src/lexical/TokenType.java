package lexical;

public enum TokenType {
    // FILE
    INVALID_TOKEN,
    UNEXPECTED_EOF,
    END_OF_FILE,
    
    // CONSTANT
    LITERAL,
    CHAR_CONST,
    FLOAT_CONST,
    INTEGER_CONST,
    
    // RELATIONAL OP
    NOT,
    AND,
    OR,
    EQUAL,
    DIFF,
    LESS,
    GREATER,
    LESS_EQ,
    GREATER_EQ,
  
    // ARITMETIC OP
    MUL,
    DIV,
    ADD,
    SUB,
    LEFT_ARROW,
    RIGHT_ARROW,
    
    // RESERVED WORDS
    IF,
    THEN,
    ELSE,
    UNTIL,
    WHILE,
    DO,
    REPEAT,
    IN,
    OUT,
    IDENTIFIER,
    SEMICOLON,
    COLON,
    COMMA,
    OPEN_BRAC,
    CLOSE_BRAC,
    ASSIGN,
    PROGRAM,
    IS,
    DECLARE,
    INIT,
    END,
    
    // TYPE
    INT,
    CHAR,
    FLOAT
}
