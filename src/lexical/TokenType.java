package lexical;

public enum TokenType {
    // SCRIPT
    INVALID_TOKEN,
    UNEXPECTED_EOF,
    END_OF_FILE,
    
    // CONTANT
    LITERAL,
    CHAR_CONST,
    FLOAT_CONST,
    INTEGER_CONST,
    
    // REL_OP
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
    MINUS,
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
    FLOAT,

}
