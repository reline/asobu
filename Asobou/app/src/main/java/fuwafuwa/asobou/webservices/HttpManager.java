package fuwafuwa.asobou.webservices;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpManager {

    public static final String SERVER = "http://198.199.94.36/AsobouyoBackend";
    public static String TAG = "HttpManager";

    public static void postData(String uri, String data) {

        try {
            uri += "?" + data;
            Log.d(TAG, uri);

            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(data.length()));

            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(data);
            os.flush();
            os.close();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getData(String uri, String data) {

        uri += "?" + data;
        Log.d(TAG, uri);

        BufferedReader reader = null;

        try {
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            StringBuilder sbuffer = new StringBuilder();
            InputStream foo = connection.getInputStream();
            InputStreamReader bar = new InputStreamReader(foo);
            reader = new BufferedReader(bar);
            String line;
            while((line = reader.readLine()) != null){
                sbuffer.append(line);
            }
            return sbuffer.toString();

        } catch (IOException e) {
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
            }
        }
    }
}
