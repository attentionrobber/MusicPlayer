package com.hyunseok.android.musicplayer_newversion;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hyunseok.android.musicplayer_newversion.util.fragment.TabPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int REQ_PERMISSION = 100; // 권한 요청 코드

    Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Music");
        //toolbar.setSubtitle("Music2");
        //toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        // Tab Layout 정의
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        // Tab 생성 및 타이틀 입력         //.setIcon(R.mipmap.ic_launcher)
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.menu_favorite)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.menu_music)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.menu_folder)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.menu_album)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Fragment 페이저 작성
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        // 아답터 생성
        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager());
        // 프래그먼트 생성 및 어댑터에 추가 // 첫번째 인자는 열의 개수, 두 번째 인자는 Tab의 flag
        adapter.add(ListFragment.newInstance(1, ListFragment.TYPE_FAVORITE)); //즐겨찾기 보기 형식
        adapter.add(ListFragment.newInstance(1, ListFragment.TYPE_SONG)); // 모든 곡 보기 형식
        adapter.add(ListFragment.newInstance(1, ListFragment.TYPE_FOLDER)); // Folder 보기 형식
        adapter.add(ListFragment.newInstance(1, ListFragment.TYPE_ALBUM)); // Album 보기 형식

        viewPager.setAdapter(adapter);
        // 1. 페이저가 변경되었을 때 탭을 바꿔주는 리스너(Pager Listener)
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // 2. 탭이 변경되었을 때 페이지를 바꿔주는 리스너
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    // 리스트 섞기
    private void setShuffle() {
        // TODO 로직 구현하기
    }

    // ToolBar의 Items의 Listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // ToolBar에 Items추가
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item, menu);
        //return true;
        return super.onCreateOptionsMenu(menu);
    }


    // 권한 관리
    private void checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 안드로이드 버전 체크
            if (PermissionControl.checkPermission(this, REQ_PERMISSION)) { // 권한 체크
                init(); // 프로그램 실행
            }
        } else {
            init(); // 프로그램 실행
        }
    }

    // 권한 체크 후 콜백처리(사용자가 확인 후 시스템이 호출하는 함수)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQ_PERMISSION) {
            if( PermissionControl.onCheckResult(grantResults) ) {
                init();
            } else {
                Toast.makeText(this, "권한을 실행하지 않으면 프로그램이 실행되지 않습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
