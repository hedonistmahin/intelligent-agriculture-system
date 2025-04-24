package com.example.agri;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // Initialize the VideoView
        videoView = findViewById(R.id.videoView);

        // Set up the video path (replace with the actual video URL or local path)
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.simple_raw;
        videoView.setVideoPath(videoPath);

        // Add media controls for video playback
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Start the video
        videoView.start();
    }
}
