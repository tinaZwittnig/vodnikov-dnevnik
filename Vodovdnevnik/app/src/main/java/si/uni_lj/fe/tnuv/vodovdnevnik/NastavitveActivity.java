package si.uni_lj.fe.tnuv.vodovdnevnik;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class NastavitveActivity extends AppCompatActivity {

    public static final String SHRANJENO = "si.uni_lj.fe.tnuv.vodovdnevnik.SHRANJENO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nastavitve);
        setSpinner();
        setListView();
        addOtrok();
        addListenerOnShrani();
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

    public void setListView() {
        String[] mobileArray = {"Android", "IPhone", "WindowsMobile", "Blackberry",
                "WebOS", "Ubuntu", "Windows7", "Max OS X"};
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, mobileArray);

        ListView listView = (ListView) findViewById(R.id.otroci_prikaz);
        listView.setAdapter(adapter);
    }

    public void addOtrok() {
        final Context context = this;
        ImageButton button = (ImageButton) findViewById(R.id.dodaj_gumb);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NastavitveActivity.this);
                builder.setTitle("Dodaj otroka");
                builder.setView(R.layout.add_otrok);
                builder.setPositiveButton("shrani", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("Prekliči", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }
    public void addListenerOnShrani() {
        final Context context = this;
        Button button = (Button) findViewById(R.id.shrani_nastavitve);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra(SHRANJENO, "Nastavitve shranjene");
                startActivity(intent);


            }
        });
    }

}