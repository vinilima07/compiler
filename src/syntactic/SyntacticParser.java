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
    
}
