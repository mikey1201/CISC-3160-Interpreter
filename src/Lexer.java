import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

class IntegerLiteral extends Token {
    public final int value;
    public IntegerLiteral(int value) {
        super(Tag.INTEGER_LITERAL);
        this.value = value;
    }
}
class Identifier extends Token {
    public final String lexeme;
    public Identifier(String lexeme) {
        super(Tag.IDENTIFIER);
        this.lexeme = lexeme;
    }
}
class Operator extends Token {
    public final char name;
    public Operator(char name) {
        super(Tag.OPERATOR);
        this.name = name;
    }
}
class PunctuationMark extends Token {
    public final char name;
    public PunctuationMark(char name) {
        super(Tag.PUNCTUATION_MARK);
        this.name = name;
    }
}
public class Lexer {
    public int line = 1;
    private char peek = ' ';
    private final Hashtable<String, Token> symbolTable = new Hashtable<>();
    InputStream input;

    public Lexer(InputStream input) {
        this.input = input;
    }

    public Token scan() throws IOException {
        // Skip whitespaces and track line count
        for (;; peek = (char) input.read()) {
            if (!Character.isWhitespace(peek)) break;
            if (peek == '\n') line++;
        }
        // Tokenize
        switch (peek) {
            case '=', '+', '-', '*' -> {
                char operator = peek;
                peek = ' ';
                return new Operator(operator);
            }
            case '(', ')', ';' -> {
                char punctuationMark = peek;
                peek = ' ';
                return new PunctuationMark(punctuationMark);
            }
            case '0' -> {
                peek = (char) input.read();
                if (Character.isDigit(peek)) {
                    throw new RuntimeException("Syntax error: Invalid integer literal on line " + line);
                }
                return new IntegerLiteral(0);
            }
            // Integer literals cannot start with a 0 unless they are only 0
            case '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                int value = 0;
                do {
                    value = 10 * value + (peek - '0');
                    peek = (char) input.read();
                } while (Character.isDigit(peek));
                return new IntegerLiteral(value);
            }
            // Variable names must begin with a letter or underscore they end with empty space
            default -> {
                if (Character.isLetter(peek) || peek == '_') {
                    StringBuilder sb = new StringBuilder();
                    do {
                        sb.append(peek);
                        peek = (char) input.read();
                    } while (Character.isLetterOrDigit(peek) || peek == '_');
                    String s = sb.toString();
                    return symbolTable.computeIfAbsent(s, Identifier::new);
                }
                return null;
            }
        }
    }
}
