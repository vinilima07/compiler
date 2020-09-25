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

    public Token(String lexeme, TokenType type) {
        this.lexeme = lexeme;
        this.type = type;
        System.out.println(this.toString());
    }
    
    public String toString() {
        return "[TYPE]: " + this.type.toString() + " [LEXEME]: " + this.lexeme;
    }
}
