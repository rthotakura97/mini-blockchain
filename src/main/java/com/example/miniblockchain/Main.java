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
    ADD STRUCTURE TO KEEP TRACK OF ALL BALANCES
    DEPLOY ON SERVER
 */

public class Main {
    public static void main(String[] args) throws IOException {

        int portNumber = 8000;
        HTTPServer.PostHandler phandler = new HTTPServer.PostHandler(new Transaction("none", "none", "none"));
        HttpServer server = HttpServer.create(new InetSocketAddress(portNumber), 0);
        System.out.println("Server started at Port " + Integer.toString(portNumber));
        server.createContext("/Post", phandler);
        server.setExecutor(null);
        server.start();

        Blockchain blockchain = new Blockchain(0, System.currentTimeMillis(), new Data(), "0");
        int exit_flag = 0;

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

                phandler.t = new Transaction(from, to, amountStr);
                //blockchain.beginMine(transaction);

        }
        //blockchain.printBlockchain();
    }

}
