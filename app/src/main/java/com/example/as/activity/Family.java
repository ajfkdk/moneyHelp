package com.example.as.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.as.R;
import com.example.as.dao.FamilyDAO;
import com.example.as.dao.FlagDAO;
import com.example.as.model.Tb_family;
import com.example.as.model.Tb_flag;

import java.io.*;
import java.util.List;

public class Family extends Activity {
    ListView txtFlag;
    TextView tvFlag;
    Button btnflagSaveButton;
    Button btnflagCancelButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family);
        FlagDAO flagDAO = new FlagDAO(Family.this);
        final FamilyDAO familyDAO = new FamilyDAO(Family.this);
        btnflagSaveButton = (Button) findViewById(R.id.btnflagSave);
        btnflagCancelButton = (Button) findViewById(R.id.btnflagCancel);
        txtFlag = findViewById(R.id.txtFlag);
        tvFlag = findViewById(R.id.tvFlag);
        if (flagDAO.getCount() == 0) {
            txtFlag.setVisibility(View.GONE);
            tvFlag.setVisibility(View.GONE);
            btnflagSaveButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    tanC();
                }
            });

            btnflagCancelButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    tanC1();
                }
            });

        } else if (flagDAO.getCount() != 0) {
            if (familyDAO.findCall().equals("家庭管理")) {
                btnflagCancelButton.setText("分享家庭");
                btnflagSaveButton.setText("添加成员");
                nameSet();
                showInfo();
                btnflagSaveButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        tanC2();
                    }
                });
                btnflagCancelButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        save(familyDAO.findAll().toString());
                    }
                });
                tvFlag.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tanCName();
                    }
                });
                txtFlag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        if (id != 0){
                            String strInfo = String.valueOf(((TextView) view).getText());
                            String strid = strInfo.replaceAll("\\s+"," ").trim();
                            String[] name = strid.split(" ");
                            tanCUser(name[1]);
                        }
                    }
                });
            } else {
                btnflagCancelButton.setText("更新家庭信息");
                btnflagSaveButton.setVisibility(View.GONE);
                nameSet();
                showInfo();
                btnflagCancelButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        System.out.println(read());
                    }
                });
            }
        }
    }

    /**
     * 家庭名称设置
     */
    public void nameSet() {
        FlagDAO flagDAO = new FlagDAO(Family.this);
        tvFlag = findViewById(R.id.tvFlag);
        tvFlag.setText(flagDAO.findName());
    }

    /**
     * 展示成员信息
     */
    private void showInfo() {
        String[] strInfos = null;
        ArrayAdapter<String> arrayAdapter = null;
        txtFlag = findViewById(R.id.txtFlag);
        FamilyDAO familyDAO = new FamilyDAO(Family.this);
        List<Tb_family> all = familyDAO.findAll();
        strInfos = new String[all.size()];
        System.out.println(all);
        int m = 0;
        if (!all.isEmpty()) {
            for (Tb_family tb_family : all) {
                System.out.println((m + 1));
                System.out.println(tb_family.getAccount());
                System.out.println(tb_family.getCall());
                String s = Integer.valueOf(m + 1).toString();
                strInfos[m] = String.format("%1$-15s %2$-15s %3$15s", s, tb_family.getAccount(),
                        tb_family.getCall());
                m++;
            }
            arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, strInfos);
            txtFlag.setAdapter(arrayAdapter);
        }
    }

    /**
     * 弹窗添加
     */
    public void tanC() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Family.this);
        builder.setTitle("创建家庭");
        View view = LayoutInflater.from(Family.this).inflate(R.layout.dialog, null);
        builder.setView(view);
        final EditText username = (EditText) view.findViewById(R.id.username);
        builder.setPositiveButton("创建", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String a = username.getText().toString().trim();
                Tb_flag tb_flag = new Tb_flag(1, a);
                FlagDAO flagDAO = new FlagDAO(Family.this);
                flagDAO.add(tb_flag);
                FamilyDAO familyDAO = new FamilyDAO(Family.this);
                familyDAO.setCall();
                finish();
                Intent intent = new Intent(Family.this, Family.class);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * 弹窗添加
     */
    public void tanCName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Family.this);
        builder.setTitle("修改家庭名称");
        View view = LayoutInflater.from(Family.this).inflate(R.layout.dialog, null);
        builder.setView(view);
        final EditText username = (EditText) view.findViewById(R.id.username);
        builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String a = username.getText().toString().trim();
                Tb_flag tb_flag = new Tb_flag(1, a);
                FlagDAO flagDAO = new FlagDAO(Family.this);
                flagDAO.update(tb_flag);
                finish();
                Intent intent = new Intent(Family.this, Family.class);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * 弹窗添加
     */
    public void tanCUser(final String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Family.this);
        builder.setTitle("修改成员信息");
        View view = LayoutInflater.from(Family.this).inflate(R.layout.dialogone, null);
        builder.setView(view);
        final EditText account = (EditText) view.findViewById(R.id.account);
        final EditText call = (EditText) view.findViewById(R.id.call);
        account.setText(name);
        account.setFocusable(false);
        account.setFocusableInTouchMode(false);
        builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String b = call.getText().toString().trim();
                FamilyDAO familyDAO = new FamilyDAO(Family.this);
                familyDAO.updateUser(name,b);
                finish();
                Intent intent = new Intent(Family.this, Family.class);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    /**
     * 弹窗添加
     */
    public void tanC1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Family.this);
        builder.setTitle("家庭名称");
        View view = LayoutInflater.from(Family.this).inflate(R.layout.dialog, null);
        builder.setView(view);
        final EditText username = (EditText) view.findViewById(R.id.username);
        builder.setPositiveButton("加入", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String a = username.getText().toString().trim();
                Tb_flag tb_flag = new Tb_flag(1, a);
                FlagDAO flagDAO = new FlagDAO(Family.this);
                flagDAO.add(tb_flag);
                FamilyDAO familyDAO = new FamilyDAO(Family.this);
                familyDAO.setCallUser();
                finish();
                Intent intent = new Intent(Family.this, Family.class);
                startActivity(intent);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * 弹窗添加
     */
    public void tanC2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Family.this);
        builder.setTitle("添加成员");
        View view = LayoutInflater.from(Family.this).inflate(R.layout.dialogone, null);
        builder.setView(view);
        final EditText account = (EditText) view.findViewById(R.id.account);
        final EditText call = (EditText) view.findViewById(R.id.call);
        builder.setPositiveButton("加入", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String a = account.getText().toString().trim();
                String b = call.getText().toString().trim();
                FamilyDAO familyDAO = new FamilyDAO(Family.this);
                Tb_family tb_family = new Tb_family();
                tb_family.setAccount(a);
                tb_family.setCall(b);
                familyDAO.add(tb_family);
                showInfo();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * 数据存贮
     */
    public void save(String content) {
        FileOutputStream fileOutputStream = null;
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            String[] SdCardPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, SdCardPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, SdCardPermission, 100);
            }
            File dir = new File(Environment.getExternalStorageDirectory().
                    getAbsoluteFile() + File.separator);
            File file = new File(dir, "data.txt");
            System.out.println(file);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(content.getBytes());
            } catch (IOException e) {

                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                        Toast.makeText(Family.this, "家庭信息保存至根目录下data" +
                                        ".txt！",
                                Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 数据读取
     */
    public String read() {
        FileInputStream fileInputStream = null;
        File file = new File(Environment.getExternalStorageDirectory().
                getAbsoluteFile() + File.separator, "data.txt");
        String[] SdCardPermission = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(this, SdCardPermission[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, SdCardPermission, 100);
        }
        try {
            fileInputStream = new FileInputStream(file);
            byte[] buff = new byte[1024];
            StringBuilder sb = new StringBuilder("");
            int len = 0;
            if ((len = fileInputStream.read(buff)) > 0) {
                sb.append(new String(buff, 0, len));
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}


