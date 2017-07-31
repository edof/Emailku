package com.mabesstudio.emailku.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mabesstudio.emailku.R;
import com.mabesstudio.emailku.app.AppConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private ImageView senderThumbnail;
    private TextView senderName, emailSubject, emailTimestamp, emailContent;
    private ToggleButton starToggle;
    private String sender, subject, content, timestamp, image;
    private boolean star, attachment;
    private ImageButton buttonReply, overflowMenu;
    private SimpleDateFormat dateFormatInput = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
    private SimpleDateFormat dateFormatOutput = new SimpleDateFormat("dd MMM yyyy hh:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        sender = intent.getExtras().getString(AppConfig.KEY_SENDER);
        subject = intent.getExtras().getString(AppConfig.KEY_SUBJECT);
        content = intent.getExtras().getString(AppConfig.KEY_SNIPPET);
        timestamp = intent.getExtras().getString(AppConfig.KEY_TIMESTAMP);
        image = intent.getExtras().getString(AppConfig.KEY_IMAGE);
        star = intent.getExtras().getBoolean(AppConfig.KEY_STAR);
        attachment = intent.getExtras().getBoolean(AppConfig.KEY_ATTACHMENT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Kotak Masuk");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        senderThumbnail = (ImageView) findViewById(R.id.iv_detail_sender);
        senderName = (TextView) findViewById(R.id.tv_detail_sender);
        emailSubject = (TextView) findViewById(R.id.tv_subject_detail);
        emailTimestamp = (TextView) findViewById(R.id.tv_timestamp_detail);
        emailContent = (TextView) findViewById(R.id.tv_content_detail);
        starToggle = (ToggleButton) findViewById(R.id.tb_star_detail);
        buttonReply = (ImageButton) findViewById(R.id.bt_reply_detail);
        overflowMenu = (ImageButton) findViewById(R.id.menu_detail);

        emailSubject.setText(subject);
        senderName.setText(sender);
        Glide.with(this)
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(senderThumbnail);
        try {
            Date parseDate = dateFormatInput.parse(timestamp);
            emailTimestamp.setText(dateFormatOutput.format(parseDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        emailContent.setText(content);
        if (star){
            starToggle.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_black));
            starToggle.setChecked(true);
        } else {
            starToggle.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_border_black));
            starToggle.setChecked(false);
        }

        buttonReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), ReplyActivity.class);
                startActivity(intent1);
            }
        });

        overflowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenuDetail();
            }
        });

    }

    private void showPopupMenuDetail(){
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(DetailActivity.this, overflowMenu);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu_inbox_detail, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(DetailActivity.this, item.getTitle() + "menu item",Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popup.show();//showing popup menu
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.inbox, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
        }
    }
}
