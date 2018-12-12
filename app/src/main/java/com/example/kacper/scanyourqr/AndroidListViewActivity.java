package com.example.kacper.scanyourqr;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Kacper & Amelka on 23.01.2018.
 */

public class AndroidListViewActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // storing string resources into Array
        String[] numbers = {"one", "two", "three", "four"};
        // here you store the array of string you got from the database

        // Binding Array to ListAdapter
        this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,
                numbers));
        // refer the ArrayAdapter Document in developer.android.com
        ListView lv = getListView();

        // listening to single list item on click
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                String num = ((TextView) view).getText().toString();

//                // Launching new Activity on selecting single List Item
//                Intent i = new Intent(getApplicationContext(), SingleListItem.class);
//                // sending data to new activity
//                i.putExtra("number", num);
//                startActivity(i);

            }
        });
    }
}