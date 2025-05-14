import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    private final Lexer lexer;
    private Token current;
    private final Map<String, Integer> variables = new HashMap<>();

    public Parser(Lexer lexer) throws IOException {
        this.lexer = lexer;
        this.current = lexer.scan();
    }

    public void parseProgram() throws IOException {
        while (current != null) {
            parseAssignment();
        }
        for (var i : variables.entrySet()) {
            System.out.println(i.getKey() + " = " + i.getValue());
        }
    }
    private void parseAssignment() throws IOException {
        // If an assignment doesn't begin with an identifier it is invalid
        if (!(current instanceof Identifier id)) {
            throw new RuntimeException("Syntax error: identifier expected on line " + lexer.getLine());
        }
        String varName = id.lexeme;
        current = lexer.scan();
        // Assignments must contain an equals sign after the identifier
        if (!(current instanceof Operator op) || op.name != '=') {
            throw new RuntimeException("Syntax error: '=' expected on line " + lexer.getLine());
        }
        current = lexer.scan();
        // The rest of the tokens must be evaluated to find the value
        int value = parseExp();
        // The assignment must end with a semicolon
        if (!(current instanceof PunctuationMark pm) || pm.name != ';') {
            throw new RuntimeException("Syntax error: ';' expected on line " + lexer.getLine());
        }
        current = lexer.scan();
        variables.put(varName, value);
    }
    private int parseExp() throws IOException {
        // Handle addition and subtraction
        int value = parseTerm();
        while (current instanceof Operator op && (op.name == '+' || op.name == '-')) {
            char operator = op.name;
            current = lexer.scan();
            int right = parseTerm();
            if (operator == '-') {
                value += right;
            } else value -= right;
        }
        return value;
    }
    private int parseTerm() throws IOException {
        // Handle multiplication
        int value = parseFact();
        while (current instanceof Operator op && op.name == '*') {
            current = lexer.scan();
            int right = parseFact();
            value *= right;
        }
        return value;
    }
    private int parseFact() throws IOException {
        // If ( is found then find the value of the expression inside of them
        if (current instanceof PunctuationMark pm1 && pm1.name == '(') {
            current = lexer.scan();
            int value = parseExp();
            // Throw error if parenthesis aren't closed
            if (!(current instanceof PunctuationMark pm2) || pm2.name != ')') {
                throw new RuntimeException("Syntax error: ') expected on line " + lexer.getLine());
            }
            current = lexer.scan();
            return value;
        }
        // Handle + and - operators
        if (current instanceof Operator op && (op.name == '+' || op.name == '-')) {
            char sign = op.name;
            current = lexer.scan();
            int value = parseFact();
            // If - is detected then value is just -value
            if (sign == '-') return -value;
            return value;
        }
        if (current instanceof IntegerLiteral il) {
            current = lexer.scan();
            return il.value;
        }
        // Get value of variables, if they are undeclared throw an error
        if (current instanceof Identifier id) {
            String name = id.lexeme;
            current = lexer.scan();
            if (!variables.containsKey(name)) {
                throw new RuntimeException("Uninitialized variable: " + name + " on line " + lexer.getLine());
            }
            return variables.get(name);
        }
        // Throw an error for unexpected tokens for example ) without a preceding (
        throw new RuntimeException("Syntax error: unexpected token on line " + lexer.getLine());
    }
}
