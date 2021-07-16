package com.example.as.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.as.R;
import com.example.as.dao.FlagDAO;
import com.example.as.dao.InaccountDAO;
import com.example.as.dao.OutaccountDAO;
import com.example.as.model.Tb_flag;
import com.example.as.model.Tb_inaccount;
import com.example.as.model.Tb_outaccount;

public class Showinfo extends Activity {
    public static final String FLAG = "id";
    Button btnOutP, btnInP, btnFamilyOutP, btnFamilyInP;
    String strType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showinfo);
        btnOutP = (Button) findViewById(R.id.btnOutP);
        btnInP = (Button) findViewById(R.id.btnInP);
        btnFamilyOutP = (Button) findViewById(R.id.btnFamilyOutP);
        btnFamilyInP = (Button) findViewById(R.id.btnFamilyInP);

        btnOutP.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ShowInfo(R.id.btnOutP);
            }
        });

        btnInP.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ShowInfo(R.id.btnInP);
            }
        });

        btnFamilyOutP.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ShowInfo(R.id.btnFamilyOutP);
            }
        });

        btnFamilyInP.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ShowInfo(R.id.btnFamilyInP);
            }
        });

    }

    private void ShowInfo(int intType) {
        Intent intent = null;
        switch (intType) {
            case R.id.btnOutP:
                strType = "OutP";
                intent = new Intent(Showinfo.this, TotalChart.class);
                intent.putExtra("passType", strType);
                startActivity(intent);
                break;
            case R.id.btnInP:
                strType = "InP";
                intent = new Intent(Showinfo.this, TotalChart.class);
                intent.putExtra("passType", strType);
                startActivity(intent);
                break;
            case R.id.btnFamilyOutP:
                strType = "FOutP";
                intent = new Intent(Showinfo.this, TotalChart.class);
                intent.putExtra("passType", strType);
                startActivity(intent);
                break;
            case R.id.btnFamilyInP:
                strType = "FInP";
                intent = new Intent(Showinfo.this, TotalChart.class);
                intent.putExtra("passType", strType);
                startActivity(intent);
                break;
        }
    }

}
