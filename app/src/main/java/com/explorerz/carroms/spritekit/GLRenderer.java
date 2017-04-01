package com.explorerz.carroms.spritekit;
/*
 * Created by sakkeerhussain on 19/03/17.
 */

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class GLRenderer implements Renderer {

    // Our matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];

    // Geometric variables
    private static float[] vertices;
    private static short indices[];
    private static float uvs[];
    private final SpriteKit spriteKit;
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private FloatBuffer uvBuffer;

    // Our screenresolution
    private float mScreenWidth = 1280;
    private float mScreenHeight = 768;

    // Misc
    private Context mContext;
    private long mLastTime;
    int mProgram;

    GLRenderer(Context c, SpriteKit spriteKit) {
        mContext = c;
        mLastTime = System.currentTimeMillis() + 100;
        this.spriteKit = spriteKit;
    }

    public void onPause() {
        /* Do stuff to pause the renderer */
    }

    public void onResume() {
        /* Do stuff to resume the renderer */
        mLastTime = System.currentTimeMillis();
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        long now = System.currentTimeMillis();
        if (mLastTime > now) return;
        long elapsed = now - mLastTime;
        render(mtrxProjectionAndView);
        mLastTime = now;
    }

    private void render(float[] mtrxProjectionAndView) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        synchronized (spriteKit.getSprites()) {
            for (Sprite sprite : spriteKit.getSprites()) {
                sprite.render(mtrxProjectionAndView, uvBuffer, drawListBuffer, indices);
            }
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mScreenWidth = width;
        mScreenHeight = height;
        spriteKit.setBoardRatio(width, height);
        GLES20.glViewport(0, 0, (int) mScreenWidth, (int) mScreenHeight);
        for (int i = 0; i < 16; i++) {
            mtrxProjection[i] = 0.0f;
            mtrxView[i] = 0.0f;
            mtrxProjectionAndView[i] = 0.0f;
        }
        Matrix.orthoM(mtrxProjection, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        setUpBuffers();
        setUpSprites();
        GLES20.glClearColor(235f / 255.0f, 235f / 255.0f, 255f / 255.0f, 255f / 255.0f);
        int vertexShader = RiGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER,
                RiGraphicTools.imageVertexShader);
        int fragmentShader = RiGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER,
                RiGraphicTools.imageFragmentShader);
        RiGraphicTools.imageShaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(RiGraphicTools.imageShaderProgram, vertexShader);
        GLES20.glAttachShader(RiGraphicTools.imageShaderProgram, fragmentShader);
        GLES20.glLinkProgram(RiGraphicTools.imageShaderProgram);
        GLES20.glUseProgram(RiGraphicTools.imageShaderProgram);
    }

    private void setUpBuffers() {
        indices = new short[6];
        indices = new short[]{0, 1, 2, 0, 2, 3};
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);


        uvs = new float[]{
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);
    }

    private void setUpSprites() {
        for (Texture texture : spriteKit.getTextures()) {
            texture.loadTexture(mContext);
        }
    }
}