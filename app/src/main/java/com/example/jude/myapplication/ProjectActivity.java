package com.example.jude.myapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectActivity extends ActionBarActivity {

    private ListView listView;
    private ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    HashMap<String,String> item1 = new HashMap<String,String>();
    HashMap<String,String> item2 = new HashMap<String,String>();
    private SimpleAdapter listAdapter;
    private int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        item1.put("id","project1");
        item1.put("name","專案一");
        item1.put("member","Admin");
        item1.put("loss","100000");
        item1.put("priority","5");
        item1.put("time","25天");
        list.add(item1);

        item2.put("id","project2");
        item2.put("name","專案二");
        item2.put("member","Admin");
        item2.put("loss","10000");
        item2.put("priority","2");
        item2.put("time","50天");
        list.add(item2);


        listView = (ListView)findViewById(R.id.listProject);
        listAdapter = new SimpleAdapter(this, list, R.layout.projectlist,
                new String[]{"id", "name", "member", "loss", "priority", "time"},
                new int[]{R.id.projectIdText, R.id.projectNameText, R.id.projectMainMemberText, R.id.projectLossText, R.id.projectPriorityText, R.id.projectTimeText});
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings1) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
