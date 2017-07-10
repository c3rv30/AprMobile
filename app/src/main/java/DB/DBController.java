package DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by c3rv30 on 6/23/17.
 */

public class DBController extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "apr";
    // Contacts table name
    private static final String TABLE_CLIENTES = "clientes";
    // clientes Table Columns names
    private static final String KEY_NOMBRE = "nom";
    private static final String KEY_NRO_SOCIO = "nro_socio";
    private static final String KEY_LEC_ANTERIOR = "lec_ant";
    private static final String KEY_LEC_ACTUAL = "lec_act";
    private static final String KEY_PER_ULT_LEC = "per_ult_lect";
    private static final String KEY_SECTOR = "sector";
    private static final String KEY_NRO_MEDIDOR = "nro_med";

    private static final String KEY_PER_LEC_ACTUAL = "per_lec_act";
    private static final String KEY_OBSERVACION = "observ";
    private static final String KEY_LATITUTE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";

    public DBController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CLIENTES_TABLE = "CREATE TABLE " + TABLE_CLIENTES + "("
                + KEY_NOMBRE + " TEXT,"
                + KEY_NRO_SOCIO + " TEXT,"
                + KEY_LEC_ANTERIOR + " INTEGER,"
                + KEY_LEC_ACTUAL + " INTEGER,"
                + KEY_PER_ULT_LEC + " TEXT,"
                + KEY_PER_LEC_ACTUAL + " TEXT,"
                + KEY_SECTOR + " TEXT,"
                + KEY_NRO_MEDIDOR + " TEXT,"
                + KEY_LATITUTE + " TEXT,"
                + KEY_LONGITUDE + " TEXT,"
                + KEY_OBSERVACION + " TEXT" + ")";
        // FALTA PERIODO ACTUAL
        db.execSQL(CREATE_CLIENTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTES);
        // Creating tables again
        onCreate(db);
    }
    // Adding new clients
    public void addClientes(HashMap<String, String> queryValues) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOMBRE, queryValues.get("nom"));
        values.put(KEY_NRO_SOCIO, queryValues.get("nro_socio"));

        String lec_ant_txt = queryValues.get("lec_ant");
        //int lec_ant = Integer.parseInt(lec_ant_txt);
        values.put(KEY_LEC_ANTERIOR, lec_ant_txt);

        String lec_act_txt = queryValues.get("lec_act");
        //int lec_act = Integer.parseInt(lec_ant_txt);
        values.put(KEY_LEC_ACTUAL, lec_act_txt);

        values.put(KEY_PER_ULT_LEC, queryValues.get("per_ult_lect"));
        values.put(KEY_SECTOR, queryValues.get("sector"));
        values.put(KEY_NRO_MEDIDOR, queryValues.get("medi"));

        // Inserting Row
        db.insert(TABLE_CLIENTES, null, values);
        db.close(); // Closing database connection
    }

    // Get list of Users from SQLite DB as Array List
    public ArrayList<HashMap<String, String>> getAllUsersLec() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CLIENTES;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("nom", cursor.getString(0));
                map.put("nro_socio", cursor.getString(1));
                map.put("lec_ant", cursor.getString(2));
                map.put("lec_act", cursor.getString(3));
                map.put("per_ult_lect", cursor.getString(4));
                map.put("per_lec_act", cursor.getString(5));
                map.put("sector", cursor.getString(6));
                map.put("medi", cursor.getString(7));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //database.close();
        return usersList;
    }

    public ArrayList<HashMap<String, String>> getNroMed() {
        ArrayList<HashMap<String, String>> usersList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor =  database.rawQuery( "SELECT "+KEY_NRO_MEDIDOR+" FROM "+TABLE_CLIENTES+"", null );
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("medi", cursor.getString(0));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //database.close();
        return usersList;
    }


    public ArrayList<String> getNroMedArray() {
        ArrayList<String> usersList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor =  database.rawQuery( "SELECT "+KEY_NRO_MEDIDOR+" FROM "+TABLE_CLIENTES+"", null );
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //HashMap<String, String> map = new HashMap<String, String>();
                String medi = cursor.getString(0);
                //map.put("medi", cursor.getString(0));
                usersList.add(medi);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //database.close();
        return usersList;
    }



    // Get list of Users from SQLite DB as Array List
    public HashMap<String, String> getClientData(String nroMedi) {
        HashMap<String, String> usersList;
        //String nroMed = nroMedi;
        usersList = new HashMap<>();
        // Select All Query
        String selectQuery = "SELECT * FROM "+TABLE_CLIENTES+" WHERE "+KEY_NRO_MEDIDOR+"='"+nroMedi+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //HashMap<String, String> map = new HashMap<>();
                usersList.put("nom", cursor.getString(0));
                usersList.put("nro_socio", cursor.getString(1));
                usersList.put("lec_ant", cursor.getString(2));
                usersList.put("lec_act", cursor.getString(3));
                usersList.put("per_ult_lect", cursor.getString(4));
                usersList.put("per_lec_act", cursor.getString(5));
                usersList.put("sector", cursor.getString(6));
                usersList.put("medi", cursor.getString(7));
                usersList.put("observ", cursor.getString(8));
                //usersList.putAll(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //database.close();
        return usersList;
    }


    public void updateClients(String nroMed, String fecAct, String lecAct, String obsrv, String lati, String longi){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update "+TABLE_CLIENTES+" set "
                +KEY_PER_LEC_ACTUAL+" = '"+ fecAct +"', "
                +KEY_LEC_ACTUAL+" = '"+ lecAct +"', "
                +KEY_LATITUTE+" = '"+ lati +"', "
                +KEY_LONGITUDE+" = '"+ longi +"', "
                +KEY_OBSERVACION+" = '"+ obsrv +"' WHERE "
                +KEY_NRO_MEDIDOR+"='"+nroMed+"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }


    public void deleteFromTableLec(){
        SQLiteDatabase database = this.getWritableDatabase();
        String query;
        query = "DELETE FROM " + TABLE_CLIENTES;
        database.execSQL(query);
    }



    /**
     * Compose JSON out of SQLite records
     * @return
     */
    public String composeJSONfromSQLite(String androidID){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<>();
        String selectQuery = "SELECT "+KEY_NRO_SOCIO+"," +
                ""+KEY_LEC_ACTUAL+"," +
                ""+KEY_PER_LEC_ACTUAL+"," +
                ""+KEY_NRO_MEDIDOR+"," +
                ""+KEY_OBSERVACION+"," +
                ""+KEY_LATITUTE+"," +
                ""+KEY_LONGITUDE+"" +
                " FROM " +TABLE_CLIENTES;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("nro_socio", cursor.getString(0));
                map.put("lec_act", cursor.getString(1));
                map.put("per_lec_act", cursor.getString(2));
                map.put("medi", cursor.getString(3));
                map.put("observ", cursor.getString(4));
                map.put("latitud", cursor.getString(5));
                map.put("longitud", cursor.getString(6));
                map.put("id_device", androidID);
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //database.close();
        Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }
}