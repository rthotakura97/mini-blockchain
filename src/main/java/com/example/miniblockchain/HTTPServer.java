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

    public static class PostHandler implements HttpHandler {

        public Transaction t;

        public PostHandler(Transaction t){
            this.t = t;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            Map<String, Object> parameters = new HashMap<String, Object>();
            String query = "from="+this.t.getFrom()+"&to="+this.t.getTo()+"&amount="+this.t.getAmount();
            parseQuery(query, parameters);

            String response = "";
            for (String key : parameters.keySet()){
                response += key + "=" + parameters.get(key) + ",";
            }
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        }
    }

    public static class ServerBlockchainHandler implements HttpHandler {

        public Blockchain blockchain;

        public ServerBlockchainHandler(Blockchain blockchain){
            this.blockchain = blockchain;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = formatBlockchainString(blockchain);
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        }
    }

    public static Map<String, String> getHTML(String urlToRead) throws IOException {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        String finalstring = result.toString();
        return split(finalstring);
    }

    public static void parseQuery(String query, Map<String,
            Object> parameters) throws UnsupportedEncodingException {

        if (query != null) {
            String pairs[] = query.split("[&]");
            for (String pair : pairs) {
                String param[] = pair.split("[=]");
                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0],
                            System.getProperty("file.encoding"));
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1],
                            System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);

                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }

    public static Blockchain getLatestBlockchain(String urlToRead) throws IOException{
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        String finalstring = result.toString();
        return parseBlockchain(finalstring);
    }

    public static String formatBlockchainString(Blockchain blockchain){
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

    public static Blockchain parseBlockchain(String s){
        Blockchain newBlockchain;
        String[] blocks = s.split("------------------------------------------------------------------");
        long genesisBlockTimestamp = getGenesisBlockTimestamp(blocks[0]);
        newBlockchain = new Blockchain(0, genesisBlockTimestamp, new Data(), "0");

        for(int i=1; i<blocks.length; i++){
            Block newBlock;
            Map<String, String> newBlockData = getBlockValues(blocks[i]);
            Map<String, String> newTransactionData = getTransactionValues(blocks[i]);
            List<Transaction> newTransactions = new ArrayList<Transaction>();
            newTransactions.add(new Transaction(newTransactionData.get("From"), newTransactionData.get("To"), newTransactionData.get("Amount")));
            newTransactions.add(new Transaction("NETWORK", "MINER", "1"));
            Data newData = new Data(Integer.parseInt(newBlockData.get("Proof")), newTransactions);
            newBlock = new Block(Integer.parseInt(newBlockData.get("Index")), Long.parseLong(newBlockData.get("Timestamp")), newData,
                    newBlockData.get("Previous Hash"));
            newBlock.printBlock();
            newBlockchain.getBlockchain().add(newBlock);
        }

        return newBlockchain;
    }

    public static Map<String, String> getTransactionValues(String blockstring){
        Map<String, String> components = new HashMap<String, String>();
        String[] splitBlock = blockstring.split(",");
        for (int j=6; j < (j + 4); j++) {
            String[] getComponents = splitBlock[j].split(":");
            components.put(getComponents[0], getComponents[1]);
        }
        return components;
    }

    public static Map<String, String> getBlockValues(String blockstring){
        Map<String, String> components = new HashMap<String, String>();
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

    public static long getGenesisBlockTimestamp(String blockstring){
        String[] splitGenesisBlock = blockstring.split(",");
        String timestampIndex = splitGenesisBlock[2];
        String[] getTimestamp = timestampIndex.split(":");
        String timestamp = getTimestamp[1];
        return Long.parseLong(timestamp);
    }

    public static Map<String, String> split(String s){
        Map<String, String> components = new HashMap<String, String>();
        String[] splitstring = s.split(",");
        for(String str: splitstring){
            String[] insidestring = str.split("=");
            components.put(insidestring[0], insidestring[1]);
        }
        return components;
    }
}
