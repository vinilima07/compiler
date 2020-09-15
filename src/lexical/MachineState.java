package lexical;

public enum MachineState {
    INITIAL,
    NUMBER,
    LITERAL,
    SLASH,
    DQUOTES,
    CMM_OPEN,
    CMM_CLOSE,
    IDENTIFIER,
    BEGIN_FLOAT,
    END_FLOAT,
    BEGIN_CHAR,
    END_CHAR,
    EXCLAMATION,
    EQUAL,
    LESS,
    GREATER,
    AND,
    OR
}
