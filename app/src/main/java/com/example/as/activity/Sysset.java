package com.example.as.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.as.R;
import com.example.as.dao.FamilyDAO;
import com.example.as.model.Tb_family;

public class Sysset extends Activity {
    EditText txtpwd,txtAccount;
    Button btnSet, btnsetCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sysset);
        txtpwd = (EditText) findViewById(R.id.txtPwd);
        txtAccount = findViewById(R.id.txtAccount);
        btnSet = (Button) findViewById(R.id.btnSet);
        btnsetCancel = (Button) findViewById(R.id.btnsetCancel);
        btnSet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                FamilyDAO familyDAO = new FamilyDAO(Sysset.this);
                Tb_family tb_family = new Tb_family();
                tb_family.setPassword(txtpwd.getText().toString());
                tb_family.setAccount(txtAccount.getText().toString());
                familyDAO.update(tb_family);
                Toast.makeText(Sysset.this, "修改成功,下次生效！", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        btnsetCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }
}
