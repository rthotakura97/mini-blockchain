package com.example.miniblockchain.BlockData;

import java.util.ArrayList;
import java.util.List;

public class Data {

    private List<Transaction> transactions;

    public Data(){
        transactions = new ArrayList<Transaction>();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void printData(){

        System.out.println("DATA");
        for(int i = 0; i<transactions.size(); i++){
            System.out.println("Transaction " + Integer.toString(i));
            System.out.println("From: " + transactions.get(i).getFrom());
            System.out.println("To: " + transactions.get(i).getTo());
            System.out.println("Amount: " + Integer.toString(transactions.get(i).getAmount()));
            System.out.println("");
        }
    }
}
