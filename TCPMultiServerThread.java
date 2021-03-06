import java.net.*;
import java.io.*;
import java.net.InetAddress;

public class TCPMultiServerThread extends Thread {
    private Socket clientTCPSocket = null;
    public TCPMultiServerThread(Socket socket) {
        super("TCPMultiServerThread");
        clientTCPSocket = socket;
    }

    public void run(){
        try {
            PrintWriter    clientSocketOut = new PrintWriter(clientTCPSocket.getOutputStream(), true);
            BufferedReader clientSocketIn  = new BufferedReader(new InputStreamReader(clientTCPSocket.getInputStream()));

            String  fromClient = "";
            boolean looping = true;
            do {


                fromClient = "";
                clientSocketOut.println("220 - cs3700a@msudenver.edu");


                boolean loop = true;
                while (loop) {
                    fromClient = clientSocketIn.readLine();
                    System.out.println("Client Message: " + fromClient);
                    String[] parts = fromClient.split(" ");

                    if (parts[0].equals("HELLO")){
                        InetAddress iPAddress = InetAddress.getLocalHost();
                        String response = "250 " + iPAddress.getHostAddress() + " Hello " + clientTCPSocket.getRemoteSocketAddress().toString().substring(1);
                        clientSocketOut.println(response);
                        System.out.println("Response sent to STMP client: " + response);
                        loop = false;
                    }



                    else {
                        clientSocketOut.println("503 5.5.2 Send hello first");
                        System.out.println("Response sent to STMP client: 503 5.5.2 Send hello first");
                    }
                }


                loop = true;
                while (loop) {
                    fromClient = clientSocketIn.readLine();
                    System.out.println("Received this message from STMP client: " + fromClient);
                    String[] parts = fromClient.split(":");

                    if (parts[0].equals("MAIL FROM") && fromClient.contains(":")) {
                        clientSocketOut.println("250 2.1.0 Sender OK");
                        System.out.println("Response sent to STMP client: 250 2.1.0 Sender OK");
                        loop = false;
                    }
                    else {
                        clientSocketOut.println("503 5.5.2 Need mail command");
                        System.out.println("Response sent to STMP client:  Need mail command");
                    }
                }


                loop = true;
                while (loop) {
                    fromClient = clientSocketIn.readLine();
                    System.out.println("Received this message from STMP client: " + fromClient);
                    String[] parts = fromClient.split(":");

                    if (parts[0].equals("RCPT TO") && fromClient.contains(":")) {
                        clientSocketOut.println("250 2.1.5 Recipient OK");
                        System.out.println("Response sent to STMP client: 250 2.1.5 Recipient OK");
                        loop = false;
                    }
                    else {
                        clientSocketOut.println("503 5.5.2 Need rcpt command");
                        System.out.println("Response sent to STMP client:  503 5.5.2 Need rcpt command");
                    }
                }


                loop = true;
                while (loop) {
                    fromClient = clientSocketIn.readLine();
                    System.out.println("Received this message from STMP client: " + fromClient);
                    if (fromClient.equals("DATA")) {
                        clientSocketOut.println("354 Start mail input; end with <CRLF>.<CRLF>");
                        System.out.println("Response sent to STMP client: 354 Start mail input; end with <CRLF>.<CRLF>");
                        loop = false;
                    }
                    else {
                        clientSocketOut.println("503 5.5.2 Need data command");
                        System.out.println("Response sent to STMP client:  503 5.5.2 Need rcpt command");
                    }
                }


                System.out.println("The mail message from the STMP client is: ");
                while ((fromClient = clientSocketIn.readLine()) != null) {
                    if (fromClient.equals(".")) {
                        break;
                    }
                    System.out.println(fromClient);
                }

                clientSocketOut.println("250 Message received and to be delivered");
                System.out.println("Response sent to STMP client:  250 Message received and to be delivered");



                if (clientSocketIn.readLine().equals("QUIT")) {
                    looping = false;
                    InetAddress iPAddress = InetAddress.getLocalHost();
                    clientSocketOut.println("221 " + iPAddress.getHostAddress() + " closing connection");
                    System.out.println("221 " + iPAddress.getHostAddress() + " closing connection");
                }

            }
            while (fromClient != null && looping);
            clientSocketOut.close();
            clientSocketIn.close();
            clientTCPSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
