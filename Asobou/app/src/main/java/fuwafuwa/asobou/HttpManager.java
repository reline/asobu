package fuwafuwa.asobou;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mena on 7/10/2015.
 * for doing an http request
 * returns a string
 */
@SuppressWarnings("ALL")
public class HttpManager {

    public static void postData(String uri, String data) {

        String TAG = "PlayTypeModeActivity";
         // add_user_id
         // add_song_id
         // add_score
         // add_date

        try {
            //String encodedData = URLEncoder.encode(data, "UTF-8");
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

            int responseCode = connection.getResponseCode();
            Log.d(TAG, "\nSending 'POST' request to URL : " + url);
            Log.d(TAG, "\nPost parameters : " + data);
            Log.d(TAG, "\nResponse Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Log.d(TAG, response.toString());

        } catch (IOException e) {
            //e.printStackTrace();
        } finally {

        }

    }

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

}   //end GETmanager class
