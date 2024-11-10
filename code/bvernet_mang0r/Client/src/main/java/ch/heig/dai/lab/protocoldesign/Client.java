package ch.heig.dai.lab.protocoldesign;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    final String SERVER_ADDRESS = "172.17.59.163";
    final int SERVER_PORT = 55555;

    public static void main(String[] args) {
        // Create a new client and run it
        Client client = new Client();
        client.run();
    }

    private void run() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {
            
            out.write("AnUknownCommand 1 2" + "\n");
            out.flush();
            System.out.println("Echo: " + in.readLine());

            out.write("UncorrectNbArgs 1 2 3" + "\n");
            out.flush();
            System.out.println("Echo: " + in.readLine());

            out.write("UncorrectTypeArg 1 a" + "\n");
            out.flush();
            System.out.println("Echo: " + in.readLine());

            out.write("UncorrectTypeArg b 3" + "\n");
            out.flush();
            System.out.println("Echo: " + in.readLine());

            out.write("ADD 1 2" + "\n");
            out.flush();
            System.out.println("Echo: " + in.readLine());

            out.write("SUB 1 3" + "\n");
            out.flush();
            System.out.println("Echo: " + in.readLine());

            out.write("SUB 4 2" + "\n");
            out.flush();
            System.out.println("Echo: " + in.readLine());

            out.write("MULT 4 5" + "\n");
            out.flush();
            System.out.println("Echo: " + in.readLine());

            out.write("DIV 6 3" + "\n");
            out.flush();
            System.out.println("Echo: " + in.readLine());

            out.write("DIV 8 0" + "\n");
            out.flush();
            System.out.println("Echo: " + in.readLine());

            out.write("MOD 5 2" + "\n");
            out.flush();
            System.out.println("Echo: " + in.readLine());


            for (int i = 0; i < 3; i++) {
                // There are two errors here!
                out.write("FIN " + i + "\n");
                out.flush();
                System.out.println("Echo: " + in.readLine());
                
            }
        } catch (IOException e) {
            System.out.println("Client: exception while using client socket: " + e);
        }
    }
}