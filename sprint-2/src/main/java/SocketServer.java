import java.net.*;
import java.io.*;

public class SocketServer {

    private ServerSocket serverSocket;
    private Socket clientSocket;

    private ObjectInputStream input;

    private static EncryptionAlgorithm encryptionAlgorithm = new Encriptacao();

    /**
     * start the server and wait for connections on the given port

     * @param port - port to connect the client to
     */
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Conexao estabelecida!");
    }

    /**
     * Handle messages received on the server and log them with the respective timestamp
     */
    public void processServerCommunications() throws IOException, ClassNotFoundException {
         while (true) {
             System.out.println("A espera da resposta do cliente");
             //creating socket and waiting for client connection
             clientSocket = serverSocket.accept();
             //read from socket to ObjectInputStream object
             input = new ObjectInputStream(clientSocket.getInputStream());
             //convert ObjectInputStream object to String
             String mensagem = (String) input.readObject();
             String mensagemOriginal = mensagem;
             mensagem = SocketServer.decrypt(mensagem);;
             System.out.println("Mensagem Recebida: " + mensagem);
             System.out.println(mensagem);
             //create ObjectOutputStream object
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
             //write object to Socket
             oos.writeObject("Olá "+ mensagem);
             //close resources
             input.close();
             clientSocket.close();
             //terminate the server if client sends exit request
             if(mensagem.equalsIgnoreCase("BYE")) {
                 stop();
                 break;
             }
         }
    }

    /**
     * Terminate the service - close ALL I/O
     */
    public void stop() throws IOException {
        System.out.println("Conexao a encerrar!");
        //Fecha a conexão
        serverSocket.close();
    }

    /**
     * Read the file at `filePath`, xreate, configure and return a new instance of the EncryptionAlgorithm

     * @param filePath - path where the configuration file is located
     */
    private void configureEncryptionAlgorithm(String filePath) {
        encryptionAlgorithm = new Encriptacao();
        try {
            encryptionAlgorithm.configure(filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Decrypts the given message using the instance's encryption algorithm
     * @param message - message to be decrypted
     * @return the decrypted (original) message
     */
    private static String decrypt(String message) { return encryptionAlgorithm.decrypt(message); }

    /**
     * Encrypts the given message using the instance's encryption algorithm
     * @param message - Message to be encrypted
     * @return the encrypted message
     */
    private String encrypt(String message) { return encryptionAlgorithm.encrypt(message); }

    /**
    * Mostly useful for debug purposes
    */
    @Override
    public String toString() {
        return "Server Configuration{" +
                "server Address=" + serverSocket.getInetAddress() +
                ", clientSocket=" + serverSocket.getLocalPort() +
                '}' +
                "EncryptionAlgorithm: " + encryptionAlgorithm;
    }

    // location in the command line arguments' array where the path is provided
    static int COMMAND_LINE_ARGUMENT_FILE_PATH = 0;

    /**
    * __Reminders:__
    * - validate inputs
    * - Start the server service & configure the encryption algorithm
    * - handle errors
    * - terminate the serviceon demand
    *
    * @param args - command line arguments. args[0] SHOULD contain the absolute path for the configuration file
    */
    public static void main(String[] args) throws IOException, ClassNotFoundException {

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
        SocketServer socketServer = new SocketServer();
        // configure the algorithm
        socketServer.configureEncryptionAlgorithm(filePath);
        // start the server
        socketServer.start(6969);
        // keep processing messages
        socketServer.processServerCommunications();
        // stop the server
        socketServer.stop();
    }
}