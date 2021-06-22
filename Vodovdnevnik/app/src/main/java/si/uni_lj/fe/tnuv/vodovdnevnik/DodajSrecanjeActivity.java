package si.uni_lj.fe.tnuv.vodovdnevnik;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;


import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DodajSrecanjeActivity extends AppCompatActivity {
    public static final String SPOROCILO = "si.uni_lj.fe.tnuv.vodovdnevnik.SPOROCILO";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_srecanje);
        DatePickerinit();
        setSpinner();
        addListenerOnShrani();
        String urlOtroci = getString(R.string.urlOtrok);
        new AsyncTaskExecutor().execute(new PrenosPodatkov(urlOtroci, this),
                this::prikaziOtroke);
    }

    @SuppressLint("SetTextI18n")
    public void DatePickerinit() {
        final DatePickerDialog[] picker = new DatePickerDialog[1];
        Button button = findViewById(R.id.datum_input);
        button.setOnClickListener(v -> {

            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker[0] = new DatePickerDialog(DodajSrecanjeActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> button.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year1), year, month, day);
            picker[0].show();
        });
    }

    public void setSpinner() {
        Spinner spinner = findViewById(R.id.prostor_input);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.prostori_seznam, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }
    @SuppressLint("SetTextI18n")
    private void prikaziOtroke(String kontakti) {
        ListView listotroci = findViewById(R.id.seznam_otrok);

        ArrayList<HashMap<String, String>> seznamKontaktov = new OtrociJsonParser().parseToArrayList(kontakti);
        SimpleAdapter adapter = new SimpleAdapter(this,
                seznamKontaktov,
                R.layout.list_item,
                new String[]{"ime"},
                new int[]{R.id.tema_list}
        );

        for (HashMap<String, String> otrokitem : seznamKontaktov) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(otrokitem.get("ime")+" "+otrokitem.get("priimek"));
            checkBox.setId(Integer.parseInt(otrokitem.get("id")));
            LinearLayout linearLayout = findViewById(R.id.checkbox_otroci1);
            linearLayout.addView(checkBox);
        }
    }

    public void addListenerOnShrani() {
        final Context context = this;
        Button button = findViewById(R.id.shrani_srecanje);
        button.setOnClickListener(v -> {
            HashMap<String, String> srecanje = new HashMap<>();
            Button datum = findViewById(R.id.datum_input);
            EditText opis = findViewById(R.id.kratek_opis_input);
            EditText tema = findViewById(R.id.tema_input);
            EditText cilji = findViewById(R.id.cilji_input);
            Spinner prostor = findViewById(R.id.prostor_input);
            EditText vescine = findViewById(R.id.vescine_input);
            srecanje.put("datum",datum.getText().toString());
            srecanje.put("opis",opis.getText().toString());
            srecanje.put("tema",tema.getText().toString());
            srecanje.put("cilji",cilji.getText().toString());
            srecanje.put("prostor",prostor.getSelectedItem().toString());
            srecanje.put("vescine",vescine.getText().toString());
            LinearLayout ceckbox = findViewById(R.id.checkbox_otroci1);
            int count = ceckbox.getChildCount();
            int [] otroci = new int[count];
            for(int i=0; i<count; i++) {
                View pogled = ceckbox.getChildAt(i);
                if (pogled instanceof CheckBox) {
                    if (((CheckBox) pogled).isChecked()) {
                        otroci[i] = pogled.getId();
                        break;
                    }
                    //do something with your child element
                }
            }
            String url = getString(R.string.urlSrecanja);
            JSONObject json = new SrecanjePostParser().parseToJson(srecanje,otroci);
            new AsyncTaskExecutor().executeOne(new PostPodatkov(url, this, json));

            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(SPOROCILO,getString(R.string.srecanje_shranjeno));
            startActivity(intent);


        });
    }

}
