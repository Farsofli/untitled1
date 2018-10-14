import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class MyChatClient {

    private Scanner in;
    private int serverPort;

    private MyChatClient(int serverPort) {
        this.serverPort = serverPort;
        this.in = new Scanner(System.in);
        System.out.println("Client created.");
    }



    private void start() {

        System.out.println("Client started.");
        System.out.println("Welcome! Please type in your username:");



        try (Socket socket = new Socket(InetAddress.getLocalHost(), serverPort)) {
     //   try (Socket socket = new Socket("192.168.1.139", serverPort)) {
            //Hardcoded, never advertised.

            System.out.println("Connection with the server " +
                    InetAddress.getLocalHost() + ":" + serverPort + " established.");

            try (OutputStream os = socket.getOutputStream();
                 InputStream is = socket.getInputStream();
                 DataOutputStream dos = new DataOutputStream(os);
                 DataInputStream dis = new DataInputStream(is)) {
                dos.flush();

                boolean hasQuit = false;


                while (!hasQuit) {


                    /*
                    for(int i = 0; i < 50 ; i++)
                    {
                        Thread.sleep(7000);
                        System.out.println("User is active.");
                    }
                    */
                    try {

                    String message = in.nextLine();

                    System.out.println("[Client >>>] - " + message);
                    dos.writeUTF(message);
                    dos.flush();


                    if (message.equalsIgnoreCase("Quit")) {
                        socket.close();
                        System.out.println("You've logged out of this server.");
                       // hasQuit = true;
                    }

                }
                catch(IOException ioe) {
                    hasQuit = true;
                    }
                }


                String msgFromServer1 = (String) dis.readUTF();
                System.out.println("[Client <<<] - " + msgFromServer1);

                //A good idea to use flush between output streams (fixed many errors)
                dos.flush();

            } catch (Exception e) {
                System.err.println("Error while talking with the server: " + e);
            }
        } catch (IOException e) {
            System.err.println("Could not connect with the server: " + e);
        }
    }

    public static void main(String[] args) {
        new MyChatClient(8081).start();
    }
}
