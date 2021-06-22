package si.uni_lj.fe.tnuv.vodovdnevnik;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PrihodnjaJsonParser {
    private static final String TAG = PrihodnjaJsonParser.class.getSimpleName();
    private ArrayList<HashMap<String, String>> contactList = new ArrayList<>();

    public ArrayList<HashMap<String, String>> parseToArrayList(String jsonStr){
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            // Getting JSON Array node
            JSONArray contacts = jsonObj.getJSONArray("srecanja");

            // looping through All Contacts
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);

                String id = c.getString("id");
                String datum = c.getString("datum");
                String kaj = c.getString("kaj");
                String ura = c.getString("ura");

                // tmp hash map for single contact
                HashMap<String, String> contact = new HashMap<>();

                // adding each child node to HashMap key => value
                contact.put("id", id);
                contact.put("datum", datum);
                contact.put("kaj", kaj);
                contact.put("ura", ura);

                // adding contact to contact list
                contactList.add(contact);
            }
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
        return contactList;
    }

}
