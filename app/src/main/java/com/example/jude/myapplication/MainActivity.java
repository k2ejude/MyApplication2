package com.example.jude.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


public class MainActivity extends ActionBarActivity {
    protected HorizontalBarChart mChart;
    private TableLayout table;

    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST  = 9000;

    String SENDER_ID = "413376427270";

    static final String TAG = "GCMDemo";

    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    Context context;
    String regid;

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

        context = getApplicationContext();

        if(checkPlayServices()){
            //If this check succeeds, proceed with normal processing.
            //Otherwise, prompt user to get valid Play Servies APK.
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);
            Log.i(TAG, regid);
            if(regid.isEmpty()){
                registerInBackground();
            }
        }else{
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
//        XAxis xAxis = mChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        YAxis yaxis = new YAxis();
//        yaxis.setAxisMaxValue(100);
//        yaxis.setValueFormatter(new PercentFormatter());

        new getMain().execute();

    }

    private boolean checkPlayServices(){
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS){
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }else{
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context){
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if(registrationId.isEmpty()){
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }

        return registrationId;
    }

    private static int getAppVersion(Context context){
        try{
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e){
            // should never happen
            throw new RuntimeException("Could not get package name: "+ e);
        }
    }

    private SharedPreferences getGCMPreferences(Context context){
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    private void registerInBackground(){
        new AsyncTask(){
            @Override
            protected String doInBackground(Object[] params) {
                String msg = "";
                try{
                    if(gcm == null){
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i(TAG, regid);
                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // Persist the registration ID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex){
                    msg = "Error: " + ex.getMessage();
                }
                return msg;
            }

            protected void onPostExecute(String msg){
                Log.i(TAG, msg);
            }
        }.execute(null,null,null);
    }

    private void sendRegistrationIdToBackend(){
        // Your implementation here.
    }

    private void storeRegistrationId(Context context, String regId){
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
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
