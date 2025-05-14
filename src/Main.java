import java.io.*;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Only one argument allowed.");
            System.exit(1);
        }
        String filename = args[0];
        try (FileInputStream input = new FileInputStream(filename)) {
            Lexer lexer = new Lexer(input);
            Parser parser = new Parser(lexer);
            parser.parseProgram();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
