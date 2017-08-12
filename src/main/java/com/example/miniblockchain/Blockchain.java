package com.example.miniblockchain;

import com.example.miniblockchain.BlockData.Data;
import com.example.miniblockchain.BlockData.Transaction;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {

    private List<Block> blockchain;

    public Blockchain(int index, long timestamp, Data data, String prev_hash){
        blockchain = new ArrayList<Block>();
        blockchain.add(new Block(index, timestamp, data, prev_hash));
    }

    public Block makeProspectiveBlock(Block lastBlock){
        int index = lastBlock.getIndex() + 1;
        long timestamp = System.currentTimeMillis();
        Data data = new Data();
        String prev_hash = lastBlock.getSelf_hash();
        return new Block(index, timestamp, data, prev_hash);
    }

    public void beginMine(Transaction transactionHistory){
        if(transactionHistory.getAmount().equals("0") || transactionHistory.getAmount().equals("none")) {
            System.out.println("Mine Unsuccesful");
            return;
        }

        int proof = proofOfWork();
        Transaction newTrans = new Transaction("NETWORK", "MINER", "1");
        Block prospectiveBlock = makeProspectiveBlock(blockchain.get(blockchain.size()-1));
        prospectiveBlock.getData().setProofId(proof);
        prospectiveBlock.getData().getTransactions().add(transactionHistory);
        prospectiveBlock.getData().getTransactions().add(newTrans);
        blockchain.add(prospectiveBlock);
    }

    public int proofOfWork(){
        int lastProof = blockchain.get(blockchain.size()-1).getData().getProofId();
        int incrementor = lastProof + 1;
        int divisor = 11;

        while (!(incrementor % lastProof == 0 && incrementor % divisor == 0)){
            incrementor++;
        }

        return incrementor;
    }


    public void printBlockchain(){

        System.out.println("\nB L O C K C H A I N\n");

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
