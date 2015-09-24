package com.components;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Cache {
    // Sets the amount of time an idle thread will wait for a task before
    // terminating
    private static final int KEEP_ALIVE_TIME = 1;
    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    // Sets the maximum threadpool size to 8
    private static final int MAXIMUM_POOL_SIZE = 8;
    private static final String TAG = "CACHE";
    private static int NUMBER_OF_CORES = Runtime.getRuntime()
            .availableProcessors();
    private final BlockingQueue<Runnable> mDownloadWorkQueue;
    // A managed pool of background download threads
    private final ThreadPoolExecutor mDownloadThreadPool;
    private LruCache<Key, Bitmap> mBitmapCache;
    private Handler handler = new MyHandler();

    public Cache(int size) {
        /*
		 * Creates a work queue for the pool of Thread objects used for
		 * downloading, using a linked list queue that blocks when the queue is
		 * empty.
		 */
        mDownloadWorkQueue = new LinkedBlockingQueue<Runnable>();
		/*
		 * Creates a new pool of Thread objects for the download work queue
		 */
        mDownloadThreadPool = new ThreadPoolExecutor(NUMBER_OF_CORES,
                MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
                mDownloadWorkQueue);
        mBitmapCache = new LruCache<Key, Bitmap>(size) {
            @Override
            protected int sizeOf(Key key, Bitmap b) {
                // Assuming that one pixel contains four bytes.
                return b.getHeight() * b.getWidth() * 4;
            }
        };

    }

    private void addBitmapToCache(Key key, Bitmap bitmap) {
        if (key == null || bitmap == null)
            return;
        if (getBitmapFromCache(key) == null) {
            mBitmapCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromCache(Key key) {

        return mBitmapCache.get(key);
    }

    public void loadBitmap(ImageView imageView, String imageKey) {
        if (imageView == null || imageKey == null)
            return;
        Key key = new Key(imageKey, imageView.toString());
        final Bitmap bm = getBitmapFromCache(key);
        if (bm == null) {
            BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            task.url = imageKey;
            mDownloadThreadPool.execute(task);
            // task.execute(imageKey);
        } else
            imageView.setImageBitmap(bm);

    }

    public void clear() {
        mBitmapCache.evictAll();
    }

    private static class MyHandler extends Handler {

        public MyHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            if (!(msg.obj instanceof HandlerMsg))
                return;
            HandlerMsg handlerMsg = (HandlerMsg) msg.obj;
            if (handlerMsg.imageReference != null && handlerMsg.bitmap != null) {

                if (handlerMsg.imageReference.get() != null) {
                    handlerMsg.imageReference.get().setImageBitmap(
                            handlerMsg.bitmap);
                    try {
                        handlerMsg.imageReference.get().refreshDrawableState();
                    } catch (Exception e) {

                    }
                }
            }

        }
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> implements
            Runnable {

        private final WeakReference<ImageView> imageViewReference;
        private int width;
        private int height;
        private String url;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage
            // collected
            imageViewReference = new WeakReference<ImageView>(imageView);
            width = imageView.getLayoutParams().width;
            height = imageView.getLayoutParams().height;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;

            bitmap = ImageUtil.decodeSampledBitmap(params[0], width, height);
            if (bitmap == null) {
                bitmap = decodeSampledBitmpaFromURL(params[0], width, height);
            }
            Key key = new Key(params[0], imageViewReference.toString());
            addBitmapToCache(key, bitmap);
            return bitmap;
        }

        private Bitmap decodeSampledBitmpaFromURL(String Url, int mMaxWidth,
                                                  int mMaxHeight) {
            Bitmap bitmap;
            try {
                URL url = new URL(Url);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.connect();
                bitmap = BitmapFactory
                        .decodeStream(connection.getInputStream());

                if (bitmap != null) {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();

                    if (width >= mMaxWidth || height >= mMaxHeight) {
                        while (true) {
                            if (width <= mMaxWidth || height <= mMaxHeight) {
                                break;
                            }
                            width /= 2;
                            height /= 2;
                        }
                        bitmap = Bitmap.createScaledBitmap(bitmap, width,
                                height, false);
                    }
                    connection.disconnect();
                    return bitmap;
                }
                return null;
            } catch (IOException e) {
                if (e != null) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {

                if (imageViewReference.get() != null) {
                    imageViewReference.get().setImageBitmap(bitmap);
                    try {
                        imageViewReference.get().refreshDrawableState();
                    } catch (Exception e) {

                    }
                }
            }
        }

        public void run() {
            android.os.Process
                    .setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            ////Log.i(TAG, "path= " + url);
            Bitmap bitmap = null;
            bitmap = ImageUtil.decodeSampledBitmap(url, width, height);
            if (bitmap == null) {
                bitmap = decodeSampledBitmpaFromURL(url, width, height);
            }
            Key key = new Key(url, imageViewReference.toString());
            addBitmapToCache(key, bitmap);
            HandlerMsg msg = new HandlerMsg();
            msg.bitmap = bitmap;
            msg.imageReference = imageViewReference;
            Message completeMessage = handler.obtainMessage(0, msg);
            completeMessage.sendToTarget();
        }
    }

    private class HandlerMsg {
        private WeakReference<ImageView> imageReference;
        private Bitmap bitmap;
    }

    private class Key {

        private String key;
        private String imageView;

        private Key(String key, String imageView) {
            super();
            this.key = key;
            this.imageView = imageView;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Key other = (Key) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
//			if (imageView == null) {
//				if (other.imageView != null)
//					return false;
//			} else if (!imageView.equals(other.imageView))
//				return false;
            if (key == null) {
                if (other.key != null)
                    return false;
            } else if (!key.equals(other.key))
                return false;
            return true;
        }

        private Cache getOuterType() {
            return Cache.this;
        }

    }
}
