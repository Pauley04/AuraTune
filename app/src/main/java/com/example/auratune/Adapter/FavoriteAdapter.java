package com.example.auratune.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auratune.Domain.FavoriteModel;
import com.example.auratune.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private Context context;
    private ArrayList<FavoriteModel> favoriteList = new ArrayList<>();

    public FavoriteAdapter(Context context) {
        this.context = context;
    }

    public void setFavoriteList(ArrayList<FavoriteModel> list) {
        this.favoriteList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoriteModel item = favoriteList.get(position);

        holder.textSongTitle.setText(item.getTitle());
        holder.textSongArtist.setText(item.getArtist());
        holder.textSongDuration.setText(item.getDuration());

        // Load image with Glide
        Glide.with(context)
                .load(item.getImageUrl())
                .placeholder(R.drawable.ic_music_note_24) // fallback image
                .into(holder.imageSongArt);
    }

    @Override
    public int getItemCount() {
        return favoriteList != null ? favoriteList.size() : 0;
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageSongArt;
        TextView textSongTitle, textSongArtist, textSongDuration;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageSongArt = itemView.findViewById(R.id.imageAlbumArt);
            textSongTitle = itemView.findViewById(R.id.textTitle);
            textSongArtist = itemView.findViewById(R.id.textArtist);
            textSongDuration = itemView.findViewById(R.id.textSongDuration);
        }
    }
}
