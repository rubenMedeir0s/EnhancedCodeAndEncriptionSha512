import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Encriptacao implements EncryptionAlgorithm {
    private CifraCeaser cifra = new CifraCeaser();
    private Plugboard plugboard = new Plugboard();

    @Override
    public void configure(String configurationFilePath) throws Exception {
        File file = new File(configurationFilePath);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();
            String plugboardXML = getTextContent(document, "plugboard").trim();
            carregarPlugboard(plugboardXML);
            CifraCeaser.guardarTipoAlfabetoXML = getTextContent(document, "alphabet").trim();
            if (CifraCeaser.guardarTipoAlfabetoXML.contains("+")) {
                for (String type : CifraCeaser.guardarTipoAlfabetoXML.split("\\+")) {
                    alfabeto(type);
                }
            } else {
                alfabeto(CifraCeaser.guardarTipoAlfabetoXML);
            }
            CifraCeaser.incrementarValor = Integer.parseInt(getTextContent(document, "increment-factor").trim());
            CifraCeaser.rotacao = Integer.parseInt(getTextContent(document, "encryption-key").trim());
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    @Override
    public String encrypt(String texto) {
        String mensagemTraduzida = plugboard.substituirPlugboard(texto);
        System.out.println("a: " + mensagemTraduzida);
        String mensagemEncriptada = cifra.cifraCeaser(mensagemTraduzida, CifraCeaser.incrementarValor, CifraCeaser.rotacao);
        System.out.println("b: " + mensagemEncriptada);
        String mensagemCifrada = plugboard.substituirPlugboard(mensagemEncriptada);
        System.out.println("c: " + mensagemCifrada);
        return mensagemCifrada;
    }

    @Override
    public String decrypt(String textoCrifra) {
        System.out.println("TEXTO: " + textoCrifra);
        String reverseMensagemTraduzida = plugboard.reverterPlugboard(textoCrifra);
        System.out.println("PLUG1: " + reverseMensagemTraduzida);
        String decryptedMessage = cifra.decifrarCifraCeaser(reverseMensagemTraduzida, CifraCeaser.incrementarValor, CifraCeaser.rotacao);
        System.out.println("CESAR: " + decryptedMessage);
        String texto2 = plugboard.reverterPlugboard(decryptedMessage);
        System.out.println("PLUG2 : " + texto2);
        return texto2;
    }

    public static String getTextContent(Document document, String name) {
        return document.getElementsByTagName(name).item(0).getTextContent();
    }
    // GAURDA O MEU ALFABETO EM LETRAS, NÚMEROS E PONTUAÇÕES
    public void alfabeto(String guardarTipoAlfabetoXML) {
        switch (guardarTipoAlfabetoXML) {
            case "UPPER" -> CifraCeaser.alfabeto += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            case "DIGITS" -> CifraCeaser.alfabeto += "1234567890";
            case "PUNCTUATION" -> CifraCeaser.alfabeto += "-.!?();/@";
        }
    }
    // CARREGA A PLUGBOARD
    public void carregarPlugboard(String s) {
        Plugboard.carregarPlugboard(s);
    }
}
