package com.icg.aprmobile;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.drawable.ic_dialog_info;
import static android.app.PendingIntent.getActivity;
import static android.app.PendingIntent.writePendingIntentOrNullToParcel;
import static android.support.v4.content.ContextCompat.getColor;

/**
 * Created by c3rv30 on 7/6/17.
 */


class CustomListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<HashMap<String, String>> userList;
    
    CustomListAdapter(Context mContext, ArrayList<HashMap<String, String>> userList){
        this.mContext = mContext;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }


    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.activity_view_user_entry, null);
        }

        TextView nom = (TextView) v.findViewById(R.id.name);
        TextView dir = (TextView) v.findViewById(R.id.dir);
        TextView medi = (TextView) v.findViewById(R.id.cod_medidor);
        TextView soc = (TextView)v.findViewById(R.id.soc);
        //ImageView info = (ImageView) v.findViewById(R.id.item_info);

        String lecAct = userList.get(position).get("lec_act");
        //String med = userList.get(position).get("medi");

        if (lecAct == null || Integer.parseInt(lecAct) == 0){
            v.setBackgroundColor(getColor(mContext, R.color.unUpdatedClient));
            nom.setTextColor(Color.BLACK);
            dir.setTextColor(Color.BLACK);
            medi.setTextColor(Color.BLACK);
        }else{
            v.setBackgroundColor(getColor(mContext, R.color.updatedClient));
            nom.setTextColor(Color.WHITE);
            dir.setTextColor(Color.WHITE);
            medi.setTextColor(Color.WHITE);
        }

        nom.setText(userList.get(position).get("nom"));
        dir.setText(userList.get(position).get("sector"));
        medi.setText(userList.get(position).get("medi"));
        soc.setText(userList.get(position).get("nro_socio"));
        //info.setImageResource(ic_dialog_info);
        return v;
    }
}























