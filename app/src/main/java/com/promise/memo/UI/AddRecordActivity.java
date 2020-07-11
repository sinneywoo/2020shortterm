package com.promise.memo.UI;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.promise.memo.DB.roomdb.AppDatabase;
import com.promise.memo.DB.roomdb.bean.RecordBean;
import com.promise.memo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class AddRecordActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private RadioButton rbIn, rbOut;
    private String type = "1";//默认type1 收入
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12;
    private String typepinyin = "canying";
    private String typeName = "餐饮";
    private EditText etMoney;
    private TextView tvName;
    private ImageView ivName;
    static AppDatabase mAppDatabase;
    private EditText etContent;//备注

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);     //设置标题栏不显示
        setContentView(R.layout.activity_add_record);
        //初始化数据库
        mAppDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "android_room_dev.db")
                .allowMainThreadQueries()
                .build();
        initView();


    }

    public void initView() {
        tvName = findViewById(R.id.tv_type);
        etContent = findViewById(R.id.et_content);
        ivName = findViewById(R.id.iv_type);
        etMoney = findViewById(R.id.et_money);
        rbIn = findViewById(R.id.rv_in);
        rbOut = findViewById(R.id.rv_out);
        rbIn.setOnCheckedChangeListener(this);
        rbOut.setOnCheckedChangeListener(this);
        btn1 = findViewById(R.id.button);
        btn1.setOnClickListener(this);
        btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        btn3 = findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        btn4 = findViewById(R.id.button4);
        btn4.setOnClickListener(this);

        btn5 = findViewById(R.id.button5);
        btn5.setOnClickListener(this);


        btn6 = findViewById(R.id.button6);
        btn6.setOnClickListener(this);

        btn7 = findViewById(R.id.button7);
        btn7.setOnClickListener(this);

        btn8 = findViewById(R.id.button8);
        btn8.setOnClickListener(this);

        btn9 = findViewById(R.id.button9);
        btn9.setOnClickListener(this);

        btn10 = findViewById(R.id.button10);
        btn10.setOnClickListener(this);

        btn11 = findViewById(R.id.button11);
        btn11.setOnClickListener(this);

        btn12 = findViewById(R.id.button12);
        btn12.setOnClickListener(this);
        findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etMoney.getText().toString())) {
                    Toast.makeText(AddRecordActivity.this, "请输入金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                RecordBean recordBean = new RecordBean();
                recordBean.setCreateDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                recordBean.setMoney(Integer.parseInt(etMoney.getText().toString()));
                recordBean.setType(type);
                recordBean.setTypeName(typeName);
                recordBean.setMonth((Calendar.getInstance().get(Calendar.MONTH) + 1) + "");
                recordBean.setTypepin(typepinyin);
                recordBean.setContent(etContent.getText().toString());
                mAppDatabase.recordDao().insert(recordBean);
                finish();

            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            //收入支出按钮的点击事件
            switch (buttonView.getId()) {
                case R.id.rv_in://点击了收入
                    type = "1";
                    break;
                case R.id.rv_out://点击了支出
                    type = "0";
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                typepinyin = "canying";
                typeName = "餐饮";
                break;
            case R.id.button2:
                typepinyin = "chongwu";
                typeName = "宠物";

                break;
            case R.id.button3:
                typepinyin = "gouwu";
                typeName = "购物";

                break;
            case R.id.button4:
                typepinyin = "jiaotong";
                typeName = "交通";

                break;
            case R.id.button5:
                typepinyin = "lvxing";
                typeName = "旅行";

                break;
            case R.id.button6:
                typepinyin = "meirong";
                typeName = "美容";


                break;
            case R.id.button7:
                typepinyin = "shejiao";


                break;
            case R.id.button8:
                typepinyin = "tongxun";
                typeName = "通讯";


                break;
            case R.id.button9:

                typepinyin = "xuexi";
                typeName = "学习";

                break;
            case R.id.button10:
                typeName = "医疗";

                typepinyin = "yiliao";

                break;
            case R.id.button11:
                typepinyin = "yundong";
                typeName = "运动";


                break;
            case R.id.button12:

                typepinyin = "zufang";
                typeName = "租房";

                break;
        }
        tvName.setText(typeName);
        ivName.setImageResource(getImageResourceId(typepinyin));

    }

    /**
     * 通過名字获取 drawable里面的图片
     *
     * @param name
     * @return
     */
    public int getImageResourceId(String name) {
        R.drawable drawables = new R.drawable();
        //默认的id
        int resId = 0x7f02000b;
        try {
            //根据字符串字段名，取字段//根据资源的ID的变量名获得Field的对象,使用反射机制来实现的
            java.lang.reflect.Field field = R.drawable.class.getField(name);
            //取值
            resId = (Integer) field.get(drawables);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resId;
    }

}
