import java.util.HashMap;
import java.util.Locale;

public class Plugboard {

    private static final HashMap<String, String> plugboardMap = new HashMap<>();
    private static final HashMap<String, String> reversePlugboard = new HashMap<>();

    public static void carregarPlugboard(String s) {
        // Apaga tudo o que estiver dentro da variável criada para evitar ter alguma coisa sempre que carrego a plugboard
        plugboardMap.clear();
        // Verifica se a plugboad tem {}
        if (s.equals("{}")) {
            return;
        }
        // Substitui a { por vazio
        s = s.replace("{", "");
        // Substitui a } por vazio
        s = s.replace("}", "");
        // Ciclo que separa todas as palavras que tiverem uma , e um espaço entre elas
        for (String s1 : s.split(", ")) {
            s1 = s1.trim();
            // Substitui as ' por vazio e de seguida separa as letras que tiverem : e um espaço entre elas
            String[] s2 = s1.replace("'", "").split(": ");
            // Demonstração de como está neste momento -> [N,Y]
            // Crio um char onde na posição 0 guardo a letra da esquerda
            char c = s2[0].charAt(0);
            // Crio um char onde na posição 1 guardo a letra da direita
            char c1 = s2[1].charAt(0);
            // Validação se as letras da equerda e da direita são realmente letras
            if (Character.isLetter(c) && Character.isLetter(c1)) {
                // Insere as letras dentro da nova plugboard
                plugboardMap.put("" + c, "" + c1);
                reversePlugboard.put("" + c1, "" + c);
            }
        }
        System.out.println(plugboardMap);
        System.out.println(reversePlugboard);
    }


    /**
     * SUBSTITUI AS LETRAS DO PLUGBOARD ONDE ENVIO UMA PALAVRA À SORTE PARA SER ALTERADA PELO PLUGBOARD
     *
     * @param palavra
     * @return
     */
    static String substituirPlugboard(String palavra) {
        String novaPalavra = "";
        // Ciclo onde separa as letras da palavra por espaços vazios
        for (String letra : palavra.split("")) {
            // Insere a nova palavra dentro da plugboard
            novaPalavra = novaPalavra + plugboardMap.getOrDefault(letra, letra);
        }
        // Retorna a nova palavra alterada
        return novaPalavra;
    }

    public String reverterPlugboard(String textoCifra) {
        String novaPalavra = "";
        // Ciclo onde separa as letras da palavra por espaços vazios
        for (String letra : textoCifra.split("")) {
            // Insere a nova palavra dentro da plugboard
            novaPalavra = novaPalavra + reversePlugboard.getOrDefault(letra, letra);
        }
        // Retorna a nova palavra alterada
        return novaPalavra;
    }
}
