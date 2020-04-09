package com.example.menudemo.ui.task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menudemo.MainActivity;
import com.example.menudemo.R;
import com.example.menudemo.ui.home.LoginActivity;

public class MessionDetailsActivity extends AppCompatActivity {
    private TextView mclass=null;
    private TextView mname=null;
    private TextView maddress=null;
    private TextView mdetails=null;
    private TextView mdeadline=null;
    private TextView mpay=null;
    private Button mcommit=null;
    private TextView mid=null;
    private TextView mtime=null;
    private TextView mman=null;
    private TextView mstatus=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mession_details);
        mclass = findViewById(R.id.ac_messionclass);
        mname = findViewById(R.id.ac_messionnname);
        maddress = findViewById(R.id.ac_messionaddress);
        mdetails = findViewById(R.id.ac_messionndetails);
        mdeadline = findViewById(R.id.ac_messionndeadline);
        mpay = findViewById(R.id.ac_messionpay);
        mcommit = findViewById(R.id.bt_accept);
        mid = findViewById(R.id.ac_messionid);
        mtime = findViewById(R.id.ac_messionlaunchtime);
        mman = findViewById(R.id.ac_messionninitiator);
        mstatus = findViewById(R.id.ac_messionnstatus);
        mcommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MessionDetailsActivity.this,"接受成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MessionDetailsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }}