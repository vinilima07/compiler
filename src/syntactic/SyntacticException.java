package syntactic;


import lexical.Token;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/***
 * Exceção customizada para o Analisador Sintático, dessa forma se sabe a origem
 * do erro e em qual passo o compilador não foi capaz de continuar.
 * 
 */
public class SyntacticException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SyntacticException(String msg, Token token) {
        super("[ Syntactic Exception ] :: " + msg + ": " + token.toString());
    }
}
