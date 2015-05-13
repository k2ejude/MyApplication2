package com.example.jude.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AnalysisCreate extends ActionBarActivity {

    EditText idText,nameText,startTimeText,endTimeText,facilityCostText,otherCostText,incomeText,remarkText;
    Spinner priorityText;
    private String[] priority = {"1","2","3","4","5"};
    private ArrayAdapter<String> priorityList;
    private Calendar calendar;
    private int sYear,sMonth,sDay,eYear,eMonth,eDay;

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
