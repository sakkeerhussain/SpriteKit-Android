package com.explorerz.carroms;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class GLSurf extends GLSurfaceView {

	private final GLRenderer mRenderer;

	public GLSurf(Context context) {
        super(context);
		setDebugFlags(GLSurfaceView.DEBUG_LOG_GL_CALLS);
		setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		setEGLContextClientVersion(2);
        mRenderer = new GLRenderer(context);
        setRenderer(mRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
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

}
