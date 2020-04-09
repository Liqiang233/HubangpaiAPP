package com.example.menudemo.ui.task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menudemo.MainActivity;
import com.example.menudemo.R;

import java.lang.reflect.Array;

public class PublishActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Spinner mclass=null;
    private EditText mname=null;
    private EditText maddress=null;
    private EditText mdetail=null;
    private EditText mdeadline=null;
    private EditText mpay=null;
    private Button mcommit=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        mclass=findViewById(R.id.sp_messionclass);
        mname=findViewById(R.id.tv_messionname);
        mdeadline=findViewById(R.id.tv_messiondeadline);
        maddress=findViewById(R.id.tv_messionaddress);
        mdeadline=findViewById(R.id.tv_messiondeadline);
        mcommit=findViewById(R.id.bt_publish);

        mcommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PublishActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PublishActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

       // String []arr={"跑腿","家政","寄养","辅导","陪护"};
        //创建ArrayAdapter对象
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arr);

        //设置adapter
        //mclass.setAdapter(adapter);
        //设置选中事件监听器
        //mclass.setOnItemClickListener((AdapterView.OnItemClickListener) this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //获得选中的任务类
        String content = adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
