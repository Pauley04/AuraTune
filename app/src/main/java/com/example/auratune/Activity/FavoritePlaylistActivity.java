package com.example.auratune.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.auratune.Adapter.FavoriteAdapter;
import com.example.auratune.Domain.Song;
import com.example.auratune.R;
import com.example.auratune.Repository.FavoriteManager;
import com.example.auratune.databinding.ActivityFavoritePlaylistBinding;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

public class FavoritePlaylistActivity extends AppCompatActivity implements FavoriteAdapter.OnFavoriteClickListener {

    private ActivityFavoritePlaylistBinding binding;
    private FavoriteAdapter adapter;
    private final List<Song> favoriteSongs = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityFavoritePlaylistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerFavorites.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavoriteAdapter(favoriteSongs, this);
        binding.recyclerFavorites.setAdapter(adapter);

        loadFavorites();
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites();
    }
    private void loadFavorites() {
        favoriteSongs.clear();
        favoriteSongs.addAll(FavoriteManager.getFavorites());
        adapter.notifyDataSetChanged();
        binding.textEmpty.setVisibility(favoriteSongs.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onFavoriteClick(int position) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putParcelableArrayListExtra("songList", new ArrayList<>(favoriteSongs));
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private void setupBottomNavigation() {
        ChipNavigationBar navigationBar = binding.bottomNavigation;
        navigationBar.setItemSelected(R.id.favorites, true);
        navigationBar.setOnItemSelectedListener(id -> {
            if (id == R.id.home) {
                Intent intent = new Intent(FavoritePlaylistActivity.this, MenuPlayerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}