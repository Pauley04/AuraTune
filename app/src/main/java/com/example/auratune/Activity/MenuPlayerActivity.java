package com.example.auratune.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.auratune.Adapter.BannerAdapter;
import com.example.auratune.Adapter.CategoryAdapter;
import com.example.auratune.Adapter.SongAdapter;
import com.example.auratune.Domain.BannerModel;
import com.example.auratune.Domain.Song;
import com.example.auratune.R;
import com.example.auratune.ViewModel.MainViewModel;
import com.example.auratune.databinding.ActivityMenuPlayerBinding;

import java.util.ArrayList;
import java.util.List;

public class MenuPlayerActivity extends AppCompatActivity implements SongAdapter.OnItemClickListener {
    private ActivityMenuPlayerBinding binding;
    private MainViewModel viewModel;

    private SongAdapter adapter;
    private List<Song> songList;
    private List<Song> previewSongs;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMenuPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Use ViewModelProvider for lifecycle awareness
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding.favoriteView.setLayoutManager(new LinearLayoutManager(this));
        binding.favoriteView.setNestedScrollingEnabled(false);

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        loadSongs();
                    } else {
                        Toast.makeText(MenuPlayerActivity.this, "Permission denied to read storage", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        initCategory();
        initBanner();
        initFavorite();

        binding.textView9.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPlayerActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void initCategory() {
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        viewModel.loadCategory().observe(this, categoryModels -> {
            binding.progressBarCategory.setVisibility(View.GONE);
            binding.categoryView.setLayoutManager(new LinearLayoutManager(
                    MenuPlayerActivity.this, LinearLayoutManager.HORIZONTAL, false));
            binding.categoryView.setAdapter(new CategoryAdapter(categoryModels));
            binding.categoryView.setNestedScrollingEnabled(true);
        });
    }

    private void banners(ArrayList<BannerModel> bannerModels) {
        binding.viewPagerBanner.setAdapter(new BannerAdapter(binding.viewPagerBanner, bannerModels));
        binding.viewPagerBanner.setClipToPadding(false);
        binding.viewPagerBanner.setClipChildren(false);
        binding.viewPagerBanner.setOffscreenPageLimit(5);
        binding.viewPagerBanner.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        binding.viewPagerBanner.setPageTransformer(compositePageTransformer);
    }

    private void initBanner() {
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        viewModel.loadBanner().observe(this, bannerModels -> {
            if (bannerModels != null && !bannerModels.isEmpty()) {
                banners(bannerModels);
                binding.progressBarBanner.setVisibility(View.GONE);
            }
        });
    }

    private void initFavorite() {
        binding.progressBarFavorite.setVisibility(View.VISIBLE);
        checkPermissionsAndLoadSongs();
    }

    private void checkPermissionsAndLoadSongs() {
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_AUDIO;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            loadSongs();
        } else {
            binding.textEmptyFavorite.setVisibility(View.VISIBLE);
            binding.textEmptyFavorite.setText(R.string.permission_prompt_music);
            binding.textEmptyFavorite.setOnClickListener(v -> requestPermissionLauncher.launch(permission));
            binding.progressBarFavorite.setVisibility(View.GONE);
        }
    }

    private List<Song> getSongs() {
        List<Song> songs = new ArrayList<>();
        Uri collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        try (Cursor cursor = getContentResolver().query(collection, null, selection, null, sortOrder)) {
            if (cursor != null) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
                int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                int albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);

                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idColumn);
                    String title = cursor.getString(titleColumn);
                    String artist = cursor.getString(artistColumn);
                    String data = cursor.getString(dataColumn);
                    long albumId = cursor.getLong(albumIdColumn);

                    songs.add(new Song(id, title, artist, data, albumId));
                }
            }
        } catch (Exception ignored) {
        }
        return songs;
    }

    private void loadSongs() {
        songList = getSongs();
        binding.progressBarFavorite.setVisibility(View.GONE);
        if (songList.isEmpty()) {
            binding.textEmptyFavorite.setVisibility(View.VISIBLE);
            binding.textEmptyFavorite.setText(R.string.no_music_message);
        } else {
            binding.textEmptyFavorite.setVisibility(View.GONE);
        }
        previewSongs = new ArrayList<>(songList.subList(0, Math.min(songList.size(), 3)));
        adapter = new SongAdapter(previewSongs, this);
        binding.favoriteView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putParcelableArrayListExtra("songList", new ArrayList<>(songList));
        Song selectedSong = previewSongs.get(position);
        int startPosition = songList.indexOf(selectedSong);
        if (startPosition < 0) {
            startPosition = position;
        }
        intent.putExtra("position", startPosition);
        startActivity(intent);
    }
}
