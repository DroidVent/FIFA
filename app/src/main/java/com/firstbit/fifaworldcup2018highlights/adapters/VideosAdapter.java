package com.firstbit.fifaworldcup2018highlights.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firstbit.fifaworldcup2018highlights.R;
import com.firstbit.fifaworldcup2018highlights.VideoPlayerActivity;
import com.firstbit.fifaworldcup2018highlights.data.Video;

import java.util.ArrayList;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder>  {
    private Context context;
    private ArrayList<Video> videos;

    public VideosAdapter(Context context, ArrayList<Video> videos) {
        this.context = context;
        this.videos = videos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);

        VideosAdapter.ViewHolder vh = new VideosAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Video video = videos.get(position);
        if (video.getThumb() != null && !video.getThumb().equals(""))
        {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.centerCrop();
            requestOptions.placeholder(R.drawable.video_placeholder);
            holder.jzvdStd.setUp(
                    video.getPath(),
                    "", Jzvd.SCREEN_WINDOW_LIST);

            Glide.with(holder.jzvdStd.getContext()).load(video.getThumb()).apply(requestOptions).into(holder.jzvdStd.thumbImageView);
//            Glide.with(context)
//                    .load(video.getThumb())
//                    .apply(requestOptions)
//                    .into(holder.ivThumb);
        }
        holder.tvVideoName.setText(video.getVideo_name());
        holder.tvVideoTime.setText(video.getMtime());
        if (video.getLeague_name() != null && !video.getLeague_name().isEmpty())
            holder.tvLeagueName.setText(video.getLeague_name());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent mIntent = new Intent(context, VideoPlayerActivity.class);
//                mIntent.putExtra("path", video.getPath());
//                context.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView ivThumb;
        TextView tvVideoName, tvVideoTime, tvLeagueName;
        CardView view;
        JzvdStd jzvdStd;
        public ViewHolder(View itemView) {
            super(itemView);
            view = (CardView)itemView.findViewById(R.id.card_view);
//            ivThumb = (ImageView)itemView.findViewById(R.id.iv_thumb);
            tvVideoName = (TextView)itemView.findViewById(R.id.tv_video_name);
            tvVideoTime = (TextView)itemView.findViewById(R.id.tv_video_time);
            tvLeagueName = (TextView)itemView.findViewById(R.id.tv_league);
            jzvdStd = itemView.findViewById(R.id.videoplayer);
        }
    }
}
