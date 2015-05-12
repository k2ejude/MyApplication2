package com.example.jude.myapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class AnalysisCreate extends ActionBarActivity {

    EditText idText,nameText,startTimeText,endTimeText,facilityCostText,otherCostText,incomeText,remarkText;
    Spinner priorityText;
    private String[] priority = {"1","2","3","4","5"};
    private ArrayAdapter<String> priorityList;

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
    }
}
