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
import android.widget.Toast;

import com.example.menudemo.MainActivity;
import com.example.menudemo.R;

public class MessionModifyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
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
        setContentView(R.layout.activity_mession_modify);
        mclass=findViewById(R.id.md_messionclass);
        mname=findViewById(R.id.md_messionname);
        mdeadline=findViewById(R.id.md_messiondeadline);
        maddress=findViewById(R.id.md_messionaddress);
        mdeadline=findViewById(R.id.md_messiondeadline);
        mcommit=findViewById(R.id.md_modify);
        mcommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MessionModifyActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MessionModifyActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //String []arr={"跑腿","家政","寄养","辅导","陪护"};
        //创建ArrayAdapter对象
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.activity_publish,arr);

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
