package com.example.mobil_12_02_api31;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.mobil_12_02_api31.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    //Teszt
    MediaPlayer mPlayer;
    private ActivityMainBinding binding;
    private String path;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.downloadButton.setOnClickListener(button -> download());
        binding.playButton.setOnClickListener(button -> playSong());
        path = this.getFilesDir().getParent();
        mPlayer = new MediaPlayer();
    }
    private void download() {
        new Thread(() -> {
            int count;
            try {
                Log.e("download", "letöltés indul");
                URL url = new URL("https://arato.inf.unideb.hu/kocsis.gergely/song.mp3");
                Log.e("download", "URL csatkakozás elso lepes");
                URLConnection connection = url.openConnection();
                Log.e("download", "URL csatkakozás masodik lepes");
                connection.connect();
                Log.e("download", "URL csatkakozás sikeres");
                int lenghtOfFile = connection.getContentLength();
                Log.e("download", "Fájlméret: " + lenghtOfFile);
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 10 * 1024);
                // Output stream to write file
                OutputStream output = new FileOutputStream(path + "/files/song.mp3");
                byte data[] = new byte[1024];
                long total = 0;
                int prevPercentage = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    long finalTotal = total;

                    int actualPercentage = (int)(finalTotal * 100) / lenghtOfFile;
                    if(actualPercentage > prevPercentage) {
                        Log.e("download", "letöltés: " + actualPercentage);
                        runOnUiThread(() -> {
                            binding.progressBar.setProgress(actualPercentage);
                            binding.progressTextView.setText("" + actualPercentage + "%");
                        });
                        prevPercentage = actualPercentage;
                    }

                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
        }
        ).start();
    }

    public void playSong() {

    }
}