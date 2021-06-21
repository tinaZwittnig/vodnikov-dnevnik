package si.uni_lj.fe.tnuv.vodovdnevnik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prikaziToast();
        addListenerOnButtonNastavitve();
        addListenerOnButtonSrecanje();
        addListenerOnPrikaziVec();

    }

    public void addListenerOnButtonNastavitve() {
        final Context context = this;
        Button button = findViewById(R.id.button_nastavitve);
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
        Button button = findViewById(R.id.button_dodaj);
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
        Button button = findViewById(R.id.prikazi_vec);
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



}


