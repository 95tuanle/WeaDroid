package com.example.tuanle.weadroid;

import java.io.Serializable;

public class Song implements Serializable {
    public static final String ARTIST_WATER_MARK = "-artist-";
    public static final String MP3 = ".mp3";
    private long id;
    private String fileName;
    private String filePath;
    private String title;
    private String artist;

     public Song(long songID, String fileName, String filePath) {
        this.fileName = fileName;
        this.id = songID;
        this.filePath = filePath;
        title=getTitle();
    }

    public long getID() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getTitle(){
        if (title != null) return title;
        int endIndex = fileName.indexOf(ARTIST_WATER_MARK);
        if (endIndex <0)
            return fileName;
        title = fileName.substring(0, endIndex);
        return title;
    }

    public String getArtist(){
         if (artist !=null) return artist;
         int beginIndex = fileName.indexOf(ARTIST_WATER_MARK);
         if (beginIndex<0)
             return "Unknown";
         beginIndex+=ARTIST_WATER_MARK.length();
         int endIndex = fileName.indexOf(MP3);
         if (endIndex <0)
             endIndex = fileName.length();
         if (endIndex <= beginIndex)
             return "Unknown";
         artist = fileName.substring(beginIndex,endIndex);
         return artist;
    }
}
