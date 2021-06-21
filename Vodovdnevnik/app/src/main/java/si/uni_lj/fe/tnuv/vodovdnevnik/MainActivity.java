package si.uni_lj.fe.tnuv.vodovdnevnik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prikaziToast();
        addListenerOnButtonNastavitve();
        addListenerOnButtonSrecanje();
        addListenerOnPrikaziVec();
        prikaziprihodnje();

    }

    public void addListenerOnButtonNastavitve() {
        final Context context = this;
        button = (Button) findViewById(R.id.button_nastavitve);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NastavitveActivity.class);
                startActivity(intent);

            }
        });
    }

    public void addListenerOnButtonSrecanje() {
        final Context context = this;
        button = (Button) findViewById(R.id.button_dodaj);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DodajSrecanjeActivity.class);
                startActivity(intent);

            }
        });

    }

    public void addListenerOnPrikaziVec() {
        final Context context = this;
        button = (Button) findViewById(R.id.prikazi_vec);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VsaSrecanjaActivity.class);
                startActivity(intent);

            }
        });
    }

    public void prikaziToast() {
        Intent intent = getIntent();
        if (intent.getExtras() != null ){
        String sporocilo = intent.getStringExtra(DodajSrecanjeActivity.SPOROCILO);
        if (sporocilo==null){
            sporocilo = intent.getStringExtra(NastavitveActivity.SHRANJENO);
        }
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, sporocilo, duration);
        toast.show();}
    }
    public void prikaziprihodnje(){
        new AsyncTaskExecutor().execute(new PrenosPodatkov(urlNaslov, this),
                rezultat -> prikaziPodatke(rezultat));

    }
    private void prikaziPodatke(String kontakti){
        ArrayList<HashMap<String, String>> seznamKontaktov = new ContactsJsonParser().parseToArrayList(kontakti);
        prikaz = (TextView) findViewById(R.)

        SimpleAdapter adapter = new SimpleAdapter(this,
                seznamKontaktov,
                R.layout.list_item,
                new String[]{"name", "email", "mobile"},
                new int[] {R.id.name, R.id.email, R.id.mobile}
        );

        lv.setAdapter(adapter);

    }



}


