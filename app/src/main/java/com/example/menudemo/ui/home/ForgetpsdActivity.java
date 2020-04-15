package com.example.menudemo.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.menudemo.R;
import com.example.menudemo.ui.utills.HttpUtillConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetpsdActivity extends AppCompatActivity {

    private Button bt_register, bt_ma;
    private EditText et_login_username, et_login_password, et_login_password2, et_ma;


    private String USERID;
    private String USERPSD;
    private String RE_USERPSD;
    private String yan, ma;
    private int userflag = 1, phoneflag = 1;

    private static Context context;
    public static final String PHONE = "^(((13|18)[0-9]{9})|(15[012356789][0-9]{8})|((147|170|171|173|175|176|177|178)[0-9]{8}))$";


    private String result;
    private String findpsdUrl = HttpUtillConnection.Ya_URL + "FindpsdUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_forgetpsd);

        context = getApplicationContext();//获取context

        bt_ma = (Button) findViewById(R.id.forgetpsd_button_getcode);
        init();
        Yan();
    }
    private void init() {
        et_login_username = (EditText) findViewById(R.id.forgetpsd_edittext_num);
        et_login_password = (EditText) findViewById(R.id.forgetpsd_edittext_psw);
        et_login_password2 = (EditText) findViewById(R.id.forgetpsd_edittext_psw);
        et_ma = (EditText) findViewById(R.id.forgetpsd_edittext_vrfcode);
    }

    //获取验证码
    private String Yan() {
        bt_ma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                Set<Integer> set = new HashSet<>();
                while (set.size() < 4) {
                    int randomInt = random.nextInt(10);
                    set.add(randomInt);
                }
                StringBuffer sb = new StringBuffer();
                for (Integer i : set) {
                    sb.append("" + i);
                }
                bt_ma.setText(sb.toString());
            }
        });
        ma = bt_ma.getText().toString().trim();
        return ma;
    }

    /**
     * 校验手机号码
     *
     * @param mobile
     * @return
     * @author Mr Ya
     */
    public static final boolean isMoblie(String mobile) {
        boolean flag = false;
        if (null != mobile && !mobile.trim().equals("") && mobile.trim().length() == 11) {
            Pattern pattern = Pattern.compile(PHONE);
            Matcher matcher = pattern.matcher(mobile.trim());
            flag = matcher.matches();
        }
        return flag;
    }
    /*点击按钮找回密码*/
    public void Createre(View view){

        USERID = et_login_username.getText().toString().trim();
        USERPSD = et_login_password.getText().toString().trim();
        RE_USERPSD = et_login_password2.getText().toString().trim();
        ma = bt_ma.getText().toString().trim();
        yan = et_ma.getText().toString().trim();
        /*判断是否输入*/
        if ((USERID.isEmpty()) || (USERPSD.isEmpty()) || (RE_USERPSD.isEmpty()) || (yan.isEmpty())) {
            userflag = 0;
            Toast.makeText(this, "注册信息不能为空，请完善信息", Toast.LENGTH_SHORT).show();

        } else {
            //判断手机号是否符合条件  11位等等
            if (USERID.length() != 11) {
                userflag = 0;
                Toast.makeText(this, "手机号必须为11位，请重新输入", Toast.LENGTH_SHORT).show();
            }
            if (USERID.length() == 11) {
                //不符合手机号的正则表达式
                if (!isMoblie(USERID)) {
                    userflag = 0;
                    Toast.makeText(this, "手机号格式不正确，请重新输入", Toast.LENGTH_SHORT).show();
                }
                //判断两次密码是否一致
                else if (!(USERPSD.equals(RE_USERPSD))) {
                    userflag = 0;
                    Toast.makeText(this, "两次输入密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                }
                //判断验证码是否正确
                else if (!(yan.equals(ma))) {
                    userflag = 0;
                    Toast.makeText(this, "验证码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                }

            }
        }
        /*条件满足*/
        if (userflag ==1){

            /*上传服务器存入数据库*/                    //查询一下，是否用户名重复


            final Map<String, String> params = new HashMap<String, String>();
            params.put("USERID",USERID);
            params.put("USERPSD",USERPSD);
            Log.i("000000000000000000","-----------");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //1、网络访问
                    try {
                        result =  HttpUtillConnection.getContextByHttp(findpsdUrl, params);
                        Log.i("000000000000000000",result.toString().trim());

                    }catch (Exception e){
                        e.printStackTrace();
                        result = "网络请求失败,请稍后再试。";
                    }

                    //2、UI线程显示服务器的响应结果
                    Message msg = new Message();
                    msg.what = 0x12;
                    Bundle data = new Bundle();
                    data.putString("result", result);
                    msg.setData(data);
                    handler.sendMessage(msg);
                }
                @SuppressLint("HandlerLeak")
                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 0x12) {
                            Bundle data = msg.getData();
                            String key = data.getString("result");//得到json返回的json
                            if (key.startsWith("\ufeff")) {
                                key = key.substring(1);
                            }
                            Toast.makeText(context, key, Toast.LENGTH_LONG).show();
                            try {
                                JSONObject json = new JSONObject(key);
                                String result = (String) json.get("result");
                                if ("error".equals(result)){//重名
                                    Toast.makeText(ForgetpsdActivity.this, "该手机号未注册！请注册", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    if ("success".equals(result)) {//创建成功

                                        Toast.makeText(ForgetpsdActivity.this, "重置密码成功！", Toast.LENGTH_SHORT).show();
                                        /*暂停1.5秒后跳转到登录界面*/
                                        final Intent localIntent = new Intent(context, LoginActivity.class);
                                        Timer timer = new Timer();
                                        TimerTask tast = new TimerTask() {
                                            @Override
                                            public void run() {
                                                startActivity(localIntent);
                                            }
                                        };
                                        timer.schedule(tast, 1500);
                                    }
                                    else if ("error".equals(result)){
                                        Toast.makeText(ForgetpsdActivity.this, "重置密码失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
            }).start();
        }


    }
    /*onResume
 从Aactivity跳到Bactivity。由于Bactivity的操作，影响了Aactivity的数据，所以再回到A的时候，需要A能够进行刷新操作。
 但是默认跳回去的时候，A界面还是上次那个。*/
    protected void onResume() {
        super.onResume();
    }

}
