package com.explorerz.carroms.spritekit;
/*
 * Created by sakkeerhussain on 19/03/17.
 */

import android.opengl.GLES20;

public class SpriteKitGraphicTools {

    public static int imageShaderProgram;

    public static final String imageVertexShader =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "  v_texCoord = a_texCoord;" +
                    "}";
    public static final String imageFragmentShader =
                    "precision mediump float;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "   vec4 color = texture2D( s_texture, v_texCoord);" +
                    "   if (color.w < "+ SpriteKit.UIConfigs.TRANSPARENCY_ALPHA+") {" +
                    "       discard;" +
                    "   }" +
                    "   else gl_FragColor = color;" +
                    "}";

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}