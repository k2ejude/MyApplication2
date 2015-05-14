package com.example.jude.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class AnalysisDetailActivity extends ActionBarActivity {
    private String serverUrl = "http://10.0.3.2:60576/PMS/api/AndroidApi/GetAnalysisDetail/";
    private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    private SimpleAdapter listAdapter;
    private ListView analysisDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_detail);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        serverUrl = serverUrl+id;

        analysisDetail = (ListView) findViewById(R.id.analysisDetail);

        new getAnalysiesDetail().execute();
    }

    private class getAnalysiesDetail extends AsyncTask<String, Void, String> {
        private String error;
        String data = "";
        private ProgressDialog dialog = new ProgressDialog(AnalysisDetailActivity.this);

        protected void onPreExecute(){
            dialog.setMessage("Please wait..");
            dialog.show();

        }

        protected void onPostExecute(String str){
            Log.d("Test",str);
            try {
                JSONArray object = new JSONArray(str);
                for(int i = 0; i < object.length(); i++){
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("id", object.getJSONObject(i).getString("id"));
                    item.put("name", object.getJSONObject(i).getString("name"));
                    item.put("income", object.getJSONObject(i).getString("income"));
                    item.put("loss", object.getJSONObject(i).getString("loss"));
                    item.put("cost", object.getJSONObject(i).getString("cost"));
                    list.add(item);
                }
                listAdapter = new SimpleAdapter(AnalysisDetailActivity.this, list, R.layout.analysisdetaillist,
                        new String[]{"id", "name", "income", "loss", "cost"},
                        new int[]{R.id.idText, R.id.nameText, R.id.incomeText, R.id.lossText, R.id.costText});
                analysisDetail.setAdapter(listAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
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
                return result.toString();


            }
            catch (Exception ex){
                error = ex.getMessage();
                Log.e("ProjectActivity Error", error);
            }
            return null;
        }
    }
}
