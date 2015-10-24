package fuwafuwa.asobou;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpManager {

    public static String TAG = "HttpManager";

    public static void postData(String uri, String data) {

        try {
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(data.length()));
            DataOutputStream os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(data);
            //os.write(encodedData.getBytes());
            os.flush();
            os.close();

            // DEBUG
            /*int responseCode = connection.getResponseCode();
            Log.d(TAG, "\nSending 'POST' request to URL : " + url);
            Log.d(TAG, "\nPost parameters : " + data);
            Log.d(TAG, "\nResponse Code : " + responseCode);*/

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //Log.d(TAG, response.toString()); // it's time to d-d-d-debug!

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getData(String uri) {
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

            // Show Http request in console ***DEBUG***
            /*int responseCode = connection.getResponseCode();
            Log.d(TAG, "\nSending 'GET' request to URL : " + url);
            //Log.d(TAG, "\nPost parameters : " + data);
            Log.d(TAG, "\nResponse Code : " + responseCode);

            Log.d(TAG, sbuffer.toString());*/

            return sbuffer.toString();

        } catch (IOException e) {
            Log.d(TAG, "Exception in HttpManager");
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

}
