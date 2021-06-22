package si.uni_lj.fe.tnuv.vodovdnevnik;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

class PostPodatkov implements Callable<String> {
    private final String urlNaslov;
    private final Activity callerActivity;
    private final JSONObject jsonObject;// klicatelja potrebujemo zaradi konteksta

    public PostPodatkov(String urlNaslov, Activity callerActivity, JSONObject jsonObject) {
        this.urlNaslov = urlNaslov;
        this.callerActivity = callerActivity;
        this.jsonObject = jsonObject;
    }

    @Override
    public String call() {
        ConnectivityManager connMgr = (ConnectivityManager) callerActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo;
        try {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        catch (Exception e){
            //je v manifestu dovoljenje za uporabo omrezja?
            return callerActivity.getResources().getString(R.string.napaka_omrezje);
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                return connect(jsonObject);
            } catch (IOException e) {
                return callerActivity.getResources().getString(R.string.napaka_povezava);
            }
        }
        else{
            return callerActivity.getResources().getString(R.string.napaka_omrezje);
        }
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the content as a InputStream, which it returns as a string.
    private String connect(JSONObject jsonParam) throws IOException {
        URL url = new URL(urlNaslov);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000 /* milliseconds */);
        conn.setConnectTimeout(10000 /* milliseconds */);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        conn.setRequestProperty("Accept","application/json");
        conn.setDoInput(true);
        conn.connect();	// Starts the query
        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
        //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
        os.writeBytes(jsonParam.toString());

        os.flush();
        os.close();

        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
        Log.i("MSG" , conn.getResponseMessage());

        conn.disconnect();

        // blokira, dokler ne dobi odgovora
        int response = conn.getResponseCode();

        // Convert the InputStream into a string
        return convertStreamToString(conn.getInputStream());
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}