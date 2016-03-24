package com.kit.widget.dialog;
//package com.kit.widget;
//
//import java.util.Calendar;
//
//import com.hiaas.hibit.nemo.R;
//
//import android.app.Activity;
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.app.TimePickerDialog;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.TimePicker;
//
//public class AlertDialog extends Activity {
//    private Button dateBtn = null;
//    private Button timeBtn = null;
//    private EditText et=null;
//    private final static int DATE_DIALOG = 0;
//    private final static int TIME_DIALOG = 1;
//    private Calendar c = null;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//
//        et=(EditText)findViewById(R.id.et);
//        dateBtn = (Button) findViewById(R.id.dateBtn);
//        timeBtn = (Button) findViewById(R.id.timeBtn);
//        dateBtn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v) {
//                showDialog(DATE_DIALOG);
//            }
//        });
//        timeBtn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v) {
//                showDialog(TIME_DIALOG);
//            }
//        });
//
//    }
//
//    /**
//     * 创建日期及时间选择对话框
//     */
//    @Override
//    protected Dialog onCreateDialog(int id) {
//        Dialog dialog = null;
//        switch (id) {
//        case DATE_DIALOG:
//            c = Calendar.getInstance();
//            dialog = new DatePickerDialog(
//                this,
//                new DatePickerDialog.OnDateSetListener() {
//                    public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) {
//                        et.setText("您选择了：" + year + "年" + (month+1) + "月" + dayOfMonth + "日");
//                    }
//                }, 
//                c.get(Calendar.YEAR), // 传入年份
//                c.get(Calendar.MONTH), // 传入月份
//                c.get(Calendar.DAY_OF_MONTH) // 传入天数
//            );
//            break;
//        case TIME_DIALOG:
//            c=Calendar.getInstance();
//            dialog=new TimePickerDialog(
//                this, 
//                new TimePickerDialog.OnTimeSetListener(){
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        et.setText("您选择了："+hourOfDay+"时"+minute+"分");
//                    }
//                },
//                c.get(Calendar.HOUR_OF_DAY),
//                c.get(Calendar.MINUTE),
//                false
//            );
//            break;
//        }
//        return dialog;
//    }
//
//}