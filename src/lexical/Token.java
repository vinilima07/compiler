package lexical;

/***
 * Contém como atributo o lexeme, que é a sequencia de caracteres 
 * lidas até formar um token e o TokenType que é o tipo de token 
 * baseado no lexema.
 * 
 */
public class Token {
    public String lexeme;
    public TokenType type;
    public int line;
    public int column;

    public Token(int line, int column, String lexeme, TokenType type) {
        this.line = line;
        this.column = column;
        this.lexeme = lexeme;
        this.type = type;
    }
    
    public String toString() {
        return "[TYPE]: " + this.type.toString() +
               " [LEXEME]: " + this.lexeme + 
               " [POS] l:" + line + " c:" + column;
    }
}
