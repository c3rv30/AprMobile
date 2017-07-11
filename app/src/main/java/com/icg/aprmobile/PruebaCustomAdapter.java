package com.icg.aprmobile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;

import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by c3rv30 on 7/10/17.
 */

class ListViewWithBaseAdapter extends Activity {

/*    ListView listView;
    private class codeLeanChapter {
        String chapterName;
        String chapterDescription;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_with_simple_adapter);

        listView = (ListView) findViewById(R.id.listView1);

        ListViewCustomAdapter adapter = new ListViewCustomAdapter(this, getDataForListView());

        listView.setAdapter(adapter);

        int totalListViewsize = adapter.getCount();
    }




    public List<codeLeanChapter> getDataForListView() {
        List<codeLeanChapter> codeLeanChaptersList = new ArrayList<codeLeanChapter>();

        for (int i = 0; i < 10; i++) {

            codeLeanChapter chapter = new codeLeanChapter();
            chapter.chapterName = "Chapter " + i;
            chapter.chapterDescription = "This is description for chapter " + i;
            codeLeanChaptersList.add(chapter);
        }

        return codeLeanChaptersList;

    }

    private class ListViewCustomAdapter extends BaseAdapter {
        Context context;
        int totalDisplayDatasize = 0;
        List<codeLeanChapter> codeLeanChapterList;

        ListViewCustomAdapter(ListViewWithBaseAdapter context,
                              List<codeLeanChapter> codeLeanChapterList) {
            this.context = context;
            this.codeLeanChapterList = codeLeanChapterList;
            if (this.codeLeanChapterList != null)
                totalDisplayDatasize = this.codeLeanChapterList.size();
            System.out.println("Inside ListViewCustomAdapter ");
        }

        @Override
        public int getCount() {
            // this could be one of the reason for not showing listview.set
            // total data length for count
            return totalDisplayDatasize;
        }

        @Override
        public codeLeanChapter getItem(int i) {
            return this.codeLeanChapterList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        private class Holder {
            TextView textView1, textView2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            View view = convertView;
                *//*
                 * First time for row if view is not created than inflate the view
                 * and create instance of the row view Cast the control by using
                 * findview by id and store it in view tag using holder class
                 *//*
            if (view == null) {
                holder = new Holder();
                // / No need to create LayoutInflater instance in
                // constructor

                convertView = LayoutInflater.from(this.context).inflate(R.layout.listitem, null);

                holder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
                holder.textView2 = (TextView) convertView.findViewById(R.id.textView2);
                convertView.setTag(holder);
            } else {
                    *//*
                     * Here view next time it wont b null as its created and
                     * inflated once and in above if statement its created. And
                     * stored it in view tag. Get the holder class from view tag
                     *//*
                holder = (Holder) convertView.getTag();
            }
            holder.textView1.setText("chapterDescription : "+ codeLeanChapterList.get(position).chapterDescription);
            holder.textView2.setText("chapterName : "+ codeLeanChapterList.get(position).chapterName);
            return convertView;
        }
    }*/
}