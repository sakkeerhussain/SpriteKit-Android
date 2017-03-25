package com.explorerz.carroms;
/*
 * Created by sakkeer hussain on 25/03/17.
 */

import android.graphics.PointF;
import android.graphics.RectF;

import com.explorerz.carroms.game.Game;

/*
@param boardRation: Ration between game UI and game engine, (UI_width/1000)
 */

class Sprite {
    private static float boardRatio;
    protected static float boardSideWidth;
    protected float angle;
    protected float scale;
    protected RectF base;
    protected PointF translation;

    static {
        boardRatio = 1;
    }

    public Sprite() {
        base = new RectF(0f, 0f, 0f, 0f);
        translation = new PointF(0f, 0f);
        scale = 1f;
        angle = 0f;
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
        float x1 = base.left * scale * boardRatio;
        float x2 = base.right * scale * boardRatio;
        float y1 = base.bottom * scale * boardRatio;
        float y2 = base.top * scale * boardRatio;

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
        float translationX = translation.x * boardRatio + boardSideWidth;
        float translationY = translation.y * boardRatio;
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
    public static void setBoardRatio(float screenWidth, int screenHeight) {
        float gameUIWidth = screenHeight > screenWidth ? screenWidth : screenHeight;
        Sprite.boardRatio = gameUIWidth / Game.TOTAL_HEIGHT;
        Sprite.boardSideWidth = Math.abs(screenWidth - screenHeight) / 2;
    }
}
