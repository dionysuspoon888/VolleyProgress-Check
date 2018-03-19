package com.example.testapi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by D on 3/19/2018.
 */

public class Radapter extends RecyclerView.Adapter<Radapter.ViewHolder> {
    private Context ctx;
    ArrayList<JSONItem> lists;



    public Radapter(Context ctx, ArrayList<JSONItem> lists){
        this.ctx = ctx;
        this.lists=lists;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image_view;


        public ViewHolder(View itemView) {
            super(itemView);
            image_view = itemView.findViewById(R.id.iv_1);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JSONItem jsonItem= lists.get(position);
        String url = jsonItem.getImageUrl();
        Picasso.with(ctx).load(url).fit().centerInside().into(holder.image_view);



    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

}
