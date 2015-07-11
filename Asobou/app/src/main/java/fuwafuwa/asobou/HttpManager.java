package fuwafuwa.asobou;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mena on 7/10/2015.
 * for doing an http request
 * returns a string
 */
public class HttpManager {

    public static String getData(String uri) {
        BufferedReader reader = null;

        try {
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            StringBuilder sbuffer = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while((line = reader.readLine()) != null){
                sbuffer.append(line);
            }

            return sbuffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        } finally {
            if (reader != null) {
                try{
                    reader.close();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }

            }   //end reader if

        }   //end finally

    }   //end getData method

}   //end HttpManager class
