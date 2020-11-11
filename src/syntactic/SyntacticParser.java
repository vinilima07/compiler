/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syntactic;

import java.util.Arrays;
import java.util.List;
import lexical.Token;
import lexical.TokenType;

/**
 * @author gustavo zille
 * @author vinicius franca
 */
public class SyntacticParser {

    private List<Token> tokens;
    private List<SyntacticException> errors;
    private int index;

    public SyntacticParser(List<Token> tokens) {
        this.tokens = tokens;
        this.index = 0;
    }

    /**
     * Avança o indexador do tokens
     */
    void advance() {
        this.index++;
    }

    /**
     * Consome o token se este for o esperado, caso nao seja e emitido um erro
     * sintatico.
     *
     * @param type
     */
    void eat(TokenType type) {
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
        errors.add(new SyntacticException("Unexpected token", tokens.get(index)));
    }

    /**
     * program ::= program identifier is body
     */
    void program() {
        eat(TokenType.PROGRAM);
        identifier();
        eat(TokenType.IS);
        body();
    }

    /**
     * decl-list ::= decl {";" decl}
     */
    void declList() {
        decl();
        while (tokens.get(index) == TokenType.SEMICOLON) {
            eat(TokenType.SEMICOLON);
            decl();
        }
    }

    /**
     * ident-list ::= identifier {"," identifier}
     */
    void identList() {
        identifier();
        while (tokens.get(index) == TokenType.COMMA) {
            eat(TokenType.COMMA);
            identifier();
        }
    }
    
    /**
     * stmt-list ::= stmt {";" stmt}
     */
    void stmtList() {
        stmt();
        while (tokens.get(index) == TokenType.SEMICOLON) {
            eat(TokenType.SEMICOLON);
            stmt();
        }
    }
    
    /**
     * assign-stmt ::= identifier "=" simple_expr
     */
    void assignStmt() {
        identifier();
        eat(TokenType.EQUAL);
        simpleExpr();
    }
    
    /**
     * condition ::= expression
     */
    void condition() {
        expression();
    }
    
    /**
     * stmt-suffix ::= until condition
     */
    void stmtSuffix() {
        eat(TokenType.UNTIL);
        condition();
    }
    
    /**
     * stmt-prefix ::= while condition do
     */
    void stmtPreffix() {
        eat(TokenType.WHILE);
        condition();
        eat(TokenType.DO);
    }
    
    /**
     * write-stmt ::= out ">>" writable
     */
    void writeStmt() {
        eat(TokenType.OUT);
        eat(TokenType.RIGHT_ARROW);
        writable();
    }
    
    /**
     * expression ::= simple-expr expression-pref
     */
    void expression() {
        simpleExpr();
        expressionPref();
    }
    
    /**
     * expression-pref ::= simple-expr relop simple-expr | λ
     */
    void expressionPref() {
        switch(tokens.get(index).type) {
            case OPEN_PAR:
            case SUB:
            case NOT:
            case INTEGER_CONST:
            case FLOAT_CONST:
            case CHAR_CONST:
                simpleExpr();
                relop();
                simpleExpr();
                break;
        }
    }
    
    /**
     * term ::= factor-a term-esc
     */
    void term() {
        factorA();
        termEsc();
    }
    
    /**
     * term-esc ::= mulop factor-a term-esc | λ
     */
    void termEsc() {
        mulOp();
        factorA();
        switch(tokens.get(index).type) {
            case MUL:
            case DIV:
            case AND:
                termEsc();
                break;
        }
    }
    
    /**
     * factor ::= identifier | constant | "(" expression ")"
     */
    void factor() {
        switch(tokens.get(index).type) {
            case IDENTIFIER:
                eat(TokenType.IDENTIFIER);
                break;
            case INTEGER_CONST:
            case FLOAT_CONST:
            case CHAR_CONST:
                constant();
                break;
            case OPEN_PAR:
                eat(TokenType.OPEN_PAR);
                break;
            default:
                error();
                break;
        }
    }
    
    /**
     * addop ::= "+" | "-" | ||
     */
    void addOp() {
        switch(tokens.get(index).type) {
            case ADD:
                eat(TokenType.ADD);
                break;
            case SUB:
                eat(TokenType.SUB);
                break;
            case OR:
                eat(TokenType.OR);
                break;
            default:
                error();
                break;
        }
    }
    
    /**
     * constant ::= integer_const | float_const | char_const
     */
    void constant() {
        switch(tokens.get(index).type) {
            case INTEGER_CONST:
                eat(TokenType.INTEGER_CONST);
                break;
            case FLOAT_CONST:
                eat(TokenType.FLOAT_CONST);
                break;
            case CHAR_CONST:
                eat(TokenType.CHAR_CONST);
                break;
            default:
                error();
                break;
        }
    }
}

