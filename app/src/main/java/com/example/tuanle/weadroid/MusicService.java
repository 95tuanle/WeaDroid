package com.example.tuanle.weadroid;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    private MusicBinder binder = new MusicBinder();
    private ArrayList<Song> songs;
    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "CONNECTED", Toast.LENGTH_SHORT).show();
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initializePlayer();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initializePlayer() {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;

    }

    public void playSong(int position) {
        Song song = songs.get(position);
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(song.getFilePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
    }

    public void stopSong() {
        mediaPlayer.stop();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    public class MusicBinder extends Binder{
        MusicService getService() {
            return MusicService.this;
        }
    }
}
