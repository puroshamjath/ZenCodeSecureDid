package amjath.purosh.zencode.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import amjath.purosh.zencode.R;
import amjath.purosh.zencode.bean.Comments;
import amjath.purosh.zencode.utils.ImageLoader;
import amjath.purosh.zencode.utils.Utils;

/**
 * Created by Techzarinfo on 5/30/2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    public ImageLoader imageLoader;
    //private ImageLoader imageLoader;
    private List<Comments> commentsList = new ArrayList<>();
    private Context c;

    public CommentsAdapter(Context c, List<Comments> comments) {
        this.c = c;
        this.commentsList = comments;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.issue_list, parent, false);

        imageLoader = new ImageLoader(c);


        return new CommentsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Comments comments = commentsList.get(position);
        holder.txtIssueTitle.setVisibility(View.GONE);
        holder.txtUsername.setText(comments.getUsername());
        holder.txtUpdatedDate.setVisibility(View.GONE);

        String description = comments.getDescription();
        if (description.length() > 1) {
            description.substring(0, Math.min(comments.getDescription().length(), 20));
        }

        holder.txtDescription.setText(description);
        holder.txtIssueId.setText(comments.getIssueId());
        holder.txtUserId.setText(comments.getUserId());
        holder.txtCreatedDate.setText(comments.getCreatedDate());

        imageLoader.DisplayImage(comments.getAvatarURL(), holder.imgAvatar);

    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout lnrView;
        public ImageView imgAvatar;
        public TextView txtIssueTitle, txtUsername, txtUpdatedDate, txtDescription, txtIssueId, txtUserId, txtCommentsUrl, txtCreatedDate;

        public MyViewHolder(View view) {
            super(view);
            lnrView = (LinearLayout) view.findViewById(R.id.lnrView);
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
