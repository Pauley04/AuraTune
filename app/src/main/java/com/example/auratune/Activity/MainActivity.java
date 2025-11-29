package com.example.auratune.Activity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.Manifest;
import android.util.Log;
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
import com.example.auratune.databinding.ActivityMainBinding;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


import java.util.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SongAdapter.OnItemClickListener {
    private ActivityMainBinding binding;
    private SongAdapter adapter;
    private List<Song> songList;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.recycleviewSongs.setLayoutManager(new LinearLayoutManager(this));
        binding.backBtn.setOnClickListener(v -> finish());

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        loadSongs();
                    } else {
                        Toast.makeText(MainActivity.this, "Permission denied to read storage", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        checkPermissionsAndLoadSongs();
        setupBottomNavigation();

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
            // show message in UI first
            binding.textEmpty.setVisibility(View.VISIBLE);
            binding.textEmpty.setText(R.string.permission_prompt_music);
            binding.textEmpty.setOnClickListener(v -> requestPermissionLauncher.launch(permission));
        }
    }

    private List<Song> getSongs() {
        List<Song> songs = new ArrayList<>();
        Uri collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        try (Cursor cursor = getContentResolver().query(collection, null, selection, null, sortOrder)) {
            if (cursor != null) {
                Log.d("AuraTune", "Cursor count: " + cursor.getCount());
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

                    Log.d("AuraTune", "Song: " + title + " by " + artist);
                    songs.add(new Song(id, title, artist, album, data, albumId));
                }
            } else {
                Log.d("AuraTune", "Cursor is null");
            }
        } catch (Exception e) {
            Log.e("AuraTune", "Error loading songs", e);
        }
        Log.d("AuraTune", "Total songs loaded: " + songs.size());
        return songs;
    }

    private void loadSongs() {
        songList = getSongs();
        if (songList.isEmpty()) {
            binding.textEmpty.setVisibility(View.VISIBLE);
            binding.textEmpty.setText(R.string.no_music_message);
        } else {
            binding.textEmpty.setVisibility(View.GONE);
        }
        adapter = new SongAdapter(songList, this);
        binding.recycleviewSongs.setAdapter(adapter);

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putParcelableArrayListExtra("songList", new ArrayList<>(songList));
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void onDeleteSong(@NonNull Song song) {
        Uri songUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song.id);
        try {
            int rowsDeleted = getContentResolver().delete(songUri, null, null);
            if (rowsDeleted > 0) {
                int index = songList.indexOf(song);
                if (index >= 0) {
                    songList.remove(index);
                    adapter.notifyItemRemoved(index);
                } else {
                    songList.remove(song);
                    adapter.notifyDataSetChanged();
                }

                if (songList.isEmpty()) {
                    binding.textEmpty.setVisibility(View.VISIBLE);
                    binding.textEmpty.setText(R.string.no_music_message);
                }

                Toast.makeText(this, R.string.delete_success, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.delete_failed, Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) {
            Log.e("AuraTune", "Failed to delete song", e);
            Toast.makeText(this, R.string.delete_failed, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupBottomNavigation() {
        ChipNavigationBar navigationBar = binding.bottomNavigation;
        navigationBar.setItemSelected(R.id.favorites, true);
        navigationBar.setOnItemSelectedListener(id -> {
            if (id == R.id.home) {
                Intent intent = new Intent(MainActivity.this, MenuPlayerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}