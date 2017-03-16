package com.hyunseok.android.musicplayer_newversion.domain;

import android.net.Uri;

import java.util.List;

/**
 * Created by Administrator on 2017-02-28.
 */

public class Artist extends Common {

    public int id;
    public String artist;
    public String artist_key;
    public int album_id;
    public Uri album_image_uri;
    public int number_of_albums;
    public int number_of_tracks;
    public List<Music> musics;

    @Override
    public String getTitle() {
        return artist;
    }

    @Override
    public String getArtist() {
        return "Tracks : " + number_of_tracks;
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public String getDurationText() {
        return null;
    }

    @Override
    public Uri getImageUri() {
        return album_image_uri;
    }
}
