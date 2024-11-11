package ch.heig.dai.lab.protocoldesign;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;

import static java.nio.charset.StandardCharsets.*;


public class Server {
    final int SERVER_PORT = 55555;
    final String END_LINE = "\n";
    final String SEPARATOR = " ";
    final int NB_ARGS = 2;

    private static enum Message {
        AVAILABLE_COMMANDS,
        ADD,
        SUB,
        MULT,
        DIV,
        MOD,
        OPERATION_SOLVED,
        ERROR_DIV_ZERO,
        WRONG_TYPE_ARG,
        WRONG_NB_ARGS,
        UNKOWN_COMMAND,
        CONNECTION_INTERRUPTED;


        public static String availableCommands(){
                    return "[ADD <v1,v2>, SUB <v1,v2>, MULT <v1,v2>, DIV <v1,v2>, MOD <v1,v2>]";
                }
            }
        
            public static void main(String[] args) {
                // Create a new server and run it
                Server server = new Server();
                server.run();
            }
        
            private String handleMessage(String message, Double arg1, Double arg2){
        
                Double result;
        
                switch(message){
                    case "ADD":
                        result = arg1 + arg2;
                        break;
                    case "SUB":
                        result = arg1 - arg2;
                        break;
                    case "MULT":
                        result = arg1 * arg2;
                        break;
                    case "DIV":
                        if(arg2 == 0){
                            return Message.ERROR_DIV_ZERO.toString() + SEPARATOR + arg1 + SEPARATOR + arg2;
                        }
                        result = arg1 / arg2;
                        break;
                    case "MOD":
                        result = arg1 % arg2;
                        break;
                    default:
                        return Message.UNKOWN_COMMAND.toString() +SEPARATOR + Message.availableCommands() + SEPARATOR + message;          
        }
        return Message.OPERATION_SOLVED.toString() + SEPARATOR + result;
    }

    private void run() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while (true) {

                try (Socket socket = serverSocket.accept();
                    var in = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
                    var out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), UTF_8))) {
                    
                    out.write(Message.AVAILABLE_COMMANDS.toString() + SEPARATOR + Message.availableCommands() + END_LINE);
                    out.flush();

                    String line;
                    while ((line = in.readLine()) != null) {

                        String[] args = line.split(SEPARATOR);

                        if(args.length != 3){
                            out.write(Message.WRONG_NB_ARGS.toString() + SEPARATOR + NB_ARGS + SEPARATOR + args.length + END_LINE);
                            out.flush();
                            continue;
                        }

                        String message = args[0];
                        Double values[] = new Double[NB_ARGS];
                        boolean isFormatValid = true;

                        for(int i = 1; i < args.length; i++){
                            try {
                                values[i-1] = Double.parseDouble(args[i]);
                            } catch (NumberFormatException e) {
                                out.write(Message.WRONG_TYPE_ARG.toString() + SEPARATOR + args[i] + END_LINE);
                                out.flush();
                                isFormatValid = false;
                                break;
                            }
                        }

                        if(!isFormatValid){
                            continue;
                        }

                        String output = handleMessage(message, values[0], values[1]);
                        
                        out.write(output + END_LINE);
                        out.flush();
                    }

                    out.write(Message.CONNECTION_INTERRUPTED.toString() + END_LINE);
                    out.flush();                
                } catch (IOException e) {
                    System.out.println("Server: socket ex.: " + e);
                }

            }
        } catch (IOException e) {
            System.out.println("Server: server socket ex.: " + e);
        }
    }
}