package com.example.miniblockchain;

import com.example.miniblockchain.BlockData.Data;
import com.example.miniblockchain.BlockData.Transaction;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import sun.rmi.runtime.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPServer {

    /*Handler for posts to /transaction*/
    public static class PostHandler implements HttpHandler {

        /*Class transaction variable, for constant updating*/
        public Transaction t;

        /*Constructor*/
        public PostHandler(Transaction t){
            this.t = t;
        }

        /*Everytime t is changed, this function updates the server*/
        @Override
        public void handle(HttpExchange t) throws IOException {

            /*Build string to post to server, this is the transaction data*/
            String response = "amount=" + this.t.getAmount() +",from=" + this.t.getFrom() + ",to=" + this.t.getTo();

            /*Send string to server*/
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        }
    }

    public static class ServerBlockchainHandler implements HttpHandler {

        /*Class variable to store and update the blockchain posted to /blockchain*/
        public Blockchain blockchain;

        /*Constructor*/
        public ServerBlockchainHandler(Blockchain blockchain){
            this.blockchain = blockchain;
        }

        /*This function updates the server every time the local blockchain variable is updated*/
        @Override
        public void handle(HttpExchange t) throws IOException {

            /*Format the blockchain string to send to server*/
            String response = formatBlockchainString(blockchain);

            /*Send to server*/
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        }
    }

    /*Read the latest transaction from the server to local*/
    public static Map<String, String> getServerTransaction(String urlToRead) throws IOException {

        /*Establish connection and mark as a GET request*/
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        /*Get full string from server*/
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        String finalstring = result.toString();

        /*Return a HashMap of the string (divide into key,value pairs)*/
        return split(finalstring);
    }

    /*Get the latest blockchain on the server to local*/
    public static Blockchain getServerBlockchain(String urlToRead) throws IOException{

        /*Establish connection and mark as a GET request*/
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        /*Get full string from server*/
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        String finalstring = result.toString();

        /*Return HashMap of the server blockchain represented as key,value pairs*/
        return parseBlockchain(finalstring);
    }

    /*Formatting the POST for the blockchain for easy viewing when on a browser*/
    public static String formatBlockchainString(Blockchain blockchain){

        /*Loop through each block and format each key,value pair*/
        String response = "";
        for(int i = 0; i<blockchain.getBlockchain().size(); i++){

            Block currBlock = blockchain.getBlockchain().get(i);
            response += "Block:" + Integer.toString(i) + ",\n";
            response += "Index:" + Integer.toString(currBlock.getIndex()) + ",\n";
            response += "Timestamp:" + Long.toString(currBlock.getTimestamp()) + ",\n";
            response += ",\n";
            response += "Proof:" + currBlock.getData().getProofId() + ",\n,\n";

            for(int j = 0; j < currBlock.getData().getTransactions().size(); j++){
                Transaction t = currBlock.getData().getTransactions().get(j);
                response += "Transaction:" + Integer.toString(j) + ",\n";
                response += "From:" + t.getFrom() + ",\n";
                response += "To:" + t.getTo() + ",\n";
                response += "Amount:" + t.getAmount() + ",\n";
                response += ",\n";
            }

            response += "Previous Hash:" + currBlock.getPrev_hash() + ",\n";
            response += "Current Hash:" + currBlock.getSelf_hash() + ",\n";
            response += "------------------------------------------------------------------\n";

        }

        return response;
    }

    /*Parsing the blockchain on the server into key,value pairs*/
    public static Blockchain parseBlockchain(String s){

        /*Initialize new local blockchain to represent the latest chain on sever*/
        Blockchain newBlockchain;

        /*Split by block and get timestamp for genesis block, all other fields are always the same*/
        String[] blocks = s.split("------------------------------------------------------------------");
        long genesisBlockTimestamp = getGenesisBlockTimestamp(blocks[0]);
        newBlockchain = new Blockchain(0, genesisBlockTimestamp, new Data(), "0");

        /*Go through each block and get key,value pairs for each field of a block*/
        for(int i=1; i<blocks.length; i++){

            /*Create Block Data and Transaction Data to construct Block for new Blockchain*/
            Block newBlock;
            Map<String, String> newBlockData = getBlockValues(blocks[i]);
            Map<String, String> newTransactionData = getTransactionValues(blocks[i]);
            List<Transaction> newTransactions = new ArrayList<Transaction>();
            newTransactions.add(new Transaction(newTransactionData.get("From"), newTransactionData.get("To"), newTransactionData.get("Amount")));
            newTransactions.add(new Transaction("NETWORK", "MINER", "1"));

            /*Create new block*/
            Data newData = new Data(Integer.parseInt(newBlockData.get("Proof")), newTransactions);
            newBlock = new Block(Integer.parseInt(newBlockData.get("Index")), Long.parseLong(newBlockData.get("Timestamp")), newData,
                    newBlockData.get("Previous Hash"));

            /*Add new block to new blockchain*/
            newBlockchain.getBlockchain().add(newBlock);
        }

        return newBlockchain;
    }

    /*Returns a HashMap of key,values of the transactions on a blockchain on the server*/
    public static Map<String, String> getTransactionValues(String blockstring){

        Map<String, String> components = new HashMap<String, String>();

        /*Split into key,value pairs and place into HashMap*/
        String[] splitBlock = blockstring.split(",");
        /*Go through each transaction field*/
        for (int j=7; j <10 ; j++) {
            String[] getComponents = splitBlock[j].split(":");
            components.put(getComponents[0], getComponents[1]);
        }

        return components;
    }

    /*Returns HashMap of key,value pairs of the different fields (Except trasactions) of the block*/
    public static Map<String, String> getBlockValues(String blockstring){

        Map<String, String> components = new HashMap<String, String>();

        /*Hardcoding needed due to lack of pattern in the indices at which the values are located*/
        String[] splitBlock = blockstring.split(",");
        String[] getComponents = splitBlock[1].split(":");
        components.put(getComponents[0], getComponents[1]);

        getComponents = splitBlock[2].split(":");
        components.put(getComponents[0], getComponents[1]);

        getComponents = splitBlock[4].split(":");
        components.put(getComponents[0], getComponents[1]);

        getComponents = splitBlock[16].split(":");
        components.put(getComponents[0], getComponents[1]);

        getComponents = splitBlock[17].split(":");
        components.put(getComponents[0], getComponents[1]);

        return components;
    }

    /*Returns timestamp of the genesis block on the server*/
    public static long getGenesisBlockTimestamp(String blockstring){

        /*Split into key,value pair and use timestamp index*/
        String[] splitGenesisBlock = blockstring.split(",");
        String timestampIndex = splitGenesisBlock[2];
        String[] getTimestamp = timestampIndex.split(":");
        String timestamp = getTimestamp[1];
        return Long.parseLong(timestamp);

    }

    /*Returns HashMap of the transaction key,value pairs from the server*/
    public static Map<String, String> split(String s){

        /*Split into different fields*/
        Map<String, String> components = new HashMap<String, String>();
        String[] splitstring = s.split(",");

        /*Loop through seperating key,value pairs*/
        for(String str: splitstring){
            String[] insidestring = str.split("=");
            components.put(insidestring[0], insidestring[1]);
        }

        return components;
    }
}
