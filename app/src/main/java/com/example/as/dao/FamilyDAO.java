package com.example.as.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.as.model.Tb_family;

import java.util.ArrayList;
import java.util.List;

public class FamilyDAO {
    private DBOpenHelper helper;
    private SQLiteDatabase db;

    public FamilyDAO(Context context) {
        helper = new DBOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    /**
     * 添加用户信息
     *
     * @param tb_family
     */
    public void add(Tb_family tb_family) {
        db.execSQL("insert into tb_family (account,password,call) values (?,?,?)",
                new Object[]{tb_family.getAccount(),tb_family.getPassword(),tb_family.getCall()});
    }

    /**
     * 修改密码
     *
     * @param tb_family
     */
    public void update(Tb_family tb_family) {
        db.execSQL("update tb_family set password = ? where account = ?",
                new Object[]{tb_family.getPassword(),tb_family.getAccount()});
    }

    /**
     * 修改成员
     *
     */
    public void updateUser(String account,String call) {
        db.execSQL("update tb_family set call = ? where account = ?",
                new Object[]{call,account});
    }

    /**
     * 查找用户信息
     *
     */
    public String find(String account) {
        Cursor cursor = db.rawQuery("select password from tb_family where account = ?", new String[]{account});
        if (cursor.moveToNext()) {
            String password = cursor.getString(cursor.getColumnIndex("password"));
            return password;
        }
        cursor.close();
        return null;
    }

    /**
     * 查找用户call
     *
     */
    public String findCall() {
        Cursor cursor = db.rawQuery("select call from tb_family",null);
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        cursor.close();
        return null;
    }

    /**
     * 设置用户call
     *
     */
    public void setCall() {
        db.execSQL("update tb_family set call = ?", new Object[]{"家庭管理员"});
    }

    /**
     * 设置用户call
     *
     */
    public void setCallUser() {
        db.execSQL("update tb_family set call = ?", new Object[]{"家庭成员"});
    }

    /**
     * 查找用户信息
     *
     */
    public List<Tb_family> findAll() {
        Cursor cursor = db.rawQuery("select * from tb_family",null);
        List<Tb_family> list = new ArrayList<Tb_family>();
        while (cursor.moveToNext()) {
            list.add(new Tb_family(cursor.getString(cursor.getColumnIndex("account")),
                            cursor.getString(cursor.getColumnIndex("call"))));
        }
        cursor.close();
        return list;
    }
}
