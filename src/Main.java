import lexical.LexicalParser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            String filePath = args[0];
            LexicalParser parser = new LexicalParser(filePath);

            parser.run();
            parser.listTokens();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
