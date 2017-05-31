package amjath.purosh.zencode.bean;

import java.io.Serializable;

/**
 * Created by Techzarinfo on 5/30/2017.
 */

public class Comments implements Serializable {

    private static final long serialVersionUID = 2L;

    String userId, userName, avatarURL, id, createdAt, updatedAt, body;

    public Comments(String userId, String userName, String avatarURL, String id, String createdAt, String updatedAt, String body) {

        this.userId = userId;
        this.userName = userName;
        this.avatarURL = avatarURL;
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.body = body;

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
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getCreatedDate() {
        return createdAt;
    }

    public String getAvatarURL() {
        return avatarURL;
    }
}
