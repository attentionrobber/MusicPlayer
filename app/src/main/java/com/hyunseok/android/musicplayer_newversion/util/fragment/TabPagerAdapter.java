package com.hyunseok.android.musicplayer_newversion.util.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-02-27.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragments;
    //private int tabCount;

    public TabPagerAdapter(FragmentManager fm) { // 수정 전 : ( , List<Fragment> fragments, int tabCount)
        super(fm);
//        this.fragments = fragments;
//        this.tabCount = tabCount;
        fragments = new ArrayList<>();
    }

    public void add(Fragment fragment) {
        fragments.add(fragment); // Fragment 초기화
    }

    @Override
    public Fragment getItem(int position) {

//        switch (position) {
//            case 0:
//                FragmentFavorite fragmentFavorite = new FragmentFavorite();
//                return fragmentFavorite;
//            case 1:
//                FragmentMusic fragmentMusic = new FragmentMusic();
//                return fragmentMusic;
//            case 2:
//                FragmentFolder fragmentFolder = new FragmentFolder();
//                return fragmentFolder;
//            case 3:
//                FragmentAlbum fragmentAlbum = new FragmentAlbum();
//                return fragmentAlbum;
//            default:
//                return null;
//        }
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
