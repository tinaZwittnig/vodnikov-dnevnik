package si.uni_lj.fe.tnuv.vodovdnevnik;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class OtrociPostParser {
    private static final String TAG = OtrociPostParser.class.getSimpleName();
    private final JSONObject jsonObj = new JSONObject();

    public JSONObject parseToJson(HashMap<String, String> otrok){
        try {
            jsonObj.put("ime",otrok.get("ime"));
            jsonObj.put("priimek", otrok.get("priimek"));
            jsonObj.put("naslov", otrok.get("naslov"));
            jsonObj.put("stevilka", otrok.get("stevilka"));
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
        return jsonObj;
    }
}
