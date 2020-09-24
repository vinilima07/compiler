import lexical.LexicalParser;

import java.io.IOException;


/**
 * Entrypoint do compilador.
 * @author Vinicius França, Gustavo Zille
 */
public class Main {
    
    
    /***
     * Recebe como argumento de execução o arquivo a ser 
     * analisado pelo analisador léxico e logo em seguida 
     * executa o parser léxico. 
     * @param args 
     */
    public static void main(String[] args) {
        try {
            String filePath = args[0];
            LexicalParser parser = new LexicalParser(filePath);
            
            parser.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
