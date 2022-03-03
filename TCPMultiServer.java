// Julian Bencomo - Antonio Diaz
// Homework 4
import java.net.*;
import java.io.*;

public class TCPMultiServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverTCPSocket = null;
        boolean connected = true;

        try {
            // Port Numbers
            // Julian  : 5010
            // Amtonio : 5070
            serverTCPSocket = new ServerSocket(5010);
        } catch (IOException e) {
            System.err.println("Could not connect to port.");
            System.exit(-1);
        }

        while (connected) {
            new TCPMultiServerThread(serverTCPSocket.accept()).start();
        }
        serverTCPSocket.close();
    }
}
