package com.icg.aprmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import DB.DBController;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    // DB Class to perform DB related operations
    DBController controller = new DBController(this);
    ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myList = (ListView) findViewById(android.R.id.list);
        // Get User records from SQLite DB
        ArrayList<HashMap<String, String>> userList = controller.getAllUsersLec();
        Toast.makeText(getApplicationContext(), "Clientes : "+userList.size(), Toast.LENGTH_LONG).show();
        // If users exists in SQLite DB
        if (userList.size() != 0) {
            // Set the User Array list in ListView (CustomListAdapter)
            myList.setAdapter(new CustomListAdapter(MainActivity.this, userList));
        }

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView medi = (TextView) view.findViewById(R.id.cod_medidor);
                String cod = medi.getText().toString();
                Intent intent = new Intent(MainActivity.this, form_data.class);
                intent.putExtra("cod_medidor", cod);
                startActivity(intent);
            }
        });
    }
}