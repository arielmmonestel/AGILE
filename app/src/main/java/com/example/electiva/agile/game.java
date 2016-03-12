package com.example.electiva.agile;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
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
    private MediaPlayer song;
    private MediaPlayer correct;
    private MediaPlayer wrong;
    public static int botonesApretados= 0;
    public static Boolean sonidoOnOffG= MainActivity.sonidoOnOff;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Heeeeeeeey MOOOOOOOOOOOOOOOOOOOM");
        System.out.println(sonidoOnOffG);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        pop = MediaPlayer.create(this,R.raw.pop);
        song = MediaPlayer.create(this,R.raw.song);
        correct = MediaPlayer.create(this,R.raw.correct);
        wrong = MediaPlayer.create(this,R.raw.wrong);
        song.setVolume(20, 20);
        ;
        if (sonidoOnOffG){
            song.start();
        }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (song.isPlaying()){
            song.stop();
            song.release();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        song.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        song.pause();
    }

    public void colocarNumero(View v) {
        int primerNumero = 0;
        int segundoNumero = 0;
        String simbolo= "";
        int respuestaUsuario = 0;
        boolean operacionEstaCorrecta;
        botonesApretados = botonesApretados + 1;
        Button idButton = (Button) v;
        String letraDelBoton = idButton.getText().toString();
        TextView operacion = (TextView) findViewById(R.id.operacion);
        String letrasQueTenia = operacion.getText().toString();
        if (sonidoOnOffG){
        pop.start();
        }

        if (botonesApretados <5) {

            if (letraDelBoton.equals("=")) {
                idButton.setBackground(getDrawable(R.drawable.btn_circle_anaranjado));
            } else if (!letraDelBoton.equals("=")) {
                idButton.setBackground(getDrawable(R.drawable.btn_rectangle_azul));
            }

            operacion.setText(letrasQueTenia + letraDelBoton);
        }
        else if (botonesApretados ==5){/*codigo que evaluará la operación*/


            operacion.setText(letrasQueTenia + letraDelBoton);
            simbolo = getSimbolo(operacion.getText().toString());
            primerNumero = getPrimerNumero(operacion.getText().toString(), simbolo);
            segundoNumero = getSegundoNumero(operacion.getText().toString(), simbolo);
            respuestaUsuario = getResultado(operacion.getText().toString());
            operacionEstaCorrecta = verificarOperacion(primerNumero, segundoNumero, simbolo, respuestaUsuario);
            animacionOperacion(operacion, operacionEstaCorrecta);

            //operacion.setText("");
           if (!letraDelBoton.equals("=")) {
                idButton.setBackground(getDrawable(R.drawable.btn_rectangle_azul));
            }
        }else{
            botonesApretados=1;
            operacion.setBackground(getDrawable(R.drawable.operacion_default));
            operacion.setText(letraDelBoton);
        }
    }

    public static boolean verificarOperacion(int numeroUno,int numeroDos,String operacion,int resultadoUsuario){
        int resultado;
        boolean correcto = false;
        if (operacion.equals("+")) {
            resultado = numeroUno+numeroDos;
        }else if (operacion.equals("-")){
            resultado = numeroUno-numeroDos;
        }else if (operacion.equals("*")){
            resultado = numeroUno*numeroDos;
        }else{
            resultado = numeroUno/numeroDos;
        }
        if(resultado==resultadoUsuario){
            correcto = true;
        }
        return correcto;
    }

    public static String getSimbolo(String cadena){
        String simbolo = "";

        if(cadena.indexOf("+") != -1){
            simbolo = "+";
        }else if (cadena.indexOf("-") != -1){
            simbolo = "-";
        }else if (cadena.indexOf("*") != -1){
            simbolo = "*";
        }else {
            simbolo = "/";
        }

        return  simbolo;
    }
    public static int getPrimerNumero(String cadena,String simbolo){
        int primerNumero = 0;
        int indiceSimbolo = cadena.indexOf(simbolo);
        primerNumero=  Integer.parseInt(cadena.substring(0,indiceSimbolo));
        return primerNumero;
    }

    public static int getSegundoNumero(String cadena,String simbolo){
        int segundoNumero = 0;
        int indiceSimbolo = cadena.indexOf(simbolo);
        int indiceIgual = cadena.indexOf("=");
        segundoNumero =  Integer.parseInt(cadena.substring(indiceSimbolo+1,indiceIgual));
        return segundoNumero ;
    }

    public static int getResultado(String cadena){
        int resultado = 0;
        int indiceIgual = cadena.indexOf("=");
        resultado =  Integer.parseInt(cadena.substring(indiceIgual+1,cadena.length()));
        return resultado ;
    }
    public  void animacionOperacion(TextView operacion,boolean correcto){
        if (correcto){
        operacion.setBackground(getDrawable(R.drawable.operacion_correcta));
            correct.start();
    }else{
            operacion.setBackground(getDrawable(R.drawable.operacion_incorrecta));
            wrong.start();
        }
    }
}
