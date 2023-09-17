package universidadean.desarrollosw.postfijo;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EvaluadorPostfijoTest {
    /**
     * Realiza la partición de una expresión en sus componenetes
     *
     * @param expresion la expresión a partir
     * @return una lista con los componentes de la expresión
     */
    static List<String> dividir(String expresion) {
        StringReader sr = new StringReader(expresion);
        StreamTokenizer st = new StreamTokenizer(sr);

        //st.whitespaceChars(0, 32);
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

    @Test
    void pruebaEstaBalanceada() {
        assertTrue(EvaluadorPostfijo.estaBalanceada(dividir("[a {b / (c-d) + e/(f+g)}-h]")));
        assertFalse(EvaluadorPostfijo.estaBalanceada(dividir("a {b [c - d ] e] ) f")));
        assertFalse(EvaluadorPostfijo.estaBalanceada(dividir("{a ( b * c ) / [d + e] / f )- g}")));
        System.out.println("Prueba superada 👍");
    }

    //---------------------------------------------------------------------------

    @Test
    void pruebaReemplazarDelimitadores() {
        List<String> lista = dividir("x [ {a ( b * c ) / [d + e] / f }- g]");
        EvaluadorPostfijo.reemplazarDelimitadores(lista);
        List<String> resul = dividir("x ( (a ( b * c ) / (d + e) / f )- g)");
        assertEquals(resul, lista, "El reemplazo de delimitadores no se hizo de forma correcta");
        System.out.println("Prueba superada 👍");
    }

    //--------------------------------------------------------------------------

    static String convertir(String expresion) {
        List<String> otraExpr = dividir(expresion);
        return String.join(" ", EvaluadorPostfijo.convertirAPostfijo(otraExpr));
    }

    @Test
    void pruebaConvertir() {
        String prueba1 = convertir("((40 + 30) - 25)");
        assertEquals("40 30 + 25 -", prueba1);
        System.out.println("Prueba 1 superada 👍");

        String postfijo = convertir("(a + (b * c))");
        assertEquals("a b c * +", postfijo);
        System.out.println("Prueba 2 superada 👍");

        postfijo = convertir("((a - b) * c)");
        assertEquals("a b - c *", postfijo);
        System.out.println("Prueba 3 superada!");

        postfijo = convertir("((a % b) * (c % d))");
        assertEquals("a b % c d % *", postfijo);
        System.out.println("Prueba 4 superada!");

        postfijo = convertir("(a / (b * (c + (d - 5))))");
        assertEquals("a b c d 5 - + * /", postfijo);
        System.out.println("Prueba 5 superada!");

        // 6. Prueba
        postfijo = convertir("((a / (b - c)) * d)");
        assertEquals("a b c - / d *", postfijo);
        System.out.println("Prueba 6 superada!");

        // 7. Prueba
        postfijo = convertir("((a - ((b / ((c - d) * e)) + f )) % g)");
        assertEquals("a b c d - e * / f + - g %", postfijo);
        System.out.println("Prueba 7 superada!");

        // 8. Prueba
        postfijo = convertir("(((a - b) * c) / (((d * e) / (f % g)) + h))");
        assertEquals("a b - c * d e * f g % / h + /", postfijo);
        System.out.println("Prueba 8 superada!");

        // 9. Prueba
        postfijo = convertir("(a * (((b + c) * d) + e))");
        assertEquals("a b c + d * e + *", postfijo);
        System.out.println("Prueba 9 superada!");
    }

    @Test
    void pruebaFinal() {
        List<String> aEvaluar = dividir("({[3 * 3] / (4 - 2)} + {5 * 6})");
        List<String> expresionFinal;
        int valorFinal;

        if (EvaluadorPostfijo.estaBalanceada(aEvaluar)) {
            EvaluadorPostfijo.reemplazarDelimitadores(aEvaluar);
            expresionFinal = EvaluadorPostfijo.convertirAPostfijo(aEvaluar);
            valorFinal = EvaluadorPostfijo.evaluarPostFija(expresionFinal);
            assertEquals(34, valorFinal);
            System.out.println("Primera prueba superada 👍");
        } else {
            fail();
        }
        //------------------------------------------------------------
        aEvaluar = dividir("([2 + 18] % {15 - 8})");
        if (EvaluadorPostfijo.estaBalanceada(aEvaluar)) {
            EvaluadorPostfijo.reemplazarDelimitadores(aEvaluar);
            expresionFinal = EvaluadorPostfijo.convertirAPostfijo(aEvaluar);
            valorFinal = EvaluadorPostfijo.evaluarPostFija(expresionFinal);
            assertEquals(6, valorFinal);
            System.out.println("Segunda prueba superada 👍");
        } else {
            fail();
        }
    }

}