package com.pmovil.soccer.entities;

public class Comment extends Data {

    private int minByMinId = -1;
    private String userName = new String();
    private String user_photo_url = new String();
    private String user_social_network = new String();
    private String comment = new String();
    private String comment_date = new String();
    private String comment_date_unix_timestamp = new String();

    public int getMinByMinId() {
        return minByMinId;
    }

    public void setMinByMinId(int minByMinId) {
        this.minByMinId = minByMinId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUser_photo_url() {
        return user_photo_url;
    }

    public void setUser_photo_url(String user_photo_url) {
        this.user_photo_url = user_photo_url;
    }

    public String getUser_social_network() {
        return user_social_network;
    }

    public void setUser_social_network(String user_social_network) {
        this.user_social_network = user_social_network;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public String getComment_date_unix_timestamp() {
        return comment_date_unix_timestamp;
    }

    public void setComment_date_unix_timestamp(
            String comment_date_unix_timestamp) {
        this.comment_date_unix_timestamp = comment_date_unix_timestamp;
    }

}
