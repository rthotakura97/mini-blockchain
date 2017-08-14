package com.example.miniblockchain;

import com.example.miniblockchain.BlockData.Data;
import com.example.miniblockchain.BlockData.Transaction;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

public class Block {

    /*Class variables, all the attributes of the block*/
    private int index;
    private long timestamp;
    private Data data;
    private String prev_hash;
    private String self_hash;

    /*Constructor, builds a block with passed in variables, then creates a hash for curr block*/
    public Block(int index, long timestamp, Data data, String prev_hash){

        /*Set local attributes*/
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.prev_hash = prev_hash;

        /*Create hash for current block*/
        this.self_hash = hashBlock();
    }

    /*Assigns a SHA-256 hash for the current block by combining all data --> index,timestamp,transactions,previous hash*/
    private String hashBlock(){

        /*Build string to be hashed*/
        String hash = Integer.toString(this.index) + Long.toString(this.timestamp) + hashTransactions() + this.prev_hash;
        /*Send to hash function*/
        hash = hashFunction(hash);
        return hash;
    }

    /*Takes all transactions and converts them to a string for easier input into hash function*/
    private String hashTransactions(){

        /*No transactions on genesis block*/
        if(index == 0){
            return null;
        }

        /*Combine all transactions into one string*/
        String transactionHash = "";
        for(int i = 0; i<data.getTransactions().size(); i++){
            Transaction t = data.getTransactions().get(i);
            transactionHash += (t.getFrom() + t.getTo() + (t.getAmount()));
        }

        return transactionHash;
    }

    /*SHA-256 hash function takes in a string and outputs a 256 bit hash*/
    private String hashFunction(String toHash){

        String hashed = Hashing.sha256()
                .hashString(toHash, Charsets.UTF_8)
                .toString();
        return hashed;
    }

    /*Get index*/
    public int getIndex(){
        return this.index;
    }

    /*Get timestamp*/
    public long getTimestamp(){
        return this.timestamp;
    }

    /*Get data block*/
    public Data getData(){
        return this.data;
    }

    /*Get previous hash*/
    public String getPrev_hash(){
        return this.prev_hash;
    }

    /*Get current hash*/
    public String getSelf_hash(){
        return this.self_hash;
    }

    /*Print the block*/
    public void printBlock(){

        /*Print each attribute*/
        System.out.println("Index: " + Integer.toString(this.index));
        System.out.println("Timestamp: " + Long.toString(this.timestamp));
        this.data.printData();
        System.out.println("Prev Hash: " + this.prev_hash);
        System.out.println("Hash: " + this.self_hash);
    }

}
