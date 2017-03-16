package com.hyunseok.android.musicplayer_newversion;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.hyunseok.android.musicplayer_newversion.domain.Music;

import java.util.ArrayList;
import java.util.List;

public class PlayerService extends Service implements ControlInterface {

    private static final int NOTIFICATION_ID = 1;

    //
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PREVIOUS = "action_previous";
    public static final String ACTION_STOP = "action_stop";
    //public static final String ACTION_REWIND = "action_rewind";
    //public static final String ACTION_FAST_FORWARD = "action_fast_forward";

    // 1. 미디어플레이어 사용 API 세팅
    public static MediaPlayer mMediaPlayer = null;
    public static String listType = "";
    public static int position = -1;

    List<Music> datas = new ArrayList<>();

    Controller controller;

    public PlayerService(){
        controller = Controller.getInstance();
        controller.addObserver(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent != null) {
            if(intent.getExtras() != null) {
                listType = intent.getExtras().getString(ListFragment.ARG_LIST_TYPE);
                position = intent.getExtras().getInt(ListFragment.ARG_POSITION);

                if(mMediaPlayer == null) {
                    initMedia();
                }
            }
        }

        handleAction(intent);

        return super.onStartCommand(intent, flags, startId);
    }

    // 1. Media Player 기본값 설정
    private void initMedia() {

        if(datas.size() < 1) {
            switch (listType) {
                case ListFragment.TYPE_SONG :
                    datas = DataLoader.getMusic(getBaseContext());
                    break;
                case ListFragment.TYPE_ALBUM:
                    break;
                case ListFragment.TYPE_FAVORITE:
                    break;
                case ListFragment.TYPE_FOLDER:
                    break;
            }
        }

        // 음원 Uri 가져오기
        Uri music_uri = datas.get(position).music_uri;

        mMediaPlayer = MediaPlayer.create(this, music_uri); // MediaPlayer를 사용하기 위해선 당연히 시스템자원이 필요하므로 this는 필수
        mMediaPlayer.setLooping(false); // 반복 여부

        // 미디어 플레이어에 완료 체크 리스너를 등록한다.
        // 음악이 끝까지 재생됐을 경우임.
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO next();
            }
        });
    }

    // 2. 명령어 실행.
    // Intent Action 에 넘어온 명령어를 분기시키는 함수
    private void handleAction( Intent intent ) {
        if( intent == null || intent.getAction() == null )
            return;

        String action = intent.getAction();
        if( action.equalsIgnoreCase( ACTION_PLAY ) ) {
            //buildNotification(); // notification bar 생성
            //playerStart(); // 음원 처리
            controller.play();
        } else if( action.equalsIgnoreCase( ACTION_PAUSE ) ) {
            //playerPause();
            controller.pause();
        } else if( action.equalsIgnoreCase( ACTION_PREVIOUS ) ) {

        } else if( action.equalsIgnoreCase( ACTION_NEXT ) ) {

        } else if( action.equalsIgnoreCase( ACTION_STOP ) ) {
            playerStop();
        }
        //else if( action.equalsIgnoreCase( ACTION_FAST_FORWARD ) ) {

        //}
        //else if( action.equalsIgnoreCase( ACTION_REWIND ) ) {

        //}
    }

    // Activity에서의 클릭 버튼 생성
    private NotificationCompat.Action generateAction(int icon, String title, String intentAction ) {
        Intent intent = new Intent( getApplicationContext(), PlayerService.class );
        intent.setAction( intentAction );
        // PendingIntent : 실행 대상이 되는 인텐트를 지연시키는 역할
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);

        return new NotificationCompat.Action.Builder(icon, title, pendingIntent).build();
    }

    // Notification Bar를 생성하는 함수
    private void buildNotification( NotificationCompat.Action action, String action_flag ) {
        // 노티바 모양
        //Notification.MediaStyle style = new Notification.MediaStyle();

        // 노티바 전체를 클릭했을 때 실행되는 메인 intent
//        Intent intentMain = new Intent( getApplicationContext(), PlayerActivity.class );
//        intentMain.putExtra( ListFragment.ARG_POSITION, position );
//        PendingIntent mainIntent = PendingIntent.getActivity(getApplicationContext(), 1, intentMain, 0);

        // STOP intent
        Intent intentStop = new Intent( getApplicationContext(), PlayerService.class );
        intentStop.setAction( ACTION_STOP );
        PendingIntent stopIntent = PendingIntent.getService(getApplicationContext(), 1, intentStop, 0);

        // 노티바 생성
        //Notification.Builder builder = new Notification.Builder( this );
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this ); // Compat은 버전처리를 자체적으로 해준다.

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle( "Media Title" )
                .setContentText( "Media Artist" );

        // Pause일 경우만 노티 삭제 가능
        //if(ACTION_PAUSE.equals(action_flag)) {
            builder.setDeleteIntent(stopIntent); // 노티바가 delete 될 때 stopIntent가 실행.
            builder.setOngoing(false);
        //}

        //builder.setStyle(style);

        builder.addAction(generateAction(android.R.drawable.ic_media_previous, "Prev", ACTION_PREVIOUS));
        builder.addAction(action);
        builder.addAction(generateAction(android.R.drawable.ic_media_next, "Next", ACTION_NEXT));

        NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        // 노티바를 화면에 보여준다.
        notificationManager.notify( NOTIFICATION_ID, builder.build() ); // 첫번째 인자는 notification 닫을 때 id값이 들어간다.
    }

    private void playerStart() {
        buildNotification( generateAction( android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE ), ACTION_PAUSE );
        mMediaPlayer.start();
    }

    private void playerPause() {
        buildNotification( generateAction( android.R.drawable.ic_media_play, "Play", ACTION_PLAY ), ACTION_PLAY );
        mMediaPlayer.pause();
    }

    private void playerStop() {
        if(mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel( NOTIFICATION_ID ); // 인자는 notification 닫을 때 id값이 들어간다.
        Intent intent = new Intent( getApplicationContext(), PlayerService.class );
        stopService( intent );
    }

    @Override
    public void startPlayer() {
        playerStart();
    }

    @Override
    public void pausePlayer() {
        playerPause();
    }

    @Override
    public void onDestroy() {
        controller.remove(this);
        super.onDestroy();
    }
}
