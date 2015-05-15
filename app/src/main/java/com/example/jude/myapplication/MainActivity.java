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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ValueFormatter;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    protected HorizontalBarChart mChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChart = (HorizontalBarChart) findViewById(R.id.chart1);
        mChart.setDrawValueAboveBar(false);
        mChart.setDescription("");
        mChart.setDrawValueAboveBar(true);
       mChart.setPinchZoom(false);
        mChart.animateY(2500);
        mChart.getLegend().setEnabled(false);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new PercentFormatter());
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setValueFormatter(new PercentFormatter());
//        YAxis yaxis = new YAxis();
//        yaxis.setAxisMaxValue(100);
//        yaxis.setValueFormatter(new PercentFormatter());

        new getMain().execute();

    }

    public class PercentFormatter implements ValueFormatter {

        protected DecimalFormat mFormat;

        public PercentFormatter() {
            mFormat = new DecimalFormat();
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value) + " %";
        }
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

    private String serverUrl = "http://10.0.3.2:60576/PMS/api/AndroidApi/GetProjectsMain";

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
                    Float yVal = Float.parseFloat(object.getJSONObject(i).getString("total").replace("%",""));
                    if(yVal == 0)
                        yVal = 12f * i;//測試假資料
                    yVals1.add(new BarEntry(yVal,i));
                }

                BarDataSet set1 = new BarDataSet(yVals1,"first");
                set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
                set1.setBarSpacePercent(35f);
                set1.setValueFormatter(new PercentFormatter());
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
