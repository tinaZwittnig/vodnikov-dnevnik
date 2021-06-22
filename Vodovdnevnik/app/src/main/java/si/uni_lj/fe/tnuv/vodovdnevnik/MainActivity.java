package si.uni_lj.fe.tnuv.vodovdnevnik;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    private String urlNaslov;
    private String urlOtroci;
    private String urlPrihodnja;
    private String urlOtrok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prikaziToast();
        addListenerOnButtonNastavitve();
        addListenerOnButtonSrecanje();
        addListenerOnPrikaziVec();
        urlNaslov = getString(R.string.urlNaslov);
        urlOtroci = getString(R.string.urlOtrok);
        urlPrihodnja = getString(R.string.urlprihodnja);



    }

    @Override
    protected void onStart() {
        super.onStart();


        new AsyncTaskExecutor().execute(new PrenosPodatkov(urlNaslov, this),
                this::prikaziPodatke);
        new AsyncTaskExecutor().execute(new PrenosPodatkov(urlOtroci, this),
                this::prikaziOtroke);
        new AsyncTaskExecutor().execute(new PrenosPodatkov(urlPrihodnja, this),
                this::prikaziPrihodnja);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void prikaziOtroke(String otroci_sez) {
        ListView listotroci = findViewById(R.id.seznam_otrok);

        ArrayList<HashMap<String, String>> seznamKontaktov = new OtrociJsonParser().parseToArrayList(otroci_sez);
        SimpleAdapter adapter = new SimpleAdapter(this,
                seznamKontaktov,
                R.layout.list_item,
                new String[]{"ime"},
                new int[]{R.id.tema_list}
        );

        listotroci.setAdapter(adapter);
        listotroci.callOnClick();
        listotroci.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            HashMap<String, String> podatki = seznamKontaktov.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            String naslov = podatki.get("ime") + " " + podatki.get("priimek");
            urlOtrok = getString(R.string.urlOtrok)+"/"+podatki.get("id")+"/sestanki";
            builder.setTitle(naslov);
            builder.setNegativeButton(getString(R.string.zapri), (dialog, which) -> dialog.cancel());
            new AsyncTaskExecutor().execute(new PrenosPodatkov(urlOtrok, this),
                    rezultat -> prikaziOtrok(rezultat, builder));
        });
    }
    private void prikaziOtrok(String srecanja, AlertDialog.Builder alert){
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.otrok_layout, null);
        ListView listotroci = dialogView.findViewById(R.id.prisotnost_otork);
        ListView listvescine = dialogView.findViewById(R.id.vescina_otork);

        ArrayList<HashMap<String, String>> seznamSrecanj = new PrisotnostParser().parseToArrayList(srecanja);
        ArrayList<HashMap<String, String>> seznamVescin = new PrisotnostParser().vescineParseToArrayList(srecanja);
        SimpleAdapter adapter = new SimpleAdapter(this,
                seznamSrecanj,
                R.layout.prihodnja_layout,
                new String[]{"tema"},
                new int[]{R.id.prihodnje_datum}
        );
        SimpleAdapter adapter2 = new SimpleAdapter(this,
                seznamVescin,
                R.layout.prihodnja_layout,
                new String[]{"vescine"},
                new int[]{R.id.prihodnje_datum}
        );

        listotroci.setAdapter(adapter);
        listvescine.setAdapter(adapter2);
        alert.setView(dialogView);
        alert.show();
    }
    private void prikaziPrihodnja(String srecanja){
        ListView lv = findViewById(R.id.koledar);
        ArrayList<HashMap<String, String>> seznamKontaktov = new PrihodnjaJsonParser().parseToArrayList(srecanja);
        SimpleAdapter adapter = new SimpleAdapter(this,
                seznamKontaktov,
                R.layout.prihodnja_layout,
                new String[]{"datum", "ura","kaj"},
                new int[]{R.id.prihodnje_datum, R.id.prihodnje_ura,R.id.prihodnje_kaj}
        );

        lv.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void prikaziPodatke(String kontakti) {
        ListView lv = findViewById(R.id.list);
        int dolzina = 4;
        ArrayList<HashMap<String, String>> seznamKontaktov = new SrecanjeJsonParser().parseToArrayList(kontakti, dolzina);
        SimpleAdapter adapter = new SimpleAdapter(this,
                seznamKontaktov,
                R.layout.list_item,
                new String[]{"tema", "datum",},
                new int[]{R.id.tema_list, R.id.datum_list}
        );

        lv.setAdapter(adapter);
        lv.callOnClick();
        lv.setOnItemClickListener((arg0, arg1, position, arg3) -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

    public void addListenerOnButtonNastavitve() {
        final Context context = this;
        Button button = findViewById(R.id.button_nastavitve);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(context, NastavitveActivity.class);
            startActivity(intent);

        });
    }

    public void addListenerOnButtonSrecanje() {
        final Context context = this;
        Button button = findViewById(R.id.button_dodaj);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(context, DodajSrecanjeActivity.class);
            startActivity(intent);

        });

    }

    public void addListenerOnPrikaziVec() {
        final Context context = this;
        Button button = findViewById(R.id.prikazi_vec);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(context, VsaSrecanjaActivity.class);
            startActivity(intent);

        });
    }

    public void prikaziToast() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String sporocilo = intent.getStringExtra(DodajSrecanjeActivity.SPOROCILO);
            if (sporocilo == null) {
                sporocilo = intent.getStringExtra(NastavitveActivity.SHRANJENO);
            }
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(this, sporocilo, duration);
            toast.show();
        }
    }


}


