package com.example.jude.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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
        LineChart chart = (LineChart)findViewById(R.id.chart);
        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();
        ArrayList<Entry> valsComp2 = new ArrayList<Entry>();
        Entry c1e1 = new Entry(100.000f, 0); // 0 == quarter 1
        valsComp1.add(c1e1);
        Entry c1e2 = new Entry(50.000f, 1); // 1 == quarter 2 ...
        valsComp1.add(c1e2);
        Entry c2e1 = new Entry(120.000f, 0); // 0 == quarter 1
        valsComp2.add(c2e1);
        Entry c2e2 = new Entry(110.000f, 1); // 1 == quarter 2 ...
        valsComp2.add(c2e2);
        LineDataSet setComp1 = new LineDataSet(valsComp1, "Company 1");
        LineDataSet setComp2 = new LineDataSet(valsComp2, "Company 2");
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(setComp1);
        dataSets.add(setComp2);

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("1.Q"); xVals.add("2.Q"); xVals.add("3.Q"); xVals.add("4.Q");


        LineData data = new LineData(xVals, dataSets);
        chart.setData(data);
        chart.setDescription("Test MPAndroidCharts");
        chart.setDrawGridBackground(true);
        chart.invalidate();
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
                Log.d("Test", projectDetail.getString("id").toString());
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
            } catch (Exception ex) {
                error = ex.getMessage();
                Log.d("ProjectActivity Error",error);
            }

//            Log.d("Test", Integer.toString(b));
            return null;
        }

    }
}
