package com.wang17.getbaiduaccesstoken;

/**
 * Created by 阿弥陀佛 on 2016/10/7.
 */
public class Song {
    /**
     * Constructor.
     */
    public Song(String name, String artist, String albums, long createTime, String language) {
        this.name = name;
        this.artist = artist;
        this.albums = albums;
        this.create_time = createTime;
        this.language = language;
    }

    private String name;

    private String artist;
    private String albums;
    private long create_time;
    private String language;
    /** Getters and setters */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbums() {
        return albums;
    }

    public Song setAlbums(String albums) {
        this.albums = albums;
        return this;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}