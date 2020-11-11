package lexical;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.FileReader;

import java.util.ArrayList;
import java.util.List;

/***
 * Parser do analisador léxico, contem como atributo, o arquivo a ser analisado,
 * a lista de tokens encontrados, assim como a tabela de simbolos utilizada para
 * conferência de tokens.
 * 
 */
public class LexicalParser {
    public PushbackReader file;

    private List<Token> tokens;
    private SymbolTable st;

    private int line, column;

    public LexicalParser(String filePath) throws IOException {
        this.file = new PushbackReader(new FileReader(filePath));
        this.tokens = new ArrayList<Token>();
        this.st = new SymbolTable();
        this.column = 0;
        this.line = 1;
    }

    /***
     * Executa a máquina de estados para o arquivo da instancia formando a lista
     * de tokens encontrados.
     * 
     */
    public void run() throws IOException {
        MachineState state = MachineState.INITIAL;
        boolean flagToEnd = false;
        String lexeme = "";
        char ch;

        while (true) {
            ch = getChar();
            
            if (ch == (char)-1) flagToEnd = true;
            
            if (ch == '\n') {
                this.column = 0;
                this.line ++;
            
            } else {
                this.column += ch == '\t' ? 4 : 1;
            }
                        
            switch (state) {
                case INITIAL:
                    lexeme = "";

                    if (Character.isDigit(ch)) {
                        lexeme += ch;
                        state = MachineState.NUMBER;

                    } else if (Character.isLetter(ch)) {
                        lexeme += ch;
                        state = MachineState.IDENTIFIER;

                    } else if (ch == '/') {
                        state = MachineState.SLASH;

                    } else if (ch == '\"') {
                        state = MachineState.LITERAL;

                    } else if (ch == '\'') {
                        state = MachineState.BEGIN_CHAR;

                    } else if (ch == '!') {
                        lexeme += ch;
                        state = MachineState.EXCLAMATION;

                    } else if (ch == '=') {
                        lexeme += ch;
                        state = MachineState.EQUAL;

                    } else if (ch == '<') {
                        lexeme += ch;
                        state = MachineState.LESS;

                    } else if (ch == '>') {
                        lexeme += ch;
                        state = MachineState.GREATER;

                    } else if (ch == '&') {
                        lexeme += ch;
                        state = MachineState.AND;

                    } else if (ch == '|') {
                        lexeme += ch;
                        state = MachineState.OR;

                    } else if (ch == '+') {
                        lexeme += ch;
                        tokens.add(new Token(line, column, lexeme, TokenType.ADD));

                    } else if (ch == '-') {
                        lexeme += ch;
                        tokens.add(new Token(line, column, lexeme, TokenType.SUB));

                    } else if (ch == '*') {
                        lexeme += ch;
                        tokens.add(new Token(line, column, lexeme, TokenType.MUL));

                    } else if (ch == ';') {
                        lexeme += ch;
                        tokens.add(new Token(line, column, lexeme, TokenType.SEMICOLON));

                    } else if (ch == ':') {
                        lexeme += ch;
                        tokens.add(new Token(line, column, lexeme, TokenType.COLON));

                    } else if (ch == ',') {
                        lexeme += ch;
                        tokens.add(new Token(line, column, lexeme, TokenType.COMMA));

                    } else if (ch == '(') {
                        lexeme += ch;
                        tokens.add(new Token(line, column, lexeme, TokenType.OPEN_PAR));

                    } else if (ch == ')') {
                        lexeme += ch;
                        tokens.add(new Token(line, column, lexeme, TokenType.CLOSE_PAR));
                    }
                    break;

                case SLASH:
                    if (ch == '*') {
                        state = MachineState.CMM_OPEN;

                    } else {
                        tokens.add(new Token(line, column, lexeme, TokenType.DIV));
                        state = MachineState.INITIAL;
                        ungetChar(ch);
                    }
                    break;
                    
                case CMM_OPEN:
                    if (ch == '*') {
                        state = MachineState.CMM_CLOSE;
                    }
                    break;

                case CMM_CLOSE:
                    if (ch == '/') {
                        state = MachineState.INITIAL;

                    } else {
                        state = MachineState.CMM_OPEN;
                    }
                    break;

                case LITERAL:
                    if (ch != '\"') {
                        lexeme += ch;

                    } else {
                        tokens.add(new Token(line, column, lexeme, TokenType.LITERAL));
                        state = MachineState.INITIAL;
                    }
                    break;

                case IDENTIFIER:
                    if (Character.isLetterOrDigit(ch) || ch == '_') {
                        lexeme += ch;

                    } else {
                        TokenType type = this.st.table.get(lexeme);

                        if (type == null) {
                            tokens.add(new Token(line, column, lexeme, TokenType.IDENTIFIER));

                        } else {
                            tokens.add(new Token(line, column, lexeme, type));
                        }

                        ungetChar(ch);
                        state = MachineState.INITIAL;
                    }
                    break;

                case NUMBER:
                    if (ch == '.') {
                        lexeme += ch;
                        state = MachineState.BEGIN_FLOAT;

                    } else if (Character.isDigit(ch)) {
                        lexeme += ch;

                    } else {
                        tokens.add(new Token(line, column, lexeme, TokenType.INTEGER_CONST));
                        ungetChar(ch);
                        state = MachineState.INITIAL;
                    }
                    break;

                case BEGIN_FLOAT:
                    if (Character.isDigit(ch)) {
                        lexeme += ch;
                        state = MachineState.END_FLOAT;

                    } else {
                        throw new LexicalException("CONSTANT FLOAT malformed", line, column);
                    }
                    break;

                case END_FLOAT:
                    if (Character.isDigit(ch)) {
                        lexeme += ch;

                    } else {
                        tokens.add(new Token(line, column, lexeme, TokenType.FLOAT_CONST));
                        ungetChar(ch);
                        state = MachineState.INITIAL;
                    }
                    break;
                    
                case BEGIN_CHAR:
                    lexeme += ch;
                    state = MachineState.END_CHAR;

                    break;

                case END_CHAR:
                    if (ch == '\'') {
                        tokens.add(new Token(line, column, lexeme, TokenType.CHAR_CONST));
                        state = MachineState.INITIAL;

                    } else {
                        throw new LexicalException("CONSTANT CHAR malformed", line, column);
                    }
                    break;

                case EXCLAMATION:
                    if (ch == '=') {
                        lexeme += ch;
                        tokens.add(new Token(line, column, lexeme, TokenType.DIFF));

                    } else {
                        tokens.add(new Token(line, column, lexeme, TokenType.NOT));
                        ungetChar(ch);
                    }

                    state = MachineState.INITIAL;
                    break;

                case EQUAL:
                    if (ch == '=') {
                        lexeme += ch;
                        tokens.add(new Token(line, column, lexeme, TokenType.EQUAL));

                    } else {
                        tokens.add(new Token(line, column, lexeme, TokenType.ASSIGN));
                        ungetChar(ch);
                    }

                    state = MachineState.INITIAL;
                    break;

                case LESS:
                    if (ch == '=') {
                        lexeme += ch;
                        tokens.add(new Token(line, column, lexeme, TokenType.LESS_EQ));

                    } else if (ch == '<') {
                        lexeme += ch;
                        tokens.add(new Token(line, column, lexeme, TokenType.LEFT_ARROW));

                    } else {
                        tokens.add(new Token(line, column, lexeme, TokenType.LESS));
                        ungetChar(ch);
                    }

                    state = MachineState.INITIAL;
                    break;

                case GREATER:
                    if (ch == '=') {
                        lexeme += ch;
                        tokens.add(new Token(line, column, lexeme, TokenType.GREATER_EQ));

                    } else if (ch == '>') {
                        lexeme += ch;
                        tokens.add(new Token(line, column, lexeme, TokenType.RIGHT_ARROW));

                    } else {
                        tokens.add(new Token(line, column, lexeme, TokenType.GREATER));
                        ungetChar(ch);
                    }

                    state = MachineState.INITIAL;
                    break;

                case AND:
                    if (ch == '&') {
                        lexeme += ch;
                        tokens.add(new Token(line, column, lexeme, TokenType.AND));
                        state = MachineState.INITIAL;

                    } else {
                        throw new LexicalException("AND operator malformed", line, column);
                    }
                    break;

                case OR:
                    if (ch == '|') {
                        lexeme += ch;
                        tokens.add(new Token(line, column, lexeme, TokenType.OR));
                        state = MachineState.INITIAL;

                    } else {
                        throw new LexicalException("OR operator malformed", line, column);
                    }
                    break;

                default:
                    break;
            }
            
            if (flagToEnd) break;
        }
        
        
    }

    /***
     * Captura o próximo caractere do arquivo.
     * @return
     * @throws IOException 
     */
    private char getChar() throws IOException {
        return (char) file.read();
    }

    /***
     * Reseta a avaliação de um determinado caractere para que este seja
     * reavaliado na máquina de estados.
     * @param ch
     * @throws IOException 
     */
    private void ungetChar(char ch) throws IOException {
        if (ch != '\n') {
            if (ch != '\t') {
                file.unread((int) ch);
                this.column -= 1;

            } else {
                this.column -= 4;
            }
        
        }
    }
    
    /**
     * Retorna a lista de tokens processados pelo analisador léxico
     * @return 
     */
    public List<Token> getTokens() {
        return this.tokens;
    }

    
    public void listTokens() {
        for(Token token: this.tokens) {
            System.out.println(token.toString());
        }
    }
}
