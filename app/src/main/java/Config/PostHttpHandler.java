package Config;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class PostHttpHandler {

    public String makeServiceCall(String reqUrl,JSONObject jsonParam) {
        String responsee = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setConnectTimeout(30000); //set meout ttio 5 seconds
            conn.setReadTimeout(30000);//new

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());


            os.write(jsonParam.toString().getBytes("UTF-8"));
            os.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            StringBuilder response = new StringBuilder();
            while ((output = br.readLine()) != null) {
                response.append(output);
                response.append('\r');
            }
            String mes = response.toString();
            responsee=mes;
            conn.disconnect();

        } catch (MalformedURLException e) {
            Log.e("", "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e("", "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
           Log.e("", "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e("", "Exception: " + e.getMessage());
        }
        return responsee;
    }




}
