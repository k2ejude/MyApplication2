package com.example.jude.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;


public class AnalysisCreate extends ActionBarActivity {

    EditText idText,nameText,startTimeText,endTimeText,facilityCostText,otherCostText,incomeText,remarkText;
    Spinner priorityText;
    private String[] priority = {"1","2","3","4","5"};
    private ArrayAdapter<String> priorityList;
    private Calendar calendar;
    private int sYear,sMonth,sDay,eYear,eMonth,eDay;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_create);

        getView();

        priorityList = new ArrayAdapter<String>(AnalysisCreate.this, android.R.layout.simple_spinner_item, priority);
        priorityList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priorityText.setAdapter(priorityList);
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
                    showDialog(0);
                    datePickerDialog.updateDate(sYear,sMonth,sDay);
                }
            }
        });
        startTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
                datePickerDialog.updateDate(sYear,sMonth,sDay);
            }
        });

        endTimeText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showDialog(0);
                datePickerDialog.updateDate(eYear,eMonth,eDay);
            }
        });
        endTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
                datePickerDialog.updateDate(eYear,eMonth,eDay);
            }
        });
    }

    private int iYear,iMonth,iDay;
    protected Dialog onCreateDialog(int id){
        datePickerDialog = new DatePickerDialog(AnalysisCreate.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                iYear = year;
                iMonth = monthOfYear;
                iDay = dayOfMonth;
                startTimeText.setText(String.valueOf(year)+"/"+String.valueOf(monthOfYear)+"/"+String.valueOf(dayOfMonth));
            }
        },iYear,iMonth,iDay);

        return datePickerDialog;
    }
}
