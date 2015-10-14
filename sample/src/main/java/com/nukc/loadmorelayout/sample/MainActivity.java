package com.nukc.loadmorelayout.sample;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nukc.loadmorelayout.LoadMoreLayout;


public class MainActivity extends AppCompatActivity {

    private LoadMoreLayout mLoadMoreLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        SampleAdapter sampleAdapter = new SampleAdapter(this);
        recyclerView.setAdapter(sampleAdapter);

        mLoadMoreLayout = (LoadMoreLayout) findViewById(R.id.loadMoreLayout);
        mLoadMoreLayout.setOnLoadMoreListener(new LoadMoreLayout.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mLoadMoreLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLoadMoreLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }


    static class SampleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


        private final LayoutInflater mInflater;

        public SampleAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 5;
        }

        static class ViewHolder extends RecyclerView.ViewHolder{
            View bg;
            ImageView image;

            public ViewHolder(View itemView) {
                super(itemView);
                bg = itemView.findViewById(R.id.bg);
                image = (ImageView) itemView.findViewById(R.id.image);
            }
        }
    }
}
