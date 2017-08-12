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

        Blockchain blockchain = new Blockchain(0, System.currentTimeMillis(), new Data(), "0");

        int portNumber = 8000;
        HTTPServer.PostHandler transactionHandler = new HTTPServer.PostHandler(new Transaction("none", "none", "none"));
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

                System.out.println("Choose an option (Transaction, Mine, Get Blocks): ");
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
                    blockchainHandler.blockchain = blockchain;
                }else if (choice.toLowerCase().equals("get blocks")){
                    blockchain.printBlockchain();
                }

        }

    }

}
