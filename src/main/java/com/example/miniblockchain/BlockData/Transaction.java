package com.example.miniblockchain.BlockData;

public class Transaction {

    private String from;
    private String to;
    private int amount;

    public Transaction(String from, String to, int amount){
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getAmount(){
        return amount;
    }

}
