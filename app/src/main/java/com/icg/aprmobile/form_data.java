package com.icg.aprmobile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;

import DB.DBController;
import android.content.Context;
import static java.security.AccessController.getContext;

/**
 * Created by c3rv30 on 6/23/17.
 */

public class form_data extends AppCompatActivity {
    // DB Class to perform DB related operations
    DBController controller = new DBController(this);
    // Location Class
    LocationController location = new LocationController();
    private java.util.Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    String androidId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);

    TextView txtnom;
    TextView txtsector;
    TextView txtnrosocio;
    TextView txtFecUltLec;
    TextView txtvalLecAnt;
    //TextView txtperUltLec;
    TextView txtnroMedi;
    EditText txtvalUltLec;
    EditText txtobvTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_data_new);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Bundle mBundle = getIntent().getExtras();
        String nroMed = (String) mBundle.get("cod_medidor");


        txtnom = (TextView) findViewById(R.id.nom);
        txtnrosocio = (TextView) findViewById(R.id.nrosocio);
        txtsector = (TextView) findViewById(R.id.sector);
        txtFecUltLec = (TextView) findViewById(R.id.fecUltLec);
        txtvalLecAnt = (TextView) findViewById(R.id.valLecAnt);
        //txtperUltLec = (TextView) findViewById(R.id.perNvaLec);
        txtnroMedi = (TextView) findViewById(R.id.nroMedi);
        txtvalUltLec = (EditText) findViewById(R.id.valUltLec);
        txtobvTxt = (EditText) findViewById(R.id.obvTxt);

        txtnroMedi.setText(nroMed);

        HashMap<String, String> map = controller.getClientData(nroMed);

        String nom = map.get("nom");
        String nro_socio = map.get("nro_socio");
        String lec_ant  = map.get("lec_ant");
        String lec_act = map.get("lec_act");
        String per_ult_lect = map.get("per_ult_lect");
        String per_lec_act = map.get("per_lec_act");
        String sector = map.get("sector");
        String medi = map.get("medi");
        String obv = map.get("observ");

        txtnom.setText(nom);
        txtnrosocio.setText(nro_socio);
        txtvalLecAnt.setText(lec_ant);
        txtvalUltLec.setText(lec_act);
        txtFecUltLec.setText(per_ult_lect);
        //txtperUltLec.setText(per_lec_act);
        txtsector.setText(sector);
        txtnroMedi.setText(medi);
        txtobvTxt.setText(obv);

        dateView = (TextView)findViewById(R.id.perNvaLec);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

    }

    public void updateDB(View v){
        updateCli();
    }

    public void updateCli(){
        String nroMed, fecAct, lecAct, obsrv;
        nroMed = txtnroMedi.getText().toString();
        fecAct = dateView.getText().toString();
        lecAct = txtvalUltLec.getText().toString();
        obsrv = txtobvTxt.getText().toString();
/*        String lati, longi;
        HashMap<String, String> coordinates = location.displayLocation();
        lati = coordinates.get("lat");
        longi = coordinates.get("long");
        Log.d("Cordenada", "Latitud "+ lati);
        Log.d("Cordenada", "Longitud "+ longi);*/


        controller.updateClients(nroMed, fecAct, lecAct, obsrv);

        Toast.makeText(getApplicationContext(), "Datos Actualizados!!", Toast.LENGTH_LONG).show();
        reloadActivity();
        Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(objIntent);
        finish();
    }



    @SuppressWarnings("deprecation")
    public void setDate(View view){
        showDialog(999);
        //Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT).show();
    }


    protected Dialog onCreateDialog(int id){
        // TODO Auto-generated method stub
        if(id == 999){
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day){
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    // Reload MainActivity
    public void reloadActivity() {
        String nroMed;
        nroMed = txtnroMedi.getText().toString();
        Intent objIntent = new Intent(getApplicationContext(), form_data.class);
        objIntent.putExtra("nro_med", nroMed.toString());
        startActivity(objIntent);
        finish();
    }
}
