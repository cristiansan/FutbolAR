package com.pmovil.soccer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends Activity {

    private static ProgressDialog progressDialog;
    private String videoPath = "";
    private VideoView videoView;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (videoView == null) {
            setContentView(R.layout.videolayout);

            videoPath = this.getIntent().getStringExtra("video");

            videoView = (VideoView) findViewById(R.id.videoview);

			/*
             * InputStream content = null; try { HttpClient httpclient = new
			 * DefaultHttpClient(); HttpResponse response =
			 * httpclient.execute(new HttpGet(url)); content =
			 * response.getEntity().getContent(); } catch (Exception e) {
			 * //Log.i("[GET REQUEST]", "Network exception", e); }
			 * 
			 * 
			 * Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			 * startActivity(intent);
			 */

            progressDialog = ProgressDialog.show(VideoActivity.this, "",
                    "Buffering video...", true);
            progressDialog.setCancelable(true);
            progressDialog.show();
            PlayVideo();
        }

    }

    private void PlayVideo() {
        try {
            getWindow().setFormat(PixelFormat.TRANSLUCENT);
            MediaController mediaController = new MediaController(
                    VideoActivity.this);
            mediaController.setAnchorView(videoView);

            Uri video = Uri.parse(videoPath);
            videoView.setMediaController(mediaController);
            // videoView.setVideoURI(video);

            MediaController mc = new MediaController(this);
            videoView.setMediaController(mc);
            videoView.setKeepScreenOn(true);
            videoView.setVideoPath(videoPath);
            videoView.requestFocus();
            videoView.setOnPreparedListener(new OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    progressDialog.dismiss();
                    videoView.start();
                }
            });

            videoView.setOnErrorListener(new OnErrorListener() {

                public boolean onError(MediaPlayer mp, int what, int extra) {
                    // TODO Auto-generated method stub
                    progressDialog.dismiss();

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri
                            .parse(videoPath)));

                    Intent in = new Intent();
                    setResult(1, in);
                    finish();
                    return false;
                }
            });

        } catch (Exception e) {
            progressDialog.dismiss();
            System.out.println("Video Play Error :" + e.toString());
            finish();
        }

    }
}