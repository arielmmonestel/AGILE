package com.example.electiva.agile;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")

public class game extends AppCompatActivity {

    TextView tiempo;
    private MediaPlayer pop;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        pop = MediaPlayer.create(this,R.raw.pop);
        tiempo = (TextView)findViewById(R.id.tiempo);
        tiempo.setText("00:03:00");
        final CounterClass timer = new CounterClass(180000,1000);
        timer.start();
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)

    @SuppressLint("NewApi")

    public class CounterClass extends CountDownTimer{
        public CounterClass(long millisInfuture, long countDownInterval){
            super(millisInfuture,countDownInterval);
        }
        public  void onTick(long millisUntilFinished){
            long millis = millisUntilFinished;
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

            tiempo.setText(hms);
        }
        public void onFinish(){
        tiempo.setText("00:00:00");
        }

    }

    public void colocarNumero(View v){
        Button idButton = (Button)v;
        pop.start();
        CharSequence letraDelBoton = idButton.getText();
        TextView operacion = (TextView) findViewById(R.id.operacion);
        String letrasQueTenia = operacion.getText().toString();
        operacion.setText(letrasQueTenia+letraDelBoton);
    }
}
