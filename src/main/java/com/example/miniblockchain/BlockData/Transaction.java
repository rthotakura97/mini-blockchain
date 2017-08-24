package com.example.miniblockchain.BlockData;

/*Represents a transaction in a block*/
public class Transaction {

    /*Class vars, transaction details*/
    private String from;
    private String to;
    private String amount;

    /*Constructor, takes in transaction details and constructs an object*/
    public Transaction(String from, String to, String amount){
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    /*Comparator compares two transactions, used for debugging purposes*/
    public boolean equals(Object obj){

        /*Check for null or same obj*/
        if(obj==null)
            return false;
        if(this==obj)
            return true;

        /*Go into next check*/
        if(obj instanceof Transaction){

            /*Check each field of transaction*/
            Transaction check = (Transaction) obj;
            if(!(from.equals(check.getFrom())))
                return false;
            if(!(to.equals(check.getTo())))
                return false;
            if(!(amount.equals(check.getAmount())))
                return false;

            return true;

        }
        return false;
    }

    /*Get from*/
    public String getFrom() {
        return from;
    }

    /*Get to*/
    public String getTo() {
        return to;
    }

    /*Get amount*/
    public String getAmount(){
        return amount;
    }

}
