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
import com.example.as.dao.OutaccountDAO;
import com.example.as.model.Tb_outaccount;

public class Outaccountinfo extends Activity {
    public static final String FLAG = "id";
    ListView lvinfo;
    String strType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outaccountinfo);
        lvinfo = (ListView) findViewById(R.id.lvoutaccountinfo);
        ShowInfo();
        lvinfo.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String strInfo = String.valueOf(((TextView) view).getText());
                String strid = strInfo.substring(0, strInfo.indexOf(' '));
                Intent intent = new Intent(Outaccountinfo.this,
                        InfoManage.class);
                intent.putExtra(FLAG, new String[]{strid, strType});
                startActivity(intent);
            }
        });
    }

    private void ShowInfo() {
        String[] strInfos = null;
        ArrayAdapter<String> arrayAdapter = null;
        strType = "btnoutinfo";
        OutaccountDAO outaccountinfo = new OutaccountDAO(Outaccountinfo.this);
        List<Tb_outaccount> listoutinfos = outaccountinfo.getScrollData(0,
                (int) outaccountinfo.getCount());
        strInfos = new String[listoutinfos.size()];
        int i = 0;
        for (Tb_outaccount tb_outaccount : listoutinfos) {
            strInfos[i] = String.format("%1$-10s %2$-10s %3$-15s %4$15s",tb_outaccount.getid(),
                    tb_outaccount.getType(),tb_outaccount.getMoney(),tb_outaccount.getTime());
            i++;
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
