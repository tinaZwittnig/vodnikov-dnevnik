package si.uni_lj.fe.tnuv.vodovdnevnik;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

class PrenosPodatkov implements Callable<String> {
    private final String urlNaslov;
    private final Activity callerActivity;  // klicatelja potrebujemo zaradi konteksta

    public PrenosPodatkov(String urlNaslov, Activity callerActivity) {
        this.urlNaslov = urlNaslov;
        this.callerActivity = callerActivity;
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
                return connect();
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
    private String connect() throws IOException {
        URL url = new URL(urlNaslov);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000 /* milliseconds */);
        conn.setConnectTimeout(10000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setRequestProperty("Accept", "application/json");
        conn.connect();	// Starts the query

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