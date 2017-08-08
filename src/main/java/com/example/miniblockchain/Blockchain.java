package com.example.miniblockchain;

import com.example.miniblockchain.BlockData.Data;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {

    private List<Block> blockchain;

    public Blockchain(int index, long timestamp, Data data, String prev_hash){
        blockchain = new ArrayList<Block>();
        blockchain.add(new Block(index, timestamp, data, prev_hash));
    }

    public void addBlock(Block toAdd){
       // int index = lastBlock.getIndex() + 1;
        //long timestamp = System.currentTimeMillis();
       // String data = "I am block #" + Integer.toString(index);
        //String prev_hash = lastBlock.getSelf_hash();

        blockchain.add(toAdd);
    }

    public void printBlockchain(){
        for(int i=0; i<blockchain.size(); i++){
            System.out.println("Block " + Integer.toString(i));
            blockchain.get(i).printBlock();
            System.out.println("");
        }
    }

    public List<Block> getBlockchain(){
        return blockchain;
    }


}
