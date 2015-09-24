package com.pmovil.soccer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.pmovil.soccer.entities.Date;
import com.pmovil.soccer.entities.Fixture;
import com.pmovil.soccer.entities.parser.JsonParsers;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DownloadUpcoming {

    public static final int NO_CONNECTION = 0;
    public static final int NO_DATA = 1;
    private static final int DOWNLOAD_FAILED = -1;
    private static final int DOWNLOAD_COMPLETE = 2;
    private static final String TAG = "DownloadUpcoming";
    // Sets the initial threadpool size to 8
    private static final int CORE_POOL_SIZE = 8;
    // Sets the maximum threadpool size to 8
    private static final int MAXIMUM_POOL_SIZE = 8;
    private static final int KEEP_ALIVE_TIME = 1;
    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT;
    private final BlockingQueue<Runnable> mDownloadWorkQueue;
    private final ThreadPoolExecutor mDownloadThreadPool;
    private int numberOfTask = 0;
    private Integer numberofTaskComplete = 0;
    private boolean error = false;
    private Handler mHandler;
    private DownloadOperationResonseHandler operationResonseHandler;

    static {
        // The time unit for "keep alive" is in seconds
        KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    }

    public DownloadUpcoming() {

		/*
         * Creates a work queue for the pool of Thread objects used for
		 * downloading, using a linked list queue that blocks when the queue is
		 * empty.
		 */
        mDownloadWorkQueue = new LinkedBlockingQueue<Runnable>();

		/*
		 * Creates a new pool of Thread objects for the download work queue
		 */
        mDownloadThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
                mDownloadWorkQueue);

		/*
		 * Instantiates a new anonymous Handler object and defines its
		 * handleMessage() method. The Handler *must* run on the UI thread,
		 * because it moves photo Bitmaps from the PhotoTask object to the View
		 * object. To force the Handler to run on the UI thread, it's defined as
		 * part of the PhotoManager constructor. The constructor is invoked when
		 * the class is first referenced, and that happens when the View invokes
		 * startDownload. Since the View runs on the UI Thread, so does the
		 * constructor and the Handler.
		 */
        mHandler = new Handler(Looper.getMainLooper()) {

			/*
			 * handleMessage() defines the operations to perform when the
			 * Handler receives a new Message to process.
			 */

            public void handleMessage(Message inputMessage) {
                // Gets the image task from the incoming Message object.

				/*
				 * Chooses the action to take, based on the incoming message
				 */
                switch (inputMessage.what) {

                    case DOWNLOAD_COMPLETE:
                        synchronized (numberofTaskComplete) {
                            numberofTaskComplete++;
                            if (numberOfTask == numberofTaskComplete) {
                                operationResonseHandler.onSuccess();
                            }
                        }

                        break;

                    case DOWNLOAD_FAILED:
                        if (!error) {
                            if (operationResonseHandler != null)
                                operationResonseHandler.onError(NO_CONNECTION);
                            // Attempts to re-use the Task object
                            cancelAll();
                            error = true;
                        }

                        break;
                    default:
                        // Otherwise, calls the super method
                        super.handleMessage(inputMessage);
                }
            }
        };

    }

    public void startDownloadDate(
            DownloadOperationResonseHandler downloadOperationResonseHandler,
            Context context) {
        this.operationResonseHandler = downloadOperationResonseHandler;
        List<Date> dates = com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(context)
                .getChampionshipByIdSelected().getDates();
        if (dates == null) {
            operationResonseHandler.onError(NO_DATA);
            return;
        }
        error = false;
        numberOfTask = dates.size();
        numberofTaskComplete = 0;
        for (int indexDates = 0; indexDates < dates.size(); indexDates++) {
            Date date = dates.get(indexDates);
            TaskDownload taskDownload = new TaskDownload(context,
                    com.pmovil.soccer.entities.parser.ResourcesMetegol.getInstance(context)
                            .getIdChampionshipSelected(), date.getId(),
                    indexDates
            );
            mDownloadThreadPool.execute(taskDownload);
        }

    }

    /**
     * Cancels all Threads in the ThreadPool
     */
    public void cancelAll() {

        //Log.e("ManagerDownloadOperation", "CancelAll");

		/*
		 * Creates an array of tasks that's the same size as the task work queue
		 */
        Runnable[] taskArray = new Runnable[mDownloadWorkQueue.size()];

        // Populates the array with the task objects in the queue
        mDownloadWorkQueue.toArray(taskArray);

        // Stores the array length in order to iterate over the array

		/*
		 * Locks on the singleton to ensure that other processes aren't mutating
		 * Threads, then iterates over the array of tasks and interrupts the
		 * task's current Thread.
		 */
        synchronized (DownloadUpcoming.class) {

            mDownloadThreadPool.purge();
            mDownloadThreadPool.shutdownNow();
            mDownloadWorkQueue.clear();

        }
    }

    public DownloadOperationResonseHandler getOperationResonseHandler() {
        return operationResonseHandler;
    }

    public void setOperationResonseHandler(
            DownloadOperationResonseHandler operationResonseHandler) {
        this.operationResonseHandler = operationResonseHandler;
    }

    public interface DownloadOperationResonseHandler {

        public void onSuccess();

        public void onError(int error);

    }

    private class TaskDownload implements Runnable {

        private int championshipId;
        private int dateId;
        private int indexDate;
        private Context context;

        public TaskDownload(Context context, int championshipId, int dateId,
                            int indexDate) {
            this.context = context;
            this.championshipId = championshipId;
            this.dateId = dateId;
            this.indexDate = indexDate;

        }

        public void run() {
            // ConnectionResponse response = connectionsWSFutebol
            // .getFixturePostSync(championshipId, dateId);

            String responseString = new String();
            try {
                if (context == null) {
                    Message completeMessage = mHandler
                            .obtainMessage(DOWNLOAD_FAILED);
                    completeMessage.sendToTarget();
                    return;
                }
                com.pmovil.soccer.entities.parser.ResourcesMetegol resourcesMetegol = com.pmovil.soccer.entities.parser.ResourcesMetegol
                        .getInstance(context);
                HttpPost httpPost = new HttpPost(
                        com.pmovil.soccer.net.ConnectionsWSMetegol.URL_BASE_POST);
                httpPost.setEntity(new UrlEncodedFormEntity(
                        resourcesMetegol.getConnWsFutebol()
                                .parametersForFixture(championshipId, dateId)
                ));
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    InputStream is = httpEntity.getContent();
                    responseString = IOUtils.toString(is);
                    is.close();
                }

                Fixture fixture = JsonParsers.ParserFixture(new JSONObject(
                        responseString));
                // CacheManager.getInstance(context).setResponseFixture(context,
                // responseString);
                Date dateCurrent = fixture.getDates();
                synchronized (resourcesMetegol) {
                    List<Date> dates = resourcesMetegol
                            .getChampionshipByIdSelected().getDates();
                    if (dates != null) {
                        dates.get(indexDate).setMatchs(dateCurrent.getMatchs());
                    }
                }

                Message completeMessage = mHandler
                        .obtainMessage(DOWNLOAD_COMPLETE);
                completeMessage.sendToTarget();
                //Log.i(TAG, "Add Matchs");
            } catch (Exception e) {

                e.printStackTrace();

                Message completeMessage = mHandler
                        .obtainMessage(DOWNLOAD_FAILED);
                completeMessage.sendToTarget();
            }
        }

    }

}
