package com.example.miniblockchain;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {

    private List<Block> blockchain;

    public Blockchain(int index, long timestamp, String data, String prev_hash){
        blockchain = new ArrayList<Block>();
        blockchain.add(new Block(index, timestamp, data, prev_hash));
    }

    public void addBlock(Block lastBlock){
        int index = lastBlock.getIndex() + 1;
        long timestamp = System.currentTimeMillis();
        String data = "I am block #" + Integer.toString(index);
        String prev_hash = lastBlock.getSelf_hash();

        blockchain.add(new Block(index, timestamp, data, prev_hash));
        System.out.println("Block #" + Integer.toString(index) + " has been added to the blockchain!");
    }

    public List<Block> getBlockchain(){
        return blockchain;
    }


}
