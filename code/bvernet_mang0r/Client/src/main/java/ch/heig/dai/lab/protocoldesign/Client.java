package ch.heig.dai.lab.protocoldesign;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    final String SERVER_ADDRESS = "10.194.36.247";
    final int SERVER_PORT = 55555;
    final String END_LINE = "\n";
    final String SEPARATOR = " ";

    public static void main(String[] args) {
        // Create a new client and run it
        Client client = new Client();
        client.run();
    }

    private void run() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            Boolean connected = true;            

            // receive supported commands list
            String availableCommands;
            availableCommands = in.readLine();
            System.out.println(availableCommands);

            // read input
            String[] serverResponse;
            String userInputLine;
            while ((userInputLine = userInput.readLine()) != null && connected) {
                // sends command to server
                out.write(userInputLine + END_LINE);
                out.flush();
                // read result
                serverResponse = in.readLine().split(SEPARATOR);
                if (serverResponse.length == 0) { 
                    System.out.println("Corrupted response from server");
                    break;
                }

                try {
                    switch (serverResponse[0]) {
                        case "OPERATION_SOLVED" : System.out.println("= " + serverResponse[1]);
                            break;
                        case "ERROR_DIV_ZERO" : System.out.println("ERROR_DIV_ZERO : division by 0 occured when dividing " + serverResponse[1] + " by " + serverResponse[2]);
                            break;
                        case "WRONG_TYPE_ARG" : System.out.println("WRONG_TYPE_ARG : Argument <" + serverResponse[1] + "> has not the right type.");
                            break;
                        case "WRONG_NB_ARGS" : System.out.println("WRONG_NB_ARGS : " + serverResponse[1] + " arguments expected, " + serverResponse[2] + " arguments received.");
                            break;
                        case "UNKOWN_COMMAND" : System.out.println("UNKOWN_COMMAND : Unkown command <" + serverResponse[2] + ">.\nAvailable commands are : " + String.join(SEPARATOR, serverResponse));
                            break;
                        case "CONNECTION_INTERRUPTED" : System.out.println("The connexion has been interrupted.");
                            connected = false;
                            break;
                        default :
                            System.out.println("Unkown response from server (" + serverResponse[0] + "). Try again.");
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(serverResponse[0] + " response from the server doesn't have the correct number of argument.");
                }
            }

        } catch (IOException e) {
            System.out.println("Client: exception while using client socket: " + e);
        }
    }
}