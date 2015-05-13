package com.example.jude.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class AnalysisCreate extends ActionBarActivity {

    EditText idText,nameText,startTimeText,endTimeText,facilityCostText,otherCostText,incomeText,remarkText;
    Spinner priorityText;
    Button memberAdd;
    ExpandableListView memberList;
    private String[] priority = {"1","2","3","4","5"};
    private ArrayAdapter<String> priorityList;
    private Calendar calendar;
    private int sYear,sMonth,sDay,eYear,eMonth,eDay;
    private SimpleExpandableListAdapter memberListAdapter;
    private String[] group = {"人員資料"};
    private String[][][] child = {
            {{"1","張三"},{"2","李四"},{"3","小五"},{"4","王六"},{"5","七爺"},{"6","八爺"}} //人員資料
    };
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

        for(int i=0; i<group.length; i++ ){
            Map<String,String> groupMap = new HashMap<String,String>();
            groupData.add(groupMap);
            groupMap.put("Member", group[i]);


            List<Map<String,String>> childlist = new ArrayList<Map<String,String>>();
            for(int j = 0; j < child[i].length ; j++){
                Map<String,String> childMap = new HashMap<String,String>();
                childMap.put("Id",child[i][j][0]);
                childMap.put("Name",child[i][j][1]);
                childlist.add(childMap);
            }
            childData.add(childlist);
        }
        memberListAdapter = new SimpleExpandableListAdapter(AnalysisCreate.this,
                groupData, R.layout.membergroup,new String[]{"Member"},new int[]{R.id.memberGroup },
                childData, R.layout.memberlist, new String[]{"Id","Name"},new int[]{R.id.memberIdText,R.id.memberNameText }
        );

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

        memberAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoCompleteTextView memberAddId;
                EditText memberAddName;
                String[] COUNTRIES = new String[] {
                        "Belgium", "France", "Italy", "Germany", "Spain"
                };
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AnalysisCreate.this, android.R.layout.simple_list_item_1,COUNTRIES);
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.memberadd, (ViewGroup) findViewById(R.id.memberadd));
                memberAddId = (AutoCompleteTextView)layout.findViewById(R.id.memberAddIdText);
                memberAddName = (EditText)layout.findViewById(R.id.memberAddNameText);
                memberAddId.setAdapter(adapter);
                memberAddName.setCursorVisible(false);
                memberAddName.setFocusable(false);
                memberAddName.setFocusableInTouchMode(false);
                Dialog dialog = new AlertDialog.Builder(AnalysisCreate.this).setTitle("新增人員")
                        .setView(layout)
                        .setPositiveButton("確認",null)
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

}
