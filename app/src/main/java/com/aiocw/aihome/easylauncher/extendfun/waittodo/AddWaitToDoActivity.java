package com.aiocw.aihome.easylauncher.extendfun.waittodo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aiocw.aihome.easylauncher.R;


public class AddWaitToDoActivity extends AppCompatActivity implements
        View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditText editText;
    private Button button;
    private TextView tv_limit_date;
    private TextView tv_limit_time;

    public String limit_date = "";
    public String limit_time = "";
    private String content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wait_to_do);
        editText  = findViewById(R.id.edit_text_wait_to_do);
        button = findViewById(R.id.btn_add);
//        时间日期控件获取
        tv_limit_date = findViewById(R.id.tv_limit_date);
        tv_limit_time = findViewById(R.id.tv_limit_time);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = editText.getText().toString();
                if (limit_date.equals("") || limit_time.equals("") || content.equals("")) {
                    Toast.makeText(AddWaitToDoActivity.this, "创建失败，请输入内容或时间或日期", Toast.LENGTH_SHORT).show();
                }else {
                    WaitToDoDB.insertWaitToDo(getBaseContext(), content, limit_date, limit_time);
//                    Intent intent = new Intent();
//                    intent.putExtra("content", content);
//                    intent.putExtra("limit_date", limit_date);
//                    intent.putExtra("limit_time", limit_time);
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });
        tv_limit_date.setOnClickListener(this);
        tv_limit_time.setOnClickListener(this);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        limit_date = String.format("%d-%d-%d", year, month+1, dayOfMonth);
        tv_limit_date.setText(limit_date);

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        limit_time = String.format("%d:%d:00", hourOfDay, minute);
        tv_limit_time.setText(limit_time);

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tv_limit_date){
            Calendar calendar=Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(this,this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
        if (v.getId()==R.id.tv_limit_time){
            Calendar calendar=Calendar.getInstance();
            TimePickerDialog dialog=new TimePickerDialog(this,this,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true);
            dialog.show();
        }
    }

}
