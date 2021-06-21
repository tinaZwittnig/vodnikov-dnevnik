package si.uni_lj.fe.tnuv.vodovdnevnik;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class DodajSrecanjeActivity extends AppCompatActivity {
    public static final String SPOROCILO = "si.uni_lj.fe.tnuv.vodovdnevnik.SPOROCILO";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_srecanje);
        DatePickerinit();
        setSpinner();
        addSelectOtrok();
        addListenerOnShrani();
    }

    public void DatePickerinit() {
        final DatePickerDialog[] picker = new DatePickerDialog[1];
        Button button = (Button) findViewById(R.id.datum_input);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker[0] = new DatePickerDialog(DodajSrecanjeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                button.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                            }
                        }, year, month, day);
                picker[0].show();
            }
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
    public void addSelectOtrok(){
        String[] otroci = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X"};
        for (String s : otroci) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(s);
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.checkbox_otroci1);
            linearLayout.addView(checkBox);
        }

    }
    public void addListenerOnShrani() {
        final Context context = this;
        Button button = (Button) findViewById(R.id.shrani_srecanje);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra(SPOROCILO, "Srečanje shranjeno!");
                startActivity(intent);


            }
        });
    }

}
