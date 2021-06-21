package si.uni_lj.fe.tnuv.vodovdnevnik;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SrecanjeJsonParser {

    private static final String TAG = SrecanjeJsonParser.class.getSimpleName();
    private final ArrayList<HashMap<String, String>> srecanjeList = new ArrayList<>();

    public ArrayList<HashMap<String, String>> parseToArrayList(String jsonStr , int dolzina){
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            // Getting JSON Array node
            JSONArray srecanja = jsonObj.getJSONArray("srecanja");
            if (dolzina == 0 || dolzina > srecanja.length()){
                dolzina = srecanja.length();
            }


            // looping through All Contacts
            for (int i = 0; i < dolzina ; i++) {
                JSONObject c = srecanja.getJSONObject(i);

                String id = c.getString("id");
                String tema = c.getString("tema");
                String datum = c.getString("datum");
                String prostor = c.getString("prostor");
                String vescine = c.getString("veščine");
                String opis = c.getString("opis");
                String cilji = c.getString("cilji");
                String otroci = c.getString("otroci");

                // tmp hash map for single contact
                HashMap<String, String> srecanje = new HashMap<>();

                // adding each child node to HashMap key => value
                srecanje.put("id", id);
                srecanje.put("tema", tema);
                srecanje.put("datum", datum);
                srecanje.put("vescine", vescine);
                srecanje.put("opis", opis);
                srecanje.put("cilji", cilji);
                srecanje.put("prostor", prostor);
                srecanje.put("otroci",otroci);

                // adding contact to contact list
                srecanjeList.add(srecanje);
            }
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
        return srecanjeList;
    }
}
