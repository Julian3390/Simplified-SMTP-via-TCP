import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) throws IOException {

        Socket tcpSocket = null;
        BufferedReader socketIn = null;
        PrintWriter socketOut = null;
        String IPaddress_DNS = "";


        BufferedReader systemInput = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.print("Enter 1 of the following - DNS or IP Address: ");
            IPaddress_DNS = systemInput.readLine();
            System.out.println("");

            // Assigned Port Nums
            // Julian  : 5010
            // Amtonio : 5070
            tcpSocket = new Socket(IPaddress_DNS, 5010);

            socketOut = new PrintWriter(tcpSocket.getOutputStream(),true);
            socketIn  = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
        }
        catch (UnknownHostException e) {
            System.err.println("Unknown Host - " + IPaddress_DNS);
            System.exit(1);
        }

        catch (IOException e) {
            System.err.println("I/O error connecting to port "  + IPaddress_DNS + ".");
            System.exit(1);
        }




        String fromServer;
        String fromUser;

        fromServer = socketIn.readLine();
        System.out.println("The 220 response from the server is: " + fromServer + "\n");

        boolean additionalResponses = true;

        while (additionalResponses)
        {
            System.out.println("Sender's email address: ");
            String senderEmail = systemInput.readLine();

            System.out.println("Receiver's email address: ");
            String receiverEmail = systemInput.readLine();

            System.out.println("Subject: ");
            String subject = systemInput.readLine();

            System.out.println("Enter email contents line by line, with the last line containing only a period: \n");
            String emailContent = "";
            boolean stillGettingContentLines = true;

            while(stillGettingContentLines)
            {
                String temp = systemInput.readLine();
                emailContent = emailContent + "\r\n" + temp;
                if (temp.equals("."))
                {
                    stillGettingContentLines = false;
                }
            }

            System.out.println("Sending to server: HELLO " + IPaddress_DNS + "\n");
            long startTimer = System.nanoTime();
            socketOut.println("HELLO " + IPaddress_DNS);
            fromServer = socketIn.readLine();
            long endTime = System.nanoTime();
            float millisecTimer = (float)(endTime - startTimer)/1000000;
            System.out.println("Server's response to HELLO " + IPaddress_DNS + " is -- " + "\n" + fromServer);
            System.out.println("Response time: " + millisecTimer + " ms." + "\n");

            System.out.println("Now sending to server: MAIL FROM: " + senderEmail + "\n");
            startTimer = System.nanoTime();
            socketOut.println("MAIL FROM: " + senderEmail);

            fromServer = socketIn.readLine();
            endTime = System.nanoTime();
            millisecTimer = (float)(endTime - startTimer)/1000000;
            System.out.println("Server's response to -- MAIL FROM: " + senderEmail + "-- " + "\n" + fromServer);
            System.out.println("Response time: " + millisecTimer + " ms." + "\n");

            System.out.println("Sending to server: RCPT TO: " + receiverEmail + "\n");
            startTimer = System.nanoTime();

            socketOut.println("RCPT TO: " + receiverEmail);
            fromServer = socketIn.readLine();
            endTime = System.nanoTime();

            millisecTimer = (float)(endTime - startTimer)/1000000;
            System.out.println("Server response to -- RCPT TO: " + receiverEmail + " -- " + "\n" + fromServer);
            System.out.println("Response Time: " + millisecTimer + " ms." + "\n");

            System.out.println("Sending to server: DATA" + "\n");
            startTimer = System.nanoTime();
            socketOut.println("DATA");

            fromServer = socketIn.readLine();
            endTime = System.nanoTime();

            millisecTimer = (float)(endTime - startTimer)/1000000;
            System.out.println("Server's response to DATA: " + "\n" + fromServer);
            System.out.println("Response Time: " + millisecTimer + " ms" + "\n");

            String mailMsg =
                    "To: " + receiverEmail + "\r\n" +
                            "From: " + senderEmail + "\r\n" +
                            "Subject: " + subject + "\r\n" +
                            "" + "\r\n" +
                            emailContent;


            System.out.println("Sending to server : \n" + mailMsg + "\n");
            startTimer = System.nanoTime();
            socketOut.println(mailMsg);
            fromServer = socketIn.readLine();
            endTime = System.nanoTime();
            millisecTimer = (float)(endTime - startTimer)/1000000;
            System.out.println("Server's response: " + "\n" + fromServer);
            System.out.println("Response Time: " + millisecTimer + " ms." + "\n");

            System.out.println("Would you like to continue? If yes enter 'yes' below. Otherwise, hit the 'enter' key. \n");
            fromUser = systemInput.readLine();

            if (!fromUser.equalsIgnoreCase("yes")) {
                System.out.println("Sending the QUIT command to the server.");
                socketOut.println("QUIT");
                fromServer = socketIn.readLine();
                System.out.println("Server's response: " + fromServer);
                additionalResponses = false;
            }
            else {
                socketOut.println("continuing");
                System.out.println("Did not tell server to quit");
            }
        }

        // Terminate
        socketOut.close();
        socketIn.close();
        systemInput.close();
        tcpSocket.close();
    }
}
