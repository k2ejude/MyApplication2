package com.example.jude.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    protected HorizontalBarChart mChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChart = (HorizontalBarChart) findViewById(R.id.chart1);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDescription("");
        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.animateY(2500);

//        ArrayList<String> xVals = new ArrayList<String>();
//        for(int i =0;i<12;i++){
//            xVals.add(String.valueOf(i+1));
//        }
//
//        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
//        for(int i=0;i<12;i++){
//            yVals1.add(new BarEntry((i+1)*10,i));
//        }

        new getMain().execute();

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
        if (id == R.id.action_project) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, ProjectActivity.class);
            startActivity(intent);
            return true;
        }
        else if( id == R.id.action_analysis){
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, AnalysisActivity.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private String serverUrl = "http://10.0.3.2:60576/PMS/api/AndroidApi/GetProjectsNain";

    private class getMain extends AsyncTask<String, Void, String> {

        private String error;
        String data = "";
        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        protected void onPreExecute(){
            dialog.setMessage("Please wait..");
            dialog.show();

        }

        protected void onPostExecute(String str){

            ArrayList<String> xVals = new ArrayList<String>();
            ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
            try {
                JSONArray object = new JSONArray(str);
                for(int i = 0; i < object.length(); i++){
                    xVals.add(object.getJSONObject(i).getString("id"));
                    yVals1.add(new BarEntry(Float.parseFloat(object.getJSONObject(i).getString("total").replace("%","")),i));
                }
                BarDataSet set1 = new BarDataSet(yVals1,"first");
                set1.setBarSpacePercent(35f);

                ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
                dataSets.add(set1);


                BarData data = new BarData(xVals, dataSets);
                data.setValueTextSize(10f);

                mChart.setData(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
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
                return str;


            } catch (Exception ex) {
                error = ex.getMessage();
                Log.e("ProjectActivity Error", error);
            }

            return null;
        }

    }
}
