package si.uni_lj.fe.tnuv.vodovdnevnik;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
    private void prikaziPodatke(String kontakti) {
        ListView lv = findViewById(R.id.seznam_vseh_srecanj);
        int dolzina = 4;
        ArrayList<HashMap<String, String>> seznamKontaktov = new SrecanjeJsonParser().parseToArrayList(kontakti, dolzina);
        Log.i("Bla", kontakti);
        SimpleAdapter adapter = new SimpleAdapter(this,
                seznamKontaktov,
                R.layout.list_item,
                new String[]{"tema", "datum",},
                new int[]{R.id.tema_list, R.id.datum_list}
        );

        lv.setAdapter(adapter);
        lv.callOnClick();
        lv.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.srecanje_layout, null);

            HashMap<String, String> podatki = seznamKontaktov.get(position);
            String naslov = String.valueOf(podatki.get("tema"));
            builder.setTitle(naslov);
            builder.setView(dialogView);
            builder.setNegativeButton(getString(R.string.zapri), (dialog, which) -> dialog.cancel());
            TextView vescine = dialogView.findViewById(R.id.vescine_srecanje);
            vescine.setText(podatki.get("vescine"));
            TextView opis = dialogView.findViewById(R.id.opis_srecanje);
            opis.setText(podatki.get("opis"));
            TextView datum = dialogView.findViewById(R.id.datum_srecanje);
            datum.setText(podatki.get("datum"));
            TextView cilji = dialogView.findViewById(R.id.cilji_srecanje);
            cilji.setText(podatki.get("cilji"));
            TextView prisotni = dialogView.findViewById(R.id.prisotni_srecanje);
            prisotni.setText(podatki.get("otroci"));
            TextView prostor = dialogView.findViewById(R.id.prostor_srecanje);
            prostor.setText(podatki.get("prostor"));

            builder.show();

        });
    }
}