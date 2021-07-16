package com.example.as.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.as.model.Tb_inaccount;

public class InaccountDAO {
    private DBOpenHelper helper;
    private SQLiteDatabase db;

    public InaccountDAO(Context context) {
        helper = new DBOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    /**
     * 添加收入信息
     *
     * @param tb_inaccount
     */
    public void add(Tb_inaccount tb_inaccount) {
        db.execSQL(
                "insert into tb_inaccount (_id,money,time,type,mark) "
                        + "values (?,?,?,?,?)",
                new Object[]{tb_inaccount.getid(), tb_inaccount.getMoney(),
                        tb_inaccount.getTime(), tb_inaccount.getType(), tb_inaccount.getMark()});
    }

    /**
     * 更新收入信息
     *
     * @param tb_inaccount
     */
    public void update(Tb_inaccount tb_inaccount) {
        db.execSQL(
                "update tb_inaccount set money = ?,time = ?,type = ?,"
                        + "mark = ? where _id = ?",
                new Object[]{tb_inaccount.getMoney(), tb_inaccount.getTime(),
                        tb_inaccount.getType(), tb_inaccount.getMark(), tb_inaccount.getid()});
    }

    /**
     * 查找收入信息
     *
     * @param id
     * @return
     */
    public Tb_inaccount find(int id) {
        Cursor cursor = db
                .rawQuery(
                        "select _id,money,time,type,mark from tb_inaccount"
                                + " where _id = ?",
                        new String[]{String.valueOf(id)});
        if (cursor.moveToNext()) {
            return new Tb_inaccount(
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
     * 收入信息汇总
     *
     * @return
     */
    public Map<String, Float> getTotal() {
        Cursor cursor = db.rawQuery("select type,sum(money) "
                + "from tb_inaccount group by type", null);
        int count = 0;
        count = cursor.getCount();

        Map<String, Float> map = new HashMap<String, Float>();
        cursor.moveToFirst();
        for (int i = 0; i < count; i++) {
            map.put(cursor.getString(0), cursor.getFloat(1));
            System.out.println("收入：" + map.values());
            cursor.moveToNext();
        }
        cursor.close();
        return map;
    }

    /**
     * 刪除收入信息
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
            db.execSQL("delete from tb_inaccount where _id in (" + sb + ")",
                    (Object[]) ids);
        }
    }

    /**
     * 获取收入信息
     *
     * @param start 起始位置
     * @param count 每页显示数量
     * @return
     */
    public List<Tb_inaccount> getScrollData(int start, int count) {
        List<Tb_inaccount> tb_inaccount = new ArrayList<Tb_inaccount>();
        Cursor cursor = db.rawQuery("select * from tb_inaccount limit ?,?",
                new String[]{String.valueOf(start), String.valueOf(count)});
        while (cursor.moveToNext()) {
            tb_inaccount.add(new Tb_inaccount(cursor.getInt(cursor
                    .getColumnIndex("_id")), cursor.getDouble(cursor
                    .getColumnIndex("money")), cursor.getString(cursor
                    .getColumnIndex("time")), cursor.getString(cursor
                    .getColumnIndex("type")),  cursor.getString(cursor
                    .getColumnIndex("mark"))));
        }
        cursor.close();
        return tb_inaccount;
    }

    /**
     * 获取总记录数
     *
     * @return
     */
    public long getCount() {
        Cursor cursor = db
                .rawQuery("select count(_id) from tb_inaccount", null);
        if (cursor.moveToNext()) {
            return cursor.getLong(0);
        }
        cursor.close();
        return 0;
    }

    /**
     * 获取收入最大编号
     *
     * @return
     */
    public int getMaxId() {
        Cursor cursor = db.rawQuery("select max(_id) from tb_inaccount", null);
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
        Cursor cursor = db.rawQuery("select sum(money) from tb_inaccount", null);
        while (cursor.moveToLast()) {
            return cursor.getInt(0);
        }
        return 0;
    }
}
