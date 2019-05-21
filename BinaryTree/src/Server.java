import javax.xml.crypto.Data;
import java.net.*;
import java.io.*;

public class Server {
    static BinaryTree<DataType> tree;
    private TypeEnum selectedType;

    public static String processInput(String inputLine){
        System.out.println(inputLine);
        String outputLine;
        switch (inputLine) {
            case "search":
//                if()
                outputLine = "szukam";
                break;
            case "insert":
                outputLine = "szukam";
                break;
            case "delete":
                outputLine = "usuwam";
                break;
            case "draw":
                outputLine = "rysuję";
                break;
            case "type":
                outputLine = "zmieniam typ";
                break;
            default:
                outputLine = "nie rozumiem komendy";
                break;
        }
        return outputLine;
    }
    public static void main(String[] args) {
        tree = new BinaryTree<>();
//        DataType value =  (Object)Integer.valueOf("123");
        if (args.length != 1) {
            System.err.println("Uzycie: java <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()))
        ) {

            String inputLine, outputLine;

            // Initiate conversation with client
            out.println("Hejka");

            while ((inputLine = in.readLine()) != null) {
                outputLine = processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Bye."))
                    break;
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}