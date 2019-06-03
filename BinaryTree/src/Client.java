import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {

        //Tworzenie gniazda, i sprawdzenie czy host/pory serwera nasłuchuje
        String hostName;
        int portNumber;

        if(args.length==0){
            hostName= "localhost";
            portNumber = 420;
        }
        else{
            hostName = args[0];
            String portStr = args[1];
            try {
                portNumber = Integer.parseInt(portStr);
            }
            catch(NumberFormatException nfe){
                System.out.println("Zły typ portu. Przełączam na domyslny port: 420");
                portNumber = 420;
            }
        }

        try (
                Socket kkSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(kkSocket.getInputStream()))
        ) {
            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;

            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: [ " + fromServer+ " ]");
                if (fromServer.equals("Bye."))
                    break;

                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}