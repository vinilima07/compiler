package lexical;

public enum MachineState {
    INITIAL,
    NUMBER,
    LITERAL,
    SLASH,
    DQUOTES,
    CMM_INIT,
    CMM_CLOSE,
    IDENTIFIER,
    FLOAT,
    BEGIN_CHAR,
    EXCLAMATION,
    EQUAL,
    LESS,
    GREATER,
    AND,
    OR,
    END_CHAR
}
