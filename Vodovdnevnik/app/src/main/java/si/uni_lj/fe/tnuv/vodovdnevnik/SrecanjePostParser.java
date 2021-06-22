package si.uni_lj.fe.tnuv.vodovdnevnik;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SrecanjePostParser {
    private static final String TAG =SrecanjePostParser.class.getSimpleName();
    private final JSONObject jsonObj = new JSONObject();

    public JSONObject parseToJson(HashMap<String, String> otrok, int[] otroci){
        try {
            jsonObj.put("datum",otrok.get("datum"));
            jsonObj.put("tema", otrok.get("tema"));
            jsonObj.put("cilji", otrok.get("cilji"));
            jsonObj.put("vescine", otrok.get("vescine"));
            jsonObj.put("opis", otrok.get("opis"));
            jsonObj.put("prostor", otrok.get("prostor"));
            JSONArray sez = new JSONArray();
            for (int i = 0; i< otroci.length ;i++){
                //JSONObject otr = new JSONObject();
                //otr.put("id",Integer.toString(otroci[i]));
                sez.put(Integer.toString(otroci[i]));

            }
            jsonObj.put("prisotni", sez);

        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
        return jsonObj;
    }
}
