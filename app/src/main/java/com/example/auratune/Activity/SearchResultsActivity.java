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

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.auratune.Adapter.SongAdapter;
import com.example.auratune.Domain.Song;
import com.example.auratune.R;
import com.example.auratune.databinding.ActivitySearchResultsBinding;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity implements SongAdapter.OnItemClickListener {

    public static final String EXTRA_QUERY = "search_query";

    private ActivitySearchResultsBinding binding;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private List<Song> songList;
    private List<Song> filteredSongs;
    private SongAdapter adapter;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySearchResultsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        query = getIntent().getStringExtra(EXTRA_QUERY);
        binding.textSearchQuery.setText(query == null || query.trim().isEmpty() ? "" : getString(R.string.search_results_for, query));

        binding.searchResultsView.setLayoutManager(new LinearLayoutManager(this));

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        loadSongs();
                    } else {
                        showPermissionMessage();
                    }
                }
        );

        checkPermissionsAndLoadSongs();
        setupBottomNavigation();

    }

    private void checkPermissionsAndLoadSongs() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            handlePermission(Manifest.permission.READ_MEDIA_AUDIO);
        } else {
            handlePermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void handlePermission(@NonNull String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            loadSongs();
        } else {
            showPermissionMessage();
            binding.textEmptyResults.setOnClickListener(v -> requestPermissionLauncher.launch(permission));
        }
    }

    private void showPermissionMessage() {
        binding.textEmptyResults.setVisibility(View.VISIBLE);
        binding.textEmptyResults.setText(R.string.permission_prompt_music);
        binding.progressBarSearch.setVisibility(View.GONE);
    }

    private void loadSongs() {
        binding.progressBarSearch.setVisibility(View.VISIBLE);
        songList = getSongs();
        filterSongs();
        binding.progressBarSearch.setVisibility(View.GONE);
    }

    private void filterSongs() {
        if (songList == null) return;

        String trimmedQuery = query != null ? query.trim().toLowerCase() : "";
        filteredSongs = new ArrayList<>();

        if (trimmedQuery.isEmpty()) {
            filteredSongs.addAll(songList);
        } else {
            for (Song song : songList) {
                String title = song.title != null ? song.title.toLowerCase() : "";
                String artist = song.artist != null ? song.artist.toLowerCase() : "";
                String album = song.album != null ? song.album.toLowerCase() : "";

                if (title.contains(trimmedQuery) || artist.contains(trimmedQuery) || album.contains(trimmedQuery)) {
                    filteredSongs.add(song);
                }
            }
        }

        if (adapter == null) {
            adapter = new SongAdapter(filteredSongs, this);
            binding.searchResultsView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        if (filteredSongs.isEmpty()) {
            binding.textEmptyResults.setVisibility(View.VISIBLE);
            binding.textEmptyResults.setText(R.string.no_search_results);
        } else {
            binding.textEmptyResults.setVisibility(View.GONE);
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
                int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
                int albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);

                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idColumn);
                    String title = cursor.getString(titleColumn);
                    String artist = cursor.getString(artistColumn);
                    String album = cursor.getString(albumColumn);
                    String data = cursor.getString(dataColumn);
                    long albumId = cursor.getLong(albumIdColumn);

                    songs.add(new Song(id, title, artist, album, data, albumId));
                }
            }
        } catch (Exception ignored) {
        }
        return songs;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putParcelableArrayListExtra("songList", new ArrayList<>(filteredSongs));
        Song selectedSong = filteredSongs.get(position);
        int startPosition = filteredSongs.indexOf(selectedSong);
        if (startPosition < 0) {
            startPosition = position;
        }
        intent.putExtra("position", startPosition);
        startActivity(intent);
    }

    @Override
    public void onDeleteSong(@NonNull Song song) {
        // Delete not supported from search results
    }

    private void setupBottomNavigation() {
        ChipNavigationBar navigationBar = binding.bottomNavigation;
        navigationBar.setItemSelected(R.id.home, true);
        navigationBar.setOnItemSelectedListener(id -> {
            if (id == R.id.home) {
                Intent intent = new Intent(SearchResultsActivity.this, MenuPlayerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if (id == R.id.favorites) {
                Intent intent = new Intent(SearchResultsActivity.this, FavoritePlaylistActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}

