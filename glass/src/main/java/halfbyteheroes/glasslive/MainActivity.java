package halfbyteheroes.glasslive;

import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;

import net.majorkernelpanic.streaming.MediaStream;
import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.audio.AudioQuality;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.rtsp.RtspClient;
import net.majorkernelpanic.streaming.video.VideoQuality;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity implements
        RtspClient.Callback,
        Session.Callback,
        SurfaceHolder.Callback {


    private final static String TAG = "MainActivity";
    private SurfaceView mSurfaceView;
    private Session mSession;
    private RtspClient mClient;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.main_layout);
        mSurfaceView = (SurfaceView) findViewById(R.id.camera_surface);
        mSession = SessionBuilder.getInstance()
                .setContext(getApplicationContext())
                .setAudioEncoder(SessionBuilder.AUDIO_AAC)
                .setAudioQuality(new AudioQuality(8000,16000))
                .setVideoEncoder(SessionBuilder.VIDEO_H263)
                .setSurfaceView(mSurfaceView)
                .setPreviewOrientation(0)
                .setCallback(this)
                .build();
        mClient = new RtspClient();
        mClient.setSession(mSession);
        mClient.setCallback(this);
        mSurfaceView.setAspectRatioMode(SurfaceView.ASPECT_RATIO_PREVIEW);
        mSurfaceView.getHolder().addCallback(this);
        mSession.setVideoQuality(new VideoQuality(1280, 720, 30, 4000));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBitrateUpdate(long bitrate) {

    }

    @Override
    public void onSessionError(int reason, int streamType, Exception e) {

    }

    @Override
    public void onPreviewStarted() {

    }

    @Override
    public void onSessionConfigured() {

    }

    @Override
    public void onSessionStarted() {

    }

    @Override
    public void onSessionStopped() {

    }

    @Override
    public void onRtspUpdate(int message, Exception exception) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mSession.startPreview();
        //mClient.setCredentials(mEditTextUsername.getText().toString(), mEditTextPassword.getText().toString());
        mClient.setServerAddress("127.0.0.1", 1234);
        mClient.setStreamPath("/test.rtsp");
        mClient.startStream();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
