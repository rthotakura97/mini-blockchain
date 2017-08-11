package com.example.miniblockchain.BlockData;

public class Transaction {

    private String from;
    private String to;
    private String amount;

    public Transaction(String from, String to, String amount){
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

    public String getAmount(){
        return amount;
    }

}
