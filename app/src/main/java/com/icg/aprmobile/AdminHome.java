package com.icg.aprmobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import DB.DBController;

/**
 * Created by c3rv30 on 7/11/17.
 */

public class AdminHome extends AppCompatActivity{
    // DB Class to perform DBrelated operations
    DBController controller = new DBController(this);
    // Progres Dialog Object
    ProgressDialog prgDialog, prgDialogUp;
    // Secure androidID
    private String id_android;

    HashMap<String, String> queryValues;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Menu Principal");
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }

        id_android = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Initialize Progress Dialog properties
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Transfiriendo datos desde el servidor remoto. Espere Porfavor...");
        prgDialog.setCancelable(false);

        prgDialogUp = new ProgressDialog(this);
        prgDialogUp.setMessage("Transfiriendo datos hacia el servidor remoto. Espere Porfavor...");
        prgDialogUp.setCancelable(false);

    }

    public void goRoute(View v){
        int rows = controller.getRows();
        if(rows != 0){
            Intent intent = new Intent(AdminHome.this, MainActivity.class);
            startActivity(intent);
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Alerta")
                    .setMessage("LISTA DE RUTA VACIA, DESEA DESCARGARLA?")
                    .setPositiveButton("SI", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            syncSQLiteMySQLDB();
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setCancelable(true).show();
        }
    }

    public void syncDB(View v) {
        int rows = controller.getRows();
        if (rows != 0) {
            new AlertDialog.Builder(this)
                    .setTitle("Advertencia")
                    .setMessage("YA EXISTE UNA RUTA ALMACENADA")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setCancelable(true).show();
        }else {
            syncSQLiteMySQLDB();
        }
    }

    public void deleteTable(View v){
        int rows = controller.getRows();
        if(rows != 0){
            new AlertDialog.Builder(this)
                    .setTitle("Alerta")
                    .setMessage("SE BORRARA LA RUTA ACTUAL, DESEA PROCEDER?")
                    .setPositiveButton("SI", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            controller.deleteFromTableLec();
                            Toast.makeText(getApplicationContext(), "RUTA ELIMINADA", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
            }).show();
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Alerta")
                    .setMessage("NO HAY RUTAS PARA BORRAR")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
    }


    public void upData(View v){
        syncSQLiteToMySQLDB();
    }

    public void reloadActivity() {
        Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(objIntent);
        finish();
    }

    // Method to Sync MySQL to SQLite DB
    public void syncSQLiteMySQLDB() {
        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        // Show ProgressBar
        prgDialog.show();
        // Make Http call to getusers.php
        client.post("http://www.informaticacasagrande.cl/aprweb/apr/lecturas/app1.asp?"+"id_device="+id_android, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                // Update SQLite DB with response sent by getusers.php
                updateSQLite(response);
                // Hide ProgressBar
                prgDialog.hide();
                Toast.makeText(getApplicationContext(), "RUTA DESCARGADA", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AdminHome.this, MainActivity.class);
                startActivity(intent);
            }
            // When error occured
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // TODO Auto-generated method stub
                // Hide ProgressBar
                prgDialog.hide();
                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "No se encontro el recurso solicitado", Toast.LENGTH_LONG).show();
                }else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Problemas con el servidor remoto", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Error inesperado! [Error mas comun: Dispositivo sin conexion a internet ]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void updateSQLite(String response){
        /*
        ArrayList<HashMap<String, String>> usersynclist;
        usersynclist = new ArrayList<HashMap<String, String>>();

        // Create GSON object
        Gson gson = new GsonBuilder().create();
        */
        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            // If no of array elements is not zero
            if(arr.length() != 0){
                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    System.out.println(obj.get("nombre"));
                    System.out.println(obj.get("nro_socio"));
                    System.out.println(obj.get("lec_anterior"));
                    System.out.println(obj.get("periodo_ultima_lectura"));
                    System.out.println(obj.get("lec_actual"));
                    System.out.println(obj.get("sector"));
                    System.out.println(obj.get("medi"));
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<>();
                    // Add userID extracted from Object
                    queryValues.put("nom", obj.get("nombre").toString());
                    queryValues.put("nro_socio", obj.get("nro_socio").toString());
                    queryValues.put("lec_ant", obj.get("lec_anterior").toString());
                    queryValues.put("per_ult_lect", obj.get("periodo_ultima_lectura").toString());
                    queryValues.put("lec_act", obj.get("lec_actual").toString());
                    queryValues.put("sector", obj.get("sector").toString());
                    queryValues.put("medi", obj.get("medi").toString());
                    // Insert User into SQLite DB
                    controller.addClientes(queryValues);
                    /*
                    HashMap<String, String> map = new HashMap<>();
                    Add status for each User in Hashmap
                    map.put("Id", obj.get("userId").toString());
                    map.put("status", "1");
                    usersynclist.add(map);
                    */
                }
                // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
                //updateMySQLSyncSts(gson.toJson(usersynclist));
                // Reload the Main Activity
                //
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Method to inform remote MySQL DB about completion of Sync activity
    public void updateMySQLSyncSts(String json) {
        System.out.println(json);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("syncsts", json);
        // Make Http call to updatesyncsts.php with JSON parameter which has Sync statuses of Users
        client.post("http://192.168.2.4:9000/mysqlsqlitesync/updatesyncsts.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(),	"MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Toast.makeText(getApplicationContext(), "Ocurrio un Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void syncSQLiteToMySQLDB(){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        ArrayList<HashMap<String, String>> userList =  controller.getAllUsersLec();
        if(userList.size()!=0){
            //if(controller.dbSyncCount() != 0){
            prgDialogUp.show();
            params.put("usersJSON", controller.composeJSONfromSQLite(id_android));
            client.post("http://www.informaticacasagrande.cl/aprweb/apr/lecturas/leerjson.asp",params ,new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    System.out.println(response);
                    prgDialogUp.hide();
                    Toast.makeText(getApplicationContext(), "DATOS ACTUALIZADOS EN EL SERVIDOR", Toast.LENGTH_LONG).show();
                    /*try {
                        JSONArray arr = new JSONArray(response);
                        System.out.println(arr.length());
                            *//*for(int i=0 ; i<arr.length() ; i++){
                                JSONObject obj = (JSONObject)arr.get(i);
                                System.out.println(obj.get("id"));
                                System.out.println(obj.get("status"));
                                controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());
                            }*//*
                        Toast.makeText(getApplicationContext(), "Sincronizacion completa!", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        //Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        Log.e("Error", "Response");
                    }*/

                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    // TODO Auto-generated method stub
                    prgDialog.hide();
                    if(statusCode == 404){
                        Toast.makeText(getApplicationContext(), "No se encontro el recurso solicitado", Toast.LENGTH_LONG).show();
                    }else if(statusCode == 500){
                        Toast.makeText(getApplicationContext(), "Problemas con el servidor remoto", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Error inesperado! [Error mas comun: Dispositivo sin conexion a internet ]", Toast.LENGTH_LONG).show();
                    }
                }
            });
            //}else{
            //    Toast.makeText(getApplicationContext(), "SQLite and Remote MySQL DBs are in Sync!", Toast.LENGTH_LONG).show();
            //}
        }else{
            Toast.makeText(getApplicationContext(), "No hay Datos para Subir", Toast.LENGTH_LONG).show();
        }
    }
}
