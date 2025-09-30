package com.example.evidencia3

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.example.evidencia3.R

class BackgroundSoundService : Service() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        try {
            // Make sure the file is named 'perfidia.mp3' in the raw folder
            mediaPlayer = MediaPlayer.create(this, R.raw.perfidia)
            mediaPlayer?.isLooping = true
            mediaPlayer?.setVolume(0.3f, 0.3f) // Reduced volume to 30% for better user experience
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onDestroy()
    }
} 