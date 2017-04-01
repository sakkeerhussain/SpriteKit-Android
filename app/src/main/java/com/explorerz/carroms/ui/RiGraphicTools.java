package com.explorerz.carroms.ui;
/*
 * Created by sakkeerhussain on 19/03/17.
 */

import android.opengl.GLES20;

class RiGraphicTools {

    // Program variables
    static int sp_SolidColor;
    static int sp_Image;

    /* SHADER Solid
     *
     * This shader is for rendering a colored primitive.
     *
     */
    static final String vs_SolidColor =
            "uniform    mat4        uMVPMatrix;" +
                    "attribute  vec4        vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    static final String fs_SolidColor =
            "precision mediump float;" +
                    "void main() {" +
                    "  gl_FragColor = vec4(0.5,0,0,1);" +
                    "}";

    static final String vs_Image =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "  v_texCoord = a_texCoord;" +
                    "}";
    static final String fs_Image =
                    "precision mediump float;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "   vec4 color = texture2D( s_texture, v_texCoord);" +
                    "   if (color.w < "+Configurations.TRANSPARENCY_ALPHA+") {" +
                    "       discard;" +
                    "   }" +
                    "   else gl_FragColor = color;" +
                    "}";

    static int loadShader(int type, String shaderCode) {

        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        // return the shader
        return shader;
    }
}