package com.example.as.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.as.R;
import com.example.as.dao.InaccountDAO;
import com.example.as.model.Tb_inaccount;

public class Inaccountinfo extends Activity {
    public static final String FLAG = "id";
    ListView lvinfo;
    String strType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inaccountinfo);
        lvinfo = (ListView) findViewById(R.id.lvinaccountinfo);
        ShowInfo();
        lvinfo.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String strInfo = String.valueOf(((TextView) view).getText());
                String strid = strInfo.substring(0, strInfo.indexOf(' '));
                Intent intent = new Intent(Inaccountinfo.this, InfoManage.class);
                intent.putExtra(FLAG, new String[]{strid, strType});
                startActivity(intent);
            }
        });
    }

    private void ShowInfo() {
        String[] strInfos = null;
        ArrayAdapter<String> arrayAdapter = null;
        strType = "btnininfo";
        InaccountDAO inaccountinfo = new InaccountDAO(Inaccountinfo.this);
        List<Tb_inaccount> listinfos = inaccountinfo.getScrollData(0,
                (int) inaccountinfo.getCount());
        strInfos = new String[listinfos.size()];
        int m = 0;
        for (Tb_inaccount tb_inaccount : listinfos) {
            strInfos[m] = String.format("%1$-10s %2$-10s %3$-15s %4$15s",tb_inaccount.getid(),
                    tb_inaccount.getType(),tb_inaccount.getMoney(),tb_inaccount.getTime());
            m++;
        }
        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, strInfos);
        lvinfo.setAdapter(arrayAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ShowInfo();
    }
}
