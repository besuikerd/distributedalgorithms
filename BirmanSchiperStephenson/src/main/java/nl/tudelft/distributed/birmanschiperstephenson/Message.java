package nl.tudelft.distributed.birmanschiperstephenson;

import java.io.Serializable;

public class Message implements Serializable {

    private Object message;
    private int sender;
    private int receiver;
    private VectorClock clock;

    public Message(int sender, int receiver, VectorClock clock, Object message) {
        this.sender = sender;
        this.receiver = receiver;
        this.clock = clock;
        this.message = message;
    }

    public Object getMessage() {
        return message;
    }

    public int getSender() {
        return sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public VectorClock getClock() {
        return clock;
    }
}
