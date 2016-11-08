package com.truemdhq.projectx.adapter;

/**
 * Created by Ravi on 29/07/15.
 */
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import com.truemdhq.projectx.R;
import com.truemdhq.projectx.activity.MainActivity;
import com.truemdhq.projectx.model.NavDrawerItem;
import io.paperdb.Paper;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);

        Typeface tf_l= Typeface.createFromAsset(context.getAssets(),"fonts/OpenSans-Regular.ttf");

        holder.title.setTypeface(tf_l);
        holder.title.setText(current.getTitle());
        //holder.nav_icon.setImageResource(current.getIcon());
        try {
            if(position==0){
                //holder.nav_icon.setImageResource(R.drawable.home);
                Glide.with(context)
                        .load(R.drawable.home)
                        .into(holder.nav_icon);
                if( Paper.book("nav").read("selected").toString().equalsIgnoreCase("0")){
                    holder.title.setTextColor(Color.parseColor("#E91E63"));
                    holder.nav_icon.setImageResource(R.drawable.home_pink_nav);
                }
            }
            else if(position==1){
                Glide.with(context)
                        .load(R.drawable.prescriptions)
                        .into(holder.nav_icon);
                if( Paper.book("nav").read("selected").toString().equalsIgnoreCase("1")){
                    holder.title.setTextColor(Color.parseColor("#E91E63"));
                }
            }
            else if(position==2){
                Glide.with(context)
                        .load(R.drawable.order3)
                        .into(holder.nav_icon);
                if( Paper.book("nav").read("selected").toString().equalsIgnoreCase("2")){
                    holder.title.setTextColor(Color.parseColor("#E91E63"));
                }
            }
            else if(position==3){
                Glide.with(context)
                        .load(R.drawable.chat3)
                        .into(holder.nav_icon);
                if( Paper.book("nav").read("selected").toString().equalsIgnoreCase("3")){
                    holder.title.setTextColor(Color.parseColor("#E91E63"));
                }
            }
            else if(position==4){
                Glide.with(context)
                        .load(R.drawable.birthday_card)
                        .into(holder.nav_icon);
                if( Paper.book("nav").read("selected").toString().equalsIgnoreCase("4")){
                    holder.title.setTextColor(Color.parseColor("#E91E63"));
                }
            }
            else if(position==5){
                Glide.with(context)
                        .load(R.drawable.lifesaver)
                        .into(holder.nav_icon);
                if( Paper.book("nav").read("selected").toString().equalsIgnoreCase("5")){
                    holder.title.setTextColor(Color.parseColor("#E91E63"));
                }
            }
            else if(position==6){
                Glide.with(context)
                        .load(R.drawable.newspaper)
                        .into(holder.nav_icon);
                if( Paper.book("nav").read("selected").toString().equalsIgnoreCase("6")){
                    holder.title.setTextColor(Color.parseColor("#E91E63"));
                }
            }
            else if(position==7){
                Glide.with(context)
                        .load(R.drawable.tos)
                        .into(holder.nav_icon);
                if( Paper.book("nav").read("selected").toString().equalsIgnoreCase("7")){
                    holder.title.setTextColor(Color.parseColor("#E91E63"));
                }
            }
            else if(position==8)
                Glide.with(context)
                        .load(R.drawable.logout)
                        .into(holder.nav_icon);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView nav_icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            nav_icon = (ImageView) itemView.findViewById(R.id.imageView_icon_nav);
        }
    }
}
