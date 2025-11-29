package com.example.auratune.Adapter;

import android.content.ContentUris;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auratune.Domain.Song;
import com.example.auratune.R;
import com.example.auratune.databinding.ViewholderFavoriteBinding;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private final List<Song> favoriteSongs;
    private final OnFavoriteClickListener listener;

    public interface OnFavoriteClickListener {
        void onFavoriteClick(int position);
    }

    public FavoriteAdapter(List<Song> favoriteSongs, OnFavoriteClickListener listener) {
        this.favoriteSongs = favoriteSongs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderFavoriteBinding binding = ViewholderFavoriteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new FavoriteViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavoriteViewHolder holder, int position) {
        Song song = favoriteSongs.get(position);
        holder.binding.textTitle.setText(song.title);
        holder.binding.textArtist.setText(song.artist);

        Uri albumArtUri = ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"),
                song.albumId
        );

        Glide.with(holder.binding.getRoot().getContext())
                .load(albumArtUri)
                .circleCrop()
                .placeholder(R.drawable.ic_music_note_24)
                .error(R.drawable.ic_music_note_24)
                .into(holder.binding.imageAlbumArt);
    }

    @Override
    public int getItemCount() {
        return favoriteSongs.size();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        final ViewholderFavoriteBinding binding;

        public FavoriteViewHolder(ViewholderFavoriteBinding binding, OnFavoriteClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int pos = getBindingAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            listener.onFavoriteClick(pos);
                        }
                    }
                }
            });
        }
    }
}
