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

public class Login extends Activity {
    EditText txtlogin, accountLogin;
    Button btnlogin, btnclose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        txtlogin = (EditText) findViewById(R.id.txtLogin);
        accountLogin = (EditText) findViewById(R.id.accountLogin);
        btnlogin = (Button) findViewById(R.id.btnLogin);
        btnclose = (Button) findViewById(R.id.btnClose);
        btnlogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                FamilyDAO familyDAO = new FamilyDAO(Login.this);
                Tb_family tb_family = new Tb_family();
                tb_family.setAccount(accountLogin.getText().toString());
                tb_family.setPassword(txtlogin.getText().toString());
                if (familyDAO.find(accountLogin.getText().toString())==null) {
                    familyDAO.add(tb_family);
                    startActivity(intent);
                } else {
                    if (familyDAO.find(accountLogin.getText().toString()).equals(txtlogin.getText().toString())) {
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, "请输入正确的密码！",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                txtlogin.setText("");
            }
        });

        btnclose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }
}
