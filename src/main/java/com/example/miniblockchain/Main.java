package com.example.miniblockchain;

import com.example.miniblockchain.BlockData.Data;
import com.example.miniblockchain.BlockData.Transaction;
import com.example.miniblockchain.HTTPServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
NEXT TO DO:
    CONSENSUS PROTOCOL
        - if local blockchian is longer, replace the servers
        - if servers blockchain is longer, replace the local
 */

public class Main {
    public static void main(String[] args) throws IOException {

        long timestamp = System.currentTimeMillis();
        Blockchain blockchain = new Blockchain(0, timestamp, new Data(), "0");

        int portNumber = 8000;
        Transaction emptyTransaction = new Transaction("none", "none", "none");
        HTTPServer.PostHandler transactionHandler = new HTTPServer.PostHandler(emptyTransaction);
        HTTPServer.ServerBlockchainHandler blockchainHandler = new HTTPServer.ServerBlockchainHandler(blockchain);
        HttpServer server = HttpServer.create(new InetSocketAddress(portNumber), 0);
        System.out.println("Server started at Port " + Integer.toString(portNumber));
        server.createContext("/transaction", transactionHandler);
        server.createContext("/blockchain", blockchainHandler);
        server.setExecutor(null);
        server.start();

        int exit_flag = 0;

        while(exit_flag == 0) {

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                System.out.println("Choose an option (Transaction, Mine, Get Blocks, Print): ");
                String choice = br.readLine();
                if(choice.toLowerCase().equals("quit")){
                    exit_flag = 1;
                    break;
                }
                if(choice.toLowerCase().equals("transaction")){

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

                    transactionHandler.t = new Transaction(from, to, amountStr);

                }else if (choice.toLowerCase().equals("mine")){
                    Map<String, String> result = HTTPServer.getHTML("http://localhost:8000/transaction");
                    blockchain.beginMine(new Transaction(result.get("from"), result.get("to"), result.get("amount")));
                    transactionHandler.t = emptyTransaction;
                    blockchainHandler.blockchain = blockchain;
                }else if (choice.toLowerCase().equals("get blocks")){
                    Blockchain latestChain = HTTPServer.getLatestBlockchain("http://localhost:8000/blockchain");
                    //compareChains(blockchain, latestChain);
                }else if (choice.toLowerCase().equals("print")){
                    blockchain.printBlockchain();
                }

        }

    }

    public static void compareChains(Blockchain blockchain, Blockchain latestChain){

        for(int i=1; i<blockchain.getBlockchain().size(); i++) {

            Block blockLocal = blockchain.getBlockchain().get(i);
            Block blockLatest = latestChain.getBlockchain().get(i);
            Data localData = blockLocal.getData();
            Data latestData = blockLatest.getData();

            if (blockLocal.getIndex() != blockLatest.getIndex())
                System.out.println("Index different");
            if (blockLocal.getTimestamp() != blockLatest.getTimestamp())
                System.out.println("Timestamp different");
            if (!(blockLocal.getPrev_hash().equals(blockLatest.getPrev_hash())))
                System.out.println("Prev hash different");
            if (!(blockLocal.getSelf_hash().equals(blockLatest.getSelf_hash())))
                System.out.println("Self hash different");
            if (localData.getProofId() != latestData.getProofId())
                System.out.println("Proof id different");
            if (!(localData.getTransactions().get(0).equals(latestData.getTransactions().get(0))))
                System.out.println("Transactions 0 different");
            if (!(localData.getTransactions().get(1).equals(latestData.getTransactions().get(1))))
                System.out.println("Transactions 1 different");

        }
    }

}
