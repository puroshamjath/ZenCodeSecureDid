package amjath.purosh.zencode.bean;

import java.io.Serializable;

/**
 * Created by Techzarinfo on 5/30/2017.
 */

public class Issues implements Serializable {

    private static final long serialVersionUID = 1L;

    String userId, userName, avatarUrl, issueId, title, createdAt, updatedAt, body, commentsUrl;

    public Issues(String userId, String userName, String avatarUrl, String issueId, String title,
                  String createdAt, String updatedAt, String body, String commentsUrl) {

        this.userId = userId;
        this.userName = userName;
        this.avatarUrl = avatarUrl;
        this.issueId = issueId;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.body = body;
        this.commentsUrl = commentsUrl;

    }

    public String getIssueTitle() {
        return title;
    }

    public String getUsername() {
        return userName;
    }

    public String getUpdatedDate() {
        return updatedAt;
    }

    public String getDescription() {
        return body;
    }

    public String getIssueId() {
        return issueId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCommentsURL() {
        return commentsUrl;
    }

    public String getCreatedDate() {
        return createdAt;
    }

    public String getAvaratarURL() {
        return avatarUrl;
    }
}
