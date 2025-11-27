package com.example.auratune.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.auratune.Domain.BannerModel;
import com.example.auratune.R;

import java.util.ArrayList;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewholder>  {
    private ArrayList<BannerModel> sliderItems;
    private ViewPager2 viewPager2;
    private Context context;
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };

    public BannerAdapter(ViewPager2 viewPager2, ArrayList<BannerModel> sliderItems) {
        this.viewPager2 = viewPager2;
        this.sliderItems = sliderItems;
    }

    @NonNull
    @Override
    public BannerAdapter.BannerViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();

        return new BannerViewholder(LayoutInflater.from(context).inflate(R.layout.item_banner,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BannerAdapter.BannerViewholder holder, int position) {
        holder.setImage(sliderItems.get(position));
        if(position==sliderItems.size()-2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public class BannerViewholder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public BannerViewholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageSlider);
        }
        void setImage(BannerModel bannerModel){
            Glide.with(context)
                    .load(bannerModel.getUrl())
                    .into(imageView);
        }
    }
}
