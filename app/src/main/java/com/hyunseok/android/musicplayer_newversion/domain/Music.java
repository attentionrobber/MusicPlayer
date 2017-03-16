package com.hyunseok.android.musicplayer_newversion.domain;

import android.net.Uri;

/**
 * POJO = Domain
 * Created by Administrator on 2017-02-01.
 */

public class Music extends Common {


    // Music Info.
    public int id; // MediaStore.Audio.Media._ID
    public Uri music_uri;
    public String title; // MediaStore.Audio.Media.TITLE
    public String artist; // MediaStore.Audio.Media.ARTIST
    public int artist_id;
    public String artist_key;
    public int album_id; // MediaStore.Audio.Media.ALBUM_ID
    public Uri album_img_uri;
    public int genre_id;
    public int duration;
    public String is_music;
    public String composer;
    public String content_type;
    public String year;


    // additional Info.
    public int order; // 음악 순서
    public boolean favorite; // 즐겨찾기


    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public String getDurationText() {
        return duration + ""; // TODO TimeUtil.convertMiliToTime(duration);
    }

    @Override
    public Uri getImageUri() {
        return album_img_uri;
    }
}
