import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {

    private static final String plugboard = "{'N': 'Y', 'X': 'P', 'M': 'R', 'Z': 'G', 'E': 'O', 'B': 'T', 'A': 'V', 'S': 'H', 'Q': 'U', 'F': 'W'}";
    private static final String alfabetoSalt = "ABCDEFGHIJKLM";
    private static final String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String wordlist = "C:\\sad\\src\\main\\java\\wordlist.txt";
    private static final String sha512 = "cef35cb86a83430637c0f49367d2e6b54e5e2eff12c506c1324280a31f271c5aca10be36d3da810f78ff82b34621632385395acc32a4c26dcd7190f6c78718ed";

    private static final HashMap<String, String> plugboardMap = new HashMap<>();

    /**
     * CARREGA A PLUGBOARD SEM {}, SEM ' E SEM :
     *
     * @param s
     */
    private static void carregarPlugboard(String s) {
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
            }
        }
    }

    /**
     * SUBSTITUI AS LETRAS DO PLUGBOARD ONDE ENVIO UMA PALAVRA À SORTE PARA SER ALTERADA PELO PLUGBOARD
     *
     * @param palavra
     * @return
     */
    private static String substituirPlugboard(String palavra) {
        String novaPalavra = "";
        // Ciclo onde separa as letras da palavra por espaços vazios
        for (String letra : palavra.split("")) {
            // Insere a nova palavra dentro da plugboard
            novaPalavra = novaPalavra + plugboardMap.getOrDefault(letra, letra);
        }
        // Retorna a nova palavra alterada
        return novaPalavra;
    }

    /**
     * SALT PARA FAZER TESTES DE KAT ONDE RECEBE COMO PARÂMETRO UMA PALAVRA DE INPUT
     *
     * @return
     */
    private static List<String> saltKat(String palavra) {
        // Lista onde vai guardar todo o processo SALT num array
        List<String> lista = new ArrayList<>();
        // Ciclo onde vai subistuir todas as letras do alfabeto por espaços vazios
        for (String a : alfabeto.split("")) {
            for (String b : alfabeto.split("")) {
                //SALT À ESQUERDA
                lista.add(a + b + ";" + palavra);
                //SALT À DIREITA
                lista.add(palavra + ";" + a + b);
            }
        }
        return lista;
    }

    /**
     * SALT PARA FAZER O BRUTE FORCE ONDE RECEBE UMA PALAVRA COMO PARÂMETRO
     * @param palavra
     * @return
     */
    private static List<String> salt(String palavra) {
        // Lista onde vai ser recebida o salt final
        List<String> lista = new ArrayList<>();
        // Ciclo onde corre todas as primeiras letras no alfabeto salt
        for (String a : alfabetoSalt.split("")) {
            // Ciclo onde corre todas as segundas letras no alfabeto salt
            for (String b : alfabetoSalt.split("")) {
                // Aplica o SALT à esquerda
                lista.add(a + b + ";" + palavra);
                // Aplica o SALTO à direita
                lista.add(palavra + ";" + a + b);
            }
        }
        return lista;
    }

    /**
     * CIFRA DE CEASER ONDE RECEBE A PALAVRA, AS ROTAÇÕES E O INCREMENTO COMO PARÂMETRO
     * @param palavra
     * @param rot
     * @param F
     * @return
     */
    private static String cifraCeaser(String palavra, int rot, int F) {
        int index = 0;
        String novaPalavra = "";
        // Lista de Strings onde vai dividir todo os letras do alfabeto em espaços vazios
        List<String> alfabeto = Arrays.stream(Main.alfabeto.split("")).toList();
        // Pega num caracter e divide por espaços vazios
        for (String caracter : palavra.split("")) {
            // Formula do enunciado
            int inc = index * F;
            // Pega no alfabeto e indexa os caracteres todos
            int alfabetoIndex = alfabeto.indexOf(caracter);
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

    /**
     * MÉTODO QUE LÊ O FICHEIRO WORDLIST.TXT QUE CONTÉM TODAS AS PALAVRAS DADAS PELOS DOCENTES
     * @param path
     * @return
     * @throws IOException
     */
    private static List<String> lerWordlist(String path) throws IOException {
        // Lista de String onde vai guardar todas as palavras do ficheiro
        List<String> listaPalavras = new ArrayList<String>();
        // Um simples BufferReader onde recebe como parãmetro o caminho do ficheiro
        BufferedReader br = new BufferedReader(new FileReader(path));
        // Lê o ficheiro linha a linha
        String linha = br.readLine();
        // Ciclo que valida que enquanto a linha não for nula adiciona o conteúdo à lista de palavras e lê a linha seguinte
        while (linha!= null) {
            listaPalavras.add(linha);
            linha = br.readLine();
        }
        // Fecha o ficheiro
        br.close();
        return listaPalavras;
    }

    /**
     * MÉTODO PARA CALCULAR O SHA-512
     * <a href="https://stackoverflow.com/questions/33085493/how-to-hash-a-password-with-sha-512-in-java">LINK ONDE ADAPETEI O CODIGO</a>
     * @param palavra
     * @return
     */
    private static String calcularSHA(String palavra) {
        // Constroi uma string
        StringBuilder stringb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] data = md.digest(palavra.getBytes());
            for (byte datum : data) {
                stringb.append(Integer.toString((datum & 0xff) + 0x100, 16).substring(1));
            }
        } catch (Exception erro) {
            System.out.println(erro);
        }
        return stringb.toString();
    }


    // Variável que apenas verifica quando encontra a password
    private static boolean foiAchado = false;

    public static void main(String[] args) throws IOException {
        // Carrega a minha plugboard
        carregarPlugboard(plugboard);
        // Ciclo que lê todas as palavras da wordlist e itera uma por uma
        for (String palavra: lerWordlist(Main.wordlist)) {
            // Itera pelas palavras da wordlist e aplica o salt às mesmas
            System.out.println("PALAVRA ATUAL:  " + palavra);
            for (String palavraComSalt: salt(palavra)) {
                String word = palavraComSalt.replace(";","");
                String plugboard1 = substituirPlugboard(word);
                // Ciclo que faz as Rotações no Rotor
                for (int i = 0; i <= 26 ; i++) {
                    // Ciclo que aumenta o fator de incremento
                    for (int j = 0; j <= 26 ; j++) {
                        // Aplica a cifra de ceaser
                        String cesar = cifraCeaser(plugboard1, i, j);
                        // Subsitui na nova plugboard a cifra feita
                        String plugboard2 = substituirPlugboard(cesar);
                        // Calcula o sha-512 da nossa plugboard
                        String hash = calcularSHA(plugboard2);
                        // Validação que compara a Hash calculada pela Hash recebida
                        if (hash.equalsIgnoreCase(sha512)) {
                            System.out.println("---------------------------------------------");
                            System.out.println("PASSWORD FINAL: " + word);
                            System.out.println("ROTAÇÃO: " + i);
                            System.out.println("INCREMENTO: " + j);
                            System.out.println("SALT: " + Arrays.toString(palavraComSalt.split(";")));
                            System.out.println("---------------------------------------------");
                            Main.foiAchado = true;
                            break;
                        }
                    }
                    // Se a password não for encontrada sai do ciclo
                    if (foiAchado)break;
                }
                // Se a password não for encontrada sai do ciclo
                if (foiAchado)break;
            }
            // Se a password não for encontrada sai do ciclo
            if (foiAchado)break;
            System.out.println("PASSWORD NÃO FOI ENCONTRADA!");
        }

        // TESTES KAT
        /*
        carregarPlugboard("{'N': 'K', 'V': 'E', 'J': 'S', 'W': 'T', 'R': 'M', 'P': 'H', 'Y': 'F', 'X': 'O', 'A': 'I', 'Q': 'C', 'G': 'D'}");

        // CICLO QUE RECEBE O INPUT DA PALAVRA DADA PELOS DOCENTES
        for (String salt : saltKat("X")) {
            // Substitui a palavra de ; para espaços vazios
            String word = salt.replace(";", "");
            // Substitui a palavra na nova plugboard
            String plugboard1 = substituirPlugboard(word);
            // Ciclo que os docentes também dizem no enunciado para fazer as rotações
            for (int i = 0; i <= 30; i++) {
                // Ciclo que os docentes também dizem no enucniado para fazer os incrementos
                for (int j = 0; j <= 30; j++) {
                    // Faz a cifra de ceaser na plugboard, com as rotações e incrementos
                    String cesar = cifraCeaser(plugboard1, i, j);
                    // Faz a substituição de uma nova plugboard
                    String plugboard2 = substituirPlugboard(cesar);
                    // Verifica se o plugboard tem a falavra final dada também pelos docentes
                    if (plugboard2.equalsIgnoreCase("SSS")) {
                        System.out.println("PALAVRA: " + word);
                        System.out.println("ROTAÇÃO: " + i);
                        System.out.println("INCREMENTO: " + j);
                        System.out.println("SALT: " + Arrays.toString(salt.split(";")));
                    }
                }
            }
        }
        */
    }
}