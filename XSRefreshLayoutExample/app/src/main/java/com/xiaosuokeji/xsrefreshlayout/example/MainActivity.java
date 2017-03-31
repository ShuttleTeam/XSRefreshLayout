package com.xiaosuokeji.xsrefreshlayout.example;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.xiaosuokeji.framework.android.xsrefreshlayout.XSRefreshLayout;
import com.xiaosuokeji.framework.android.xsrefreshlayout.XSRefreshListener;

public class MainActivity extends AppCompatActivity implements XSRefreshListener {

    XSRefreshLayout layoutRefresh;

    ListView listTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        layoutRefresh = (XSRefreshLayout) findViewById(R.id.layout_xs_refresh);


        layoutRefresh.setXsRefreshListener(this);


        listTest = (ListView) findViewById(R.id.list_test);

        String strs[] = {"item1", "imte2", "item3", "item4", "imte5", "item6", "item7", "imte8", "item9", "item10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, strs);

        listTest.setAdapter(adapter);

    }


    @Override
    public void onRefresh() {
        new WaitThread().run();
    }

    @Override
    public void onLoadMore() {
        new WaitThread().run();
    }

    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {

            Toast.makeText(MainActivity.this, "刷新完成", Toast.LENGTH_SHORT).show();

            layoutRefresh.finishRefresh();
            layoutRefresh.finishLoadMore();
        }
    };

    class WaitThread implements Runnable {
        @Override
        public void run() {
            handler.sendEmptyMessageDelayed(1, 3000);
        }
    }
}
