package si.uni_lj.fe.tnuv.vodovdnevnik;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NastavitveActivity extends AppCompatActivity {

    public static final String SHRANJENO = "si.uni_lj.fe.tnuv.vodovdnevnik.SHRANJENO";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nastavitve);
        setSpinner();
        addOtrok();
        addListenerOnShrani();
        String urlOtroci = getString(R.string.urlOtrok);
        new AsyncTaskExecutor().execute(new PrenosPodatkov(urlOtroci, this),
                this::setListView);
    }

    public void setSpinner() {
        Spinner spinner = findViewById(R.id.rod_input);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.rodovi_seznam, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setListView(String kontakti) {
        ListView listotroci = findViewById(R.id.otroci_prikaz);
        ArrayList<HashMap<String, String>> seznamKontaktov = new OtrociJsonParser().parseToArrayList(kontakti);
        SimpleAdapter adapter = new SimpleAdapter(this,
                seznamKontaktov,
                R.layout.activity_listview,
                new String[]{"ime"},
                new int[] {R.id.name}
        );

        listotroci.setAdapter(adapter);
        listotroci.callOnClick();
        listotroci.setOnItemClickListener((arg0, arg1, position, arg3) -> {
AlertDialog.Builder builder = new AlertDialog.Builder(NastavitveActivity.this);
String naslov = String.valueOf(seznamKontaktov.get(position));
builder.setTitle(naslov);
builder.setView(R.layout.list_item);
builder.setPositiveButton(getString(R.string.uredi), (dialog, which) -> {

});
builder.setNegativeButton(getString(R.string.izbrisi), (dialog, which) -> dialog.cancel());

builder.show();
});
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void addOtrok() {
        ImageButton button = findViewById(R.id.dodaj_gumb);
        button.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(NastavitveActivity.this);
            builder.setTitle(getString(R.string.dodaj_otroka));
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.add_otrok, null);
            builder.setView(dialogView);
            builder.setPositiveButton(getString(R.string.shrani), (dialog, which) -> {
                HashMap<String, String> otrok = new HashMap<>();
                EditText inputime = dialogView.findViewById(R.id.otrok_ime_input);
                EditText inputpriimek = dialogView.findViewById(R.id.otrok_priimek_input);
                EditText inputnaslov = dialogView.findViewById(R.id.otrok_naslov_input);
                EditText inputstevilka = dialogView.findViewById(R.id.otrok_tel_starsi_input);
                otrok.put("ime",inputime.getText().toString());
                otrok.put("priimek",inputpriimek.getText().toString());
                otrok.put("naslov",inputnaslov.getText().toString());
                otrok.put("stevilka",inputstevilka.getText().toString());
                String urlOtrok = getString(R.string.urlOtrok);
                JSONObject json = new OtrociPostParser().parseToJson(otrok);
                new AsyncTaskExecutor().executeOne(new PostPodatkov(urlOtrok, this, json));
                String urlOtroci = getString(R.string.urlOtrok);
                new AsyncTaskExecutor().execute(new PrenosPodatkov(urlOtroci, this),
                        this::setListView);


            });
            builder.setNegativeButton(getString(R.string.preklici), (dialog, which) -> dialog.cancel());

            builder.show();
        });
    }
    public void addListenerOnShrani() {
        final Context context = this;
        Button button = findViewById(R.id.shrani_nastavitve);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra(SHRANJENO, getString(R.string.nastavitve_shranjene));
            startActivity(intent);


        });
    }


}