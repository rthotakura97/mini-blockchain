package com.example.miniblockchain;

import com.example.miniblockchain.BlockData.Data;
import com.example.miniblockchain.BlockData.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/*
NEXT TO DO:
    ADD STRUCTURE TO KEEP TRACK OF ALL BALANCES
    DEPLOY ON SERVER
 */

public class Main {
    public static void main(String[] args) throws IOException {

        Blockchain blockchain = new Blockchain(0, System.currentTimeMillis(), new Data(), "0");
        int exit_flag = 0;
        Transaction transaction;
        while(exit_flag == 0) {


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

                transaction = new Transaction(from, to, amount);
                blockchain.beginMine(transaction);

        }

        blockchain.printBlockchain();
    }
}
