package com.explorerz.carroms.spritekit;
/*
 * Created by sakkeerhussain on 01/04/17.
 */

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class SpriteKit {

    private final SpriteKitSurfaceView glSurfaceView;
    private final SpriteKitRenderer mRenderer;
    private List<Sprite> sprites;
    private List<Texture> textures;
    float boardRatio = 1;
    float boardSideWidth;
    float boardSideHeight;
    private OnTouchEventListener mListenre;

    public SpriteKit(Context context, ViewGroup viewGroup) {
        sprites = new ArrayList<>();
        textures = new ArrayList<>();
        glSurfaceView = new SpriteKitSurfaceView(context.getApplicationContext(), this);
        viewGroup.addView(glSurfaceView);

        this.mRenderer = new SpriteKitRenderer(context, this);
        glSurfaceView.setRenderer(mRenderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    public void onPause() {
        glSurfaceView.onPause();
        mRenderer.onPause();
    }

    public void onResume() {
        glSurfaceView.onResume();
        mRenderer.onResume();
    }

    boolean onTouchEvent(MotionEvent e) {
        return mListenre != null && mListenre.onTouch(e);
    }

    List<Sprite> getSprites() {
        return sprites;
    }

    List<Texture> getTextures() {
        return textures;
    }

    void setBoardRatio(float screenWidth, float screenHeight) {
        if (screenWidth < screenHeight) {
            screenWidth = screenHeight;
            screenHeight = screenWidth;
        }

        boardSideWidth = 0;
        boardSideHeight = 0;
        float heightRatio = screenHeight / UIConfigs.HEIGHT;
        float widthAccordingToHeightRatio = heightRatio * UIConfigs.WIDTH;
        if (widthAccordingToHeightRatio < screenWidth) {
            boardRatio = heightRatio;
            boardSideWidth = (screenWidth - widthAccordingToHeightRatio) / 2;
        } else {
            boardRatio = screenWidth / UIConfigs.WIDTH;
            float heightAccordingRatio = boardRatio * UIConfigs.HEIGHT;
            boardSideHeight = (screenHeight - heightAccordingRatio) / 2;
        }
    }


    public static class UIConfigs {
        private static float HEIGHT = 1000;
        private static float WIDTH = 1000;
        private static String TRANSPARENCY_ALPHA = "0.5";
        public static boolean setHeight(float height) {
            UIConfigs.HEIGHT = height;
            return true;
        }
        public static boolean setwidth(float width) {
            UIConfigs.WIDTH = width;
            return true;
        }
        public static boolean setTransparencyAlpha(float transparencyAlpha) {
            if (transparencyAlpha > 1 || transparencyAlpha<0){
                return false;
            }
            TRANSPARENCY_ALPHA = String.valueOf(transparencyAlpha);
            return true;
        }
        public static String getTransparencyAlpha() {
            return TRANSPARENCY_ALPHA;
        }
    }

    public interface OnTouchEventListener {
        boolean onTouch(MotionEvent e);
    }


    //public apis
    public boolean addSprite(Sprite sprite) {
        if (sprite.getTexture() != null) {
            if (!(textures.contains(sprite.getTexture()))) {
                textures.add(sprite.getTexture());
            }
            return sprites.add(sprite);
        }
        return false;
    }

    public void setOnTouchListener(OnTouchEventListener mListenre) {
        this.mListenre = mListenre;
    }
}
