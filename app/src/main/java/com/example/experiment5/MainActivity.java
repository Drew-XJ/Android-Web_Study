package com.example.experiment5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.experiment5.Post_study.PostActivity;
import com.example.experiment5.Web_study.WebAcivity;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Button web_start = (Button) findViewById(R.id.web_learn);
        Button post_start = (Button) findViewById(R.id.post_learn);


        web_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,WebAcivity.class);
                startActivity(intent);
            }
        });

        post_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,PostActivity.class);
                startActivity(intent);
            }
        });

    }

}
