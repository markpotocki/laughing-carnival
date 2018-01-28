package chain;

import client.scripts.ScriptType;

import java.io.Serializable;
import java.util.Date;

public final class Transaction implements Serializable {

    private String senderSigniture;

    private String recieverSigniture;

    private Date timestamp;

    private ScriptType type;

    private int outputAmount;


    private byte[] inputTransaction;
    private byte[] outputTransaction;

    public Transaction(Date timestamp, byte[] inputTransaction, String senderSigniture, byte[] outputTransaction, String recieverSigniture, ScriptType type, int outputAmount) {
        this(timestamp, senderSigniture, recieverSigniture, type, outputAmount);
        this.inputTransaction = inputTransaction;
        this.outputTransaction = outputTransaction;
    }

    public Transaction(Date timestamp, String senderSigniture, String recieverSigniture, ScriptType type, int outputAmount) {
        this.senderSigniture = senderSigniture;
        this.recieverSigniture = recieverSigniture;
        this.timestamp = timestamp;
        this.type = type;
        this.outputAmount = outputAmount;
    }

    public String getSenderSigniture() {
        return senderSigniture;
    }

    public String getRecieverSigniture() {
        return recieverSigniture;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public ScriptType getType() {
        return type;
    }

    public int getOutputAmount() {
        return outputAmount;
    }

    public byte[] getInputTransaction() {
        return inputTransaction;
    }

    public byte[] getOutputTransaction() {
        return outputTransaction;
    }

    private int getByteBufferLength() {
       return senderSigniture.getBytes().length + recieverSigniture.getBytes().length + timestamp.toString().getBytes().length + type.toString().getBytes().length;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "senderSigniture='" + senderSigniture + '\'' +
                ", recieverSigniture='" + recieverSigniture + '\'' +
                ", timestamp=" + timestamp +
                ", type=" + type +
                '}';
    }
}
