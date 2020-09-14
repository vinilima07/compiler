package lexical;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.ArrayList;
import java.util.List;

public class LexicalParser {
    
    private List<Token> tokens;
    public PushbackReader file;
    public int count;

    public LexicalParser(String path) throws FileNotFoundException {
        this.file = new PushbackReader(new FileReader(path));
        this.tokens = new ArrayList<Token>();
        this.count = 0;
        System.out.println(file.markSupported());
    }

    public void run() throws IOException {
        MachineState state = MachineState.INITIAL;
        String lexeme = "";
        int column = 0;
        int line = 0;
        char ch;

        while ((ch = nextChar()) != (char) -1) {
            column++;
            if (ch == '\n') {
                line++;
            }

            switch (state) {
                case INITIAL:
                    lexeme = "";
                    if (Character.isDigit(ch)) {
                        lexeme += ch;
                        state = MachineState.NUMBER;
                    } else if (Character.isLetter(ch)) {
                        lexeme += ch;
                        // TODO: VERIFICAR SE É UMA
                        state = MachineState.IDENTIFIER;
                    } else if (ch == '/') {
                        state = MachineState.SLASH;
                    } else if (ch == '"') {
                        state = MachineState.LITERAL;
                    }
                    if (ch == '\'') {
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
                        tokens.add(new Token(lexeme, TokenType.MINUS));

                    } else if (ch == '*') {
                        lexeme += ch;
                        tokens.add(new Token(lexeme, TokenType.MUL));

                    } else if (ch == ';') {
                        lexeme += ch;
                        tokens.add(new Token(lexeme, TokenType.SEMICOLON));

                    } else if (ch == '(') {
                        lexeme += ch;
                        tokens.add(new Token(lexeme, TokenType.OPEN_BRAC));

                    } else if (ch == ')') {
                        lexeme += ch;
                        tokens.add(new Token(lexeme, TokenType.CLOSE_BRAC));
                    }
                    break;
                case SLASH:
                    if (ch == '*') {
                        state = MachineState.CMM_INIT;
                    } else {
                        tokens.add(new Token(lexeme, TokenType.DIV));
                        state = MachineState.INITIAL;
                    }
                    break;
                case CMM_INIT:
                    if (ch == '*') {
                        state = MachineState.CMM_CLOSE;
                    }
                    break;
                case CMM_CLOSE:
                    if (ch == '/') {
                        state = MachineState.INITIAL;
                    } else {
                        state = MachineState.CMM_INIT;
                    }
                    break;
                case LITERAL:
                    if (ch != '"') {
                        lexeme += ch;
                    } else {
                        tokens.add(new Token(lexeme, TokenType.LITERAL));
                        state = MachineState.INITIAL;
                    }
                    break;
                case IDENTIFIER:
                    if (Character.isLetterOrDigit(ch) || ch == '_') {
                        lexeme += ch;
                    } else {
                        ungetChar(ch);
                        tokens.add(new Token(lexeme, TokenType.IDENTIFIER));
                        state = MachineState.INITIAL;
                    }
                    break;
                case NUMBER:
                    if (ch == '.') {
                        lexeme += ch;
                        state = MachineState.FLOAT;
                    } else if (Character.isDigit(ch)) {
                        lexeme += ch;
                    } else {
                        tokens.add(new Token(lexeme, TokenType.INTEGER_CONST));
                        ungetChar(ch);
                        state = MachineState.INITIAL;
                    }
                    break;
                case FLOAT:
                    if (Character.isDigit(ch)) {
                        lexeme += ch;
                    } else {
                        tokens.add(new Token(lexeme, TokenType.FLOAT_CONST));
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
                        tokens.add(new Token(lexeme, TokenType.CHAR_CONST));
                        state = MachineState.INITIAL;

                    } else {
                        throw new LexicalException(line, column);
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
                        throw new LexicalException(line, column);
                    }
                    break;

                case OR:
                    if (ch == '|') {
                        lexeme += ch;
                        tokens.add(new Token(lexeme, TokenType.OR));
                        state = MachineState.INITIAL;

                    } else {
                        throw new LexicalException(line, column);
                    }
                    break;

            }

        }

    }

    private char nextChar() throws IOException {
        return (char) file.read();
    }

    private void ungetChar(char ch) throws IOException {
        file.unread((int) ch);
    }

    public void printTable() {
        for(Token token: this.tokens) {
            System.out.println(token.toString());
        }
    }
}
