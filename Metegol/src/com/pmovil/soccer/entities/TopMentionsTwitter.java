package com.pmovil.soccer.entities;

public class TopMentionsTwitter extends Data {

    private String fname = new String();
    private String lname = new String();
    private String hashtag = new String();
    private String twitterUser = new String();
    private String image = new String();
    private String mentions = new String();
    private String mentionsMax = new String();

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getTwitterUser() {
        return twitterUser;
    }

    public void setTwitterUser(String twitter_user) {
        this.twitterUser = twitter_user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMentions() {
        return mentions;
    }

    public void setMentions(String mentions) {
        this.mentions = mentions;
    }

    public String getMentionsMax() {
        return mentionsMax;
    }

    public void setMentionsMax(String mentionsMax) {
        this.mentionsMax = mentionsMax;
    }
}
