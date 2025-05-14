import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

enum Tag {
    INTEGER_LITERAL,
    IDENTIFIER,
    OPERATOR,
    PUNCTUATION_MARK
}
class Token {
    public final Tag tag;
    public Token(Tag tag) {
        this.tag = tag;
    }
}
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
    private Hashtable<String, Token> symbolTable = new Hashtable<>();
    InputStream input;

    public Lexer(InputStream input) {
        this.input = input;
    }

    public Token scan() throws IOException {
        for (;; peek = (char) input.read()) {
            if (!Character.isWhitespace(peek)) break;
            if (peek == '\n') line++;
        }
        switch (peek) {
            case '=', '+', '-', '*' -> {
                char operator = peek;
                peek = ' ';
                return new Operator(operator);
            }
            case ';' -> {
                char punctuationMark = peek;
                peek = ' ';
                return new PunctuationMark(punctuationMark);
            }
            case '0' -> {
                peek = ' ';
                return new IntegerLiteral(0);
            }
            case '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                int value = 0;
                do {
                    value = 10 * value + (peek - '0');
                    peek = (char) input.read();
                } while (Character.isDigit(peek));
                return new IntegerLiteral(value);
            }
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
