package amjath.purosh.zencode.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import amjath.purosh.zencode.CommentsActivity;
import amjath.purosh.zencode.R;
import amjath.purosh.zencode.bean.Issues;
import amjath.purosh.zencode.utils.ImageLoader;
import amjath.purosh.zencode.utils.Utils;

/**
 * Created by Techzarinfo on 5/30/2017.
 */

public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.MyViewHolder> {

    public ImageLoader imageLoader;
    private Context c;
    private List<Issues> issuesList = new ArrayList<>();

    public IssuesAdapter(Context c, List<Issues> issuesList) {
        this.c = c;
        this.issuesList = issuesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.issue_list, parent, false);

        imageLoader = new ImageLoader(c);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Issues issue = issuesList.get(position);
        holder.txtIssueTitle.setText(issue.getIssueTitle());
        holder.txtUsername.setText(issue.getUsername());
        holder.txtUpdatedDate.setText(Utils.getDateFromMonth(issue.getUpdatedDate()));

        String description = issue.getDescription();
        if (description.length() > 200) {
            description = description.substring(0, 200);
        }

        holder.txtDescription.setText(description);
        holder.txtIssueId.setText(issue.getIssueId());
        holder.txtUserId.setText(issue.getUserId());
        holder.txtCommentsUrl.setText(issue.getCommentsURL());
        holder.txtCreatedDate.setText(issue.getCreatedDate());

        imageLoader.DisplayImage(issue.getAvaratarURL(), holder.imgAvatar);

        holder.lnrView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(c, CommentsActivity.class);
                i.putExtra("Issue", issue);
                c.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return issuesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout lnrView;
        public ImageView imgAvatar;
        public TextView txtIssueTitle, txtUsername, txtUpdatedDate, txtDescription, txtIssueId, txtUserId, txtCommentsUrl, txtCreatedDate;

        public MyViewHolder(View view) {
            super(view);
            lnrView = (LinearLayout)view.findViewById(R.id.lnrView);
            imgAvatar = (ImageView) view.findViewById(R.id.imgAvatar);
            txtIssueTitle = (TextView) view.findViewById(R.id.txtIssueTitle);
            txtUsername = (TextView) view.findViewById(R.id.txtUsername);
            txtUpdatedDate = (TextView) view.findViewById(R.id.txtUpdatedDate);
            txtDescription = (TextView) view.findViewById(R.id.txtDescription);
            txtIssueId = (TextView) view.findViewById(R.id.txtIssueId);
            txtUserId = (TextView) view.findViewById(R.id.txtUserId);
            txtCommentsUrl = (TextView) view.findViewById(R.id.txtCommentsUrl);
            txtCreatedDate = (TextView) view.findViewById(R.id.txtCreatedDate);
        }
    }
}
