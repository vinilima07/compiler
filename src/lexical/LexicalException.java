package lexical;

/***
 * Exceção customizada para o Analisador Léxico, dessa forma se sabe a origem
 * do erro e em qual passo o compilador não foi capaz de continuar.
 * 
 */
public class LexicalException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public LexicalException(String msg, int line, int column) {
        super("[ Lexical Exception ] :: " + msg + " " + line + ":" + column);
    }
}
