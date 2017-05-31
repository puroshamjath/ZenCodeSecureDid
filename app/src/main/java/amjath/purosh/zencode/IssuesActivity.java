package amjath.purosh.zencode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import amjath.purosh.zencode.adapter.IssuesAdapter;
import amjath.purosh.zencode.bean.Comments;
import amjath.purosh.zencode.bean.Issues;
import amjath.purosh.zencode.utils.MySingleton;
import amjath.purosh.zencode.utils.Utils;


public class IssuesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private List<Issues> issuesList = new ArrayList<>();
    private static HashMap<String, ArrayList<Comments>> commentsHashmap = new HashMap<>();

    private Issues issues;
    private IssuesAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initiatingViews()
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new IssuesAdapter(IssuesActivity.this, issuesList);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (Utils.isInternetAvailable(IssuesActivity.this) == true) {
                                            swipeRefreshLayout.setRefreshing(true);
                                            syncData();
                                        } else {

                                            Utils.showMessageDialog(IssuesActivity.this, "Internet Unavailable");

                                            loadCache();

                                        }
                                    }
                                }
        );
    }

    private void loadCache() {

        FileInputStream fis;
        try {
            fis = openFileInput("issueList");
            ObjectInputStream oi = new ObjectInputStream(fis);
            issuesList = (List<Issues>) oi.readObject();
            oi.close();

            adapter = new IssuesAdapter(IssuesActivity.this, issuesList);
            recyclerView.setAdapter(adapter);

        } catch (FileNotFoundException e) {
            Log.e("InternalStorage", e.getMessage());
        } catch (IOException e) {
            Log.e("InternalStorage", e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRefresh() {
        if (Utils.isInternetAvailable(IssuesActivity.this) == true) {
            swipeRefreshLayout.setRefreshing(true);
            syncData();
        } else {
            loadCache();
        }
    }

    private void syncData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Utils.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                swipeRefreshLayout.setRefreshing(false);

                Log.e("Response", response);

                try {

                    issuesList.clear();

                    JSONArray array = new JSONArray(response);

                    if (array.length() > 0) {

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject jobject = array.getJSONObject(i);
                            String issueId = jobject.getString("id");
                            String title = jobject.getString("title");
                            String createdAt = jobject.getString("created_at");
                            String updatedAt = jobject.getString("updated_at");
                            String body = jobject.getString("body");
                            String commentsUrl = jobject.getString("comments_url");

                            JSONObject userObject = jobject.getJSONObject("user");
                            String userId = userObject.getString("id");
                            String userName = userObject.getString("login");
                            String avatarUrl = userObject.getString("avatar_url");

                            issues = new Issues(userId, userName, avatarUrl, issueId, title, createdAt, updatedAt, body, commentsUrl);
                            issuesList.add(issues);
                        }

                        adapter.notifyDataSetChanged();

                    } else {
                        Utils.showMessageDialog(IssuesActivity.this, "No Issues Found");
                    }


                } catch (Exception e) {
                    Utils.showMessageDialog(IssuesActivity.this, "Failed\nTry again later");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
                Log.e("Response", error.toString());
                Utils.showMessageDialog(IssuesActivity.this, "Failed\nTry again later");
            }
        });
        MySingleton.getmInstance(IssuesActivity.this).addToRequest(stringRequest);

    }

    public static void insertComments(Context c, String issueId, List<Comments> commentsList) {
        commentsHashmap.put(issueId, (ArrayList<Comments>) commentsList);

        //writing comments
        try {
            FileOutputStream fos = c.openFileOutput("commentsHashMap", Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(commentsHashmap);
            of.flush();
            of.close();
            fos.close();
        } catch (Exception e) {
            Log.e("InternalStorage", e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        writeIssue();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        writeIssue();
    }

    @Override
    protected void onStop() {
        super.onStop();
        writeIssue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        writeIssue();
    }

    private void writeIssue() {

        //writing issue
        try {
            FileOutputStream fos = openFileOutput("issueList", Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(issuesList);
            of.flush();
            of.close();
            fos.close();
        } catch (Exception e) {
            Log.e("InternalStorage", e.getMessage());
        }

    }

}
