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


import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")

public class game extends AppCompatActivity {

    TextView tiempo;
    private MediaPlayer pop;
    private MediaPlayer song;
    private MediaPlayer correct;
    private MediaPlayer wrong;
    public static int cantBotonesApretados= 0;
    public static Boolean sonidoOnOffG= MainActivity.sonidoOnOff;
    public static ArrayList<Button> botones = new ArrayList<>();
    public static ArrayList<Button> botonesApretados = new ArrayList<>();

    @Override

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        agregarBotonesALista();
        habilitarBotones(botones);
        pop = MediaPlayer.create(this,R.raw.pop);
        song = MediaPlayer.create(this,R.raw.song);
        correct = MediaPlayer.create(this,R.raw.correct);
        wrong = MediaPlayer.create(this,R.raw.wrong);
        song.setVolume(20, 20);
        song.start();
        if (!MainActivity.sonidoOnOff){

           song.stop();
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
        cantBotonesApretados = cantBotonesApretados + 1;
        Button idButton = (Button) v;
        String letraDelBoton = idButton.getText().toString();
        TextView operacion = (TextView) findViewById(R.id.operacion);
        String letrasQueTenia = operacion.getText().toString();

        if (MainActivity.sonidoOnOff){
        pop.start();
        }
        if(!botonYaFueApretado(idButton,botonesApretados)) {
            if (cantBotonesApretados < 5) {

                if (letraDelBoton.equals("=")) {
                    idButton.setBackground(getDrawable(R.drawable.btn_circle_anaranjado));
                } else if (!letraDelBoton.equals("=")) {
                    idButton.setBackground(getDrawable(R.drawable.btn_rectangle_azul));
                    botonesApretados.add(idButton);
                }

                operacion.setText(letrasQueTenia + letraDelBoton);
            } else if (cantBotonesApretados == 5) {/*codigo que evaluará la operación*/

                // botonesApretados.clear();
                operacion.setText(letrasQueTenia + letraDelBoton);
                simbolo = getSimbolo(operacion.getText().toString());
                primerNumero = getPrimerNumero(operacion.getText().toString(), simbolo);
                segundoNumero = getSegundoNumero(operacion.getText().toString(), simbolo);
                respuestaUsuario = getResultado(operacion.getText().toString());
                operacionEstaCorrecta = verificarOperacion(primerNumero, segundoNumero, simbolo, respuestaUsuario);
                animacionOperacion(operacion, operacionEstaCorrecta);


                if (!letraDelBoton.equals("=")) {
                    idButton.setBackground(getDrawable(R.drawable.btn_rectangle_azul));
                    botonesApretados.add(idButton);
                }
            } else {
                cantBotonesApretados = 1;
                operacion.setBackground(getDrawable(R.drawable.operacion_default));
                idButton.setBackground(getDrawable(R.drawable.btn_rectangle_azul));
                operacion.setText(letraDelBoton);
                botonesApretados.add(idButton);
            }
        }else{
            cantBotonesApretados--;
        }
        System.out.println("BOTON:+++++++++++++++++++++--------------------+++    "+botonesApretados.get(botonesApretados.size()-1).getText());
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
            if(MainActivity.sonidoOnOff) {
                correct.start();
            }
    }else{
            operacion.setBackground(getDrawable(R.drawable.operacion_incorrecta));
            if(MainActivity.sonidoOnOff) {
                wrong.start();
            }
        }
    }

    public void agregarBotonesALista(){

        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);
        Button button6 = (Button) findViewById(R.id.button6);
        Button button7 = (Button) findViewById(R.id.button7);
        Button button8 = (Button) findViewById(R.id.button8);
        Button button9 = (Button) findViewById(R.id.button9);
        Button button10 = (Button) findViewById(R.id.button10);
        Button button11 = (Button) findViewById(R.id.button11);
        Button button12 = (Button) findViewById(R.id.button12);
        Button button13 = (Button) findViewById(R.id.button13);
        Button button14 = (Button) findViewById(R.id.button14);
        Button button15 = (Button) findViewById(R.id.button15);
        Button button16 = (Button) findViewById(R.id.button16);
        Button button17 = (Button) findViewById(R.id.button17);
        Button button19 = (Button) findViewById(R.id.button19);
        Button button20 = (Button) findViewById(R.id.button20);
        Button button21 = (Button) findViewById(R.id.button21);
        Button button22 = (Button) findViewById(R.id.button22);
        Button button23 = (Button) findViewById(R.id.button23);
        Button button24 = (Button) findViewById(R.id.button24);
        Button button25 = (Button) findViewById(R.id.button25);
        Button button26 = (Button) findViewById(R.id.button26);
        Button button27 = (Button) findViewById(R.id.button27);
        Button button28 = (Button) findViewById(R.id.button28);
        Button button29 = (Button) findViewById(R.id.button29);
        Button button30 = (Button) findViewById(R.id.button30);
        Button button31 = (Button) findViewById(R.id.button31);
        Button button32 = (Button) findViewById(R.id.button32);
        Button button33 = (Button) findViewById(R.id.button33);
        Button button34 = (Button) findViewById(R.id.button34);
        Button button35 = (Button) findViewById(R.id.button35);
        botones.clear();
        botones.add(button1);
        botones.add(button2);
        botones.add(button3);
        botones.add(button4);
        botones.add(button5);
        botones.add(button6);
        botones.add(button7);
        botones.add(button8);
        botones.add(button9);
        botones.add(button10);
        botones.add(button11);
        botones.add(button12);
        botones.add(button13);
        botones.add(button14);
        botones.add(button15);
        botones.add(button16);
        botones.add(button17);
        botones.add(button19);
        botones.add(button20);
        botones.add(button21);
        botones.add(button22);
        botones.add(button23);
        botones.add(button24);
        botones.add(button25);
        botones.add(button26);
        botones.add(button27);
        botones.add(button28);
        botones.add(button29);
        botones.add(button30);
        botones.add(button31);
        botones.add(button32);
        botones.add(button33);
        botones.add(button34);
        botones.add(button35);
    }

    public void habilitarBotones(List<Button> listaBotones){
        //4 botones
       // int cantidadDeBotones = (int) (Math.floor(Math.random()*(8-1+1)+1))*4; //cantidad de botones q se van a habilitar,
                                                                                // se multiplican * 4 porque se necesita al menos 4 botones para hacer una operacion
        int cantidadDeBotones = 12;
        int[] indiceDeBotonesActivos = new int[35];
        String[] textoBotones= textoDeCadaBoton(cantidadDeBotones,1,5,4);
        int botonQueSeActivara;
        int i=0;
        int n=0;

        while (i <cantidadDeBotones){
            botonQueSeActivara =  (int) (Math.floor(Math.random()*(33-0+1)+0)); // este numero funcionara como indice dentro de la lista botones
            if(!existeElemento(indiceDeBotonesActivos,botonQueSeActivara)){
                indiceDeBotonesActivos[n]=botonQueSeActivara;
                listaBotones.get(botonQueSeActivara).setText(textoBotones[i]);

                listaBotones.get(botonQueSeActivara).setVisibility(View.VISIBLE);
                n++;
                i++;
            }
        }
    }
    public static Boolean existeElemento(int[] array,int valor){
        int i;
        Boolean resultado = false;
        for (i=0;i < array.length;i++){
            if (array[i]==valor){
                resultado= true;
            }
        }
        return  resultado;
    }
    public static String[] textoDeCadaBoton(int cantDeBotones,int NmasPequeno, int nMasGrande, int tiposDeSignos){// tipos de signos representa: 1 solo sumas, 2 sumas y restas, 3 sumas,restas y producto,
                                                                                                                    // 4 sumas, restas, producto y division
        String[] textoBotones = new String[cantDeBotones];
        int cantNumeros = cantDeBotones/2;
        int cantSignos = cantDeBotones/4;
        int cantSoluciones = cantSignos;
        int numerosBotones;
        int i=0;
        while (i < cantNumeros){ // este while carga el arreglo de numeros para ser operados
            numerosBotones =  (int) (Math.floor(Math.random()*(nMasGrande-NmasPequeno+1)+NmasPequeno));
            textoBotones[i]= String.valueOf(numerosBotones);
            i++;

        }
        while(i<cantNumeros+cantSignos){
            String signo = randomDeSigno(tiposDeSignos);
            textoBotones[i]=signo;
            i++;

        }
        int indiceOperando1 = 0;
        int indiceOperando2 =1;
        int indiceOperador = cantNumeros;
        int primerNumero;
        int segundoNumero;
        String operador;
        String solucionOperacion;
        while(i<cantDeBotones){
            primerNumero =Integer.parseInt(textoBotones[indiceOperando1]);
            segundoNumero = Integer.parseInt(textoBotones[indiceOperando2]);
            operador = textoBotones[indiceOperador ];
            if (primerNumero>=segundoNumero) {
                solucionOperacion = hacerOperacion(primerNumero, segundoNumero, operador);
                System.out.println("SOLUCION AGREGADA:------------------" + primerNumero + "   " +operador + "  " + segundoNumero+"    "+solucionOperacion);



            }else{
                solucionOperacion = hacerOperacion(segundoNumero,primerNumero, operador);
                System.out.println("SOLUCION AGREGADA:------------------" +segundoNumero  + "   " +operador + "  " + primerNumero+"    "+solucionOperacion);

            }
            textoBotones[i]=solucionOperacion;
            i++;
            indiceOperando1+=2;
            indiceOperando2+=2;
            indiceOperador++;

        }


        return textoBotones;
    }
    public static String randomDeSigno(int tiposDeSignos){
        String signo ;
        int numeroRandom = (int) (Math.floor(Math.random()*(tiposDeSignos-1+1)+1));
        if(numeroRandom==1){
            signo = "+";
        }else if(numeroRandom==2){
            signo ="-";
        }else if (numeroRandom ==3){
            signo = "*";
        }else{
            signo = "/";
        }

        return signo;
    }

    public static String hacerOperacion(int numeroUno,int numeroDos,String operacion){
        int resultado;

        if (operacion.equals("+")) {
            resultado = numeroUno+numeroDos;
        }else if (operacion.equals("-")){
            resultado = numeroUno-numeroDos;
        }else if (operacion.equals("*")){
            resultado = numeroUno*numeroDos;
        }else{
            resultado = numeroUno/numeroDos;
        }

        return  String.valueOf(resultado);
    }

    public static Boolean botonYaFueApretado(Button idButton, ArrayList<Button>botonesApretados){
        Boolean fueApretado= false;
        for(int i=0;i<botonesApretados.size();i++){
            if (botonesApretados.get(i).getId() == idButton.getId()){
                fueApretado=true;
            }
        }
        return  fueApretado;
    }
}

