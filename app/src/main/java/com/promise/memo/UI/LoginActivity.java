package com.promise.memo.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.promise.memo.qq.QQLogin;
import com.promise.memo.DB.UserDao;
import com.promise.memo.R;
import com.promise.memo.Util.EditTextClearTools;
import com.promise.memo.Util.PermissionsUtil;
import com.promise.memo.qq.QQLogin;

import de.hdodenhof.circleimageview.CircleImageView;


public class LoginActivity extends AppCompatActivity{

    private EditText userName,passWord;
    private ImageView unameClear,pwdClear;
    private TextView userReg;
    private CircleImageView iv_icon;
    private Button login,qqlogin;
    private UserDao userdao;
    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        QQLogin.INSTANCE.initQQ(LoginActivity.this);//初始化QQ第三方登录
        PermissionsUtil.checkAndRequestPermissions(this);
        init();//初始化组件
        loadSP();//读取SharedPreferences数据
        ViewClick();//注册组件点击事件
    }
    private void init(){
        userName = (EditText) findViewById(R.id.et_userName);
        passWord = (EditText) findViewById(R.id.et_password);
        unameClear = (ImageView) findViewById(R.id.iv_unameClear);
        pwdClear = (ImageView) findViewById(R.id.iv_pwdClear);
        iv_icon= (CircleImageView) findViewById(R.id.iv_icon);
        userReg = (TextView) findViewById(R.id.link_signup);
        login= (Button) findViewById(R.id.btn_login);
        qqlogin=(Button) findViewById(R.id.qq_login);
        EditTextClearTools.addClearListener(userName,unameClear);
        EditTextClearTools.addClearListener(passWord,pwdClear);

    }

    private void loadSP() {
        sp = getSharedPreferences("userInfo", 0);
        ed = sp.edit();
        boolean remember_Pw =sp.getBoolean("REMEMBER_PW", false);
        boolean auto_Login =sp.getBoolean("AUTO_LOGIN", false);
        String names=sp.getString("USER_NAME","");
        String passwords=sp.getString("PASSWORD", "");
        if(remember_Pw){
            userName.setText(names);
            passWord.setText(passwords);
            Bitmap bt = BitmapFactory.decodeFile("/sdcard/Memo/" + names + "head.jpg");// 从SD卡中找头像，转换成Bitmap
            if (bt != null) {
                @SuppressWarnings("deprecation")
                Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
                iv_icon.setImageDrawable(drawable);
            } else {
                //如果SD里面没有就使用默认头像
            }
        }
        Intent intent=getIntent();
        if(intent.getStringExtra("code")!=null){
            if(intent.getStringExtra("code").equals("relogin")){
                auto_Login=false;
            }
        }

        if(auto_Login){
            login();
        }
    }

    private void login() {
        userdao = new UserDao(LoginActivity.this);
        final String username=userName.getText().toString();
        final String password=passWord.getText().toString();

        if(username.isEmpty()){
            Toast.makeText(getApplicationContext(),"帐号不能为空",Toast.LENGTH_LONG).show();
            return;
        }else if(password.isEmpty()){
            Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_LONG).show();
            return;
        }

        Cursor cursor = userdao.query(username.trim(), password.trim());
        if (cursor.moveToNext()) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this,MainActivity.class);
            intent.putExtra("login_user",username);

            cursor.close();

            startActivity(intent);
            finish();
        }else{

            Toast.makeText(LoginActivity.this, "密码验证失败，请重新验证登录", Toast.LENGTH_SHORT).show();
        }

    }


//    private void qqlogin() {
//        userdao = new UserDao(LoginActivity.this);
//        final String username="124";
//        final String password="123";
//
//        Cursor cursor = userdao.query(username.trim(), password.trim());
//        if (cursor.moveToNext()) {
//            Intent intent = new Intent();
//            intent.setClass(LoginActivity.this,MainActivity.class);
//            intent.putExtra("login_user",username);
//
//            cursor.close();
//
//            startActivity(intent);
//            finish();
//        }else{
//            Toast.makeText(LoginActivity.this, "密码验证失败，请重新验证登录", Toast.LENGTH_SHORT).show();
//        }
//
//    }
    private void goReg() {
        userReg.setTextColor(Color.rgb(0, 0, 0));
        Intent intent = new Intent(getApplicationContext(), RegActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void ViewClick() {

        userReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goReg();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        qqlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QQLogin.INSTANCE.loginByQQ(LoginActivity.this);            }
        });

    }

@Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        QQLogin.INSTANCE.onActivityResultHandle(requestCode,resultCode,data,LoginActivity.this);
    }

//@Override
//    public void onClick(View view){
//        switch (view.getId()){
//            case R.id.qq_login:
//                QQLogin.INSTANCE.loginByQQ(LoginActivity.this);
//                userdao = new UserDao(LoginActivity.this);
//                final String username="。";
//                final String password="123";
//                Cursor cursor = userdao.query(username.trim(), password.trim());
//                if (cursor.moveToNext()) {
//                    Intent intent = new Intent();
//                    intent.setClass(LoginActivity.this,MainActivity.class);
//                    intent.putExtra("login_user",username);
//
//                    cursor.close();
//
//                    startActivity(intent);
//                    finish();
//                }
////                break;
//        }
//    }


    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            System.exit(0);
            return;
        }
        else { Toast.makeText(getBaseContext(), "再按一次返回退出程序", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
