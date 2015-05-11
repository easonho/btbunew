package com.meicai.util.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.imitation.Zxing.camera.CameraManager;
import com.imitation.Zxing.decoding.CaptureActivityHandler;
import com.imitation.Zxing.view.ViewfinderView;
import com.meicai.util.R;

import java.io.IOException;
import java.util.Vector;

/**
 * Created by siyun on 2015/1/28.
 */
abstract public class CaptureActivity extends Activity implements SurfaceHolder.Callback {

    private boolean mHasSurface;
    private boolean mPlayBeep;
    private MediaPlayer mMediaPlayer;
    private boolean mVibrate;
    private CaptureActivityHandler mHandler;
    private Vector<BarcodeFormat> mDecodeFormats;
    private String mCharacterSet;
    private static final float BEEP_VOLUME = 0.10f;

    public abstract void OnDecode(Result obj, Bitmap barcode);

    public abstract ViewfinderView GetViewfinderView();

    public abstract SurfaceView GetSurfaceView();

    public CaptureActivityHandler getHandler() {
        return mHandler;
    }

    public void drawViewfinder() {
        ViewfinderView v = GetViewfinderView();
        if (v != null) {
            v.drawViewfinder();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CameraManager.init(getApplication());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if (!mHasSurface) {
            mHasSurface = true;
            initCamera();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHasSurface = false;
    }


    @Override
    protected void onResume() {
        super.onResume();

        SurfaceView surfaceView = GetSurfaceView();

        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (mHasSurface) {
            initCamera();
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        mDecodeFormats = null;
        mCharacterSet = null;


        mPlayBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            mPlayBeep = false;
        }

        initBeepSound();

        mVibrate = true;
    }

    private void initBeepSound() {
        if (mPlayBeep && mMediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mMediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
                file.close();
                mMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mMediaPlayer.prepare();
            } catch (IOException e) {
                mMediaPlayer = null;
            }
        }
    }

    protected void playBeepSoundAndVibrate() {

        if (mPlayBeep && mMediaPlayer != null) {
            mMediaPlayer.start();
        }

        if (mVibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mHandler != null) {
            mHandler.quitSynchronously();
            mHandler = null;
        }
        CameraManager.get().closeDriver();

    }


    protected void initCamera() {
        SurfaceHolder surfaceHolder = GetSurfaceView().getHolder();
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        try {
            CameraManager.get().openDriver(surfaceHolder);
            Boolean open = sp.getBoolean("LIGHT", false);
            if (open) {

                CameraManager.get().openLight();
            }
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (mHandler == null) {
            mHandler = new CaptureActivityHandler(this, mDecodeFormats,
					mCharacterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {


    }

    private static final long VIBRATE_DURATION = 200L;

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    public void ResetCamera() {
        initCamera();
        if (mHandler != null) {
            mHandler.restartPreviewAndDecode();
        }
    }


}

