package com.vivamedia.CMTablet;

import android.content.Context;
import com.vivamedia.CMTablet.MyGLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class GLES2SurfaceView extends MyGLSurfaceView {
    public boolean preservesEGLContext;

    /* loaded from: classes.dex */
    public class GLES2Renderer implements MyGLSurfaceView.Renderer {
        public GLES2Renderer() {
        }

        @Override // com.vivamedia.CMTablet.MyGLSurfaceView.Renderer
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            MainActivity.onSurfaceCreatedNative();
        }

        @Override // com.vivamedia.CMTablet.MyGLSurfaceView.Renderer
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            MainActivity.onSurfaceChangedNative(width, height);
        }

        @Override // com.vivamedia.CMTablet.MyGLSurfaceView.Renderer
        public void onDrawFrame(GL10 gl) {
            MainActivity.onRenderNative();
        }
    }

    public GLES2SurfaceView(Context context) {
        super(context);
        this.preservesEGLContext = true;
        try {
            setPreserveEGLContextOnPause(true);
        } catch (NoSuchMethodError e) {
            this.preservesEGLContext = false;
        }
        setEGLContextClientVersion(2);
        setRenderer(new GLES2Renderer());
        setRenderMode(1);
    }

    @Override // com.vivamedia.CMTablet.MyGLSurfaceView
    public void onPause() {
        super.onPause();
    }

    @Override // com.vivamedia.CMTablet.MyGLSurfaceView
    public void onResume() {
        super.onResume();
    }
}
