package com.promise.memo.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


import com.promise.memo.Adapter.RecordAdapter;
import com.promise.memo.DB.roomdb.AppDatabase;
import com.promise.memo.DB.roomdb.bean.RecordBean;
import com.promise.memo.R;

import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

public class RecordActivity extends FragmentActivity {

    private Button add;
    private RecyclerView listView;
    static AppDatabase mAppDatabase;
    private Spinner spMonth;
    private TextView tvIn, tvOut;
    private ArrayAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);     //设置标题栏不显示
        setContentView(R.layout.activity_record);
        //初始化数据库
        mAppDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "android_room_dev.db")
                .allowMainThreadQueries()
                .build();
        init();
    }

    private void init() {
        add = findViewById(R.id.add);
        tvIn = findViewById(R.id.in);
        tvOut = findViewById(R.id.out);
        listView = findViewById(R.id.listview);
        listView.setLayoutManager(new LinearLayoutManager(this));
        spMonth = findViewById(R.id.dayMonthSpinner);
        //将可选内容与ArrayAdapter连接起来
        adapter2 = ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_item);

        //设置下拉列表的风格
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter2 添加到spinner中
        spMonth.setAdapter(adapter2);

        //添加事件Spinner事件监听
        spMonth.setOnItemSelectedListener(new SpinnerXMLSelectedListener());

        //设置默认值
        spMonth.setVisibility(View.VISIBLE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordActivity.this, AddRecordActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    //使用XML形式操作
    class SpinnerXMLSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            if (arg2 == 0) {//查询全部
                RecordAdapter adapter = new RecordAdapter();
                //获取所有数据
                List<RecordBean> allRecord = mAppDatabase.recordDao().getAllRecord();
                //根据类型获取 收支总额
                int totalInMoney = mAppDatabase.recordDao().getTotalMoney("1");
                int totalOutMoney = mAppDatabase.recordDao().getTotalMoney("0");
                tvIn.setText(totalInMoney + "");
                tvOut.setText(totalOutMoney + "");
                adapter.setList(allRecord);
                listView.setAdapter(adapter);
            } else {//按月查询
                List<RecordBean> allRecord = mAppDatabase.recordDao().getAllRecord(arg2 + "");
                RecordAdapter adapter = new RecordAdapter();
                adapter.setList(allRecord);
                //根据类型和月份获取 收支总额
                int totalInMoney = mAppDatabase.recordDao().getTotalMoney("1", arg2 + "");
                int totalOutMoney = mAppDatabase.recordDao().getTotalMoney("0", arg2 + "");
                tvIn.setText(totalInMoney + "");
                tvOut.setText(totalOutMoney + "");
                listView.setAdapter(adapter);
            }

        }

        public void onNothingSelected(AdapterView<?> arg0) {

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        RecordAdapter adapter = new RecordAdapter();
        //获取所有数据
        List<RecordBean> allRecord = mAppDatabase.recordDao().getAllRecord();
        //根据类型获取 收支总额
        int totalInMoney = mAppDatabase.recordDao().getTotalMoney("1");
        int totalOutMoney = mAppDatabase.recordDao().getTotalMoney("0");
        tvIn.setText(totalInMoney + "");
        tvOut.setText(totalOutMoney + "");
        adapter.setList(allRecord);
        listView.setAdapter(adapter);
    }
}
