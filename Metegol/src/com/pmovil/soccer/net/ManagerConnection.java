package com.pmovil.soccer.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

public class ManagerConnection {

    private static final String TAG = "Connection";
    // Sets the amount of time an idle thread will wait for a task before
    // terminating
    private static final int KEEP_ALIVE_TIME = 1;
    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    // Sets the maximum threadpool size to 8
    private static final int MAXIMUM_POOL_SIZE = 8;
    /**
     * NOTE: This is the number of total available cores. On current versions of
     * Android, with devices that use plug-and-play cores, this will return less
     * than the total number of cores. The total number of cores is not
     * available in current Android implementations.
     */
    private static int NUMBER_OF_CORES = Runtime.getRuntime()
            .availableProcessors();
    private volatile static ManagerConnection INSTANCE = null;
    private final BlockingQueue<Runnable> mDownloadWorkQueue;
    // A managed pool of background download threads
    private final ThreadPoolExecutor mDownloadThreadPool;
    private HttpGet httpGet;
    private HttpHead httpHead;
    private HttpPost httpPost;
    private Map<String, String> headers;
    private List<NameValuePair> nameValuePairs = null;
    private int connectionTimeout = -1;
    private int socketTimeout = -1;
    private String host = AuthScope.ANY_HOST;
    private int port = AuthScope.ANY_PORT;
    private CredentialsProvider credentialsProvider = null;
    private AsyncConnection asyncConnection;
    private Handler handler = new MyHandler();
    private boolean acceptGzip = false;

    private ManagerConnection() {
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
    }

    public static ManagerConnection getInstance() {
        createInstance();
        return INSTANCE;
    }

    private static void createInstance() {
        if (INSTANCE == null)
            synchronized (ManagerConnection.class) {
                if (INSTANCE == null)
                    INSTANCE = new ManagerConnection();
            }
    }

    public static String getMacAdress(Context context) {
        WifiManager wifiMan = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        String macAddr = wifiInf.getMacAddress();
        return macAddr;
    }

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            //Log.i("checkInternetConnection", "Internet Connection Present");
            return true;
        } else {
            //Log.e("checkInternetConnection", "No Internet connection");
            return false;
        }
    }

    public void headAsyn(String url,
                         ConnectionHandlerResponseHeaders connectionHandlerResponseHeaders) {
        asyncConnection = new AsyncConnection();
        asyncConnection.connectionHandlerResponseHeaders = connectionHandlerResponseHeaders;
        initHead(url);
    }

    public void getAsyn(String url,
                        ConnectionHandlerResponseFile connectionResponseHandlerFile) {
        asyncConnection = new AsyncConnection();
        asyncConnection.connectionHandlerResponseFile = connectionResponseHandlerFile;
        initGet(url);
    }

    public void getAsyn(String url, ConnectionHandlerResponse connectionResponse) {
        asyncConnection = new AsyncConnection();
        asyncConnection.connectionHandlerResponse = connectionResponse;
        initGet(url);
    }

    public void getAsyn(String url,
                        ConnectionHandlerResponseBody connectionResponseHandler) {
        asyncConnection = new AsyncConnection();
        asyncConnection.connectionHandlerResponseBody = connectionResponseHandler;
        initGet(url);
    }

    public void postAsyn(String url,
                         ConnectionHandlerResponseFile connectionResponseHandlerFile) {
        asyncConnection = new AsyncConnection();
        asyncConnection.connectionHandlerResponseFile = connectionResponseHandlerFile;
        initPost(url);
    }

    public void postAsyn(String url,
                         ConnectionHandlerResponse connectionResponse) {
        asyncConnection = new AsyncConnection();
        asyncConnection.connectionHandlerResponse = connectionResponse;
        initPost(url);
    }

    public void postAsyn(String url,
                         ConnectionHandlerResponseBody connectionResponseHandler) {
        asyncConnection = new AsyncConnection();
        asyncConnection.connectionHandlerResponseBody = connectionResponseHandler;
        initPost(url);
    }

    public ConnectionResponse get(String url) {
        url = parserUrl(url);
        initHttpClient();
        return startSyncConnection(new HttpGet(url));
    }

    public ConnectionResponse get(URI url) {
        initHttpClient();
        return startSyncConnection(new HttpGet(url));
    }

    public ConnectionResponse post(String url) {
        url = parserUrl(url);
        initHttpClient();
        HttpPost httpPost = new HttpPost(url);
        addParameters(httpPost);
        return startSyncConnection(httpPost);
    }

    public ConnectionResponse post(URI url) {
        initHttpClient();
        HttpPost httpPost = new HttpPost(url);
        addParameters(httpPost);
        return startSyncConnection(httpPost);
    }

    public ConnectionResponse head(URI url) {
        initHttpClient();
        return startSyncConnection(new HttpHead(url));
    }

    public ConnectionResponse head(String url) {
        url = parserUrl(url);
        initHttpClient();
        return startSyncConnection(new HttpHead(url));
    }

    private void initHead(String url) {
        url = parserUrl(url);
        //Log.i(TAG, "url=" + url);
        httpHead = new HttpHead(url);
        startAsynConnection(httpHead);

    }

    private void initPost(String url) {
        url = parserUrl(url);
        //Log.i(TAG, "url=" + url);
        httpPost = new HttpPost(url);
        addParameters(httpPost);
        startAsynConnection(httpPost);
    }

    private void initGet(String url) {
        url = parserUrl(url);
        //Log.i(TAG, "url=" + url);
        httpGet = new HttpGet(url);
        startAsynConnection(httpGet);
    }

    private void addParameters(HttpPost httpPost) {
        if (httpPost != null && nameValuePairs != null)
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        if (nameValuePairs != null)
            nameValuePairs.clear();
        nameValuePairs = null;
    }

    private String parserUrl(String url) {
        if (url == null)
            return "";
        return url.replace(" ", "%20");
    }

    private ConnectionResponse startSyncConnection(HttpRequestBase requestBase) {
        ConnectionResponse connectionResponse = new ConnectionResponse();
        DefaultHttpClient httpClient = initHttpClient();
        includeHeaders(requestBase);

        if (acceptGzip) {
            requestBase.addHeader("Accept-Encoding", "gzip");

        }
        try {
            HttpResponse httpResponse = httpClient.execute(requestBase);
            connectionResponse.url = requestBase.getURI().toString();
            connectionResponse.statusCode = httpResponse.getStatusLine()
                    .getStatusCode();

            HashMap<String, String> headers = new HashMap<String, String>();
            for (Header header : httpResponse.getAllHeaders()) {
                headers.put(header.getName(), header.getValue());

            }
            connectionResponse.headers = headers;
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                InputStream is = httpEntity.getContent();


                String contentEncoding = headers.get("Content-Encoding");
                if (contentEncoding != null
                        && contentEncoding.equalsIgnoreCase("gzip")) {
                    is = new GZIPInputStream(is);
                }
                connectionResponse.bodyResponse = IOUtils.toByteArray(is);
                is.close();
            }
            connectionResponse.error = null;
            //Log.i(TAG, connectionResponse.url);
        } catch (ClientProtocolException e) {
            connectionResponse.error = "";
            e.printStackTrace();
            if (e.getMessage() != null)
                connectionResponse.error = e.getMessage();
        } catch (IOException e) {
            connectionResponse.error = "";
            if (e.getMessage() != null)
                connectionResponse.error = e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            connectionResponse.error = "";
            e.printStackTrace();
            if (e.getMessage() != null)

                connectionResponse.error = e.getMessage();

        }
        return connectionResponse;
    }

    private void startAsynConnection(HttpRequestBase requestBase) {
        includeHeaders(requestBase);
        asyncConnection.request = requestBase;
        mDownloadThreadPool.execute(asyncConnection);

        // asyncConnection.httpClient = httpClient;
        // asyncConnection.execute(requestBase);
    }

    private DefaultHttpClient initHttpClient() {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        if (connectionTimeout != -1)
            httpClient.getParams().setParameter(
                    HttpConnectionParams.CONNECTION_TIMEOUT, connectionTimeout);

        if (socketTimeout != -1)
            httpClient.getParams().setParameter(
                    HttpConnectionParams.SO_TIMEOUT, socketTimeout);
        if (credentialsProvider != null)
            httpClient.setCredentialsProvider(credentialsProvider);
        return httpClient;
    }

    public void setUserPassWithHostPort(String user, String pass, String host,
                                        int port) {
        credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(host, port),
                new UsernamePasswordCredentials(user, pass));

    }

    public void setUserPass(String user, String pass) {
        this.setUserPassWithHostPort(user, pass, host, port);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setHeader(String name, String value) {
        if (headers == null)
            headers = new HashMap<String, String>();
        headers.put(name, value);

    }

    private void includeHeaders(HttpRequestBase httpBase) {
        if (headers != null) {
            for (String key : headers.keySet()) {
                String value = headers.get(key);
                httpBase.setHeader(key, value);
            }
        }
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    // private static String convertStreamToString(InputStream is)
    // throws IOException {
    // BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    // StringBuilder sb = new StringBuilder();
    // String line = null;
    //
    // while ((line = reader.readLine()) != null) {
    // sb.append(line);
    // }
    //
    // is.close();
    // is = null;
    // return sb.toString();
    // }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public List<NameValuePair> getNameValuePairs() {
        return nameValuePairs;
    }

    public void setNameValuePairs(List<NameValuePair> nameValuePairs) {
        this.nameValuePairs = nameValuePairs;
    }

    public boolean isAcceptGzip() {
        return acceptGzip;
    }

    public void setAcceptGzip(boolean acceptGzip) {
        this.acceptGzip = acceptGzip;
    }

    public interface ConnectionHandlerResponse {
        public void onSuccess(ConnectionResponse response);

        public void onError(String response);
    }

    public interface ConnectionHandlerResponseFile {
        public void onSuccess(byte[] fileData, String nameFile);

        public void onError(String responsel);
    }

    public interface ConnectionHandlerResponseBody {
        public void onSuccess(String response);

        public void onError(String response);
    }

    public interface ConnectionHandlerResponseHeaders {
        public void onSuccess(Map<String, String> headers, String url);

        public void onError(String response);
    }

    private static class MyHandler extends Handler {

        public MyHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            if (!(msg.obj instanceof ConnectionResponse)) {
                return;
            }
            ConnectionResponse connectionResponse = (ConnectionResponse) msg.obj;
            if (connectionResponse.connectionHandlerResponseHeaders != null
                    && connectionResponse.error != null)
                connectionResponse.connectionHandlerResponseHeaders
                        .onError(connectionResponse.error);
            if (connectionResponse.connectionHandlerResponse != null)
                if (connectionResponse.error == null) {
                    ConnectionResponse response = new ConnectionResponse();
                    response.bodyResponse = connectionResponse.bodyResponse;
                    response.headers = connectionResponse.headers;
                    response.statusCode = connectionResponse.statusCode;
                    response.url = connectionResponse.url;
                    connectionResponse.connectionHandlerResponse
                            .onSuccess(connectionResponse);
                } else {
                    connectionResponse.connectionHandlerResponse
                            .onError(connectionResponse.error);
                }
            if (connectionResponse.connectionHandlerResponseBody != null)
                if (connectionResponse.error == null) {
                    try {
                        connectionResponse.connectionHandlerResponseBody
                                .onSuccess(new String(
                                        connectionResponse.bodyResponse,
                                        "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else
                    connectionResponse.connectionHandlerResponseBody
                            .onError(connectionResponse.error);
            if (connectionResponse.connectionHandlerResponseFile != null)
                if (connectionResponse.error == null) {
                    String name = connectionResponse.url;
                    if (name.contains("/")) {
                        try {
                            name = name.substring(name.lastIndexOf("/") + 1,
                                    name.length());
                        } catch (Exception e) {
                        }
                    }
                    connectionResponse.connectionHandlerResponseFile.onSuccess(
                            connectionResponse.bodyResponse, name);
                } else
                    connectionResponse.connectionHandlerResponseFile
                            .onError(connectionResponse.error);

        }
    }

    public static class ConnectionResponse {

        private ConnectionHandlerResponseFile connectionHandlerResponseFile;
        private ConnectionHandlerResponseBody connectionHandlerResponseBody;
        private ConnectionHandlerResponseHeaders connectionHandlerResponseHeaders;
        private ConnectionHandlerResponse connectionHandlerResponse;
        private int statusCode;
        private Map<String, String> headers;
        private byte[] bodyResponse;
        private String url;
        private String error;

        /**
         * Return status code of connection. return -1 if the connection was not
         * carried
         *
         * @return status code
         */

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public void setHeaders(Map<String, String> headers) {
            this.headers = headers;
        }

        public byte[] getBodyResponse() {
            return bodyResponse;
        }

        public void setBodyResponse(byte[] bodyResponse) {
            this.bodyResponse = bodyResponse;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

    }

    private class AsyncConnection implements Runnable {

        private ConnectionHandlerResponseFile connectionHandlerResponseFile;
        private ConnectionHandlerResponseBody connectionHandlerResponseBody;
        private ConnectionHandlerResponseHeaders connectionHandlerResponseHeaders;
        private ConnectionHandlerResponse connectionHandlerResponse;
        private HttpRequestBase request;

        public AsyncConnection() {
        }

        public void run() {
            android.os.Process
                    .setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            ConnectionResponse conn = startSyncConnection(request);
            conn.connectionHandlerResponse = connectionHandlerResponse;
            conn.connectionHandlerResponseBody = connectionHandlerResponseBody;
            conn.connectionHandlerResponseFile = connectionHandlerResponseFile;
            conn.connectionHandlerResponseHeaders = connectionHandlerResponseHeaders;
            Message completeMessage = handler.obtainMessage(0, conn);
            completeMessage.sendToTarget();

        }
    }

}
