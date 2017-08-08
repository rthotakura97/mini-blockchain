package com.example.miniblockchain;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import java.security.MessageDigest;

public class Block {

    private int index;
    private long timestamp;
    private String data;
    private String prev_hash;
    private String self_hash;

    public Block(int index, long timestamp, String data, String prev_hash){
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.prev_hash = prev_hash;
        this.self_hash = hashBlock();
    }


    private String hashBlock(){
        String hash = Integer.toString(this.index) + Long.toString(this.timestamp) + this.data + this.prev_hash;
        hash = hashFunction(hash);
        return hash;
    }

    private String hashFunction(String toHash){
        String hashed = Hashing.sha256()
                .hashString(toHash, Charsets.UTF_8)
                .toString();
        return hashed;
    }

    public int getIndex(){
        return this.index;
    }

    public long getTimestamp(){
        return this.timestamp;
    }

    public String getData(){
        return this.data;
    }

    public String getPrev_hash(){
        return this.prev_hash;
    }

    public String getSelf_hash(){
        return this.self_hash;
    }

    public void printBlock(){
        System.out.println("Index: " + Integer.toString(this.index));
        System.out.println("Timestamp: " + Long.toString(this.timestamp));
        System.out.println("Data: " + this.data);
        System.out.println("Prev Hash: " + this.prev_hash);
        System.out.println("Hash: " + this.self_hash);
    }


}
