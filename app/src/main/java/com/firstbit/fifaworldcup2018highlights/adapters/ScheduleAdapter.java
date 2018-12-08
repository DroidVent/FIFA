package com.firstbit.fifaworldcup2018highlights.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firstbit.fifaworldcup2018highlights.R;
import com.firstbit.fifaworldcup2018highlights.data.Match;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private ArrayList<Match> matches;
    Context context;
    StorageReference storageReference;
    public ScheduleAdapter(Context context, ArrayList<Match> matches) {
        this.matches = matches;
        this.context = context;
        storageReference =  FirebaseStorage.getInstance().getReference().child("images");
    }

    @NonNull
    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.ViewHolder holder, int position) {
        Match match = matches.get(position);
        holder.tvTeam.setText(match.getTeam());
        holder.tvTime.setText(match.getTime());
        holder.tvVenue.setText(match.getVenue());
        if (match.getTeams() != null && match.getTeams().size() != 0)
        {
            setImage(match.getTeams().get(0), holder.ivFirstTeam);
            setImage(match.getTeams().get(1), holder.ivSecondTeam);
        }
        else
        {
            setImage("", holder.ivFirstTeam);
            setImage("", holder.ivSecondTeam);
        }
    }
    private void setImage(String imageName, final ImageView ivFirstTeam)
    {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder);
        Glide.with(context)
                .load(imageName)
                .apply(requestOptions)
                .into(ivFirstTeam);
    }
    @Override
    public int getItemCount() {
        return matches.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvVenue, tvTime, tvTeam;
        ImageView ivFirstTeam, ivSecondTeam;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTeam = (TextView)itemView.findViewById(R.id.tv_team);
            tvVenue = (TextView)itemView.findViewById(R.id.tv_venue);
            tvTime = (TextView)itemView.findViewById(R.id.tv_time);
            ivFirstTeam = (ImageView)itemView.findViewById(R.id.iv_team_first);
            ivSecondTeam = (ImageView)itemView.findViewById(R.id.iv_team_second);
        }
    }
}
