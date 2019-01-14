package com.firstbit.fifaworldcup2018highlights.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firstbit.fifaworldcup2018highlights.R;
import com.firstbit.fifaworldcup2018highlights.data.Group;
import com.firstbit.fifaworldcup2018highlights.data.Standing;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MultiTypeDemoAdapter  extends SectioningAdapter {
    static final String TAG = MultiTypeDemoAdapter.class.getSimpleName();

    static final int USER_HEADER_TYPE_0 = 0;
    static final int USER_HEADER_TYPE_1 = 1;

    static final int USER_ITEM_TYPE_0 = 0;
    static final int USER_ITEM_TYPE_1 = 1;

    ArrayList<Group> sections;

    public class ItemViewHolder0 extends SectioningAdapter.ItemViewHolder {
        TextView tvTeam, tvPosition, tvPlayed, tvDiff, tvWon, tvLose, tvPts;

        public ItemViewHolder0(View itemView) {
            super(itemView);
            tvTeam = (TextView) itemView.findViewById(R.id.tv_team);
            tvPosition = (TextView) itemView.findViewById(R.id.tv_position);
            tvPlayed = (TextView) itemView.findViewById(R.id.tv_played);
            tvDiff = (TextView) itemView.findViewById(R.id.tv_diff);
            tvWon = (TextView) itemView.findViewById(R.id.tv_won);
            tvLose = (TextView) itemView.findViewById(R.id.tv_lose);
            tvPts = (TextView) itemView.findViewById(R.id.tv_points);

        }
    }

    public class HeaderViewHolder0 extends SectioningAdapter.HeaderViewHolder {
        TextView textView;

        public HeaderViewHolder0(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_text);
        }
    }
    public class ItemViewHolder1 extends SectioningAdapter.ItemViewHolder {
        TextView textView;

        public ItemViewHolder1(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_diff);
        }
    }

    public class HeaderViewHolder1 extends SectioningAdapter.HeaderViewHolder {
        TextView textView;

        public HeaderViewHolder1(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_text);
        }
    }
    public MultiTypeDemoAdapter(ArrayList<Group> groups) {
        sections = groups;
    }

    @Override
    public int getNumberOfSections() {
        return sections.size();
    }
    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return sections.get(sectionIndex).getStandings().size();
    }
    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    @Override
    public boolean doesSectionHaveFooter(int sectionIndex) {
        return false;
    }
    @Override
    public GhostHeaderViewHolder onCreateGhostHeaderViewHolder(ViewGroup parent) {
        final View ghostView = new View(parent.getContext());
        ghostView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return new GhostHeaderViewHolder(ghostView);
    }
    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ItemViewHolder0(inflater.inflate(R.layout.standing_item, parent, false));

    }
    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new HeaderViewHolder0(inflater.inflate(R.layout.header_view, parent, false));
    }
    @Override
    public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemType) {
        Group group = sections.get(sectionIndex);

        ItemViewHolder0 ivh = (ItemViewHolder0) viewHolder;
        HashMap<String, Standing>mp = group.getStandings().get(itemIndex);
        ivh.tvTeam.setText((CharSequence) mp.get("team"));
        ivh.tvPosition.setText((CharSequence) mp.get("position"));
        ivh.tvPlayed.setText((CharSequence) mp.get("played"));
        ivh.tvWon.setText((CharSequence) mp.get("win"));
        ivh.tvLose.setText((CharSequence) mp.get("lose"));
        ivh.tvDiff.setText((CharSequence) mp.get("difference"));
        ivh.tvPts.setText((CharSequence) mp.get("points"));

    }
    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerType) {
        Group s = sections.get(sectionIndex);

        switch (headerType) {
            case USER_HEADER_TYPE_0: {
                HeaderViewHolder0 hvh = (HeaderViewHolder0) viewHolder;
                hvh.textView.setText(s.getGroup());
                break;
            }
            case USER_HEADER_TYPE_1: {
                HeaderViewHolder1 hvh = (HeaderViewHolder1) viewHolder;
                hvh.textView.setText(s.getGroup());
                break;
            }

            default:
                throw new IllegalArgumentException("Unrecognized headerType: " + headerType);
        }

    }
}
