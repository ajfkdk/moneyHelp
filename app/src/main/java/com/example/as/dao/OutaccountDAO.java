package com.example.as.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.as.model.Tb_outaccount;

public class OutaccountDAO {
    private DBOpenHelper helper;
    private SQLiteDatabase db;

    public OutaccountDAO(Context context) {
        helper = new DBOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    /**
     * 添加支出信息
     *
     * @param tb_outaccount
     */
    public void add(Tb_outaccount tb_outaccount) {
        db.execSQL(
                "insert into tb_outaccount (_id,money,time,type,mark) values (?,?,?,?,?)",
                new Object[]{tb_outaccount.getid(), tb_outaccount.getMoney(),
                        tb_outaccount.getTime(), tb_outaccount.getType(), tb_outaccount.getMark()});
    }

    /**
     * 更新支出信息
     *
     * @param tb_outaccount
     */
    public void update(Tb_outaccount tb_outaccount) {
        db.execSQL(
                "update tb_outaccount set money = ?,time = ?,type = ?,mark = ? where _id = ?",
                new Object[]{tb_outaccount.getMoney(),
                        tb_outaccount.getTime(), tb_outaccount.getType(), tb_outaccount.getMark(),
                        tb_outaccount.getid()});
    }

    /**
     * 查找支出信息
     *
     * @param id
     * @return
     */
    public Tb_outaccount find(int id) {
        Cursor cursor = db
                .rawQuery(
                        "select _id,money,time,type,mark from tb_outaccount where _id = ?",
                        new String[]{String.valueOf(id)});
        if (cursor.moveToNext()) {
            return new Tb_outaccount(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getDouble(cursor.getColumnIndex("money")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("type")),
                    cursor.getString(cursor.getColumnIndex("mark")));
        }
        cursor.close();
        return null;
    }

    /**
     * 刪除支出信息
     *
     * @param ids
     */
    public void detele(Integer... ids) {
        if (ids.length > 0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < ids.length; i++) {
                sb.append('?').append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            db.execSQL("delete from tb_outaccount where _id in (" + sb + ")",
                    (Object[]) ids);
        }
    }

    /**
     * 支出信息汇总
     *
     * @return
     */
    public Map<String, Float> getTotal() {
        Cursor cursor = db.rawQuery("select type,sum(money) from tb_outaccount group by type", null);
        int count = 0;
        count = cursor.getCount();
        Map<String, Float> map = new HashMap<String, Float>();
        cursor.moveToFirst();
        for (int i = 0; i < count; i++) {
            map.put(cursor.getString(0), cursor.getFloat(1));
            System.out.println("支出：" + cursor.getString(0) + cursor.getFloat(1));
            cursor.moveToNext();
        }
        cursor.close();
        return map;
    }

    /**
     * 获取支出信息
     *
     * @param start 起始位置
     * @param count 每页显示数量
     * @return
     */
    public List<Tb_outaccount> getScrollData(int start, int count) {
        List<Tb_outaccount> tb_outaccount = new ArrayList<Tb_outaccount>();
        Cursor cursor = db.rawQuery("select * from tb_outaccount limit ?,?",
                new String[]{String.valueOf(start), String.valueOf(count)});
        while (cursor.moveToNext()) {
            tb_outaccount.add(new Tb_outaccount(cursor.getInt(cursor
                    .getColumnIndex("_id")), cursor.getDouble(cursor
                    .getColumnIndex("money")), cursor.getString(cursor
                    .getColumnIndex("time")), cursor.getString(cursor
                    .getColumnIndex("type")),  cursor.getString(cursor
                    .getColumnIndex("mark"))));
        }
        cursor.close();
        return tb_outaccount;
    }

    /**
     * 获取总记录数
     *
     * @return
     */
    public long getCount() {
        Cursor cursor = db.rawQuery("select count(_id) from tb_outaccount",
                null);
        if (cursor.moveToNext()) {
            return cursor.getLong(0);
        }
        cursor.close();
        return 0;
    }

    /**
     * 获取支出最大编号
     *
     * @return
     */
    public int getMaxId() {
        Cursor cursor = db.rawQuery("select max(_id) from tb_outaccount", null);
        while (cursor.moveToLast()) {
            return cursor.getInt(0);
        }
        cursor.close();
        return 0;
    }

    /**
     * 总钱数
     */
    public int getSumM(){
        Cursor cursor = db.rawQuery("select sum(money) from tb_outaccount", null);
        while (cursor.moveToLast()) {
            System.out.println(cursor.getInt(0));
            return cursor.getInt(0);
        }
        return 0;
    }
}
