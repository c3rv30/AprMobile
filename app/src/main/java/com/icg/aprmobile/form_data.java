package com.icg.aprmobile;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Logger;

import DB.DBController;


/**
 * Created by c3rv30 on 6/23/17.
 */

public class form_data extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{
    // DB Class to perform DB related operations
    private DBController controller = new DBController(this);
    // Location Class
    private LocationController location = new LocationController();
    // Secure ID
    //private SecureID secureId = new SecureID();
    private java.util.Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    // GeoLocation
    private static final int MY_PERMISSION_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 7172;

    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static int UPDATE_INTERVAL = 5000; // SEC
    private static int FATEST_INTERVAL = 3000; // SEC
    private static int DISPLACEMENT = 10; // METERS

    TextView txtnom, txtsector, txtnrosocio, txtFecUltLec, txtvalLecAnt, txtnroMedi, txtperUltLec, txtLongitude, txtLatitude;
    EditText txtvalUltLec, txtobvTxt;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayServices())
                        buildGoogleApiClient();
                        createLocationRequest();
                    break;
                }
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_form_data_new);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                getSupportActionBar().setLogo(R.mipmap.ic_launcher);
                getSupportActionBar().setDisplayUseLogoEnabled(true);
            }

            Bundle mBundle = getIntent().getExtras();
            String nroMed = (String) mBundle.get("cod_medidor");

            txtnom = (TextView) findViewById(R.id.nom);
            txtnrosocio = (TextView) findViewById(R.id.nrosocio);
            txtsector = (TextView) findViewById(R.id.sector);
            txtFecUltLec = (TextView) findViewById(R.id.fecUltLec);
            txtvalLecAnt = (TextView) findViewById(R.id.valLecAnt);
            txtnroMedi = (TextView) findViewById(R.id.medi);
            txtvalUltLec = (EditText) findViewById(R.id.valUltLec);
            txtobvTxt = (EditText) findViewById(R.id.obvTxt);

            txtLatitude = (TextView) findViewById(R.id.latitude);
            txtLongitude = (TextView) findViewById(R.id.longitude);

            txtnroMedi.setText(nroMed);
            HashMap<String, String> map = controller.getClientData(nroMed);

            txtnom.setText(map.get("nom"));
            txtnrosocio.setText(map.get("nro_socio"));
            txtvalLecAnt.setText(map.get("lec_ant"));
            txtvalUltLec.setText(map.get("lec_act"));
            txtFecUltLec.setText(map.get("per_ult_lect"));
            txtsector.setText(map.get("sector"));
            txtnroMedi.setText(map.get("medi"));
            txtobvTxt.setText(map.get("observ"));

            dateView = (TextView) findViewById(R.id.perNvaLec);
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            showDate(year, month + 1, day);


            if (android.support.v4.app.ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && android.support.v4.app.ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Run-tome request permission
                android.support.v4.app.ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, MY_PERMISSION_REQUEST_CODE);
            } else {
                if (checkPlayServices()) {
                    buildGoogleApiClient();
                    createLocationRequest();
/*            }else {
                //Toast.makeText(getApplicationContext(), "Error de GPS y GOOGLE PLAY SERVICES", Toast.LENGTH_LONG).show();

                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Alerta")
                        .setMessage("Error de GPS y GOOGLE PLAY SERVICES")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                        }).show();
            }*/
                }
            }


        }

    public void updateDB(View v){
        updateCli();
    }

    public void updateCli(){
        displayLocation();
        String nroMed, fecAct, lecAct, obsrv,lati,longi,androidID;
        nroMed = txtnroMedi.getText().toString();
        fecAct = dateView.getText().toString();
        lecAct = txtvalUltLec.getText().toString();
        obsrv = txtobvTxt.getText().toString();
        lati = txtLatitude.getText().toString();
        longi = txtLongitude.getText().toString();
        /*
        String lati, longi;
        HashMap<String, String> coordinates = location.displayLocation();
        lati = coordinates.get("lat");
        longi = coordinates.get("long");
        Log.d("Cordenada", "Latitud "+ lati);
        Log.d("Cordenada", "Longitud "+ longi);
        */
        
        controller.updateClients(nroMed, fecAct, lecAct, obsrv, lati, longi);

        Toast.makeText(getApplicationContext(), "Datos Actualizados!!", Toast.LENGTH_LONG).show();
        reloadActivity();
        Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(objIntent);
        finish();
    }



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
        objIntent.putExtra("nro_med", nroMed);
        startActivity(objIntent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
            //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private void tooglePeriodLocationUpdates(){
        if (!mRequestingLocationUpdates){
            //btnLocationUpdates.setText("Stop location update");
            mRequestingLocationUpdates = true;
            startLocationUpdates();
        }else {
            //btnLocationUpdates.setText("Start location update");
            mRequestingLocationUpdates = false;
            stopLocationUpdates();
        }
    }

    private void displayLocation(){
        if (android.support.v4.app.ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && android.support.v4.app.ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null){
            String latitude = Double.toString(mLastLocation.getLatitude());
            String longitude = Double.toString(mLastLocation.getLongitude());

            //txtCoordinates.setText(latitude + " / " + longitude);
            txtLatitude.setText(latitude);
            txtLongitude.setText(longitude);
        } else {
            //txtCoordinates.setText("Couldn't get the location. Make sure location is enable on the device");
            Toast.makeText(getApplicationContext(), "No se puede obtener la localizacion. Asegurese de que la localizacion esta activada en el dispositivo", Toast.LENGTH_LONG).show();
        }
    }


    private void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        // Fix first time run app if permission doesn't grant yet so can't get anything
        mGoogleApiClient.connect();
    }

/*    private boolean checkPlayServices(){
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        //int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS){

            //if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }else{
                Toast.makeText(getApplicationContext(), "Este Dispositivo no es compatible.", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }
*/

    private boolean checkPlayServices() {
        final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //Logger.logE(TAG, "This device is not supported.");
                Toast.makeText(getApplicationContext(), "Este Dispositivo no es compatible.", Toast.LENGTH_LONG).show();
                //finish();
            }
            return false;
        }
        return true;
    }

    private void startLocationUpdates(){
        if (android.support.v4.app.ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && android.support.v4.app.ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        if(mRequestingLocationUpdates)
            startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
    }

}