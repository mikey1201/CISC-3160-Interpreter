import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        String test = """
                x = 001;
                """;
        InputStream in = new ByteArrayInputStream(test.getBytes());
        Lexer lexer = new Lexer(in);
        Parser parser = new Parser(lexer);
        parser.parseProgram();
    }
}
