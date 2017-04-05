package com.explorerz.carroms.spritekit;
/*
 * Created by sakkeer hussain on 25/03/17.
 */

import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;

/*
@param boardRation: Ration between game UI and game engine, (UI_width/1000)
 */

public class Sprite {
    private final SpriteKit spriteKit;
    private float angle;
    private float scale;
    private RectF base;
    private PointF translation;
    private Texture texture;

    public Sprite(SpriteKit spriteKit, Texture texture) {
        base = new RectF(0f, 0f, 0f, 0f);
        translation = new PointF(0f, 0f);
        scale = 1f;
        angle = 0f;
        this.texture = texture;
        this.spriteKit = spriteKit;
    }

    public Sprite(SpriteKit spriteKit, int resourceId) {
        this(spriteKit, new Texture(resourceId));
    }

    public void translate(float deltaX, float deltaY) {
        // Update our location.
        translation.x += deltaX;
        translation.y += deltaY;
    }

    public void scale(float deltaS) {
        scale += deltaS;
    }

    public void rotate(float deltaA) {
        angle += deltaA;
    }

    public float[] getTransformedVertices() {
        // Start with scaling
        float x1 = base.left * scale * spriteKit.boardRatio;
        float x2 = base.right * scale * spriteKit.boardRatio;
        float y1 = base.bottom * scale * spriteKit.boardRatio;
        float y2 = base.top * scale * spriteKit.boardRatio;

        // We now detach from our Rect because when rotating,
        // we need the seperate points, so we do so in opengl order
        PointF one = new PointF(x1, y2);
        PointF two = new PointF(x1, y1);
        PointF three = new PointF(x2, y1);
        PointF four = new PointF(x2, y2);

        // We create the sin and cos function once,
        // so we do not have calculate them each time.
        float sin = (float) Math.sin(angle);
        float cos = (float) Math.cos(angle);

        // Then we rotate each point
        one.x = x1 * cos - y2 * sin;
        one.y = x1 * sin + y2 * cos;
        two.x = x1 * cos - y1 * sin;
        two.y = x1 * sin + y1 * cos;
        three.x = x2 * cos - y1 * sin;
        three.y = x2 * sin + y1 * cos;
        four.x = x2 * cos - y2 * sin;
        four.y = x2 * sin + y2 * cos;

        // Finally we translate the sprite to its correct position.
        float translationX = translation.x * spriteKit.boardRatio + spriteKit.boardSideWidth;
        float translationY = translation.y * spriteKit.boardRatio + spriteKit.boardSideHeight;
        one.x += translationX;
        one.y += translationY;
        two.x += translationX;
        two.y += translationY;
        three.x += translationX;
        three.y += translationY;
        four.x += translationX;
        four.y += translationY;

        // We now return our float array of vertices.
        return new float[]
                {
                        one.x, one.y, 0.0f,
                        two.x, two.y, 0.0f,
                        three.x, three.y, 0.0f,
                        four.x, four.y, 0.0f,
                };
    }

    //setters
    Texture getTexture() {
        return texture;
    }

    void render(float[] matrix, FloatBuffer uvBuffer, ShortBuffer drawListBuffer, short[] indices) {
        this.draw(matrix, getVertexBuffer(), uvBuffer, drawListBuffer, indices);
    }

    private void draw(float[] matrix, FloatBuffer vertexBuffer, FloatBuffer uvBuffer, ShortBuffer drawListBuffer, short[] indices) {
        texture.bindTexture(0);
        GLES20.glEnable(GLES20.GL_BLEND_COLOR);
        GLES20.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glDepthMask(false);
        int mPositionHandle = GLES20.glGetAttribLocation(SpriteKitGraphicTools.imageShaderProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);
        int mTexCoordLoc = GLES20.glGetAttribLocation(SpriteKitGraphicTools.imageShaderProgram, "a_texCoord");
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);
        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                0, uvBuffer);
        int mtrxhandle = GLES20.glGetUniformLocation(SpriteKitGraphicTools.imageShaderProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, matrix, 0);
        int mSamplerLoc = GLES20.glGetUniformLocation(SpriteKitGraphicTools.imageShaderProgram, "s_texture");
        GLES20.glUniform1i(mSamplerLoc, 0);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
        GLES20.glDisable(GLES20.GL_BLEND_COLOR);
    }

    private FloatBuffer getVertexBuffer() {
        float[] vertices = this.getTransformedVertices();
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.clear();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
        return vertexBuffer;
    }




    //public apis
    public void setSize(int width, int height){
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        base = new RectF(-halfWidth, halfHeight, halfWidth, -halfHeight);
        //translation = new PointF(halfWidth, halfHeight);
    }
    public void setSize(int radius){
        setSize(radius*2, radius*2);
    }
    public void setPosition(PointF center){
        translation = new PointF(center.x, center.y);
    }
    public PointF getPosition(){
        return new PointF(translation.x, translation.y);
    }
}
