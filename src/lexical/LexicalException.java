/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexical;

/**
 *
 * @author vinicius
 */
public class LexicalException extends RuntimeException {

    public LexicalException(int line, int column) {
        super("");
        System.out.println("Lexical exception: " + line + "." + column);
    }
}
