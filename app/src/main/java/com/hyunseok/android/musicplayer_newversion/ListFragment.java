package com.hyunseok.android.musicplayer_newversion;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyunseok.android.musicplayer_newversion.domain.Artist;
import com.hyunseok.android.musicplayer_newversion.domain.Common;
import com.hyunseok.android.musicplayer_newversion.domain.Music;
import com.hyunseok.android.musicplayer_newversion.dummy.DummyContent;

import java.util.List;


public class ListFragment<T> extends Fragment { // Generic( <T> )으로 사용

    private static final String ARG_COLUMN_COUNT = "column-count";
    public static final String ARG_LIST_TYPE = "list-type";
    public static final String ARG_POSITION = "position";

    public static final String TYPE_FAVORITE = "FAVORITE";
    public static final String TYPE_SONG = "SONG";
    public static final String TYPE_FOLDER = "FOLDER";
    public static final String TYPE_ALBUM = "ALBUM";


    private int mColumnCount = 1;
    private String mListType = "";

    private List<?> datas;

    public ListFragment() {
    }

    public static ListFragment newInstance(int columnCount, String flag) {
        Bundle args = new Bundle();
        ListFragment fragment = new ListFragment();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_LIST_TYPE, flag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mListType = getArguments().getString(ARG_LIST_TYPE);

            if(TYPE_FAVORITE.equals(mListType)) {
                datas = DataLoader.getFavorite(getContext());
            } else if(TYPE_SONG.equals(mListType)) {
                datas = DataLoader.getMusic(getContext());
            } else if(TYPE_FOLDER.equals(mListType)) {
                datas = DataLoader.getFolder(getContext());
            } else if (TYPE_ALBUM.equals(mListType)) {
                datas = DataLoader.getAlbum(getContext());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new ListAdapter(context, datas, mListType));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
