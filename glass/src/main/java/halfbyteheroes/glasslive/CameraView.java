package halfbyteheroes.glasslive;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by dev on 13.10.14.
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
    private Camera _camera;
    private Context _context;

    public CameraView(Context _context){
        super(_context);
        this._context = _context;
    }

    public CameraView(Context _context, AttributeSet _attributes){
        super(_context, _attributes);
        this._context = _context;
    }

    public CameraView(Context _context, AttributeSet _attributes, int _i1){
        super(_context, _attributes, _i1);
        this._context = _context;
    }

    @Override
    public void surfaceCreated(SurfaceHolder _holder) {
        _camera = Camera.open();
        Camera.Parameters parameters = _camera.getParameters();
        parameters.setPreviewFpsRange(30000, 30000);
        _camera.setParameters(parameters);
    }

    @Override
    public void surfaceChanged(SurfaceHolder _holder, int _i1, int _i2, int _i3) {
        if (_camera != null){
            try {
                _camera.setPreviewDisplay(_holder);
                Toast _test_toast = Toast.makeText(_context,"holder set",Toast.LENGTH_LONG);
                _test_toast.show();
            } catch (IOException e) {
                this.releaseCamera();
            }
            _camera.startPreview();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        _camera.release();
    }

    private void releaseCamera() {
        _camera.release();
    }
}
