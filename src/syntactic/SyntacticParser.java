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
 * Parser do analisador sintatico, recebe a lista de tokens do analisado
 * lexico (pela main) e executa a analise
 * @author gustavo zille
 * @author vinicius franca
 */
public class SyntacticParser {

    public List<Token> tokens;
    public List<SyntacticException> errors;
    public int index;

    public SyntacticParser(List<Token> tokens) {
        this.tokens = tokens;
        this.index = 0;
    }

    /**
     * Avança o indexador do tokens
     */
    public void advance() {
        if (index+1 >= tokens.size() && tokens.get(index).type != TokenType.END) {
            error();
        }
        this.index++;
    }

    /**
     * Consome o token se este for o esperado, caso nao seja e emitido um erro
     * sintatico.
     *
     * @param type
     */
    public void eat(TokenType type) {
        if (type != tokens.get(index).type ) {
            error();
        } 
        advance();
    }

    /**
     * Emite um erro semantico
     */
    public void error() {
        for (Token t: tokens.subList(0, index)) System.out.println(t.toString());
        throw new SyntacticException("Unexpected token", tokens.get(index));
    }
    
    /**
     * Inicia o parser sintatico
     */
    public void run(){
        System.out.println("\nExecutando Analisador Sintatico...");
        program();
        System.out.println("Análise Sintatica finalizada sem erros\n");
    }

    /**
     * program ::= program identifier is body
     */
    public void program() {
        eat(TokenType.PROGRAM);
        eat(TokenType.IDENTIFIER);
        eat(TokenType.IS);
        body();
    }

    /**
     * decl-list ::= decl {";" decl}
     */
    public void declList() {
        decl();
        while (tokens.get(index).type == TokenType.SEMICOLON) {
            eat(TokenType.SEMICOLON);
            decl();
        }
    }

    /**
     * ident-list ::= identifier {"," identifier}
     */
    public void identList() {
        eat(TokenType.IDENTIFIER);
        while (tokens.get(index).type == TokenType.COMMA) {
            eat(TokenType.COMMA);
            eat(TokenType.IDENTIFIER);
        }
    }
    
    /**
     * stmt-list ::= stmt {";" stmt}
     */
    public void stmtList() {
        stmt();
        while (tokens.get(index).type == TokenType.SEMICOLON) {
            eat(TokenType.SEMICOLON);
            stmt();
        }
    }
    
    /**
     * assign-stmt ::= identifier "=" simple-expr
     */
    public void assignStmt() {
        eat(TokenType.IDENTIFIER);
        eat(TokenType.ASSIGN);
        simpleExpr();
    }
    
    /**
     * condition ::= expression
     */
    public void condition() {
        expression();
    }
    
    /**
     * body ::= [declare decl-list] init stmt-list end
     */
    public void body() {
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
    public void decl() {
        identList();
        eat(TokenType.COLON);
        type();
    }

    /**
     * type ::= int | float | char
     */
    public void type() {
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
    public void stmt() {
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
    public void ifStmt() {
        eat(TokenType.IF);
        condition();
        eat(TokenType.THEN);
        stmtList();
        ifStmtPref();
    }

    /**
     * if-stmt-pref ::= end | else stmt-list end
     */
    public void ifStmtPref() {
        switch(tokens.get(index).type) {
            case END:
                eat(TokenType.END); 
                break;
            case ELSE:
                eat(TokenType.ELSE);
                stmtList();
                eat(TokenType.END);
                break;
            default:
                error(); 
                break;
        }
    }

    /**
     * repeat-stmt ::= repeat stmt-list stmt-suffix
     */
    public void repeatStmt() {
        eat(TokenType.REPEAT);
        stmtList();
        stmtSuffix();
    }

    /**
     * while-stmt ::= stmt-prefix stmt-list end
     */
    public void whileStmt() {
        stmtPreffix();
        stmtList();
        eat(TokenType.END);
    }

    /**
     * readStmt ::= in "<<" identifier
     */
    public void readStmt() {
        eat(TokenType.IN);
        eat(TokenType.LEFT_ARROW);
        eat(TokenType.IDENTIFIER);
    }

    /**
     * writable ::= simple-expr | literal
     */
    public void writable() {
        switch(tokens.get(index).type) {
            case NOT:
            case SUB:
            case IDENTIFIER:
            case INTEGER_CONST:
            case FLOAT_CONST:
            case CHAR_CONST:
            case OPEN_PAR:
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
    public void simpleExpr() {
        term();
        simpleExprEsc();
    }

    /**
     * simple-expr-esc ::= addop term simple-expr-esc | lambda
     */
    public void simpleExprEsc() {
        switch(tokens.get(index).type) {
            case ADD:
            case SUB:
            case OR:
                addOp();
                term();
                simpleExprEsc();
                break;
        }
    }

    /**
     * factor-a ::= factor | ! factor | "-" factor
     */
    public void factorA() {
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
    public void relop() {
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
    public void mulOp() {
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
    
    /**
     * stmt-suffix ::= until condition
     */
    public void stmtSuffix() {
        eat(TokenType.UNTIL);
        condition();
    }
    
    /**
     * stmt-prefix ::= while condition do
     */
    public void stmtPreffix() {
        eat(TokenType.WHILE);
        condition();
        eat(TokenType.DO);
    }
    
    /**
     * write-stmt ::= out ">>" writable
     */
    public void writeStmt() {
        eat(TokenType.OUT);
        eat(TokenType.RIGHT_ARROW);
        writable();
    }
    
    /**
     * expression ::= simple-expr expression-pref
     */
    public void expression() {
        simpleExpr();
        expressionPref();
    }
    
    /**
     * expression-pref ::= relop simple-expr | λ
     */
    public void expressionPref() {
        switch(tokens.get(index).type) {
            case EQUAL:
            case GREATER:
            case GREATER_EQ:
            case LESS:
            case LESS_EQ:
            case DIFF:
                relop();
                simpleExpr();
                break;
        }
    }
    
    /**
     * term ::= factor-a term-esc
     */
    public void term() {
        factorA();
        termEsc();
    }
    
    /**
     * term-esc ::= mulop factor-a term-esc | λ
     */
    public void termEsc() {
        switch(tokens.get(index).type) {
            case MUL:
            case DIV:
            case AND:
                mulOp();
                factorA();
                termEsc();
                break;
        }
    }
    
    /**
     * factor ::= identifier | constant | "(" expression ")"
     */
    public void factor() {
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
                expression();
                eat(TokenType.CLOSE_PAR);
                break;
            default:
                error();
                break;
        }
    }
    
    /**
     * addop ::= "+" | "-" | ||
     */
    public void addOp() {
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
    public void constant() {
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

