package com.example.auratune.Domain;

public class FavoriteModel {
    private String id;        // Firestore document id or unique key
    private String title;
    private String artist;
    private String duration;  // e.g., "3:45"
    private String imageUrl;  // cover art URL or storage path
    private String data;      // optional: local file path or stream URL

    // No-arg constructor required by Firebase
    public FavoriteModel() { }

    public FavoriteModel(String id, String title, String artist, String duration, String imageUrl, String data) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.imageUrl = imageUrl;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

}
