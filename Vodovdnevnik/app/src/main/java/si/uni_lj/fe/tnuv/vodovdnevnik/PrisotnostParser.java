package si.uni_lj.fe.tnuv.vodovdnevnik;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PrisotnostParser {
    private static final String TAG = PrisotnostParser.class.getSimpleName();
    private final ArrayList<HashMap<String, String>> temaList = new ArrayList<>();
    private final ArrayList<HashMap<String, String>> vescinaList = new ArrayList<>();

    public ArrayList<HashMap<String, String>> parseToArrayList(String jsonStr){
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            // Getting JSON Array node
            JSONArray srecanja = jsonObj.getJSONArray("srecanja");

            // looping through All Contacts
            for (int i = 0; i < srecanja.length(); i++) {
                JSONObject c = srecanja.getJSONObject(i);

                String tema = c.getString("tema");

                HashMap<String, String> teme = new HashMap<>();

                // adding each child node to HashMap key => value
                teme.put("tema", tema);
                temaList.add(teme);
            }
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
        return temaList;
    }
    public ArrayList<HashMap<String, String>> vescineParseToArrayList(String jsonStr){
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray srecanja = jsonObj.getJSONArray("srecanja");
            for (int i = 0; i < srecanja.length(); i++) {
                JSONObject c = srecanja.getJSONObject(i);
                String vescina = c.getString("veščine");
                HashMap<String, String> vescine = new HashMap<>();
                vescine.put("vescine", vescina);
                vescinaList.add(vescine);
            }
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
        return vescinaList;
    }
}
