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

/*Sets up server, manages blockchains, takes in and acts on user input*/
public class Main {

    public static void main(String[] args) throws IOException {

        /*Initialize the local blockchain*/
        long timestamp = System.currentTimeMillis();
        Blockchain blockchain = new Blockchain(0, timestamp, new Data(), "0");

        /*Start the server at port 8000*/
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        System.out.println("Server started at Port " + Integer.toString(8000));

        /*Initialize the handler for /transaction --> latest transaction on server*/
        Transaction emptyTransaction = new Transaction("none", "none", "none");
        HTTPServer.PostHandler transactionHandler = new HTTPServer.PostHandler(emptyTransaction);
        server.createContext("/transaction", transactionHandler);

        /*Initialize the handler for /blockchain  --> latest blockchain emitted to server*/
        HTTPServer.ServerBlockchainHandler blockchainHandler = new HTTPServer.ServerBlockchainHandler(blockchain);
        server.createContext("/blockchain", blockchainHandler);

        server.setExecutor(null);
        server.start();

        /*Simple user interface --> reprompt until "quit" is entered*/
        int exit_flag = 0;
        while(exit_flag == 0) {

                /*Initialze buffered reader and prompt for user input*/
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Choose an option (Transaction, Mine, Get Blocks, Print): ");
                String choice = br.readLine();

                /*Quit program*/
                if(choice.toLowerCase().equals("quit")){
                    exit_flag = 1;
                    break;
                }else if(choice.toLowerCase().equals("transaction")){

                    /*Get transaction details to send to server*/
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

                    /*Update the server with the latest transaction*/
                    transactionHandler.t = new Transaction(from, to, amountStr);

                }else if (choice.toLowerCase().equals("mine")){

                    /*Mine the current transaction on the server*/
                    Map<String, String> result = HTTPServer.getServerTransaction("http://localhost:8000/transaction");
                    boolean check = blockchain.beginMine(new Transaction(result.get("from"), result.get("to"), result.get("amount")));
                    if(check == true) {
                        /*Reset the transaction to none after transaction is mined*/
                        transactionHandler.t = emptyTransaction;
                        /*Update blockchain on the server after finished mining*/
                        blockchainHandler.blockchain = blockchain;
                    }

                }else if (choice.toLowerCase().equals("get blocks")){

                    /*Get latest blockchain from server for comparison*/
                    Blockchain latestChain = HTTPServer.getServerBlockchain("http://localhost:8000/blockchain");
                    /*If latest blockchain is longer, replace own local blockchain*/
                    if(latestChain.getBlockchain().size() > blockchain.getBlockchain().size()){
                        blockchain = latestChain;
                    }

                }else if (choice.toLowerCase().equals("print")){

                    /*Print blockchain*/
                    blockchain.printBlockchain();

                }
        }

    }

    /*Test function to compare two different blockchains, outputs differences*/
    public static boolean compareChains(Blockchain blockchain, Blockchain latestChain){

        /*Loop through each block looking for differences*/
        for(int i=1; i<blockchain.getBlockchain().size(); i++) {

            Block blockLocal = blockchain.getBlockchain().get(i);
            Block blockLatest = latestChain.getBlockchain().get(i);
            Data localData = blockLocal.getData();
            Data latestData = blockLatest.getData();

            if (blockLocal.getIndex() != blockLatest.getIndex()) {
                System.out.println("Index different");
                return false;
            }
            if (blockLocal.getTimestamp() != blockLatest.getTimestamp()) {
                System.out.println("Timestamp different");
                return false;
            }
            if (!(blockLocal.getPrev_hash().equals(blockLatest.getPrev_hash()))) {
                System.out.println("Prev hash different");
                return false;
            }
            if (!(blockLocal.getSelf_hash().equals(blockLatest.getSelf_hash()))) {
                System.out.println("Self hash different");
                return false;
            }
            if (localData.getProofId() != latestData.getProofId()) {
                System.out.println("Proof id different");
                return false;
            }
            if (!(localData.getTransactions().get(0).equals(latestData.getTransactions().get(0)))) {
                System.out.println("Transactions 0 different");
                return false;
            }
            if (!(localData.getTransactions().get(1).equals(latestData.getTransactions().get(1)))) {
                System.out.println("Transactions 1 different");
                return false;
            }
        }

        return true;
    }

}
