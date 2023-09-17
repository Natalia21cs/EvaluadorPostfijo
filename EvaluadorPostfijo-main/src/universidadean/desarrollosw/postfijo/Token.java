package universidadean.desarrollosw.postfijo;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de los datos que son reconocidos en una expresión
 */
class Token {
    public enum TokenType {
        TOKEN_OPERATOR,
        TOKEN_NUMBER,
        TOKEN_IDENTIFIER,
        TOKEN_PARENTHESIS,
        TOKEN_OTHER,
        TOKEN_NONE
    }

    // Atributos
    private TokenType type;
    private String svalue;
    private int ivalue;

    // Constructor
    Token(int tokenType, String sVal, int iVal) {
        char f;

        switch (tokenType) {
            case StreamTokenizer.TT_NUMBER:
                this.type = TokenType.TOKEN_NUMBER;
                this.ivalue = iVal;
                this.svalue = String.valueOf(iVal);
                break;

            case StreamTokenizer.TT_WORD:
                f = sVal.charAt(0);
                if (Character.isJavaIdentifierStart(f)) {
                    this.type = TokenType.TOKEN_IDENTIFIER;
                }
                else {
                    this.type = TokenType.TOKEN_OTHER;
                }
                this.svalue = sVal;
                break;

            default:
                f = (char) tokenType;
                if (f == '+' || f == '-' || f == '*' || f == '/' || f == '%' || f == '^') {
                    this.type = TokenType.TOKEN_OPERATOR;
                    this.ivalue = tokenType;
                    this.svalue = String.valueOf(f);
                }
                else if (f == '(' || f == '[' || f == '{' || f == '}' || f == ']' || f == ')') {
                    this.type = TokenType.TOKEN_PARENTHESIS;
                    this.ivalue = tokenType;
                    this.svalue = String.valueOf(f);
                }
                else {
                    this.type = TokenType.TOKEN_NONE;
                    this.svalue = sVal;
                    this.ivalue = iVal;
                }
                break;
        }
    }

    public boolean isNumber() {
        return type == TokenType.TOKEN_NUMBER;
    }

    public boolean isParenthesis() {
        return type == TokenType.TOKEN_PARENTHESIS;
    }

    public boolean isOperator() {
        return type == TokenType.TOKEN_OPERATOR;
    }

    public boolean isIdentifier() {
        return type == TokenType.TOKEN_IDENTIFIER;
    }

    public int getNumber() {
        if (isNumber()) {
            return this.ivalue;
        }
        return 0;
    }

    public char getOperator() {
        if (isOperator()) {
            return (char) this.ivalue;
        }
        return ' ';
    }

    public char getParenthesis() {
        if (isParenthesis()) {
            return (char) this.ivalue;
        }
        return ' ';
    }

    public String getIdentifier() {
        if (isIdentifier()) {
            return svalue;
        }
        return null;
    }

    public String getValue() {
        return svalue;
    }

    @Override
    public String toString() {
        return String.format("Token{Tipo: %s, valor: '%s'}",
                type.toString(),
                (isNumber() ? String.valueOf(ivalue) : svalue));
    }
}
