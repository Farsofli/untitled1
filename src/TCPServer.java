import java.io.*;
import java.net.*;
import java.util.Random;

public class TCPServer {

    //Datainputstream
    //Dataoutputstream
    private int serverPort;
    private int port;
    private String welcome = "Welcome";

    private TCPServer (int port) {
        this.port = port;
    }

    private void start() {

        System.out.println("Creating server on port: " + port);

        ServerSocket myServerSocket = null;
        try  {
            myServerSocket = new ServerSocket(port);
            System.out.println("Server started, waiting for clients to connect to the server...");

            int counter = 0;

            while (counter >= 0) {
                System.out.println("Waiting for client to connect...");

                //Client has been accepted
                Socket clientSocket = myServerSocket.accept();

                System.out.println("A client connected, handling its requests ...");

                new Thread(new ClientHandler(clientSocket, ++counter)).start();
            }
        }


        catch (BindException e) {
            System.err.println("Port already used " + port + ": " + e);
            System.err.println("New Port number is " + port + ": " + e);
            myServerSocket = null;

            try {
                myServerSocket = new ServerSocket(port);
            } catch (Exception ee) {

                System.err.println("Test" + ee);
            }
        }
        catch (IOException e) {
            System.err.println("Could not open server socket on port " + port + ": " + e);
        }
        catch (Exception e) {
            System.err.println("Into Catch the rest of excetions" + e);
        }
    }

    private class ClientHandler implements Runnable {

        private Socket clientSocket;
        private int counter;

        ClientHandler(Socket clientSocket, int counter) {
            this.clientSocket = clientSocket;
            this.counter = counter;
        }

        @Override
        public void run() {
            System.out.println("Dealing with client number " + counter);
            try {


                InputStream is = clientSocket.getInputStream();
                OutputStream os = clientSocket.getOutputStream();

                try (DataInputStream ois = new DataInputStream(is);
                     DataOutputStream oos = new DataOutputStream(os)) {

                    String msg = (String) ois.readUTF();
                    System.out.println("[Server <<<] - " + msg);

                    String serverReply1 = welcome;
                    System.out.println("[Server >>>] - " + serverReply1);
                    oos.writeUTF(serverReply1);
                    oos.flush();


                } catch (Exception e) {
                    System.err.println("Error while talking with the client: " + e);
                }
            } catch (IOException e) {
                System.err.println("Could not talk with the client: " + e);
            }
        }
    }

    public static void main(String[] args) {
        new TCPServer(8081).start();
    }
}