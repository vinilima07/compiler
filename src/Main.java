import java.io.IOException;
import lexical.LexicalParser;


public class Main {
    public static void main(String[] args) {
        try {
            String path = args[0];
            LexicalParser parser;
            parser = new LexicalParser(path);
            parser.run();
            parser.printTable();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
