package com.example.miniblockchain;

import com.example.miniblockchain.BlockData.Data;
import com.example.miniblockchain.BlockData.Transaction;

import java.util.ArrayList;
import java.util.List;

/*Represents the blockchain*/
public class Blockchain {

    private static final String MINE_REWARD = "1";
    /*Blockchain clas variable*/
    private List<Block> blockchain;

    /*Constructor, takes in genesis block data to start the blockchain*/
    public Blockchain(int index, long timestamp, Data data, String prev_hash) {

        /*Inititalizes blockchain and adds genesis block*/
        blockchain = new ArrayList<Block>();
        blockchain.add(new Block(index, timestamp, data, prev_hash));

    }

    /*Makes the next block after the proof of work from mining is finished*/
    public Block makeProspectiveBlock(Block lastBlock, Data newData) {

        /*Use last blocks fields to initialize part of the block*/
        int index = lastBlock.getIndex() + 1;
        long timestamp = System.currentTimeMillis();
        String prev_hash = lastBlock.getSelf_hash();

        /*Use Data that is passed in and the index,timestamp,prev_hash created to create new block*/
        return new Block(index, timestamp, newData, prev_hash);
    }

    private List<Transaction> validateTransactions(List<Transaction> transactionsHistory) {
        List<Transaction> validTransactions = new ArrayList<Transaction>();
        for (Transaction transactionHistory : transactionsHistory) {

            /*Make sure block is valid, or return false*/
            if (transactionHistory.getAmount().equals("0") || transactionHistory.getAmount().equals("none")) {
                System.out.println("Mine Unsuccesful");
            }

            validTransactions.add(transactionHistory);

        }
        return validTransactions;
    }

    /*Mines the transaction and creates the block to add to the blockchain*/
    public boolean beginMine(List<Transaction> transactionsHistory) {

        if (transactionsHistory.isEmpty()) {
            return false;
        }

        /*Proof of work function*/
        int proof = proofOfWork();

        /*Check valid transactions*/
        List<Transaction> validTransactions = validateTransactions(transactionsHistory);
        if (validTransactions.isEmpty()) {
            return false;
        }

        /*Make transactions for the original transaction and the reward for mining the block*/
        Transaction newTrans = new Transaction("NETWORK", "MINER", MINE_REWARD);
        validTransactions.add(newTrans);

        /*Create data for the new block*/
        Data newDataBlock = new Data(proof, validTransactions);

        /*Create new block*/
        Block prospectiveBlock = makeProspectiveBlock(blockchain.get(blockchain.size() - 1), newDataBlock);

        /*Add to blockchain*/
        blockchain.add(prospectiveBlock);
        System.out.println("Mine Successful");

        return true;
    }

    /*Simple proof of work algorithm to prove cpu usage was used to mine block*/
    public int proofOfWork() {

        /*Proof of work --> simple arithmetic --> find number that is divisible by both lastProof and 11*/
        int lastProof = blockchain.get(blockchain.size() - 1).getData().getProofId();
        int incrementor = lastProof + 1;
        int divisor = 11;

        while (!(incrementor % lastProof == 0 && incrementor % divisor == 0)) {
            incrementor++;
        }

        return incrementor;
    }

    /*Prints current blockchain*/
    public void printBlockchain() {

        System.out.println("\nB L O C K C H A I N\n");

        for (int i = 0; i < blockchain.size(); i++) {
            System.out.println("Block " + Integer.toString(i));
            blockchain.get(i).printBlock();
            System.out.println("");
        }
    }

    /*Validates each block in the chain looking for any hash pointer descrepancies, which can point to a tampering problem*/
    public boolean validateChain() {

        /*Loop through chain and check hash pointers, inconsistencies means block was tampered w/*/
        for (int i = 0; i < blockchain.size() - 1; i++) {

            /*Get current hash and the next blocks prev hash and compare*/
            String currHash = blockchain.get(i).getSelf_hash();
            String nextHash = blockchain.get(i + 1).getPrev_hash();
            if (!(currHash.equals(nextHash)))
                return false;
        }

        return true;
    }

    /*Get blockchain*/
    public List<Block> getBlockchain() {
        return blockchain;
    }

}
