package lexical;

public class LexicalException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public LexicalException(String msg, int line, int column) {
        super("[ Lexical Exception ] :: " + msg + " " + line + ":" + column);
    }
}
