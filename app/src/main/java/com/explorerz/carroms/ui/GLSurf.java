package com.explorerz.carroms.ui;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class GLSurf extends GLSurfaceView {

	private final GLRenderer mRenderer;

	public GLSurf(Context context) {
        super(context);

		this.getHolder().setFormat(PixelFormat.RGB_565);
		this.getHolder().setFormat(PixelFormat.TRANSPARENT);

        this.setDebugFlags(GLSurfaceView.DEBUG_LOG_GL_CALLS);
        this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.setEGLContextClientVersion(2);
        this.mRenderer = new GLRenderer(context);
        this.setRenderer(mRenderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
	public void onPause() {
		super.onPause();
		mRenderer.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mRenderer.onResume();
	}

	@Override
    public boolean onTouchEvent(MotionEvent e) {
        mRenderer.processTouchEvent(e);
        return true;
    }

}
