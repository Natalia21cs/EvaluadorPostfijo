/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad Ean (Bogotá - Colombia)
 * Departamento de Tecnologías de la Información y Comunicaciones
 * Licenciado bajo el esquema Academic Free License version 2.1
 * <p>
 * Proyecto Evaluador de Expresiones Postfijas
 * Fecha: Febrero 2021
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package universidadean.desarrollosw.postfijo;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.*;

public class EvaluadorPostfijo {

    static boolean Apertura(String element) {
        return element.equals("(") || element.equals("[") || element.equals("{");
    }

    static boolean Cierre(String element) {
        return element.equals(")") || element.equals("]") || element.equals("}");
    }

    static boolean Par(String apertura, String cierre) {
        return (apertura.equals("(") && cierre.equals(")")) ||
                (apertura.equals("[") && cierre.equals("]")) ||
                (apertura.equals("{") && cierre.equals("}"));
    }

    static boolean estaBalanceada(List<String> expresion) {
        Stack<String> delimitadores = new Stack<>();

        for (String element : expresion) {
            if (Apertura(element)) {
                delimitadores.push(element);
            } else if (Cierre(element)) {
                if (delimitadores.isEmpty()) {
                    return false;
                }
                if (!Par(delimitadores.peek(), element)) {
                    return false;
                } else {
                    delimitadores.pop();
                }
            }
        }
        return delimitadores.isEmpty();
    }

    static void reemplazarDelimitadores(List<String> expresion) {
        for (int i = 0; i < expresion.size(); i++) {
            String element = expresion.get(i);
            if (element.equals("[") || element.equals("{")) {
                expresion.set(i, "(");
            } else if (element.equals("]") || element.equals("}")) {
                expresion.set(i, ")");
            }
        }
    }

    static List<String> convertirAPostfijo(List<String> expresion) {
        Stack<String> pilaOperadores = new Stack<>();
        List<String> salida = new ArrayList<>();

        for (String element : expresion) {
            if (element.matches("\\d+")) {
                salida.add(element);
            } else if (esOperador(element)) {
                while (!pilaOperadores.isEmpty() && precedencia(element) <= precedencia(pilaOperadores.peek())) {
                    salida.add(pilaOperadores.pop());
                }
                pilaOperadores.push(element);
            } else if (element.equals("(")) {
                pilaOperadores.push(element);
            } else if (element.equals(")")) {
                while (!pilaOperadores.isEmpty() && !pilaOperadores.peek().equals("(")) {
                    salida.add(pilaOperadores.pop());
                }
                pilaOperadores.pop();
            }
        }

        while (!pilaOperadores.isEmpty()) {
            salida.add(pilaOperadores.pop());
        }

        return salida;
    }

    static boolean esOperador(String elemento) {
        return elemento.equals("+") || elemento.equals("-") || elemento.equals("*") || elemento.equals("/");
    }

    static int precedencia(String operador) {
        if (operador.equals("+") || operador.equals("-")) {
            return 1;
        } else if (operador.equals("*") || operador.equals("/")) {
            return 2;
        }
        return 0;
    }

    static int evaluarPostFija(List<String> expresion) {
        Stack<Integer> pila = new Stack<>();

        for (String elemento : expresion) {
            if (elemento.matches("\\d+")) {
                int numero = Integer.parseInt(elemento);
                pila.push(numero);
            } else if (esOperador(elemento)) {
                int segundoNumero = pila.pop();
                int primerNumero = pila.pop();
                int resultado = aplicarOperador(primerNumero, segundoNumero, elemento);
                pila.push(resultado);
            }
        }

        return pila.pop();
    }

    static int aplicarOperador(int numero1, int numero2, String operador) {
        switch (operador) {
            case "+":
                return numero1 + numero2;
            case "-":
                return numero1 - numero2;
            case "*":
                return numero1 * numero2;
            case "/":
                if (numero2 != 0) {
                    return numero1 / numero2;
                } else {
                    throw new ArithmeticException("División por cero");
                }
            default:
                throw new IllegalArgumentException("Operador no válido: " + operador);
        }
    }

    static List<String> dividir(String expresion) {
        StringReader sr = new StringReader(expresion);
        StreamTokenizer st = new StreamTokenizer(sr);

        st.slashSlashComments(false);
        st.slashStarComments(false);
        st.commentChar('#');
        st.ordinaryChar('/');
        st.ordinaryChar('-');

        List<String> tokenList = new LinkedList<>();

        try {
            int tok = st.nextToken();
            while (tok != StreamTokenizer.TT_EOF) {
                Token t = new Token(tok, st.sval, (int) st.nval);
                tokenList.add(t.getValue());
                tok = st.nextToken();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokenList;
    }
}


