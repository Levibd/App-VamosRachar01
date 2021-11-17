package com.example.vamosrachar2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, TextToSpeech.OnInitListener {


    EditText edtValor,edtGalera;
    TextView    tvRes;
    FloatingActionButton share, tocar;
    TextToSpeech    ttsPlayer;
    int galera = 2;
    double valor = 0.0;
    String resFormatado = "0,00";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtValor = (EditText) findViewById(R.id.edtValor);
        edtValor.addTextChangedListener(this);
        edtGalera = (EditText) findViewById(R.id.edtGalera);
        edtGalera.addTextChangedListener(this);


        tvRes = (TextView) findViewById(R.id.textViewResultado);
        share = (FloatingActionButton) findViewById(R.id.share);
        share.setOnClickListener(this);
        tocar = (FloatingActionButton) findViewById(R.id.tocar);
        tocar.setOnClickListener(this);

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, 1122);



    }

    protected   void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1122) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {

                ttsPlayer = new TextToSpeech(this, this);
            } else {

                Intent installTTSIntent = new Intent();
                installTTSIntent
                        .setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    protected void calcular() {

        DecimalFormat df = new DecimalFormat("#.00");

        try {
            galera = Integer.parseInt(edtGalera.getText().toString());
            valor = Double.parseDouble(edtValor.getText().toString());
            if (galera != 0) {
                resFormatado = df.format(valor/galera);
                tvRes.setText("R$ "+ resFormatado);

            } else {
                tvRes.setText("R$0,00");
            }
        } catch (Exception e) {
            Log.v("PDM","sem valores corretos");

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        calcular();


    }

    @Override
    public void onClick(View view) {
        if (view==share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "A conta dividida por pessoa deu "+tvRes.getText().toString());
            startActivity(intent);
        }

        if (view == tocar){
            if (ttsPlayer != null) {
                ttsPlayer.speak("The value per person is " + resFormatado + "reais.", TextToSpeech.QUEUE_FLUSH,null, "ID1");
            }
        }

    }

    @Override
    public void onInit(int initStatus) {

        if (initStatus == TextToSpeech.SUCCESS) {
            Toast.makeText(this, "TTS ativado",
                    Toast.LENGTH_LONG).show();
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sem TTS habilitado", Toast.LENGTH_LONG).show();
        }

    }
}