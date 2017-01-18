package com.estimote.AtwaterKentLocalization;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfessorTableActivity extends AppCompatActivity {


    private Button but2;

    private void init(){
        but2 = (Button)findViewById(R.id.but2);
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent(ProfessorTableActivity.this, MainActivity.class);
                startActivity(nextScreen);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_table);
        init();
    }
}
