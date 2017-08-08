package com.example.miniblockchain;

import com.example.miniblockchain.BlockData.Data;
import com.example.miniblockchain.BlockData.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {

        Blockchain blockchain = new Blockchain(0, System.currentTimeMillis(), null, "0");
        int exit_flag = 0;
        while(exit_flag == 0) {

            int transactionCount = 0;
            Block lastBlock = blockchain.getBlockchain().get(blockchain.getBlockchain().size() - 1);
            Block toAdd = new Block(lastBlock.getIndex() + 1, System.currentTimeMillis(), new Data(), lastBlock.getSelf_hash());
            while (transactionCount != 3) {

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("From: ");
                String from = br.readLine();
                if(from.equals("quit")){
                    exit_flag = 1;
                    break;
                }
                System.out.print("To: ");
                String to = br.readLine();
                if(to.equals("quit")){
                    exit_flag = 1;
                    break;
                }
                System.out.print("Amount: ");
                String amountStr = br.readLine();
                if(amountStr.equals("quit")){
                    exit_flag = 1;
                    break;
                }
                int amount = Integer.parseInt(amountStr);

                toAdd.getData().getTransactions().add(new Transaction(from, to, amount));

                transactionCount++;
            }

            if(toAdd.getData().getTransactions().size() != 0)
                blockchain.addBlock(toAdd);
        }

        System.out.println("\nB L O C K C H A I N");
        blockchain.printBlockchain();
    }
}
