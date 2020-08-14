package com.example.apprachar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, TextToSpeech.OnInitListener {

EditText pessoas;
EditText dinheiro;
TextView divisao;
TextToSpeech ttsPlayer;
FloatingActionButton share, play;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pessoas = (EditText) findViewById(R.id.pessoasText);
        dinheiro = (EditText) findViewById(R.id.dinheiroText);
        divisao = (TextView) findViewById(R.id.divisaoText);
        pessoas.addTextChangedListener(this);
        dinheiro.addTextChangedListener(this);
        share = (FloatingActionButton) findViewById(R.id.shareBtn);
        share.setOnClickListener(this);
        play = (FloatingActionButton) findViewById(R.id.playBtn);
        play.setOnClickListener(this);

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent,1122);


    }
protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1122){
        if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
            ttsPlayer = new TextToSpeech(this,this);
        }else{
            Intent installTTSIntent = new Intent();
            installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            startActivity(installTTSIntent);
        }

    }
}
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    public void afterTextChanged(Editable s){
       try {
           double din = Double.parseDouble(dinheiro.getText().toString());
           int pes = Integer.parseInt(pessoas.getText().toString());
           din = din/pes;
           DecimalFormat df = new DecimalFormat("#.00");
           divisao.setText("R$ "+df.format(din));

       } catch (Exception e) {
           divisao.setText("R$ 0.00");
       }
    }

    @Override
    public void onClick(View view) {
    if (view==share)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"O valor da conta para cada pessoa será: "+divisao.getText().toString());
        startActivity(intent);
    }
    if (view ==play){
        if (ttsPlayer!= null){
            ttsPlayer.speak("O valor da conta para cada pessoa será de " + divisao.getText().toString(),TextToSpeech.QUEUE_FLUSH,null,"ID");

        }
    }
    }

    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            Toast.makeText(this, "TTS ativado...",
                    Toast.LENGTH_LONG).show();
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sem TTS habilitado...",
                    Toast.LENGTH_LONG).show();
        }

    }
}