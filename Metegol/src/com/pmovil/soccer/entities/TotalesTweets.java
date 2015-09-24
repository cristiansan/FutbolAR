package com.pmovil.soccer.entities;

public class TotalesTweets {

    private TotalTweets tweets = new TotalTweets();
    private TotalTweets retweets = new TotalTweets();
    private TotalTweets total = new TotalTweets();

    public TotalTweets getTweets() {
        return tweets;
    }

    public void setTweets(TotalTweets tweets) {
        this.tweets = tweets;
    }

    public TotalTweets getRetweets() {
        return retweets;
    }

    public void setRetweets(TotalTweets retweets) {
        this.retweets = retweets;
    }

    public TotalTweets getTotal() {
        return total;
    }

    public void setTotal(TotalTweets total) {
        this.total = total;
    }

}
