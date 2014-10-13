package halfbyteheroes.glasslive;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback

{
    private SurfaceHolder _surface_holder = null;
    private Camera _camera = null;

    public CameraView(Context _context) {
        super(_context);
        _surface_holder = this.getHolder();
        _surface_holder.addCallback(this);
        _surface_holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder _holder) {
        _camera = Camera.open();
        this.setCameraParameters(_camera);
        try {
            _camera.setPreviewDisplay(_holder);
        }
        catch (Exception _ex) {
            this.releaseCamera();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder _holder, int _format, int _width, int _height) {
        if (_camera != null) {
            _camera.startPreview();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder _holder) {
        this.releaseCamera();
    }

    public void setCameraParameters(Camera _camera) {
        if (_camera != null) {
            Camera.Parameters parameters = _camera.getParameters();
            parameters.setPreviewFpsRange(30000, 30000);
            // 640 x 360
            parameters.setPreviewSize(640,360);
            _camera.setParameters(parameters);
        }
    }

    public void releaseCamera() {
        if (_camera != null) {
            _camera.release();
            _camera = null;
        }
    }
}