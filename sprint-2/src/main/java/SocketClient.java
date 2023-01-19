import javax.print.DocFlavor;
import java.net.*;
import java.io.*;
import java.util.Scanner;


public class SocketClient {
    private static Socket socket;
    private static int serverPort;
    private static ObjectOutputStream output;
    private static ObjectInputStream input;

    private static EncryptionAlgorithm encryptionAlgorithm = new Encriptacao();

    // server IP address
    private static InetAddress host;
    static final String CLOSE_COMMAND = "BYE";

    /**
     * connect the client to the server at `SERVER_IP` on the given port
     * @param port - port to connect the client to
     */
    private void startConnection(int port) throws IOException {
        host = InetAddress.getLocalHost();
        System.out.println(host);
        serverPort = port;
    }

    /**
     * Handle the client's communications
     */
    private void handleClientCommunications() throws IOException, InterruptedException {
        while (true) {
            socket = new Socket(host.getHostName(), serverPort);
            output = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Enviando um pedido ao Socket Server");
            System.out.println("Escreva uma mensagem para ser enviada ao Servidor: ");
            String messagemCliente = stdinput.readLine();
            messagemCliente = SocketClient.encrypt(messagemCliente);
            if (messagemCliente.equalsIgnoreCase(CLOSE_COMMAND)) {
                sendCloseCommand();
                stop();
                break;
            } else output.writeObject("" + messagemCliente);
        }
    }


    /**
     * notify the server to close the session
     */
    private static void sendCloseCommand() throws IOException {
        output.writeObject("BYE");
    }

    /**
     * Close ALL client I/O
     */
    private static void stop() throws IOException, InterruptedException {
        output.close();
        System.out.println("Comunicação terminou.");
    }

    /**
     * read the file on the given `filePath`, create, configure and return a new instance of the EncryptionAlgorithm
     * @param filePath - path where the configuration file is located
     */
    private void configureEncryptionAlgorithm(String filePath) throws Exception{
        encryptionAlgorithm.configure(filePath);
    }

    /**
     * Encrypts the given message using the instance's encryption algorithm
     * @param message - Message to be encrypted
     * @return the encrypted message
     */
    private static String encrypt(String message) { return encryptionAlgorithm.encrypt(message); }

    /**
     * Mostly useful for debug purposes
     */
    public String toString() {
        return "Client Configuration{" +
                "socket Address=" + socket.getInetAddress() +
                ", clientSocket=" + socket.getPort() +
                '}' +
                "EncryptionAlgorithm: " + encryptionAlgorithm;
    }

    /**
     * __Reminders:__
     * - validate inputs
     * - connect to the server & configure the encryption algorithm
     * - handle errors
     *
     * @param args - command line arguments. args[0] SHOULD contain the absolute path for the configuration file
     */
    public static void main(String[] args) throws Exception {
        if(args.length < 1){
            System.out.println("Argumentos insuficientes!");
            return;
        }

        if (!new File(args[0]).exists()){
            System.out.println("Ficheiro não encontrado!");
            return;
        }

        if (!args[0].endsWith(".xml")){
            System.out.println("Formato XML inválido!");
            return;
        }

        String filePath = args[0];
        SocketClient socketClient = new SocketClient();
        // configure encryption algorithm
        socketClient.configureEncryptionAlgorithm(filePath);
        // start the client
        socketClient.startConnection(6969);
        // keep processing user input
        socketClient.handleClientCommunications();
        // close the connection when user uses the CLOSE command or an error occurs
        stop();
    }
}