package com.mabesstudio.emailku.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mabesstudio.emailku.R;
import com.mabesstudio.emailku.activity.DetailActivity;
import com.mabesstudio.emailku.app.AppConfig;
import com.mabesstudio.emailku.listener.OnLoadMoreListener;
import com.mabesstudio.emailku.model.Email;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class EmailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Email> emailArrayList;
    private Context mContext;
    private SimpleDateFormat dateFormatInput = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
    private SimpleDateFormat dateFormatOutput = new SimpleDateFormat("dd MMM", Locale.getDefault());

    private final int VIEW_ITEM = 1;
    private final int VIEW_LOAD = 0;

    //batas jumlah minimal pemicu load more saat onscroll
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public EmailAdapter(Context mContext, ArrayList<Email> emailArrayList, RecyclerView recyclerView) {
        this.emailArrayList = emailArrayList;
        this.mContext = mContext;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = layoutManager.getItemCount();
                    lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        //item terakhir dicapai
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_inbox, parent, false);
            vh = new InboxViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
            vh = new LoadingViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Email email = emailArrayList.get(position);
        if (holder instanceof InboxViewHolder) {
            final InboxViewHolder inboxHolder = (InboxViewHolder) holder;
            Glide.with(mContext)
                    .load(email.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(inboxHolder.ivThumbnail);

            //show hide attachment icon
            if (email.isAttachment()) {
                inboxHolder.ivAttachment.setVisibility(View.VISIBLE);
            } else {
                inboxHolder.ivAttachment.setVisibility(View.INVISIBLE);
            }
            inboxHolder.tvSender.setText(email.getSender());
            inboxHolder.tvSubject.setText(email.getSubject());
            inboxHolder.tvSnippet.setText(email.getSnippet());
            try {
                Date parseDate = dateFormatInput.parse(email.getTimestamp());
                inboxHolder.tvTimestamp.setText(dateFormatOutput.format(parseDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //init starred email
            if (email.isStar()) {
                inboxHolder.toggleStar.setChecked(true);
            } else {
                inboxHolder.toggleStar.setChecked(false);
            }

        } else if (holder instanceof LoadingViewHolder) {
            ((LoadingViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return emailArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return emailArrayList.get(position) != null ? VIEW_ITEM : VIEW_LOAD;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

    private class InboxViewHolder extends RecyclerView.ViewHolder {
        LinearLayout inboxItem;
        ImageView ivThumbnail, ivAttachment;
        TextView tvSender, tvSubject, tvSnippet, tvTimestamp;
        ToggleButton toggleStar;
        Context context;

        InboxViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();
            inboxItem = (LinearLayout) itemView.findViewById(R.id.inbox_item);
            ivThumbnail = (ImageView) itemView.findViewById(R.id.iv_inbox_item);
            ivAttachment = (ImageView) itemView.findViewById(R.id.attach);
            tvSender = (TextView) itemView.findViewById(R.id.tv_sender);
            tvSubject = (TextView) itemView.findViewById(R.id.tv_subject);
            tvSnippet = (TextView) itemView.findViewById(R.id.tv_snippet);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tv_timestamp);
            toggleStar = (ToggleButton) itemView.findViewById(R.id.toggle_star);

            inboxItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Email email = emailArrayList.get(getAdapterPosition());
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(AppConfig.KEY_ID, email.getId());
                    intent.putExtra(AppConfig.KEY_SENDER, email.getSender());
                    intent.putExtra(AppConfig.KEY_SUBJECT, email.getSubject());
                    intent.putExtra(AppConfig.KEY_SNIPPET, email.getSnippet());
                    intent.putExtra(AppConfig.KEY_TIMESTAMP, email.getTimestamp());
                    intent.putExtra(AppConfig.KEY_IMAGE, email.getImage());
                    intent.putExtra(AppConfig.KEY_STAR, email.isStar());
                    intent.putExtra(AppConfig.KEY_ATTACHMENT, email.isAttachment());
                    context.startActivity(intent);
                }
            });

            toggleStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (toggleStar.isChecked()){
                        toggleStar.setChecked(false);
                    } else {
                        toggleStar.setChecked(true);
                    }
                }
            });
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }
}
