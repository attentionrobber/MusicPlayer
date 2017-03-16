package com.hyunseok.android.musicplayer_newversion.util.Toast;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017-02-08.
 */

public class Message {

    public static void show(Context context, String msg) {
        android.widget.Toast.makeText(context, "", android.widget.Toast.LENGTH_SHORT).show();
    }
}
