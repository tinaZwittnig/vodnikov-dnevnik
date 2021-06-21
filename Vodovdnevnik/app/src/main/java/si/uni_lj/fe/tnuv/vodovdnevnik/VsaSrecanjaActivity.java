package si.uni_lj.fe.tnuv.vodovdnevnik;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class VsaSrecanjaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vsa_srecanja);

        String urlNaslov = getString(R.string.urlNaslov);
        new AsyncTaskExecutor().execute(new PrenosPodatkov(urlNaslov, this),
                this::prikaziPodatke);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void prikaziPodatke(String kontakti){
        ListView lv = findViewById(R.id.seznam_vseh_srecanj);
        int dolzina = 4;
        ArrayList<HashMap<String, String>> seznamKontaktov = new SrecanjeJsonParser().parseToArrayList(kontakti, dolzina);
        Log.i("Bla",kontakti);
        SimpleAdapter adapter = new SimpleAdapter(this,
                seznamKontaktov,
                R.layout.list_item,
                new String[]{"tema", "datum",},
                new int[] {R.id.tema_list, R.id.datum_list}
        );

        lv.setAdapter(adapter);
        lv.callOnClick();
        lv.setOnItemClickListener((arg0, arg1, position, arg3) -> {
AlertDialog.Builder builder = new AlertDialog.Builder(VsaSrecanjaActivity.this);
String naslov = String.valueOf(seznamKontaktov.get(position));
builder.setTitle(naslov);
builder.setView(R.layout.srecanje_layout);
builder.setPositiveButton(getString(R.string.shrani), (dialog, which) -> {

});
builder.setNegativeButton(getString(R.string.preklici), (dialog, which) -> dialog.cancel());

builder.show();
});
    }
}