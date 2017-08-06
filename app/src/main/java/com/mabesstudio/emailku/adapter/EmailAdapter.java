package com.mabesstudio.emailku.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mabesstudio.emailku.R;
import com.mabesstudio.emailku.activity.DetailActivity;
import com.mabesstudio.emailku.app.AppConfig;
import com.mabesstudio.emailku.helper.loadmore.OnLoadMoreListener;
import com.mabesstudio.emailku.model.Email;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.view.View.GONE;

public class EmailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Email> emailArrayList;
    private ArrayList<Email> itemsPendingRemoval;
    private Context mContext;
    private SimpleDateFormat dateFormatInput = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
    private SimpleDateFormat dateFormatOutput = new SimpleDateFormat("dd MMM", Locale.getDefault());
    private static final int TIME_DISMISS = 3000;
    private Handler handler = new Handler();

    //map item untuk pending runnable supaya memungkinkan untuk cancel remove item
    private HashMap<Email, Runnable> pendingRunnable = new HashMap<>();

    private final int VIEW_ITEM = 1;
    private final int VIEW_LOAD = 0;

    //batas jumlah minimal pemicu load more saat onscroll
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public EmailAdapter(final Context mContext, ArrayList<Email> emailArrayList, RecyclerView recyclerView) {
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
        itemsPendingRemoval = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_layout, parent, false);
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

            if (itemsPendingRemoval.contains(email)){
                //swipe state
                inboxHolder.inboxItem.setVisibility(GONE);
                inboxHolder.swipeLayout.setVisibility(View.VISIBLE);
                inboxHolder.undo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        undoOption(email);
                    }
                });
            } else {
                //normal state
                inboxHolder.inboxItem.setVisibility(View.VISIBLE);
                inboxHolder.swipeLayout.setVisibility(GONE);

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
            }

        } else if (holder instanceof LoadingViewHolder) {
            ((LoadingViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    private void undoOption(Email email) {
        Runnable pendingRemovalRunnable = pendingRunnable.get(email);
        pendingRunnable.remove(email);
        if (pendingRemovalRunnable != null){
            handler.removeCallbacks(pendingRemovalRunnable);
        }
        itemsPendingRemoval.remove(email);
        //rebind row in normal state
        notifyItemChanged(emailArrayList.indexOf(email));
    }

    public void pendingRemoval(int position){
        final Email email = emailArrayList.get(position);
        if (!itemsPendingRemoval.contains(email)){
            itemsPendingRemoval.add(email);
            //redraw row saat dipilih undo
            notifyItemChanged(position);

            //membuat, menyimpan, dan mengepos runnable pada saat remove data
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(emailArrayList.indexOf(email));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, TIME_DISMISS);
            pendingRunnable.put(email, pendingRemovalRunnable);
        }
    }

    //menghapus item
    private void remove(int position) {
        Email email = emailArrayList.get(position);
        if (itemsPendingRemoval.contains(email)){
            itemsPendingRemoval.remove(email);
        }
        if (emailArrayList.contains(email)){
            emailArrayList.remove(position);
            notifyItemRemoved(position);
        }
    }

    //mengetahui state pending saat remove
    public boolean isPendingRemoval(int position){
        Email email = emailArrayList.get(position);
        return itemsPendingRemoval.contains(email);
    }

    @Override
    public int getItemCount() {
        return emailArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (emailArrayList.get(position) != null){
            return VIEW_ITEM;
        } else {
            return VIEW_LOAD;
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

    private class InboxViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout swipeLayout;
        LinearLayout inboxItem;
        ImageView ivThumbnail, ivAttachment;
        TextView tvSender, tvSubject, tvSnippet, tvTimestamp;
        ToggleButton toggleStar;
        Context context;
        TextView undo;

        InboxViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();
            inboxItem = (LinearLayout) itemView.findViewById(R.id.inbox_item);
            swipeLayout = (LinearLayout) itemView.findViewById(R.id.swipe_layout);
            undo = (TextView) itemView.findViewById(R.id.txt_undo);
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

        LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }
}
