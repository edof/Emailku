package com.mabesstudio.emailku.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mabesstudio.emailku.R;
import com.mabesstudio.emailku.adapter.EmailAdapter;
import com.mabesstudio.emailku.app.AppConfig;
import com.mabesstudio.emailku.app.VolleySingleton;
import com.mabesstudio.emailku.listener.OnLoadMoreListener;
import com.mabesstudio.emailku.model.Email;
import com.mabesstudio.emailku.setup.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends Fragment {

    private ArrayList<Email> emailList;
    private EmailAdapter emailAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerViewInbox;
    private SwipeRefreshLayout swipeRefreshInbox;
    private ProgressBar progressBarInbox;
    private Context mContext;
    int offset = 0;

    public InboxFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        progressBarInbox = (ProgressBar) view.findViewById(R.id.pb_inbox);
        recyclerViewInbox = (RecyclerView) view.findViewById(R.id.rv_inbox);
        swipeRefreshInbox = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_inbox);
        emailList = new ArrayList<>();

        getDataEmail(offset);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerViewInbox.setHasFixedSize(true);

        //create new linear layout manager object
        linearLayoutManager = new LinearLayoutManager(mContext);

        //use a linear layout manager into recycler view
        recyclerViewInbox.setLayoutManager(linearLayoutManager);

        //create new email adapter object
        emailAdapter = new EmailAdapter(mContext, emailList, recyclerViewInbox);

        //use adapter into recycler view
        recyclerViewInbox.setAdapter(emailAdapter);

        //add divider into recycler view
        recyclerViewInbox.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        //animate recycler view
        animateRecyclerView(recyclerViewInbox);

        //recycler view load more data
        emailAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerViewInbox.post(new Runnable() {
                    @Override
                    public void run() {
                        //add null , so the adapter will check view_type and show progress bar at bottom
                        emailList.add(null);
                        emailAdapter.notifyItemInserted(emailList.size() - 1);
                        offset = emailAdapter.getItemCount();
                        loadMoreData(offset);
                    }
                });
            }
        });

        //swipe to refresh
        swipeRefreshInbox.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshInbox.setRefreshing(true);
                if (swipeRefreshInbox.isRefreshing()) {
                    emailList.clear();
                    getDataEmail(offset);
                }
            }
        });

        return view;
    }

    //get email data from JSON
    private void getDataEmail(final int offset) {
        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.URL_EMAIL_INBOX, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try {
                    JSONArray myArray = new JSONArray(response);
                    for (int i = 0; i < myArray.length(); i++) {
                        try {
                            JSONObject jsonObject = myArray.getJSONObject(i);
                            Email email = new Email();
                            email.setId(jsonObject.getInt(AppConfig.KEY_ID));
                            email.setSender(jsonObject.getString(AppConfig.KEY_SENDER));
                            email.setSubject(jsonObject.getString(AppConfig.KEY_SUBJECT));
                            email.setSnippet(jsonObject.getString(AppConfig.KEY_SNIPPET));
                            email.setTimestamp(jsonObject.getString(AppConfig.KEY_TIMESTAMP));
                            email.setImage(jsonObject.getString(AppConfig.KEY_IMAGE));
                            email.setStar(Boolean.parseBoolean(jsonObject.getString(AppConfig.KEY_STAR)));
                            email.setAttachment(Boolean.parseBoolean(jsonObject.getString(AppConfig.KEY_ATTACHMENT)));
                            emailList.add(email);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    emailAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBarInbox.setVisibility(View.GONE);
                if (swipeRefreshInbox.isRefreshing()) {
                    swipeRefreshInbox.setRefreshing(false);
                }
                animateRecyclerView(recyclerViewInbox);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBarInbox.setVisibility(View.GONE);
                Toast.makeText(mContext, "Gagal mengambil data email", Toast.LENGTH_SHORT).show();
                if (swipeRefreshInbox.isRefreshing()) {
                    swipeRefreshInbox.setRefreshing(false);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("offset", String.valueOf(offset));
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    //load more data from JSON
    private void loadMoreData(final int offset) {
        StringRequest request = new StringRequest(Request.Method.POST, AppConfig.URL_EMAIL_INBOX, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //remove progress bar if server was response
                Log.d("load more", response);
                emailList.remove(emailList.size() - 1);
                emailAdapter.notifyItemRemoved(emailList.size());
                try {
                    JSONArray myArray = new JSONArray(response);
                    for (int i = 0; i < myArray.length(); i++) {
                        try {
                            JSONObject jsonObject = myArray.getJSONObject(i);
                            Email email = new Email();
                            email.setId(jsonObject.getInt(AppConfig.KEY_ID));
                            email.setSender(jsonObject.getString(AppConfig.KEY_SENDER));
                            email.setSubject(jsonObject.getString(AppConfig.KEY_SUBJECT));
                            email.setSnippet(jsonObject.getString(AppConfig.KEY_SNIPPET));
                            email.setTimestamp(jsonObject.getString(AppConfig.KEY_TIMESTAMP));
                            email.setImage(jsonObject.getString(AppConfig.KEY_IMAGE));
                            email.setStar(Boolean.parseBoolean(jsonObject.getString(AppConfig.KEY_STAR)));
                            email.setAttachment(Boolean.parseBoolean(jsonObject.getString(AppConfig.KEY_ATTACHMENT)));
                            emailList.add(email);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    emailAdapter.notifyDataSetChanged();
                    emailAdapter.setLoaded();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(mContext, "Load more email failed", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("offset", String.valueOf(offset));
                return param;
            }
        };
        VolleySingleton.getInstance(mContext).addToRequestQueue(request);
    }

    //set animation fade in for recycler view
    private void animateRecyclerView(final RecyclerView rv) {
        rv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                rv.getViewTreeObserver().removeOnPreDrawListener(this);
                for (int i = 0; i < rv.getChildCount(); i++) {
                    View view = rv.getChildAt(i);
                    view.setAlpha(0.0f);
                    view.animate().alpha(1.0f).setDuration(300).setStartDelay(i * 50).start();
                }
                return true;
            }
        });
    }
}