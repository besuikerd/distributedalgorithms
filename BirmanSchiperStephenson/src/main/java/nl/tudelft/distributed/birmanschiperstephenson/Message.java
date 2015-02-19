package nl.tudelft.distributed.birmanschiperstephenson;

import java.io.Serializable;

public class Message implements Serializable {

    private Object message;
    private int sender;
    private int receiver;
    private int[] clock;

    public Message(int sender, int receiver, int[] clock, Object message) {
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

    public int[] getClock() {
        return clock;
    }
}
