package com.example.jude.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ProjectDetailActivity extends ActionBarActivity {

    String id = "";
    private String serverUrl = "http://192.168.2.106:60576/PMS/api/AndroidApi/GetProjectDetail/";
    TextView projectIdText,projectNameText,projectMemberText,projectPriorityText,projectStartTimeText,projectEndTimeText,projectFacilityText,projectOtherText,projectIncomeText,projectLossText;
    BarChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitity_projectdetail);

        getView();

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        serverUrl = serverUrl+id;

        new getProjectDetail().execute();
    }

    private void getView()
    {
        projectIdText = (TextView)findViewById(R.id.projectIdText);
        projectNameText = (TextView)findViewById(R.id.projectNameText);
        projectMemberText = (TextView)findViewById(R.id.projectMemberText);
        projectPriorityText = (TextView)findViewById(R.id.projectPriorityText);
        projectStartTimeText = (TextView)findViewById(R.id.projectStartTimeText);
        projectEndTimeText = (TextView)findViewById(R.id.projectEndTimeText);
        projectFacilityText = (TextView)findViewById(R.id.projectFacilityText);
        projectOtherText = (TextView)findViewById(R.id.projectOtherText);
        projectIncomeText = (TextView)findViewById(R.id.projectIncomeText);
        projectLossText = (TextView)findViewById(R.id.projectLossText);
        chart = (BarChart) findViewById(R.id.chart);
    }

    private class getProjectDetail extends AsyncTask<String, Void, String> {
        private String error;
        String data = "";
        private ProgressDialog dialog = new ProgressDialog(ProjectDetailActivity.this);

        protected void onPreExecute(){
            dialog.setMessage("Please wait..");
            dialog.show();

        }

        protected void onPostExecute(String str){
            JSONObject object = null;
            try {
                object = new JSONObject(str);
                projectIdText.setText(object.getString("id").toString());
                projectNameText.setText(object.getString("name").toString());
                projectMemberText.setText(object.getString("member").toString());
                projectPriorityText.setText(object.getString("priority").toString());
                projectStartTimeText.setText(object.getString("startTime").toString());
                projectEndTimeText.setText(object.getString("endTime").toString());
                projectFacilityText.setText(object.getString("facility").toString());
                projectOtherText.setText(object.getString("other").toString());
                projectIncomeText.setText(object.getString("income").toString());
                projectLossText.setText(!object.getString("loss").toString().equals("null") ? object.getString("loss").toString() : "0");

                ArrayList<BarEntry> valsComp1 = new ArrayList<BarEntry>();

                Float facility = 0f,other = 0f, income = 0f,loss = 0f;
                String facilityStr = object.getString("facility");
                String otherStr = object.getString("other");
                String icomeStr =  object.getString("income");
                String lossStr = object.getString("loss");
                if(facilityStr != "null")
                    facility = Float.parseFloat(facilityStr);
                if(otherStr != "null")
                    other = Float.parseFloat(otherStr);
                if(icomeStr != "null")
                    income = Float.parseFloat(icomeStr);
                if(lossStr != "null")
                    loss = Float.parseFloat(lossStr);

                BarEntry c1e1 = new BarEntry(facility, 0); // 0 == quarter 1
                BarEntry c1e2 = new BarEntry(other, 1); // 0 == quarter 1
                BarEntry c1e3 = new BarEntry(income, 2); // 0 == quarter 1
                BarEntry c1e4 = new BarEntry(loss, 3); // 0 == quarter 1

                valsComp1.add(c1e1);
                valsComp1.add(c1e2);
                valsComp1.add(c1e3);
                valsComp1.add(c1e4);

                BarDataSet setComp1 = new BarDataSet(valsComp1, object.getString("name").toString()+"成本");

                ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
                dataSets.add(setComp1);

                ArrayList<String> xVals = new ArrayList<String>();
                xVals.add("固定成本"); xVals.add("其他成本"); xVals.add("預期收益"); xVals.add("預期損益");


                BarData data = new BarData(xVals, dataSets);
                chart.setData(data);
                chart.setDrawGridBackground(true);
                chart.setDescription("");
                chart.getLegend().setEnabled(false);
                chart.invalidate();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            dialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            BufferedReader reader = null;
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
                return result.toString();

            } catch (Exception ex) {
                error = ex.getMessage();
                Log.e("ProjectActivity Error",error);
            }

            return null;
        }

    }
}
