# Calulator
Bénédicte Vernet, Benoît Jaouen

## Section 1 : Overview
Simple calculator protocol (SCP) is a client-server protocol. The client connects to a server and request the solution of simple math operations. The server sends back the solution or an error message that specifies the source of the error.

## Section 2 : Transport layer protocol
SCP uses TCP. The client established the connection. It has to know the IP address of the server. The server listens on TCP port 55555. 
The server is in charge of closing the connection.

The client breaks down complex operation (with more than 2 values) into simple operations (only 2 values involved). The client opens a connection for each complex operation and closes it when it gets the final result. After a certain amount of time of inactivity, the server closes the connection.

## Section 3 : Messages

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

The server sends back an error message if the client sends the wrong number of arguments (2 arguments expected).

- UNKOWN_COMMAND <commandsAvailable, commandReceived>

- CONNECTION_INTERRUPTED

The server sends a message to inform it has closed the connection due to inactivity.


The server sends back an error message if the client sends an unknown command. 

All messages are UTF-8 encoded with `\n` as end-of-line character. 

## Section 4 : example dialogs

#### Successful operation solving

1. Client : Open TCP connection
2. Client : ADD 1 2
3. Server : OPERATION_SOLVED 3
4. Client : MULT 3 2 
5. Server : OPERATION_SOLVED 6
6. Client : Closes TCP connection

#### Wrong type arguments

1. Client : Open TCP connection
2. Client : ADD 1 a
3. Server : WRONG_TYPE_ARG a
4. Client : Closes TCP connection

#### Inactivity 

1. Client : Open TCP connection
2. Client : ADD 1 4
3. Server : OPERATION_SOLVED 5

(...)

4. Server : CONNECTION_INTERRUPTED
5. Server : Closes TCP connection
