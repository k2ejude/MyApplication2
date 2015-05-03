package com.example.jude.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.URL;
import java.util.ArrayList;

public class ProjectDetailActivity extends ActionBarActivity {

    String id = "";
    private String serverUrl = "http://10.26.6.194:60576/PMS/api/AndroidApi/GetProjectDetail/";
    TextView projectIdText,projectNameText,projectMemberText,projectPriorityText,projectStartTimeText,projectEndTimeText,projectFacilityText,projectOtherText,projectIncomeText,projectLossText;
    BarChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitity_projectdetail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        getView();

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        serverUrl = serverUrl+id;

        new getProjectDetail().execute(serverUrl);

        ArrayList<BarEntry> valsComp1 = new ArrayList<BarEntry>();

        BarEntry c1e1 = new BarEntry(100.000f, 0); // 0 == quarter 1
        BarEntry c1e2 = new BarEntry(100.000f, 1); // 0 == quarter 1
        BarEntry c1e3 = new BarEntry(100.000f, 2); // 0 == quarter 1
        BarEntry c1e4 = new BarEntry(100.000f, 3); // 0 == quarter 1

        valsComp1.add(c1e1);
        valsComp1.add(c1e2);
        valsComp1.add(c1e3);
        valsComp1.add(c1e4);

        BarDataSet setComp1 = new BarDataSet(valsComp1, "成本");

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(setComp1);

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("固定成本"); xVals.add("其他成本"); xVals.add("預期收益"); xVals.add("預期損益");


        BarData data = new BarData(xVals, dataSets);
        chart.setData(data);
        chart.setDrawGridBackground(true);
        chart.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_project, menu);
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

    private class getProjectDetail extends AsyncTask<String, Void, Void> {
        private String error;
        String data = "";
        private ProgressDialog dialog = new ProgressDialog(ProjectDetailActivity.this);

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
                JSONObject projectDetail = new JSONObject(object.getString("Message"));
                projectIdText.setText(projectDetail.getString("id").toString());
                projectNameText.setText(projectDetail.getString("name").toString());
                projectMemberText.setText(projectDetail.getString("member").toString());
                projectPriorityText.setText(projectDetail.getString("priority").toString());
                projectStartTimeText.setText(projectDetail.getString("startTime").toString());
                projectEndTimeText.setText(projectDetail.getString("endTime").toString());
                projectFacilityText.setText(projectDetail.getString("facility").toString());
                projectOtherText.setText(projectDetail.getString("other").toString());
                projectIncomeText.setText(projectDetail.getString("income").toString());
                projectLossText.setText(projectDetail.getString("loss").toString());

                ArrayList<BarEntry> valsComp1 = new ArrayList<BarEntry>();

                BarEntry c1e1 = new BarEntry(Float.parseFloat(projectDetail.getString("facility")), 0); // 0 == quarter 1
                BarEntry c1e2 = new BarEntry(Float.parseFloat(projectDetail.getString("other")), 1); // 0 == quarter 1
                BarEntry c1e3 = new BarEntry(Float.parseFloat(projectDetail.getString("income")), 2); // 0 == quarter 1
                BarEntry c1e4 = new BarEntry(Float.parseFloat(projectDetail.getString("loss")), 3); // 0 == quarter 1

                valsComp1.add(c1e1);
                valsComp1.add(c1e2);
                valsComp1.add(c1e3);
                valsComp1.add(c1e4);

                BarDataSet setComp1 = new BarDataSet(valsComp1, "成本");

                ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
                dataSets.add(setComp1);

                ArrayList<String> xVals = new ArrayList<String>();
                xVals.add("固定成本"); xVals.add("其他成本"); xVals.add("預期收益"); xVals.add("預期損益");


                BarData data = new BarData(xVals, dataSets);
                chart.setData(data);
                chart.setDrawGridBackground(true);
                chart.invalidate();
            } catch (Exception ex) {
                error = ex.getMessage();
                Log.d("ProjectActivity Error",error);
            }

            return null;
        }

    }
}
