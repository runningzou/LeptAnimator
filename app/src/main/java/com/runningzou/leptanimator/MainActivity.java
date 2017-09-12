package com.runningzou.leptanimator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {


    private ListView mListView;

    private static final String[] sTitles = new String[]{
            "FunctionTest",
            "BounceEditText",
            "scrollActivity"

    };

    private static final Class<Activity>[] sClazzs = new Class[]{
            FunctionTestActivity.class,
            BounceEditTextActivity.class,
            ScrollActivity.class

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.lv_main);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sTitles);
        mListView.setAdapter(arrayAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, sClazzs[i]);
                startActivity(intent);
            }
        });
    }


}
