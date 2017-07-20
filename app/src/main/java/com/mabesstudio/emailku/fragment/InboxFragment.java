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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;
import com.mabesstudio.emailku.R;
import com.mabesstudio.emailku.adapter.EmailAdapter;
import com.mabesstudio.emailku.app.AppConfig;
import com.mabesstudio.emailku.app.VolleySingleton;
import com.mabesstudio.emailku.entity.InboxMail;
import com.mabesstudio.emailku.setup.DividerItemDecoration;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends Fragment {

    private RecyclerView rv_inbox;
    private SwipeRefreshLayout refreshInbox;
    private ProgressBar progressBarInbox;

    public InboxFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        rv_inbox = (RecyclerView) view.findViewById(R.id.rv_inbox);
        progressBarInbox = (ProgressBar) view.findViewById(R.id.pb_inbox);
        refreshInbox = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_inbox);

        getDataEmail();

        refreshInbox.setRefreshing(true);
        refreshInbox.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("fragment_inbox");
                getActivity().getSupportFragmentManager().beginTransaction().attach(fragment).detach(fragment).commit();
                if (refreshInbox.isRefreshing()){
                    refreshInbox.setRefreshing(false);
                }
            }
        });
        return view;
    }

    private void getDataEmail() {
        StringRequest request = new StringRequest(Request.Method.GET, AppConfig.URL_EMAIL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBarInbox.setVisibility(View.GONE);
                refreshInbox.setRefreshing(false);
                Log.d("response", response);
                ArrayList<InboxMail> inboxMailArrayList = new JsonConverter<InboxMail>().toArrayList(response, InboxMail.class);
                EmailAdapter emailAdapter = new EmailAdapter(inboxMailArrayList, getActivity());
                rv_inbox.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                rv_inbox.setHasFixedSize(true);
                rv_inbox.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
                rv_inbox.setAdapter(emailAdapter);
                rv_inbox.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        rv_inbox.getViewTreeObserver().removeOnPreDrawListener(this);
                        for (int i = 0; i < rv_inbox.getChildCount(); i++) {
                            View view = rv_inbox.getChildAt(i);
                            view.setAlpha(0.0f);
                            view.animate().alpha(1.0f).setDuration(300).setStartDelay(i * 50).start();
                        }
                        return true;
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), "Gagal mengambil data email", Toast.LENGTH_SHORT).show();
                progressBarInbox.setVisibility(View.GONE);
                refreshInbox.setRefreshing(false);
            }
        });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
}