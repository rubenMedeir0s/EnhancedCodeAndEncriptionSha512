import com.sun.tools.javac.Main;

import java.util.Arrays;
import java.util.List;

public class CifraCeaser {
    public static String guardarTipoAlfabetoXML;
    public static int incrementarValor;
    public static int rotacao;
    public static String alfabeto = "";

    static String cifraCeaser(String palavra, int rot, int F) {
        int index = 0;
        String novaPalavra = "";
        // Lista de Strings onde vai dividir todo os letras do alfabeto em espaços vazios
        List<String> alfabeto = Arrays.stream(CifraCeaser.alfabeto.split("")).toList();
        // Pega num caracter e divide por espaços vazios
        for (String caracter : palavra.split("")) {
            // Formula do enunciado
            int inc = index * F;
            // Pega no alfabeto e indexa os caracteres todos
            int alfabetoIndex = alfabeto.indexOf(caracter);

            if (alfabetoIndex == -1) {
                novaPalavra = novaPalavra + caracter;
                continue;
            }
            // A nova letra vai ter o alfabeto indexado e vai somar as rotações e o incremento
            int novaLetra = alfabetoIndex + rot + inc;
            // Ciclo que valida que enquanto a nova letra é maior do que o tamanho do alfabeto (Não há letras depois do Z)
            while (novaLetra >= alfabeto.size()) {
                // A nova letra tem que ser a nova letra menos o tramanho do alfabeto para tudo correr bem
                novaLetra = novaLetra - alfabeto.size();
            }
            // A letra vai receber a nova letra do alfabeto quando sai do ciclo
            String letra = alfabeto.get(novaLetra);
            // Na nova palavra vai ser adicionada a nova letra
            novaPalavra = novaPalavra + letra;
            index++;
        }
        return novaPalavra;
    }

    static String decifrarCifraCeaser(String palavra, int rot, int F) {
        int index = 0;
        String novaPalavra = "";
        // Lista de Strings onde vai dividir todo os letras do alfabeto em espaços vazios
        List<String> alfabeto = Arrays.stream(CifraCeaser.alfabeto.split("")).toList();
        // Pega num caracter e divide por espaços vazios
        for (String caracter : palavra.split("")) {
            // Formula do enunciado
            int inc = index * F;
            // Pega no alfabeto e indexa os caracteres todos
            int alfabetoIndex = alfabeto.indexOf(caracter);
            if (alfabetoIndex == -1) {
                novaPalavra = novaPalavra + caracter;
                continue;
            }
            // A nova letra vai ter o alfabeto indexado e vai somar as rotações e o incremento
            int novaLetra = alfabetoIndex - rot - inc;
            // Ciclo que valida que enquanto a nova letra é maior do que o tamanho do alfabeto (Não há letras depois do Z)
            while (novaLetra < 0) {
                // A nova letra tem que ser a nova letra menos o tramanho do alfabeto para tudo correr bem
                novaLetra = novaLetra + (alfabeto.size() -1);
            }
            // A letra vai receber a nova letra do alfabeto quando sai do ciclo
            String letra = alfabeto.get(novaLetra);
            // Na nova palavra vai ser adicionada a nova letra
            novaPalavra = novaPalavra + letra;
            index++;
        }
        return novaPalavra;
    }
}
