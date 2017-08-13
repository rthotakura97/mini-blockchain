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

    public boolean equals(Object obj){
        if(obj==null)
            return false;
        if(this==obj)
            return true;
        if(obj instanceof Transaction){
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
