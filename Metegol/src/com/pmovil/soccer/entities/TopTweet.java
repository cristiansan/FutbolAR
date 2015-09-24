package com.pmovil.soccer.entities;

public class TopTweet extends Data {

    private String type = new String();
    private String name = new String();
    private String hashtag = new String();
    private String mentions = new String();
    private String mentionsMax = new String();
    private String urlImage = new String();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
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

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

}