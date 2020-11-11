/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syntactic;

import java.util.List;
import lexical.Token;
import lexical.TokenType;

/**
 * @author gustavo zille
 * @author vinicius franca
 */
public class SyntacticParser {
    private List <Token> tokens;
    private int index;

    public SyntacticParser(List<Token> tokens) {
        this.tokens = tokens;
        this.index = 0;
    }
    
    void advance() {
        this.index++;
    }
    
    /**
     * Consome o token se este for o esperado, caso nao seja e emitido um erro
     * sintatico.
     * @param type 
     */
    void eat(TokenType type){
        if (type == tokens.get(index).type) {
            advance();
        } else {
            error();
        }
        
    }
    
    /**
     * TODO: implement
     */
    void error() {
        System.out.printf("deu erro kk %s", tokens.get(index));
    }
    
    /**
     * body ::= [declare decl-list] init stmt-list end
     */
    void body() {
        if(tokens.get(index).type == TokenType.DECLARE) {
            advance();
            declList();
        }
        eat(TokenType.INIT);
        stmtList();
        eat(TokenType.END);
    }

    /**
     * decl ::= ident-list : type
     */
    void decl() {
        identList();
        eat(TokenType.COLON);
        type();
    }

    /**
     * type ::= int | float | char
     */
    void type() {
        switch(tokens.get(index).type) {
            case INT:
                eat(TokenType.INT); break;
            case FLOAT:
                eat(TokenType.FLOAT); break;
            case CHAR:
                eat(TokenType.CHAR); break;
            default:
                error(); break;
        }
    }

    /**
     * stmt ::= assign-stmt | if-stmt | while-stmt | repeat-stmt | read-stmt | write-stmt
     */
    void stmt() {
        switch(tokens.get(index).type) {
            case IDENTIFIER:
                assignStmt(); break;
            case IF:
                ifStmt(); break;
            case WHILE:
                whileStmt(); break;
            case REPEAT:
                repeatStmt(); break;
            case IN:
                readStmt(); break;
            case OUT:
                writeStmt(); break;
            default:
                error(); break;
        }
    }

    /**
     * if-stmt ::= if condition then stmt-list if-stmt-pref
     */
    void ifStmt() {
        eat(TokenType.IF);
        condition();
        eat(TokenType.THEN);
        stmtList();
        ifStmtPref();
    }

    /**
     * if-stmt-pref ::= end | else stmt-list end
     */
    void ifStmtPref() {
        switch(tokens.get(index).type) {
            case END:
                eat(TokenType.END); break;
            case FLOAT:
                eat(TokenType.ELSE);
                stmtList();
                eat(TokenType.END);
                break;
            default:
                error(); break;
        }
    }

    /**
     * repeat-stmt ::= repeat stmt-list stmt-suffix
     */
    void repeatStmt() {
        eat(TokenType.REPEAT);
        stmtList();
        stmtSuffix();
    }

    /**
     * while-stmt ::= stmt-prefix stmt-list end
     */
    void whileStmt() {
        stmtPrefix();
        stmtList();
        eat(TokenType.END);
    }

    /**
     * readStmt ::= in "<<" identifier
     */
    void readStmt() {
        eat(TokenType.IN);
        eat(TokenType.LEFT_ARROW);
        identifier();
    }

    /**
     * writable ::= simple-expr | literal
     */
    void writable() {
        switch(tokens.get(index).type) {
            case NOT:
            case SUB:
            case IDENTIFIER:
            case INTEGER_CONST:
            case FLOAT_CONST:
            case CHAR_CONST:
            case OPEN_BRAC:
                simpleExpr(); break;
            case LITERAL:
                eat(TokenType.LITERAL); break;
            default:
                error();
        }
    }

    /**
     * simple-expr ::= term simple-expr-esc
     */
    void simpleExpr() {
        term();
        simpleExprEsc();
    }

    /**
     * simple-expr-esc ::= addop term simple-expr-esc | lambda
     */
    void simpleExprEsc() {
        switch(tokens.get(index).type) {
            case ADD:
            case SUB:
            case OR:
                addop();
                term();
                simpleExprEsc();
                break;
        }
    }

    /**
     * factor-a ::= factor | ! factor | "-" factor
     */
    void factorA() {
        if(tokens.get(index).type == TokenType.NOT) {
            eat(TokenType.NOT);
        } else if(tokens.get(index).type == TokenType.SUB) {
            eat(TokenType.SUB);
        }
        factor();
    }

    /**
     * relop ::= "==" | ">" | ">=" | "<" | "<=" | "!="
     */
    void relop() {
        switch(tokens.get(index).type) {
            case EQUAL:
                eat(TokenType.EQUAL); break;
            case GREATER:
                eat(TokenType.GREATER); break;
            case GREATER_EQ:
                eat(TokenType.GREATER_EQ); break;
            case LESS:
                eat(TokenType.LESS); break;
            case LESS_EQ:
                eat(TokenType.LESS_EQ); break;
            case DIFF:
                eat(TokenType.DIFF); break;
            default:
                error();
        }
    }

    /**
     * mulop ::= "*" | "/" | &&
     */
    void mulop() {
        switch(tokens.get(index).type) {
            case MUL:
                eat(TokenType.MUL); break;
            case DIV:
                eat(TokenType.DIV); break;
            case AND:
                eat(TokenType.AND); break;
            default:
                error();
        }
    }
}
