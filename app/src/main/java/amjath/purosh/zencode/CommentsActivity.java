package amjath.purosh.zencode;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import amjath.purosh.zencode.adapter.CommentsAdapter;
import amjath.purosh.zencode.bean.Comments;
import amjath.purosh.zencode.bean.Issues;
import amjath.purosh.zencode.utils.MySingleton;
import amjath.purosh.zencode.utils.Utils;

public class CommentsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private HashMap<String, ArrayList<Comments>> commentsHashmap = new HashMap<>();

    private Comments comments;
    private List<Comments> commentsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommentsAdapter adapter;

    private TextView txtTitle;

    private Issues issue;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_comments);

        issue = (Issues) getIntent().getSerializableExtra("Issue");

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(issue.getIssueTitle());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CommentsAdapter(CommentsActivity.this, commentsList);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (Utils.isInternetAvailable(CommentsActivity.this) == true) {
                                            swipeRefreshLayout.setRefreshing(true);
                                            syncComments();
                                        } else {
                                            loadCache();
                                            Utils.showMessageDialog(CommentsActivity.this, "Internet Unavailable");
                                        }
                                    }
                                }
        );

    }

    private void loadCache() {

        FileInputStream fis;
        try {

            fis = openFileInput("commentsHashMap");
            ObjectInputStream oi = new ObjectInputStream(fis);
            commentsHashmap = (HashMap<String, ArrayList<Comments>>) oi.readObject();

            if (commentsHashmap.containsKey(issue.getIssueId())) {
                commentsList = commentsHashmap.get(issue.getIssueId());
            }

            oi.close();

            adapter = new CommentsAdapter(CommentsActivity.this, commentsList);
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
        if (Utils.isInternetAvailable(CommentsActivity.this) == true) {
            swipeRefreshLayout.setRefreshing(true);
            syncComments();
        } else {
            Utils.showMessageDialog(CommentsActivity.this, "Internet Unavailable");
        }
    }

    private void syncComments() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, issue.getCommentsURL(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                swipeRefreshLayout.setRefreshing(false);

                Log.e("Response", response);

                try {

                    commentsList.clear();

                    comments = new Comments(issue.getUserId(), issue.getUsername(), issue.getAvaratarURL(), issue.getIssueId(),
                            issue.getCreatedDate(), issue.getUpdatedDate(), issue.getDescription());
                    commentsList.add(comments);

                    JSONArray commentsArray = new JSONArray(response);

                    if (commentsArray.length() > 0) {

                        for (int i = 0; i < commentsArray.length(); i++) {

                            JSONObject commentObject = commentsArray.getJSONObject(i);

                            String id = commentObject.getString("id");
                            String createdAt = commentObject.getString("created_at");
                            String updatedAt = commentObject.getString("updated_at");
                            String body = commentObject.getString("body");

                            JSONObject userObject = commentObject.getJSONObject("user");

                            String userName = userObject.getString("login");
                            String userId = userObject.getString("id");
                            String avatarURL = userObject.getString("avatar_url");

                            comments = new Comments(userId, userName, avatarURL, id, createdAt, updatedAt, body);
                            commentsList.add(comments);

                        }

                        adapter.notifyDataSetChanged();

                        IssuesActivity.insertComments(CommentsActivity.this, issue.getIssueId(), commentsList);

                    } else {
                        Utils.showMessageDialog(CommentsActivity.this, "No Comments Found");
                    }


                } catch (Exception e) {
                    Utils.showMessageDialog(CommentsActivity.this, "Failed\n Try again later");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
                Log.e("Response", error.toString());
                Utils.showMessageDialog(CommentsActivity.this, "Failed\nTry again later");
            }

        });
        MySingleton.getmInstance(CommentsActivity.this).addToRequest(stringRequest);

    }

    @Override
    public void onBackPressed() {

        finish();

    }

}
