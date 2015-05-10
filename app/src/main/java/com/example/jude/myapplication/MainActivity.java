package com.example.jude.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private ImageView project,analysis;
    private TextView projextText, analysisText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        project = (ImageView)findViewById(R.id.project);
        projextText = (TextView)findViewById(R.id.projectText);
        analysis = (ImageView)findViewById(R.id.analysis);
        analysisText = (TextView)findViewById(R.id.analysisText);

        projextText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ProjectActivity.class);
                startActivity(intent);
            }
        });

        project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ProjectActivity.class);
                startActivity(intent);
            }
        });

        analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AnalysisActivity.class);
                startActivity(intent);
            }
        });

        analysisText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AnalysisActivity.class);
                startActivity(intent);
            }
        });

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
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, ProjectActivity.class);
            startActivity(intent);
            return true;
        }
        else if( id == R.id.action_settings2){
            Log.d("Test","analysis");
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
