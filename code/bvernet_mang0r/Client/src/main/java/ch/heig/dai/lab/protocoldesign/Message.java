package ch.heig.dai.lab.protocoldesign;

public enum Message {
    ADD(2, '+'),
    SUB(2, '-'),
    MULT(2, '*'),
    DIV(2, '/'),
    MOD(2, '%'),
    OPERATION_SOLVED(1),
    ERROR_DIV_ZERO(2),
    WRONG_TYPE_ARG(1),
    WRONG_NB_ARGS(2),
    UNKOWN_COMMAND(2),
    CONNECTION_INTERRUPTED(0);

    public static final String OPERATION_CHARACTERS = "+-*/%";

    private final int nbArguments;
    private final Character c;

    private Message(int nbArguments) {
        this.nbArguments = nbArguments; 
        this.c = null;
    }
    private Message(int nbArguments, char c) { 
        this.nbArguments = nbArguments; 
        this.c = c;
    }

    public int nbArguments() { return nbArguments; }
    
    public Message from (char c) {
        for (Message m : Message.values()) {
            if (this.c.equals(c)) {
                return m;
            }
        }
        return null;
    }
}