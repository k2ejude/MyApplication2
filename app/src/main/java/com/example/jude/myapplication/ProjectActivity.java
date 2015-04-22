package com.example.jude.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ProjectActivity extends ActionBarActivity {

    private ListView listView;
    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    private SimpleAdapter listAdapter;
    private String serverUrl = "http://10.26.6.194:60576/PMS/api/AndroidApi/GetProjects";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        new getProjects().execute(serverUrl);


        listView = (ListView) findViewById(R.id.listProject);

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

    private class getProjects extends AsyncTask<String, Void, Void> {


        private String error;
        String data = "";
        private ProgressDialog dialog = new ProgressDialog(ProjectActivity.this);

        protected void onPreExecute(){
            dialog.setMessage("Please wait..");
            dialog.show();

        }

        protected void onPostExecute(Void unused){
            dialog.dismiss();
        }

        @Override
        protected Void doInBackground(String... params) {
            BufferedReader reader = null;
            JSONObject object = null;
            try {
                HttpClient client = new DefaultHttpClient();
                URL url = new URL(params[0]);
                HttpGet get = new HttpGet(String.valueOf(url));
                HttpResponse response = client.execute(get);
                HttpEntity resEntity = response.getEntity();
                String a = EntityUtils.toString(resEntity);
                object = new JSONObject(a);
                Log.d("Test", object.toString());
                JSONArray array = new JSONArray(object.getString("Message"));
                for(int i = 0; i < array.length(); i++){
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("id", array.getJSONObject(i).getString("id"));
                    item.put("name", array.getJSONObject(i).getString("name"));
                    item.put("member", array.getJSONObject(i).getString("member"));
                    item.put("loss", array.getJSONObject(i).getString("loss"));
                    item.put("priority", array.getJSONObject(i).getString("priority"));
                    item.put("time", array.getJSONObject(i).getString("time"));
                    list.add(item);
                }
                listAdapter = new SimpleAdapter(ProjectActivity.this, list, R.layout.projectlist,
                        new String[]{"id", "name", "member", "loss", "priority", "time"},
                        new int[]{R.id.projectIdText, R.id.projectNameText, R.id.projectMainMemberText, R.id.projectLossText, R.id.projectPriorityText, R.id.projectTimeText});
                listView.setAdapter(listAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent();
                        intent.setClass(ProjectActivity.this, ProjectDetailActivity.class);
                        startActivity(intent);
                    }
                });

            } catch (Exception ex) {
                error = ex.getMessage();
                Log.d("ProjectActivity Error",error);
            } finally {
                try {
                    reader.close();
                }
                catch (Exception ex){}
            }


//            Log.d("Test", Integer.toString(b));
            return null;
        }

    }
}
