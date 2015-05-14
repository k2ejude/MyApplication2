package com.example.jude.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Spinner;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AnalysisCreate extends ActionBarActivity {
    private String serverUrl = "http://10.0.3.2:60576/PMS/api/AndroidApi/GetMembers";
    private String serverUrlPost = "http://10.0.3.2:60576/PMS/api/AndroidApi/Analysis/";
    private ArrayList<String> list = new ArrayList<String>();
    EditText idText,nameText,startTimeText,endTimeText,facilityCostText,otherCostText,incomeText,remarkText,memberAddName;
    Spinner priorityText;
    Button memberAdd;
    AutoCompleteTextView memberAddId;
    ExpandableListView memberList;
    private String[] priority = {"1","2","3","4","5"};
    private ArrayAdapter<String> priorityList;
    private Calendar calendar;
    private int sYear,sMonth,sDay,eYear,eMonth,eDay;
    private SimpleExpandableListAdapter memberListAdapter;
    private String[] group = {"人員資料"};
    private ArrayList<ArrayList<ArrayList<String>>> child = new ArrayList<ArrayList<ArrayList<String>>>();
    List<Map<String,String>> groupData = new ArrayList<Map<String,String>>();
    List<List<Map<String,String>>> childData = new ArrayList<List<Map<String,String>>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_create);

        getView();

        priorityList = new ArrayAdapter<String>(AnalysisCreate.this, android.R.layout.simple_spinner_item, priority);
        priorityList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priorityText.setAdapter(priorityList);

        GroupList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.memu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create) {
            String cId = idText.getText().toString();
            String cName = nameText.getText().toString();
            String cStartTime = startTimeText.getText().toString();
            String cEndTime = endTimeText.getText().toString();
            String cPriority = priorityText.getSelectedItem().toString();
            String cFacility = facilityCostText.getText().toString();
            String cOther = otherCostText.getText().toString();
            String cIncome = incomeText.getText().toString();
            String cRemark = remarkText.getText().toString();
            String cmembers = child.toString();
            serverUrlPost = serverUrlPost + cId + "?name="+cName+"&startTime="+cStartTime+"&endTime="+cEndTime+"&priority="+
                    cPriority+"&facility="+cFacility+"&other="+cOther+"&income="+cIncome+"&remark="+cRemark;
            if(!child.isEmpty())
            {
                for (int i = 0;i<child.get(0).size();i++){
                    serverUrlPost = serverUrlPost+"&memberId="+child.get(0).get(i).get(0).toString();
                }
            }

            Log.d("Test",serverUrlPost);
            new createAnalysis().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void GroupList(){
        groupData = new ArrayList<Map<String,String>>();
        childData = new ArrayList<List<Map<String,String>>>();
        for(int i=0; i<group.length; i++ ){
            Map<String,String> groupMap = new HashMap<String,String>();
            groupMap.put("Member", group[i]);
            groupData.add(groupMap);


            List<Map<String,String>> childlist = new ArrayList<Map<String,String>>();
            if(child.size() != 0){
                for(int j = 0; j < child.get(i).toArray().length ; j++){
                    Map<String,String> childMap = new HashMap<String,String>();
                    childMap.put("Id",child.get(i).get(j).get(0));
                    childMap.put("Name",child.get(i).get(j).get(1));
                    childlist.add(childMap);
                }
            }
            childData.add(childlist);
        }

        memberListAdapter = new SimpleExpandableListAdapter(AnalysisCreate.this,
                groupData, R.layout.membergroup,new String[]{"Member"},new int[]{R.id.memberGroup },
                childData, R.layout.memberlist, new String[]{"Id","Name"},new int[]{R.id.memberIdText,R.id.memberNameText }
        );
        memberListAdapter.notifyDataSetChanged();
        memberList.setAdapter(memberListAdapter);
    }

    private void getView(){
        idText = (EditText)findViewById(R.id.idText);
        nameText = (EditText)findViewById(R.id.nameText);
        startTimeText = (EditText)findViewById(R.id.startTimeText);
        endTimeText = (EditText)findViewById(R.id.endTimeText);
        facilityCostText = (EditText)findViewById(R.id.facilityCostText);
        otherCostText = (EditText)findViewById(R.id.otherCostText);
        incomeText = (EditText)findViewById(R.id.incomeText);
        remarkText = (EditText)findViewById(R.id.remarkText);
        priorityText = (Spinner)findViewById(R.id.priorityText);
        memberAdd = (Button)findViewById(R.id.memberAdd);
        memberList = (ExpandableListView)findViewById(R.id.memberList);
        memberList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, final int childPosition, long id) {
                Dialog dialog = new AlertDialog.Builder(AnalysisCreate.this).setTitle("刪除").setMessage("確定要刪除嗎?").
                        setPositiveButton("確定",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                child.get(0).remove(childPosition);
                                GroupList();

                            }
                        }).
                        setNegativeButton("取消",null).
                        show();
                return false;
            }
        });

        memberAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.memberadd, (ViewGroup) findViewById(R.id.memberadd));
                memberAddId = (AutoCompleteTextView)layout.findViewById(R.id.memberAddIdText);
                new getMembers().execute();

                memberAddName = (EditText)layout.findViewById(R.id.memberAddNameText);
                memberAddName.setCursorVisible(false);
                memberAddName.setFocusable(false);
                memberAddName.setFocusableInTouchMode(false);
                Dialog dialog = new AlertDialog.Builder(AnalysisCreate.this).setTitle("新增人員")
                        .setView(layout)
                        .setPositiveButton("確認",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                memberAddId = (AutoCompleteTextView)((AlertDialog)dialog).findViewById(R.id.memberAddIdText);
                                memberAddName = (EditText)((AlertDialog)dialog).findViewById(R.id.memberAddNameText);
                                ArrayList<String> data = new ArrayList<String>();
                                if(!memberAddId.getText().toString().isEmpty())
                                {
                                    data.add(memberAddId.getText().toString());
                                    data.add(memberAddName.getText().toString());
                                    if(child.size() == 0){
                                        child.add(new ArrayList<ArrayList<String>>());
                                        child.get(0).add(data);
                                    }
                                    else{
                                        Boolean check = false;//false表示ChildData內沒有資料
                                        for(int i = 0; i<childData.get(0).size();i++){
                                            String childCheck = childData.get(0).get(i).get("Id").toString();
                                            if(memberAddId.getText().toString().equals(childCheck))
                                                check = true;
                                        }
                                        if(!check)
                                            child.get(0).add(data);
                                    }

                                    GroupList();
                                }

                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });

        calendar = Calendar.getInstance();
        sYear = calendar.get(Calendar.YEAR);
        sMonth = calendar.get(Calendar.MONTH);
        sDay = calendar.get(Calendar.DAY_OF_MONTH);
        eYear = calendar.get(Calendar.YEAR);
        eMonth = calendar.get(Calendar.MONTH);
        eDay = calendar.get(Calendar.DAY_OF_MONTH);

        startTimeText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DatePickerDialog dialog = new DatePickerDialog(AnalysisCreate.this,sdatePicker,sYear,sMonth,sDay);
                    dialog.show();
                }
            }
        });
        startTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(AnalysisCreate.this,sdatePicker,sYear,sMonth,sDay);
                dialog.show();
            }
        });

        endTimeText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DatePickerDialog dialog = new DatePickerDialog(AnalysisCreate.this,edatePicker,eYear,eMonth,eDay);
                    dialog.show();
                }
            }
        });
        endTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(AnalysisCreate.this,edatePicker,eYear,eMonth,eDay);
                dialog.show();
            }
        });
    }


    DatePickerDialog.OnDateSetListener sdatePicker = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            sYear = year;
            sMonth = monthOfYear;
            sDay = dayOfMonth;
            startTimeText.setText(String.valueOf(year)+"/"+String.valueOf(monthOfYear + 1)+"/"+String.valueOf(dayOfMonth));
        }
    };


    DatePickerDialog.OnDateSetListener edatePicker = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            eYear = year;
            eMonth = monthOfYear;
            eDay = dayOfMonth;
            endTimeText.setText(String.valueOf(year)+"/"+String.valueOf(monthOfYear + 1)+"/"+String.valueOf(dayOfMonth));
        }
    };

    private class getMembers extends AsyncTask<String, Void, String> {

        private String error;
        String data = "";
        private ProgressDialog dialog = new ProgressDialog(AnalysisCreate.this);

        protected void onPreExecute(){
            dialog.setMessage("Please wait..");
            dialog.show();

        }

        protected void onPostExecute(String str){
            list = new ArrayList<String>();
            try {
                JSONArray object = new JSONArray(str);
                for(int i = 0; i < object.length(); i++){
                    String item = object.getJSONObject(i).getString("id")+ " " + object.getJSONObject(i).getString("name");
                    list.add(item);
                }
                ArrayAdapter listAdapter = new ArrayAdapter(AnalysisCreate.this,android.R.layout.simple_dropdown_item_1line, list);
                memberAddId.setAdapter(listAdapter);
                memberAddId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String[] str = parent.getItemAtPosition(position).toString().split(" ");
                        memberAddId.setText(str[0]);
                        memberAddName.setText(str[1]);
                    }
                });
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
                Log.e("ProjectActivity Error",error);
            }

            return null;
        }

    }

    private class createAnalysis extends AsyncTask<String, Void, String> {

        private String error;
        String data = "";
        private ProgressDialog dialog = new ProgressDialog(AnalysisCreate.this);

        protected void onPreExecute(){
            dialog.setMessage("Please wait..");
            dialog.show();

        }

        protected void onPostExecute(String str){
            dialog.dismiss();
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("id", str);
            intent.putExtras(bundle);
            intent.setClass(AnalysisCreate.this, AnalysisDetailActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(serverUrlPost);
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
                Log.e("ProjectActivity Error",error);
            }

            return null;
        }

    }

}
