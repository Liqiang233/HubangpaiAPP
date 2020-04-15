package com.example.menudemo.ui.task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.menudemo.MainActivity;
import com.example.menudemo.R;

public class TasklistActivity extends AppCompatActivity {
    private Button mall=null;
    private Button ming=null;
    private Button mdrawback=null;
    private Button msee=null;
    private Button mwait=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasklist);

        //返回和标题
        TextView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mall=findViewById(R.id.m_all);
        ming=findViewById(R.id.m_ing);
        mdrawback=findViewById(R.id.m_drawback);
        msee=findViewById(R.id.m_see);
        mwait=findViewById(R.id.m_wait);

        msee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TasklistActivity.this, MessionModifyActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
