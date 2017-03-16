package com.hyunseok.android.musicplayer_newversion;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyunseok.android.musicplayer_newversion.domain.Album;
import com.hyunseok.android.musicplayer_newversion.domain.Common;
import com.hyunseok.android.musicplayer_newversion.domain.Music;
import com.hyunseok.android.musicplayer_newversion.dummy.DummyContent.DummyItem;

import java.util.List;
import java.util.Objects;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<?> datas;
    private String flag;
    private int item_layout_id;

    private Context context;

    public ListAdapter(Context context, List<?> datas, String flag) {
        this.context = context;
        this.datas = datas;
        this.flag = flag;
        switch (flag) { // MainActivity에서 Adapter를 호출할 때 View를 바꿔줄 수 있다.
            //case ListFragment.TYPE_ARTIST:
            case ListFragment.TYPE_FAVORITE:
                item_layout_id = R.layout.list_fragment_item;
                break;
            case ListFragment.TYPE_SONG:
                item_layout_id = R.layout.list_fragment_item;
                break;
            case ListFragment.TYPE_FOLDER:
                item_layout_id = R.layout.list_fragment_item;
                break;
            case ListFragment.TYPE_ALBUM:
                item_layout_id = R.layout.list_fragment_item;
                break;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(item_layout_id, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Common common = (Common) datas.get(position);

        holder.position = position; // 현재 위치 받아오기
        holder.tv_title.setText(common.getTitle());
        holder.tv_artist.setText(common.getArtist());;
        Glide.with(context).load(common.getImageUri())
                .placeholder(R.mipmap.default_album_image).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public int position;

        public int id;
        public String title;

        ConstraintLayout box;
        ImageView imageView;
        TextView tv_title, tv_artist;

        public ViewHolder(View view) {
            super(view);

            box = (ConstraintLayout) view.findViewById(R.id.list_item);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_artist = (TextView) view.findViewById(R.id.tv_artist);

            box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayerActivity.class);
                    intent.putExtra(ListFragment.ARG_POSITION, position);
                    intent.putExtra(ListFragment.ARG_LIST_TYPE, flag);
                    context.startActivity(intent);
                }
            });
        }
    }
}
