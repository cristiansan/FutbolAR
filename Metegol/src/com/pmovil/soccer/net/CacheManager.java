package com.pmovil.soccer.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.net.URL;

public class CacheManager implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final String FILENAME = "cache.futband";

    private static CacheManager INSTANCE;

    private String responsePosition, responseScorers, responseGame,
            responseMinByMin, responseNews, responseVideo, responsefootBall,
            responseStatistic, responseFixture, responseChampionship;

    private CacheManager() {
        responsePosition = responseScorers = responseGame = responseMinByMin = null;
        responseNews = responseVideo = responsefootBall = responseStatistic = null;
        responseFixture = responseChampionship = null;
    }

    public static CacheManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (CacheManager.class) {
                if (INSTANCE == null) {
                    // INSTANCE = new CacheManager(context);
                    INSTANCE = loadCacheFromStorage(context);
                }
            }
        }
        return INSTANCE;
    }

    public static CacheManager loadCacheFromStorage(Context context) {
        CacheManager simpleClass = null;
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            simpleClass = (CacheManager) is.readObject();
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (simpleClass != null)
            return simpleClass;

        return new CacheManager();
    }

    // public Context getContext() {
    // return context;
    // }

    public static void saveBitmapToInternalStorage(Context context,
                                                   Bitmap bitmap, String imageNameWithExtension) {
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(
                    imageNameWithExtension.substring(0,
                            imageNameWithExtension.lastIndexOf('.'))
                            + ".jpeg", Context.MODE_PRIVATE
            );
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap laodBitmapFromInternalStorage(Context context,
                                                       String imageNameWithExtension) {
        FileInputStream fis;
        try {
            fis = context.openFileInput(imageNameWithExtension.substring(0,
                    imageNameWithExtension.lastIndexOf('.')) + ".jpeg");
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            fis.close();
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFileName(URL extUrl) {
        // URL:
        // "http://photosaaaaa.net/photos-ak-snc1/v315/224/13/659629384/s659629384_752969_4472.jpg"
        String filename = "";
        // PATH:
        // /photos-ak-snc1/v315/224/13/659629384/s659629384_752969_4472.jpg
        String path = extUrl.getPath();
        // Checks for both forward and/or backslash
        // NOTE:**While backslashes are not supported in URL's
        // most browsers will autoreplace them with forward slashes
        // So technically if you're parsing an html page you could run into
        // a backslash , so i'm accounting for them here;
        String[] pathContents = path.split("[\\\\/]");
        if (pathContents != null) {
            int pathContentsLength = pathContents.length;
            System.out.println("Path Contents Length: " + pathContentsLength);
            for (int i = 0; i < pathContents.length; i++) {
                System.out.println("Path " + i + ": " + pathContents[i]);
            }
            // lastPart: s659629384_752969_4472.jpg
            String lastPart = pathContents[pathContentsLength - 1];
            String[] lastPartContents = lastPart.split("\\.");
            if (lastPartContents != null && lastPartContents.length > 1) {
                int lastPartContentLength = lastPartContents.length;
                System.out
                        .println("Last Part Length: " + lastPartContentLength);
                // filenames can contain . , so we assume everything before
                // the last . is the name, everything after the last . is the
                // extension
                String name = "";
                for (int i = 0; i < lastPartContentLength; i++) {
                    System.out.println("Last Part " + i + ": "
                            + lastPartContents[i]);
                    if (i < (lastPartContents.length - 1)) {
                        name += lastPartContents[i];
                        if (i < (lastPartContentLength - 2)) {
                            name += ".";
                        }
                    }
                }
                String extension = lastPartContents[lastPartContentLength - 1];
                filename = name + "." + extension;
                System.out.println("Name: " + name);
                System.out.println("Extension: " + extension);
                System.out.println("Filename: " + filename);
            }
        }
        return filename;
    }

    public boolean saveCacheToStorage(Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(INSTANCE);
            os.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public JSONObject getCacheJSON(CacheType type) {
        switch (type) {
            case CACHE_POSITION:
                return getJSONFromString(responsePosition);
            case CACHE_SCORERS:
                return getJSONFromString(responseScorers);
            case CACHE_GAME:
                return getJSONFromString(responseGame);
            case CACHE_MIN_BY_MIN:
                return getJSONFromString(responseMinByMin);
            case CACHE_FOOTBALL:
                return getJSONFromString(responsefootBall);
            case CACHE_NEWS:
                return getJSONFromString(responseNews);
            case CACHE_STATISTIC:
                return getJSONFromString(responseStatistic);
            case CACHE_VIDEO:
                return getJSONFromString(responseVideo);
            case CACHE_CHAMPIONSHIP:
                return getJSONFromString(responseChampionship);
            case CACHE_FIXTURE:
                return getJSONFromString(responseFixture);
            default:
                break;
        }
        return null;
    }

    private JSONObject getJSONFromString(String jsonString) {
        try {
            //Log.d("CACHE", jsonString);
            JSONObject json = new JSONObject(jsonString);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setResponsePosition(Context context, String responsePosition) {
        this.responsePosition = responsePosition;
        saveCacheToStorage(context);
    }

    public void setResponseScorers(Context context, String responseScorers) {
        this.responseScorers = responseScorers;
        saveCacheToStorage(context);
    }

    public void setResponseGame(Context context, String responseGame) {
        this.responseGame = responseGame;
        saveCacheToStorage(context);
    }

    public void setResponseMinByMin(Context context, String responseMinByMin) {
        this.responseMinByMin = responseMinByMin;
        saveCacheToStorage(context);
    }

    public void setResponseNews(Context context, String responseNews) {
        this.responseNews = responseNews;
        saveCacheToStorage(context);

    }

    public void setResponseVideo(Context context, String responseVideo) {
        this.responseVideo = responseVideo;
        saveCacheToStorage(context);

    }

    public void setResponsefootBall(Context context, String responsefootBall) {
        this.responsefootBall = responsefootBall;
        saveCacheToStorage(context);

    }

    public void setResponseStatistic(Context context, String responseStatistic) {
        this.responseStatistic = responseStatistic;
        saveCacheToStorage(context);

    }

    public void setResponseFixture(Context context, String responseFixture) {
        this.responseFixture = responseFixture;
        saveCacheToStorage(context);

    }

    public void setResponseChampionship(Context context,
                                        String responseChampionship) {
        this.responseChampionship = responseChampionship;
        saveCacheToStorage(context);

    }

    public enum CacheType {
        CACHE_POSITION, CACHE_SCORERS, CACHE_GAME, CACHE_MIN_BY_MIN, CACHE_NEWS, CACHE_VIDEO, CACHE_FOOTBALL, CACHE_STATISTIC, CACHE_FIXTURE, CACHE_CHAMPIONSHIP
    }

}
