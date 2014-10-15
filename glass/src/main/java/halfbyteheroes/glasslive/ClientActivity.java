package halfbyteheroes.glasslive;

import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import net.majorkernelpanic.streaming.MediaStream;
import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.audio.AudioQuality;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.rtsp.RtspClient;
import net.majorkernelpanic.streaming.rtsp.RtspServer;
import net.majorkernelpanic.streaming.video.VideoQuality;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientActivity extends Activity implements
        RtspClient.Callback,
        Session.Callback,
        SurfaceHolder.Callback {


    private final static String TAG = "MainActivity";
    private SurfaceView mSurfaceView;
    private SurfaceView mServerView;
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
                .setVideoEncoder(SessionBuilder.VIDEO_H264 )
                .setSurfaceView(mSurfaceView)
                .setPreviewOrientation(0)
                .setCallback(this)
                .build();
        mClient = new RtspClient();
        mClient.setSession(mSession);
        mClient.setCallback(this);
        mSurfaceView.setAspectRatioMode(SurfaceView.ASPECT_RATIO_PREVIEW);
        mSession.getVideoTrack().setStreamingMethod(MediaStream.MODE_MEDIARECORDER_API);
        mSurfaceView.getHolder().addCallback(this);
        // H264: 352, 288, 30, 300000
        mSession.setVideoQuality(new VideoQuality(1280, 720, 30, 500000));

        ((TextView)findViewById(R.id.info_label)).setText("");
        ((SurfaceView)findViewById(R.id.camera_surface)).setBackgroundColor(Color.TRANSPARENT);
        Toast.makeText(getApplicationContext(), "swipe down to exit the RTSP client", Toast.LENGTH_LONG);
        this.setTitle("Glass.Live | RTSP client");
    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        switch(keycode) {
            case KeyEvent.KEYCODE_BACK:
                /* close the application */
                this.finish();
                return true;

            default: return super.onKeyDown(keycode, event);
        }
    }


    private String displayConnectString() {
        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);
        Toast.makeText(this.getApplicationContext(), ipAddress, Toast.LENGTH_LONG).show();
        return ipAddress;
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
    protected void onDestroy() {
        super.onDestroy();
        mClient.stopStream();
        mClient.release();
        mSession.stop();
        mSession.release();
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
        mClient.setServerAddress("192.168.43.95", 1935);
        mClient.setStreamPath("/live/test");
        mClient.startStream();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
