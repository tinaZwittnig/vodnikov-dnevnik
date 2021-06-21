package si.uni_lj.fe.tnuv.vodovdnevnik;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OtrociJsonParser {
    private static final String TAG = si.uni_lj.fe.tnuv.vodovdnevnik.OtrociJsonParser.class.getSimpleName();
    private final ArrayList<HashMap<String, String>> srecanjeList = new ArrayList<>();

    public ArrayList<HashMap<String, String>> parseToArrayList(String jsonStr) {
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            // Getting JSON Array node
            JSONArray otroci = jsonObj.getJSONArray("otroci");

            // looping through All Contacts
            for (int i = 0; i < otroci.length(); i++) {
                JSONObject c = otroci.getJSONObject(i);

                String id = c.getString("id");
                String ime = c.getString("ime");
                String priimek = c.getString("priimek");
                String naslov = c.getString("naslov");
                String stevilka = c.getString("številka");


                // tmp hash map for single contact
                HashMap<String, String> otrok = new HashMap<>();

                // adding each child node to HashMap key => value
                otrok.put("id", id);
                otrok.put("ime", ime);
                otrok.put("priimek", priimek);
                otrok.put("naslov", naslov);
                otrok.put("stevilka", stevilka);


                // adding contact to contact list
                srecanjeList.add(otrok);
            }
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
        return srecanjeList;
    }
}

