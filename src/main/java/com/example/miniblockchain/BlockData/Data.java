package com.example.miniblockchain.BlockData;

import java.util.ArrayList;
import java.util.List;

/*Represents "data" field of a block --> proof, transactions*/
public class Data {

    /*Class variables*/
    private List<Transaction> transactions;
    private int proofId;

    /*Constructor*/
    public Data(){
        proofId = 1;
        transactions = new ArrayList<Transaction>();
    }

    /*Constructor if specific values are specified*/
    public Data(int proofId, List<Transaction> transactions){
        this.proofId = proofId;
        this.transactions = transactions;
    }

    /*Get transactions*/
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /*Get proofId*/
    public int getProofId() {
        return proofId;
    }

    /*Print the data block*/
    public void printData(){

        /*Print out the proof of work, then each transaction*/
        System.out.println("DATA---------------------------------------------");
        System.out.println("Proof of work: " + Integer.toString(proofId));
        System.out.println("");

        for(int i = 0; i<transactions.size(); i++){
            System.out.println("Transaction " + Integer.toString(i));
            System.out.println("From: " + transactions.get(i).getFrom());
            System.out.println("To: " + transactions.get(i).getTo());
            System.out.println("Amount: " + (transactions.get(i).getAmount()));
            System.out.println("");
        }

        System.out.println("--------------------------------------------------");
    }
}
