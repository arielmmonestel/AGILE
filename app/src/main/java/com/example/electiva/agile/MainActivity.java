package com.example.electiva.agile;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer reproductor;
    private MediaPlayer opcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button jugar = (Button) findViewById(R.id.jugarbutton);
        Button ayuda = (Button) findViewById(R.id.ayudabutton);

        reproductor = MediaPlayer.create(this,R.raw.fondo);
        reproductor.setLooping(true);
        reproductor.start();

        jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcion = MediaPlayer.create(MainActivity.this, R.raw.opcion);
                opcion.start();
                startActivity(new Intent(MainActivity.this, game.class));
            }
        });

        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcion = MediaPlayer.create(MainActivity.this, R.raw.opcion);
                opcion.start();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reproductor.isPlaying()){
            reproductor.stop();
            reproductor.release();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        reproductor.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        reproductor.pause();
    }
}
