package com.hyunseok.android.musicplayer_newversion;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hyunseok.android.musicplayer_newversion.domain.Music;

import java.util.List;

// TODO 버튼 클릭, NotiBar 지웠을 때 처리

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, ControlInterface{

    // 음악 데이터
    List<Music> datas; // ArrayList
    int position = -1; // 현재 음악

    // 뷰페이저
    ViewPager viewPager_Player;
    PlayerAdapter adapter;
    // 위젯
    ImageButton imgbtn_play, imgbtn_prev, imgbtn_next;
    SeekBar seekBar;
    TextView tv_duration;

    String list_type = "";

    Controller controller; // 동작 컨트롤러

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //playStatus = STOP;

        setVolumeControlStream(AudioManager.STREAM_MUSIC); // 볼륨 컨트롤을 Ringtone이 아닌 Media로 바꿈.

        imgbtn_prev = (ImageButton) findViewById(R.id.imgbtn_prev);
        imgbtn_play = (ImageButton) findViewById(R.id.imgbtn_play);
        imgbtn_next = (ImageButton) findViewById(R.id.imgbtn_next);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        tv_duration = (TextView) findViewById(R.id.tv_duration);

        imgbtn_next.setOnClickListener(this);
        imgbtn_play.setOnClickListener(this);
        imgbtn_prev.setOnClickListener(this);


        // 0. 데이터 가져오기
        datas = DataLoader.getMusic(this);
        // 1. ViewPager 가져오기
        viewPager_Player = (ViewPager) findViewById(R.id.viewPager_Player);
        // 2. ViewPager용 아답터 생성
        adapter = new PlayerAdapter(datas, this);
        // 3. ViewPager 아답터 연결
        viewPager_Player.setAdapter(adapter); // 위젯에 특성에 맞게 아답터가 있다. 위젯이 달라지면 사용 안되므로 새로운 커스텀아답터를 만들어준다.
                                                // ViewPlager의 아답터는 PagerAdapter
        // 4. ViewPager, SeekBar 체인지 리스너 연결
        //viewPager_Player.addOnPageChangeListener(viewPagerListener);
        //seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        // PageTransformer 연결
        //viewPager_Player.setPageTransformer(false, pageTransformer);

        // 5. RecyclerView위에 있는 CardView에서 한 Card아이템을 클릭시 특정 페이지 호출
        Intent intent = getIntent();
        if(intent != null) {
            Bundle bundle =  intent.getExtras();
            list_type = bundle.getString(ListFragment.ARG_LIST_TYPE);
            position = bundle.getInt(ListFragment.ARG_POSITION);

            if(position > 0) {
                viewPager_Player.setCurrentItem(position);
            }
            init(); // 음악 기본 정보를 설정해준다.
        }

        controller = Controller.getInstance();
        controller.addObserver(this);
    }

    /**
     * 음악 초기 세팅(player, seeBar, setText)
     */
    private void init() {
        playerInit();
        controllerInit();
    }

    private void playerInit() {
        // Service로 이관
    }

    private void controllerInit() {
        // view 바꿈.
        Music music = datas.get(position);
        tv_duration.setText(music.getDurationText());

        seekBar.setMax(music.getDuration());
    }

    private void prev() {

    }

    // 음악을 서비스로 실행시킨다.
    private void play() {
        Intent intent = new Intent(this, PlayerService.class);
        intent.setAction(PlayerService.ACTION_PLAY);
        intent.putExtra(ListFragment.ARG_POSITION, position);
        intent.putExtra(ListFragment.ARG_LIST_TYPE, list_type);
        startService(intent);
    }

    private void playStart() {

    }

    private void playPause() {

    }

    private void playRestart() {

    }

    private void next() {

    }

    /**
     * 버튼의 클릭리스너
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.imgbtn_prev :
                prev();
                break;
            case R.id.imgbtn_play :
                play();
                break;
            case R.id.imgbtn_next :
                next();
                break;
        }
    }

    @Override
    public void startPlayer() {
        imgbtn_play.setImageResource(android.R.drawable.ic_media_pause);
    }

    @Override
    public void pausePlayer() {
        imgbtn_play.setImageResource(android.R.drawable.ic_media_play);
    }

    @Override
    public void onDestroy() {
        controller.remove(this);
        super.onDestroy();
    }
}
