package com.mabesstudio.emailku.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mabesstudio.emailku.entity.InboxMail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.mabesstudio.emailku.R;

public class EmailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<InboxMail> arrayInbox;
    private Context mContext;
    private SimpleDateFormat dateFormatInput = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
    private SimpleDateFormat dateFormatOutput = new SimpleDateFormat("dd MMM", Locale.getDefault());

    public EmailAdapter(ArrayList<InboxMail> arrayInbox, Context mContext) {
        this.arrayInbox = arrayInbox;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_inbox_item, parent, false);
        return new InboxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        InboxMail inboxMail = arrayInbox.get(position);
        if (holder instanceof InboxViewHolder) {
            final InboxViewHolder inboxHolder = (InboxViewHolder) holder;
            Glide.with(mContext)
                    .load(inboxMail.image)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(inboxHolder.ivThumbnail);
            if (inboxMail.attachment.contentEquals("1")) {
                inboxHolder.ivAttachment.setVisibility(View.VISIBLE);
            } else if (inboxMail.attachment.contentEquals("0")) {
                inboxHolder.ivAttachment.setVisibility(View.INVISIBLE);
            }
            inboxHolder.tvSender.setText(inboxMail.sender);
            inboxHolder.tvSubject.setText(inboxMail.subject);
            inboxHolder.tvSnippet.setText(inboxMail.snippet);
            try {
                Date parseDate = dateFormatInput.parse(inboxMail.timestamp);
                inboxHolder.tvTimestamp.setText(dateFormatOutput.format(parseDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (inboxMail.star.contentEquals("0")) {
                inboxHolder.toggleStar.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_border_black));
                inboxHolder.toggleStar.setChecked(false);
            } else if (inboxMail.star.contentEquals("1")) {
                inboxHolder.toggleStar.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_black));
                inboxHolder.toggleStar.setChecked(true);
            }

            inboxHolder.toggleStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inboxHolder.toggleStar.isChecked()) {
                        inboxHolder.toggleStar.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_border_black));
                        Toast.makeText(mContext, "Mail not starred", Toast.LENGTH_SHORT).show();
                        inboxHolder.toggleStar.setChecked(false);
                    } else {
                        inboxHolder.toggleStar.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_black));
                        Toast.makeText(mContext, "Mail starred", Toast.LENGTH_SHORT).show();
                        inboxHolder.toggleStar.setChecked(true);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return arrayInbox.size();
    }

    private class InboxViewHolder extends RecyclerView.ViewHolder {

        LinearLayout inboxItem;
        ImageView ivThumbnail, ivAttachment;
        TextView tvSender, tvSubject, tvSnippet, tvTimestamp;
        ToggleButton toggleStar;
        final Context context;

        InboxViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();
            inboxItem = (LinearLayout) itemView.findViewById(R.id.recyclerview_inbox_item);
            ivThumbnail = (ImageView) itemView.findViewById(R.id.iv_inbox_item);
            ivAttachment = (ImageView) itemView.findViewById(R.id.attach);
            tvSender = (TextView) itemView.findViewById(R.id.tv_sender);
            tvSubject = (TextView) itemView.findViewById(R.id.tv_subject);
            tvSnippet = (TextView) itemView.findViewById(R.id.tv_snippet);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tv_timestamp);
            toggleStar = (ToggleButton) itemView.findViewById(R.id.toggle_star);


        }
    }
}
