package halfbyteheroes.glasslive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;


public class ChooserActivity extends Activity {

    Button _start_client_button;
    Button _start_server_button;

    Intent _client_activity;
    Intent _server_activity;

    GestureDetector _gesture_detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.chooser_activity_layout);
        _start_client_button = (Button)findViewById(R.id.start_client_button);
        _start_server_button = (Button)findViewById(R.id.start_server_button);
        _gesture_detector = createGestureDetector(this);
    }

    private GestureDetector createGestureDetector(Context context) {
        GestureDetector gestureDetector = new GestureDetector(context);
        //Create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if (gesture == Gesture.TAP) {
                        if(_start_client_button.isFocused()) {
                            _client_activity = new Intent(getApplicationContext(), ClientActivity.class);
                            startActivity(_client_activity);
                        }
                        if(_start_server_button.isFocused()) {
                            _server_activity = new Intent(getApplicationContext(), ServerActivity.class);
                            startActivity(_server_activity);
                        }
                    return true;
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                    /* choose upper button */
                    if(_start_server_button.isFocused()) _start_client_button.requestFocus();
                    else _start_server_button.requestFocus();
                    return true;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    /* choose lower button */
                    if(_start_client_button.isFocused()) _start_server_button.requestFocus();
                    else _start_client_button.requestFocus();
                    return true;
                }
                return false;
            }
        });
        return gestureDetector;
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (_gesture_detector != null) {
            return _gesture_detector.onMotionEvent(event);
        }
        return false;
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
