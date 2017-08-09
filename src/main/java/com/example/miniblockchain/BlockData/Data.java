package com.example.miniblockchain.BlockData;

import java.util.ArrayList;
import java.util.List;

public class Data {

    private List<Transaction> transactions;
    private int proofId;

    public Data(){
        proofId = 1;
        transactions = new ArrayList<Transaction>();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public int getProofId() {
        return proofId;
    }

    public void setProofId(int id){
        proofId = id;
    }

    public void printData(){

        System.out.println("DATA---------------------------------------------");
        System.out.println("Proof of work: " + Integer.toString(proofId));
        System.out.println("");
        for(int i = 0; i<transactions.size(); i++){
            System.out.println("Transaction " + Integer.toString(i));
            System.out.println("From: " + transactions.get(i).getFrom());
            System.out.println("To: " + transactions.get(i).getTo());
            System.out.println("Amount: " + Integer.toString(transactions.get(i).getAmount()));
            System.out.println("");
        }
        System.out.println("--------------------------------------------------");
    }
}
