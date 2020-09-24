package lexical;

import java.util.HashMap;

/***
 * Classe para armazenar a tabela de simbolos que posteriormente sera 
 * utilizada para conferir os tokens da linguagem.
 * 
 */
public class SymbolTable {
    public HashMap<String, TokenType> table;

    public SymbolTable() {
        table = new HashMap<String, TokenType>();
        
        table.put("program", TokenType.PROGRAM);
        table.put("is", TokenType.IS);
        table.put("declare", TokenType.DECLARE);
        table.put("init", TokenType.INIT);
        table.put("end", TokenType.END);
        table.put("int", TokenType.INT);
        table.put("float", TokenType.FLOAT);
        table.put("char", TokenType.CHAR);
        table.put("if", TokenType.IF);
        table.put("then", TokenType.THEN);
        table.put("else", TokenType.ELSE);
        table.put("repeat", TokenType.REPEAT);
        table.put("until", TokenType.UNTIL);
        table.put("while", TokenType.WHILE);
        table.put("do", TokenType.DO);
        table.put("in", TokenType.IN);
        table.put("out", TokenType.OUT);
        table.put("<<", TokenType.LEFT_ARROW);
        table.put(">>", TokenType.RIGHT_ARROW);
        table.put("(", TokenType.OPEN_BRAC);
        table.put(")", TokenType.CLOSE_BRAC);
        table.put("==", TokenType.EQUAL);
        table.put(">", TokenType.GREATER);
        table.put(">=", TokenType.GREATER_EQ);
        table.put("<", TokenType.LESS);
        table.put("<=", TokenType.LESS_EQ);
        table.put("!=", TokenType.DIFF);
        table.put("+", TokenType.ADD);
        table.put("-", TokenType.SUB);
        table.put("*", TokenType.MUL);
        table.put("/", TokenType.DIV);
        table.put("||", TokenType.OR);
        table.put("&&", TokenType.AND);
    }
}
