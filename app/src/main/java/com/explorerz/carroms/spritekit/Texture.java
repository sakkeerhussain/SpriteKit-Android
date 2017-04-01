package com.explorerz.carroms.spritekit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Created by HAYDN on 1/7/2015.
 */
public class Texture {

    private final int resourceId;
    private int textureID[] = {0};
    private int width, height;

    public Texture(int resourceId) {
        this.resourceId = resourceId;
    }

    public void destroy() {
        GLES20.glDeleteTextures(1, textureID, 0);
    }


    public int getTextureID() {
        return textureID[0];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void bindTexture(int id) {
        GLES20.glActiveTexture(id);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID[0]);
    }

    public void loadTexture(final Context context) {
        GLES20.glGenTextures(1, textureID, 0);

        try {
            if (textureID[0] != 0) {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;

                final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

                width = bitmap.getWidth();
                height = bitmap.getHeight();

                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID[0]);

                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
                bitmap.recycle();
            }

            if (textureID[0] == 0) {
                throw new RuntimeException("Error loading texture.");
            }
        } catch (Exception e) {
            GLES20.glDeleteTextures(1, textureID, 0);
            textureID[0] = 0;
            throw e;
        }
    }
}
