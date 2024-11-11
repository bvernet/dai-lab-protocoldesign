# Calulator
Bénédicte Vernet, Benoît Jaouen

## Section 1 : Overview
Simple calculator protocol (SCP) is a client-server protocol. The client connects to a server and request the solution of simple math operations. The server sends back the solution or an error message that specifies the source of the error.

## Section 2 : Transport layer protocol
SCP uses TCP. The client established the connection. It has to know the IP address of the server. The server listens on TCP port 55555. 
The client is in charge of closing the connection.

## Section 3 : Messages

- AVAILABLE_COMMANDS \<availableCommands>

The server sends to the client the available commands.

- ADD <value1, value2>

The client requests the solution of `value1 + value2`.

- SUB <value1, value2>

The client requests the solution of `value1 - value2`.

- MULT <value1, value2>

The client requests the solution of `value1 x value2`.

- DIV <value1, value2>

The client requests the solution of `value1 / value2`.

- MOD <value1, value2>

The client requests the solution of `value1 % value2`.

- OPERATION_SOLVED \<answer>

The server sends back the solution of the operation.

- ERROR_DIV_ZERO <value1, value2>

The server sends back an error message if the client try to do a division by zero.

- WRONG_TYPE_ARG \<value>

The server sends back an error message for the first non integer argument sent by the client.

- WRONG_NB_ARGS <nbValuesExpected, nbValuesReceived>

The server sends back an error message if the client sends the wrong number of arguments (3 arguments expected: the command and the 2 values).

- UNKNOWN_COMMAND <commandsAvailable, commandReceived>

The server sends back an error message if the client sends an unknown command.

- CONNECTION_INTERRUPTED

The server sends a message to inform it has closed the connection.


All messages are UTF-8 encoded with `\n` as end-of-line character. 

## Section 4 : example dialogs

#### Successful operation solving

1. Client : Open TCP connection
2. Server : AVAILABLE COMMANDS [ADD <v1,v2>, SUB <v1,v2>, MULT <v1,v2>, DIV <v1,v2>, MOD <v1,v2>]
3. Client : ADD 1 2
4. Server : OPERATION_SOLVED 3
5. Client : MULT 3 2 
6. Server : OPERATION_SOLVED 6
7. Client : Closes TCP connection

#### Wrong type arguments

1. Client : Open TCP connection
2. Server : AVAILABLE COMMANDS [ADD <v1,v2>, SUB <v1,v2>, MULT <v1,v2>, DIV <v1,v2>, MOD <v1,v2>]
3. Client : ADD 1 a
4. Server : WRONG_TYPE_ARG a
5. Client : Closes TCP connection

#### Wrong number of arguments

1. Client : Open TCP connection
2. Server : AVAILABLE COMMANDS [ADD <v1,v2>, SUB <v1,v2>, MULT <v1,v2>, DIV <v1,v2>, MOD <v1,v2>]
3. Client : ADD 1 2 3
4. Server : WRONG_NB_ARGS 3 4
5. Client : Closes TCP connection
