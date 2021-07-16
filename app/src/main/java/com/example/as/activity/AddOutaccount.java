package com.example.as.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.as.R;
import com.example.as.dao.OutaccountDAO;
import com.example.as.model.Tb_outaccount;

public class AddOutaccount extends Activity {
    protected static final int DATE_DIALOG_ID = 0;
    EditText txtMoney, txtTime, txtAddress, txtMark;
    Spinner spType;
    Button btnSaveButton;
    Button btnCancelButton;
    private int mYear;
    private int mMonth;
    private int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addoutaccount);
        txtMoney = (EditText) findViewById(R.id.txtMoney);
        txtTime = (EditText) findViewById(R.id.txtTime);
        txtMark = (EditText) findViewById(R.id.txtMark);
        spType = (Spinner) findViewById(R.id.spType);
        btnSaveButton = (Button) findViewById(R.id.btnSave);
        btnCancelButton = (Button) findViewById(R.id.btnCancel);
        txtTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        btnSaveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String strMoney = txtMoney.getText().toString();
                if (!strMoney.isEmpty()) {
                    OutaccountDAO outaccountDAO = new OutaccountDAO(
                            AddOutaccount.this);
                    Tb_outaccount tb_outaccount = new Tb_outaccount(
                            outaccountDAO.getMaxId() + 1, Double
                            .parseDouble(strMoney), txtTime
                            .getText().toString(), spType
                            .getSelectedItem().toString(), txtMark
                            .getText().toString());
                    outaccountDAO.add(tb_outaccount);
                    txtMoney.setText("");
                    txtMoney.setHint("输入金额");
                    txtTime.setText("");
                    txtTime.setHint("选择日期");
                    txtMark.setText("");
                    spType.setSelection(0);
                    Toast.makeText(AddOutaccount.this, "新增支出添加成功！",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddOutaccount.this, "请输入支出金额！",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        updateDisplay();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
                        mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
        }
    };

    private void updateDisplay() {
        txtTime.setText(new StringBuilder().append(mYear).append("-")
                .append(mMonth + 1).append("-").append(mDay));
    }
}
