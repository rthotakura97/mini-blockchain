package com.example.miniblockchain;

public class Main {

    public static void main(String[] args) {

        Blockchain blockchain = new Blockchain(0, System.currentTimeMillis(), "I am block #0", "0");

        for(int i = 0; i<5; i++){
            blockchain.addBlock(blockchain.getBlockchain().get(i));
            //blockchain.getBlockchain().get(i).printBlock();
            System.out.println("");
        }

    }

}
