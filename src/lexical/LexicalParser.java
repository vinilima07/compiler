package lexical;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.FileReader;

import java.util.ArrayList;
import java.util.List;

public class LexicalParser {
    public PushbackReader file;

    private List<Token> tokens;
    private SymbolTable st;

    public LexicalParser(String filePath) throws IOException {
        this.file = new PushbackReader(new FileReader(filePath));
        this.tokens = new ArrayList<Token>();
        this.st = new SymbolTable();
    }

    public void run() throws IOException {
        MachineState state = MachineState.INITIAL;
        int line = 0, column = 0;
        String lexeme = "";
        char ch;

        while ((ch = getChar()) != (char) -1) {
            column++;

            if (ch == '\n') {
                column = 0;
                line++;
            }

            switch (state) {
                case INITIAL:
                    lexeme = "";

                    if (Character.isDigit(ch)) {
                        lexeme += ch;
                        state = MachineState.NUMBER;

                    /*
                    TODO: Verificar posicionamento do caso.

                    } else if (Character.isLetter(ch)) {
                        lexeme += ch;

                        state = MachineState.IDENTIFIER;
                    */

                    } else if (ch == '/') {
                        state = MachineState.SLASH;

                    } else if (ch == '\"') {
                        state = MachineState.LITERAL;

                    }else if (ch == '\'') {
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
                        tokens.add(new Token(lexeme, TokenType.ADD));

                    } else if (ch == '-') {
                        lexeme += ch;
                        tokens.add(new Token(lexeme, TokenType.SUB));

                    } else if (ch == '*') {
                        lexeme += ch;
                        tokens.add(new Token(lexeme, TokenType.MUL));

                    } else if (ch == ';') {
                        lexeme += ch;
                        tokens.add(new Token(lexeme, TokenType.SEMICOLON));

                    } else if (ch == ':') {
                        lexeme += ch;
                        tokens.add(new Token(lexeme, TokenType.COLON));

                    } else if (ch == ',') {
                        lexeme += ch;
                        tokens.add(new Token(lexeme, TokenType.COMMA));

                    } else if (ch == '(') {
                        lexeme += ch;
                        tokens.add(new Token(lexeme, TokenType.OPEN_BRAC));

                    } else if (ch == ')') {
                        lexeme += ch;
                        tokens.add(new Token(lexeme, TokenType.CLOSE_BRAC));
                    
                    } else if (ch != '\n' && ch != '\t' && ch != ' ') {
                        lexeme += ch;
                        state = MachineState.IDENTIFIER;
                    }
                    break;

                case SLASH:
                    if (ch == '*') {
                        state = MachineState.CMM_OPEN;

                    } else {
                        tokens.add(new Token(lexeme, TokenType.DIV));
                        state = MachineState.INITIAL;
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
                        tokens.add(new Token(lexeme, TokenType.LITERAL));
                        state = MachineState.INITIAL;
                    }
                    break;

                case IDENTIFIER:
                    if (Character.isLetterOrDigit(ch)) {
                        lexeme += ch;

                    } else {
                        TokenType type = this.st.table.get(lexeme);

                        if (type == null) {
                            tokens.add(new Token(lexeme, TokenType.IDENTIFIER));

                        } else {
                            tokens.add(new Token(lexeme, type));
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
                        tokens.add(new Token(lexeme, TokenType.INTEGER_CONST));
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
                        tokens.add(new Token(lexeme, TokenType.FLOAT_CONST));
                        ungetChar(ch);
                        state = MachineState.INITIAL;
                    }

                case BEGIN_CHAR:
                    lexeme += ch;
                    state = MachineState.END_CHAR;

                    break;

                case END_CHAR:
                    if (ch == '\'') {
                        tokens.add(new Token(lexeme, TokenType.CHAR_CONST));
                        state = MachineState.INITIAL;

                    } else {
                        throw new LexicalException("CONSTANT CHAR malformed", line, column);
                    }
                    break;

                case EXCLAMATION:
                    if (ch == '=') {
                        lexeme += ch;
                        tokens.add(new Token(lexeme, TokenType.DIFF));

                    } else {
                        tokens.add(new Token(lexeme, TokenType.NOT));
                        ungetChar(ch);
                    }

                    state = MachineState.INITIAL;
                    break;

                case EQUAL:
                    if (ch == '=') {
                        lexeme += ch;
                        tokens.add(new Token(lexeme, TokenType.EQUAL));

                    } else {
                        tokens.add(new Token(lexeme, TokenType.ASSIGN));
                        ungetChar(ch);
                    }

                    state = MachineState.INITIAL;
                    break;

                case LESS:
                    if (ch == '=') {
                        lexeme += ch;
                        tokens.add(new Token(lexeme, TokenType.LESS_EQ));

                    } else if (ch == '<') {
                        lexeme += ch;
                        tokens.add(new Token(lexeme, TokenType.LEFT_ARROW));

                    } else {
                        tokens.add(new Token(lexeme, TokenType.LESS));
                        ungetChar(ch);
                    }

                    state = MachineState.INITIAL;
                    break;

                case GREATER:
                    if (ch == '=') {
                        lexeme += ch;
                        tokens.add(new Token(lexeme, TokenType.GREATER_EQ));

                    } else if (ch == '>') {
                        lexeme += ch;
                        tokens.add(new Token(lexeme, TokenType.RIGHT_ARROW));

                    } else {
                        tokens.add(new Token(lexeme, TokenType.GREATER));
                        ungetChar(ch);
                    }

                    state = MachineState.INITIAL;
                    break;

                case AND:
                    if (ch == '&') {
                        lexeme += ch;
                        tokens.add(new Token(lexeme, TokenType.AND));
                        state = MachineState.INITIAL;

                    } else {
                        throw new LexicalException("AND operator malformed", line, column);
                    }
                    break;

                case OR:
                    if (ch == '|') {
                        lexeme += ch;
                        tokens.add(new Token(lexeme, TokenType.OR));
                        state = MachineState.INITIAL;

                    } else {
                        throw new LexicalException("OR operator malformed", line, column);
                    }
                    break;

                default:
                    break;
            }
        }
    }

    private char getChar() throws IOException {
        return (char) file.read();
    }

    private void ungetChar(char ch) throws IOException {
        file.unread((int) ch);
    }

    public void listTokens() {
        for(Token token: this.tokens) {
            System.out.println(token.toString());
        }
    }
}