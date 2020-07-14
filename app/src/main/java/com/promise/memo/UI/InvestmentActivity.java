package com.promise.memo.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.promise.memo.R;

import javax.xml.transform.Templates;

public class InvestmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest);

        Button btnCancelSelect = (Button) findViewById(R.id.cancel_select);
        btnCancelSelect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(InvestmentActivity.this , MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        Button btnSelect = (Button) findViewById(R.id.investment_select);
        btnSelect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //获取投资金额
                EditText investmentNum = (EditText) findViewById(R.id.investment_num);

                String sss = investmentNum.getText().toString().trim();
                if (sss.isEmpty()) {
                    Toast.makeText(InvestmentActivity.this, "金额不能为空！", Toast.LENGTH_SHORT).show();
                }else {
                    double investnum = Double.parseDouble(investmentNum.getText().toString());
                    System.out.println(investnum);
                    //获取投资收益
                    double day_shouyiNum = investnum * 0.003;
                    double quarter_shouyiNum = investnum * 0.0135;
                    double year_shouyiNum = investnum * 0.0175;
                    System.out.println(day_shouyiNum);
                    System.out.println(quarter_shouyiNum);
                    System.out.println(year_shouyiNum);
                    String day_shoyinum = String.valueOf(day_shouyiNum);
                    String quarter_shoyinum = String.valueOf(quarter_shouyiNum);
                    String year_shoyinum = String.valueOf(year_shouyiNum);
                    //显示投资收益
                    TextView day_shouyi = (TextView)findViewById(R.id.day_shouyi);
                    TextView quarter_shouyi = (TextView)findViewById(R.id.quarter_shouyi);
                    TextView year_shouyi = (TextView)findViewById(R.id.year_shouyi);
                    day_shouyi.setText(day_shoyinum);
                    quarter_shouyi.setText(quarter_shoyinum);
                    year_shouyi.setText(year_shoyinum);
                }
            }
        });


    }
}
