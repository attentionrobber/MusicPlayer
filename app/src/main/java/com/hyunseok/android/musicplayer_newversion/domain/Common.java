package com.hyunseok.android.musicplayer_newversion.domain;

import android.net.Uri;

/**
 * Created by Administrator on 2017-02-28.
 */

public abstract class Common {

    public abstract String getTitle();
    public abstract String getArtist();
    public abstract int getDuration();
    public abstract String getDurationText();
    public abstract Uri getImageUri();
}
