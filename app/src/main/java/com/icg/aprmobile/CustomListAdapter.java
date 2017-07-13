package com.icg.aprmobile;

import android.content.Context;
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
    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return userList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Holder holder = null;
        View v = convertView;
        if (v == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.activity_view_user_entry, null);
        }

        TextView nom = (TextView) v.findViewById(R.id.name);
        TextView dir = (TextView) v.findViewById(R.id.dir);
        TextView medi = (TextView) v.findViewById(R.id.cod_medidor);
        ImageView info = (ImageView) v.findViewById(R.id.item_info);

        String lecAct = userList.get(position).get("lec_act");
        String med = userList.get(position).get("medi");

        if (lecAct == null){
            v.setBackgroundColor(getColor(mContext, R.color.updatedClient));
        }else{
            v.setBackgroundColor(getColor(mContext, R.color.unUpdatedClient));
        }

        nom.setText(userList.get(position).get("nom"));
        dir.setText(userList.get(position).get("sector"));
        medi.setText(userList.get(position).get("medi"));
        info.setImageResource(ic_dialog_info);
        return v;
    }
}























