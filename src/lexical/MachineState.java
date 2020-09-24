package lexical;

/***
 * Estrutura de dados que representa os estados da maquina de 
 * estados identificadora de tokens e formadora de lexemas.
 * 
 */
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
