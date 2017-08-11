package com.example.miniblockchain;

import com.example.miniblockchain.BlockData.Transaction;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

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
