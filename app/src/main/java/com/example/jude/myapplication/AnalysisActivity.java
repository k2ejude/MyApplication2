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
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class AnalysisActivity extends ActionBarActivity {

    private String serverUrl = "http://10.0.3.2:60576/PMS/api/AndroidApi/GetAnalysies";
    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    private SimpleAdapter listAdapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        listView = (ListView) findViewById(R.id.listView);

        new getAnalysies().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_analysis, menu);
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


    private class getAnalysies extends AsyncTask<String, Void, Void>{
        private String error;
        String data = "";
        private ProgressDialog dialog = new ProgressDialog(AnalysisActivity.this);

        protected void onPreExecute(){
            dialog.setMessage("Please wait..");
            dialog.show();

        }

        protected void onPostExecute(Void unused){
            dialog.dismiss();
        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(serverUrl);
                HttpResponse response = client.execute(get);
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                String str = result.toString();
                JSONArray object = new JSONArray(str);
                for(int i = 0; i < object.length(); i++){
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("id", object.getJSONObject(i).getString("id"));
                    item.put("name", object.getJSONObject(i).getString("name"));
                    item.put("content", object.getJSONObject(i).getString("content"));
                    item.put("loss", object.getJSONObject(i).getString("loss"));
                    item.put("time", object.getJSONObject(i).getString("time"));
                    list.add(item);
                }
                listAdapter = new SimpleAdapter(AnalysisActivity.this, list, R.layout.analysislist,
                        new String[]{"id", "name", "content", "loss", "time"},
                        new int[]{R.id.analysisIdText, R.id.analysisNameText, R.id.analysisContentText, R.id.analysisLossText, R.id.analysisTimeText});
                listView.setAdapter(listAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView id = (TextView)view.findViewById(R.id.analysisIdText);
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", id.getText().toString());
                        intent.putExtras(bundle);
//                        intent.setClass(AnalysisActivity.this, ProjectDetailActivity.class);
//                        startActivity(intent);
                        String a = "AAAA";
                    }
                });

            }
            catch (Exception ex){
                error = ex.getMessage();
                Log.e("ProjectActivity Error", error);
            }
            return null;
        }
    }
}
