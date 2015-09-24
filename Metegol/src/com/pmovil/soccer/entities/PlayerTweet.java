package com.pmovil.soccer.entities;

public class PlayerTweet {

    private String tweetId = new String();

    private String date = new String();
    private String body = new String();
    private String userId = new String();
    private String username = new String();
    private String playerMentions = new String();
    private String pictureUrl = new String();

    public String getPlayerMentions() {
        return playerMentions;
    }

    public void setPlayerMentions(String playerMentions) {
        this.playerMentions = playerMentions;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }


}
