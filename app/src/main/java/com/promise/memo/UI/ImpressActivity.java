package com.promise.memo.UI;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.promise.memo.DB.UserDao;
import com.promise.memo.R;
import com.promise.memo.Util.EditTextClearTools;

public class ImpressActivity extends AppCompatActivity {
    private EditText userName,passWord,rePassword;
    private ImageView unameClear,pwdClear,repwdClear;
    private TextView userLogin;
    private Button register;
    private UserDao userdao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impressperinfo);
        userdao = new UserDao(this);
        init();
        ViewClick();
    }

    private void ViewClick() {

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin.setTextColor(Color.rgb(0, 0, 0));
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username=userName.getText().toString();
                final String password=passWord.getText().toString();
                final String repassword=rePassword.getText().toString();
                if(username.isEmpty()){
                    Toast.makeText(getApplicationContext(),"帐号不能为空",Toast.LENGTH_LONG).show();
                    return;
                }else if(password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_LONG).show();
                    return;
                }else if(!password.equals(repassword)){
                    Toast.makeText(getApplicationContext(),"两次密码输入不一致",Toast.LENGTH_LONG).show();
                    return;
                }

                Cursor cursor = userdao.query(username.trim(), password.trim());
                if (cursor.moveToNext()) {
                    Toast.makeText(getApplicationContext(),"该用户已被注册，请重新输入",Toast.LENGTH_LONG).show();
                    userName.requestFocus();
                }else{
                    userdao.insertUser(username,password);
                    Intent intent = new Intent();
                    intent.setClass(ImpressActivity.this,MainActivity.class);
                    intent.putExtra("login_user",username);

                    cursor.close();

                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    private void init() {
        userName = (EditText) findViewById(R.id.etv_userName);
        passWord = (EditText) findViewById(R.id.etv_password);
        rePassword = (EditText) findViewById(R.id.etv_repassword);
        unameClear = (ImageView) findViewById(R.id.ivv_unameClear);
        pwdClear = (ImageView) findViewById(R.id.ivv_pwdClear);
        repwdClear = (ImageView) findViewById(R.id.ivv_repwdClear);
        userLogin = (TextView) findViewById(R.id.livnk_signup);
        register= (Button) findViewById(R.id.btvn_login);
        EditTextClearTools.addClearListener(userName,unameClear);
        EditTextClearTools.addClearListener(passWord,pwdClear);
        EditTextClearTools.addClearListener(rePassword,repwdClear);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
    }
}
