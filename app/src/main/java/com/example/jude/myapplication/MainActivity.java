package com.example.jude.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
    private TableLayout table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChart = (HorizontalBarChart) findViewById(R.id.chart1);
        table = (TableLayout)findViewById(R.id.table);
        mChart.setDrawValueAboveBar(false);
        mChart.setDescription("");
        mChart.setDrawValueAboveBar(true);
        mChart.setPinchZoom(false);
        mChart.animateY(2500);
        mChart.getLegend().setEnabled(false);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMaxValue(100f);
        leftAxis.setValueFormatter(new PercentFormatter());
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setAxisMaxValue(100f);
        rightAxis.setValueFormatter(new PercentFormatter());
//        XAxis xAxis = mChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
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

                    TableRow tableRow = new TableRow(MainActivity.this);
                    TextView text1 = new TextView(MainActivity.this);
                    text1.setText(object.getJSONObject(i).getString("id"));
                    text1.setGravity(Gravity.CENTER);
                    text1.setBackgroundColor(Color.rgb(255, 255, 255));

                    TextView text2 = new TextView(MainActivity.this);
                    text2.setText(object.getJSONObject(i).getString("name"));
                    text2.setGravity(Gravity.CENTER);
                    text2.setBackgroundColor(Color.rgb(255, 255, 255));

                    TextView text3 = new TextView(MainActivity.this);
                    text3.setText(object.getJSONObject(i).getString("day"));
                    text3.setGravity(Gravity.CENTER);
                    text3.setBackgroundColor(Color.rgb(255, 255, 255));
                    TextView text4 = new TextView(MainActivity.this);
                    text4.setText(object.getJSONObject(i).getString("endTime"));
                    text4.setGravity(Gravity.CENTER);
                    text4.setBackgroundColor(Color.rgb(255, 255, 255));
                    TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
                    params.setMargins(2,2,2,2);

                    tableRow.addView(text1,params);
                    tableRow.addView(text2,params);
                    tableRow.addView(text3,params);
                    tableRow.addView(text4,params);
                    table.addView(tableRow);
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
