package si.uni_lj.fe.tnuv.vodovdnevnik;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SrecanjaJsonParser {

    private static final String TAG = SrecanjaJsonParser.class.getSimpleName();
    private ArrayList<HashMap<String, String>> contactList = new ArrayList<>();

    public ArrayList<HashMap<String, String>> parseToArrayList(String jsonStr){
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            // Getting JSON Array node
            JSONArray contacts = jsonObj.getJSONArray("contacts");

            // looping through All Contacts
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);

                String id = c.getString("id");
                String name = c.getString("name");
                String email = c.getString("email");
                String address = c.getString("address");
                String gender = c.getString("gender");

                // Phone node is JSON Object
                JSONObject phone = c.getJSONObject("phone");
                String mobile = phone.getString("mobile");
                String home = phone.getString("home");
                String office = phone.getString("office");

                // tmp hash map for single contact
                HashMap<String, String> contact = new HashMap<>();

                // adding each child node to HashMap key => value
                contact.put("id", id);
                contact.put("name", name);
                contact.put("email", email);
                contact.put("mobile", mobile);

                // adding contact to contact list
                contactList.add(contact);
            }
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
        return contactList;
    }
}
