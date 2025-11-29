package com.example.auratune.Repository;

import androidx.annotation.NonNull;

import com.example.auratune.Domain.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class FavoriteManager {
    private static final List<Song> favoriteSongs = new ArrayList<>();

    private FavoriteManager() { }

    @NonNull
    public static List<Song> getFavorites() {
        return Collections.unmodifiableList(favoriteSongs);
    }

    public static boolean isFavorite(@NonNull Song song) {
        for (Song favorite : favoriteSongs) {
            if (favorite.id == song.id) {
                return true;
            }
        }
        return false;
    }

    public static void addToFavorites(@NonNull Song song) {
        if (!isFavorite(song)) {
            favoriteSongs.add(song);
        }
    }

    public static void removeFromFavorites(@NonNull Song song) {
        Iterator<Song> iterator = favoriteSongs.iterator();
        while (iterator.hasNext()) {
            Song favorite = iterator.next();
            if (favorite.id == song.id) {
                iterator.remove();
                return;
            }
        }
    }
}
