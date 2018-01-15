package com.ll.loadmore;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity {
    private LLSwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private List<String> list=new ArrayList<>();
    private RecyclerAdapter adapter;
    private int page = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        swipeRefreshLayout=findViewById(R.id.swipe);
        adapter=new RecyclerAdapter();
        recyclerView=findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setItemCount(20);
//        // 手动调用,通知系统去测量
        swipeRefreshLayout.measure(0, 0);
        swipeRefreshLayout.setRefreshing(true);

        initEvent();
        initData();
    }
    private void initEvent() {

        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });


        // 设置下拉加载更多
        swipeRefreshLayout.setOnLoadMoreListener(new LLSwipeRefreshLayout.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMoreData();
            }
        });
    }

    private void loadMoreData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                for (int i =(20*page); i < 20 *(page+1); i++) {
                    list.add("我是天才" + i + "号");
                }
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setLoading(false);
                Toast.makeText(RecyclerActivity.this, "加载了" + 20 + "条数据", Toast.LENGTH_SHORT).show();
            }
        }, 1000);
    }


    private void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 0;
                list.clear();
                for (int i = 0; i < 20; i++) {
                    list.add("我是天才" + i + "号");
                }
                adapter.notifyDataSetChanged();

                Toast.makeText(RecyclerActivity.this, "刷新了20条数据", Toast.LENGTH_SHORT).show();

                // 加载完数据设置为不刷新状态，将下拉进度收起来
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, 1000);
    }


    /**
     * 适配器
     */
    private class RecyclerAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(RecyclerActivity.this).inflate(R.layout.recycler_item,parent,false);

            return new RecyclerHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TextView textView=holder.itemView.findViewById(R.id.recycler_item_text);
            textView.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
