package halfbyteheroes.glasslive;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
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
public class ServerActivity extends Activity implements SurfaceHolder.Callback, Session.Callback {

    private int mRtspPort = -1;
    Session mSession;
    ActionBar mActionBar;

    private ServiceConnection mRtspServerConnection = new ServiceConnection() {

        private static final int RTSP_PORT = 1234;

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            RtspServer s = ((RtspServer.LocalBinder) binder).getService();
            s.setPort(RTSP_PORT);
            mRtspPort = s.getPort();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.main_layout);
        mActionBar = getActionBar();
        // Configures the SessionBuilder
        mSession = SessionBuilder.getInstance()
                .setSurfaceView((SurfaceView) findViewById(R.id.camera_surface))
                .setCallback(this)
                .setPreviewOrientation(0)
                .setContext(getApplicationContext())
                .setAudioEncoder(SessionBuilder.AUDIO_AAC)
                .setAudioQuality(new AudioQuality(32000,44000))
                .setVideoEncoder(SessionBuilder.VIDEO_H263)
                .setVideoQuality(new VideoQuality(1280, 720, 30, 500000))
                .build();

        mSession.getVideoTrack().setStreamingMethod(MediaStream.MODE_MEDIACODEC_API_2);
        // Starts the RTSP server
        bindService(new Intent(this, RtspServer.class), mRtspServerConnection, Context.BIND_AUTO_CREATE);

        ((TextView)findViewById(R.id.info_label)).setText("Waiting for RTSP connection to "+this.getConnectString()+":1234");
        Toast.makeText(this.getApplicationContext(), "swipe down to exit the RTSP server", Toast.LENGTH_LONG);
        this.setTitle("Glass.Live | RTSP server");
    }



    @Override
    public void onResume() {
        super.onResume();
        SessionBuilder.getInstance().getSurfaceView().setAspectRatioMode(SurfaceView.ASPECT_RATIO_PREVIEW);
        SessionBuilder.getInstance().getSurfaceView().getHolder().addCallback(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(mRtspServerConnection);
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
        mActionBar.hide();
        ((SurfaceView)findViewById(R.id.camera_surface)).setBackgroundColor(Color.TRANSPARENT);
        ((TextView)findViewById(R.id.info_label)).setText("");
    }

    @Override
    public void onSessionStopped() {
        mActionBar.show();
        ((TextView)findViewById(R.id.info_label)).setText("Waiting for RTSP connection to " + this.getConnectString() + ":1234");
        ((SurfaceView)findViewById(R.id.camera_surface)).setBackgroundColor(Color.BLACK);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //mSession.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }


    private String getConnectString() {
        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);
        return ipAddress;
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
}