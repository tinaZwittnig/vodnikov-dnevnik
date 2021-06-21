package si.uni_lj.fe.tnuv.vodovdnevnik;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    private String urlNaslov;
    private String urlOtroci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prikaziToast();
        addListenerOnButtonNastavitve();
        addListenerOnButtonSrecanje();
        addListenerOnPrikaziVec();
        Log.i("Bla", "init");
        urlNaslov = getString(R.string.urlNaslov);
        urlOtroci = getString(R.string.urlOtrok);


    }

    @Override
    protected void onStart() {
        super.onStart();


        new AsyncTaskExecutor().execute(new PrenosPodatkov(urlNaslov, this),
                this::prikaziPodatke);
        new AsyncTaskExecutor().execute(new PrenosPodatkov(urlOtroci, this),
                this::prikaziOtroke);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void prikaziOtroke(String kontakti) {
        ListView listotroci = findViewById(R.id.seznam_otrok);

        ArrayList<HashMap<String, String>> seznamKontaktov = new OtrociJsonParser().parseToArrayList(kontakti);
        Log.i("otroci", kontakti);
        SimpleAdapter adapter = new SimpleAdapter(this,
                seznamKontaktov,
                R.layout.list_item,
                new String[]{"ime"},
                new int[]{R.id.tema_list}
        );

        listotroci.setAdapter(adapter);
        listotroci.callOnClick();
        listotroci.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            String naslov = String.valueOf(seznamKontaktov.get(position));
            builder.setTitle(naslov);
            builder.setView(R.layout.list_item);
            builder.setPositiveButton(getString(R.string.shrani), (dialog, which) -> {

            });
            builder.setNegativeButton(getString(R.string.preklici), (dialog, which) -> dialog.cancel());

            builder.show();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void prikaziPodatke(String kontakti) {
        ListView lv = findViewById(R.id.list);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            String naslov = String.valueOf(seznamKontaktov.get(position));
            builder.setTitle(naslov);
            builder.setView(R.layout.srecanje_layout);
            builder.setPositiveButton(getString(R.string.shrani), (dialog, which) -> {

            });
            builder.setNegativeButton(getString(R.string.preklici), (dialog, which) -> dialog.cancel());

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


