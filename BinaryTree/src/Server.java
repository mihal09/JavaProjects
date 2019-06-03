import javax.xml.crypto.Data;
import java.net.*;
import java.io.*;

public class Server {
    static BinaryTree tree;
    static TypeEnum selectedType = TypeEnum.INTEGER;
    public static Comparable parseInput(String inputObject) throws Exception {
        Comparable object = inputObject;
        try {
            switch (selectedType) {
                case INTEGER:
                    object = Integer.parseInt(inputObject);
                    break;
                case DOUBLE:
                    object = Double.parseDouble(inputObject);
                    break;
                case STRING:
                    object = inputObject;
                    break;
            }
        }
        catch (Exception e){
            throw new Exception();
        }
        return inputObject;
    }

    public static String processInput(String inputLine){
        System.out.println(inputLine);
        String[] words = inputLine.split(" ");
        String firstWord = words[0];
        String outputLine;
        switch (firstWord) {
            case "search":
                if(tree.search(words[1]))
                    outputLine = "znaleziono!";
                else
                    outputLine = "nie znaleziono!";
                break;
            case "insert":
                try {
                    Comparable object = parseInput(words[1]);
                    tree.insert(object);
                    outputLine = tree.draw();
                }
                catch (Exception e){
                    outputLine = "Blad przy parsowaniu, zmien typ";
                }
                break;
            case "delete":
                try {
                    Comparable object = parseInput(words[1]);
                    tree.delete(object);
                    outputLine = tree.draw();
                } catch (Exception e) {
                    outputLine = "Blad przy parsowaniu, zmien typ";
                }
                break;
            case "draw":
                outputLine = tree.draw();
                break;
            case "type":
                tree = new BinaryTree();
                if(words[1].equals("Integer"))
                    selectedType = TypeEnum.INTEGER;
                if(words[1].equals("Double"))
                    selectedType = TypeEnum.DOUBLE;
                if(words[1].equals("String"))
                    selectedType = TypeEnum.STRING;
                outputLine = "zmieniam typ";
                break;
            default:
                outputLine = "nie rozumiem komendy";
                break;
        }
        return outputLine;
    }
    public static void main(String[] args) {
        tree = new BinaryTree();
//        if (args.length != 1) {
//            System.err.println("Uzycie: java <port number>");
//            System.exit(1);
//        }
//
//        int portNumber = Integer.parseInt(args[0]);

        int portNumber = 420;

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
            out.println("Witam serdecznie, czekam na polecenia.");

            while ((inputLine = in.readLine()) != null) {
                outputLine = processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("exit"))
                    break;
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}