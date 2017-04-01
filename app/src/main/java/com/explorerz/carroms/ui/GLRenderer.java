package com.explorerz.carroms.ui;
/*
 * Created by sakkeerhussain on 19/03/17.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.view.MotionEvent;

import com.explorerz.carroms.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;

class GLRenderer implements Renderer {

    // Our matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];

    // Geometric variables
    private static float[] vertices;
    private static short indices[];
    private static float uvs[];
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

    private CaromBoardSprite boardBgSprite;
    private RedCaromSprite redCaromSprite;


    private int bgTextureId;
    private int redTextureId;

    GLRenderer(Context c) {
        mContext = c;
        mLastTime = System.currentTimeMillis() + 100;
        boardBgSprite = new CaromBoardSprite();
        redCaromSprite = new RedCaromSprite();
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

    private void render(float[] m) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        updateSprite(boardBgSprite);
        draw(m, bgTextureId);
        updateSprite(redCaromSprite);
        draw(m, redTextureId);
    }

    private void draw(float[] m, int textureId) {
        GLES20.glActiveTexture(0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glEnable(GLES20.GL_BLEND_COLOR);
        GLES20.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glDepthMask(false);
        int mPositionHandle = GLES20.glGetAttribLocation(RiGraphicTools.sp_Image, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);
        int mTexCoordLoc = GLES20.glGetAttribLocation(RiGraphicTools.sp_Image, "a_texCoord");
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);
        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                0, uvBuffer);
        int mtrxhandle = GLES20.glGetUniformLocation(RiGraphicTools.sp_Image, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);
        int mSamplerLoc = GLES20.glGetUniformLocation(RiGraphicTools.sp_Image, "s_texture");
        GLES20.glUniform1i(mSamplerLoc, 0);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
        GLES20.glDisable(GLES20.GL_BLEND_COLOR);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mScreenWidth = width;
        mScreenHeight = height;
        Sprite.setBoardRatio(width, height);
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
        setUpDrawListBuffer();
        setUpImage();
        GLES20.glClearColor(235f / 255.0f, 235f / 255.0f, 255f / 255.0f, 255f / 255.0f);
        int vertexShader = RiGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER,
                RiGraphicTools.vs_Image);
        int fragmentShader = RiGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER,
                RiGraphicTools.fs_Image);
        RiGraphicTools.sp_Image = GLES20.glCreateProgram();
        GLES20.glAttachShader(RiGraphicTools.sp_Image, vertexShader);
        GLES20.glAttachShader(RiGraphicTools.sp_Image, fragmentShader);
        GLES20.glLinkProgram(RiGraphicTools.sp_Image);
        GLES20.glUseProgram(RiGraphicTools.sp_Image);
    }

    private void setUpImage() {
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
        int[] texturenames = new int[1];
        GLES20.glGenTextures(1, texturenames, 0);
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.carrom_board);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);
        bgTextureId = texturenames[0];

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        // We are done using the bitmap so we should recycle it.
        bmp.recycle();

        //setting up second texture
        texturenames = new int[1];
        GLES20.glGenTextures(1, texturenames, 0);
        bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.coin_white);
        redTextureId = texturenames[0];

        // Bind texture to texturename
        //GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        // We are done using the bitmap so we should recycle it.
        bmp.recycle();
    }

    private void setUpDrawListBuffer() {
        indices = new short[6];
        indices = new short[]{0, 1, 2, 0, 2, 3};
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);
    }

    private void updateSprite(Sprite sprite) {
        vertices = sprite.getTransformedVertices();
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.clear();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }

    void processTouchEvent(MotionEvent event) {
        // Get the half of screen value
        int screenhalf = (int) (mScreenWidth / 2);
        int screenheightpart = (int) (mScreenHeight / 3);
        if (event.getX() < screenhalf) {
            // Left screen touch
            if (event.getY() < screenheightpart)
                redCaromSprite.scale(-0.01f);
            else if (event.getY() < (screenheightpart * 2))
                redCaromSprite.translate(-10f, -10f);
            else
                redCaromSprite.rotate(0.01f);
        } else {
            // Right screen touch
            if (event.getY() < screenheightpart)
                redCaromSprite.scale(0.01f);
            else if (event.getY() < (screenheightpart * 2))
                redCaromSprite.translate(10f, 10f);
            else
                redCaromSprite.rotate(-0.01f);
        }
    }
}