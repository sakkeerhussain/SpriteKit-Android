package com.explorerz.carroms.ui;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class SpriteKitSurfaceView extends GLSurfaceView {

	private final SpriteKit spriteKit;

	public SpriteKitSurfaceView(Context context, SpriteKit spriteKit) {
        super(context);
		this.getHolder().setFormat(PixelFormat.RGB_565);
		this.getHolder().setFormat(PixelFormat.TRANSPARENT);
        this.setDebugFlags(GLSurfaceView.DEBUG_LOG_GL_CALLS);
        this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.setEGLContextClientVersion(2);

		this.spriteKit = spriteKit;
    }

	public void onPause() {
		super.onPause();
	}

	public void onResume() {
		super.onResume();
	}

	@Override
    public boolean onTouchEvent(MotionEvent e) {
		return spriteKit.onTouchEvent(e);
    }
}
