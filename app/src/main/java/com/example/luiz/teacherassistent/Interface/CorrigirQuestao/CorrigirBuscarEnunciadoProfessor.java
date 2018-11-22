package com.example.luiz.teacherassistent.Interface.CorrigirQuestao;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.luiz.teacherassistent.Helper.ProcessSingleImageTask;
import com.example.luiz.teacherassistent.Helper.RealPathUtil;
import com.example.luiz.teacherassistent.Helper.api.DetectionResult;
import com.example.luiz.teacherassistent.Interface.Menus.MenuAluno;
import com.example.luiz.teacherassistent.Interface.Menus.MenuProfessor;
import com.example.luiz.teacherassistent.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

/**
 * Created by Chico on 28/02/2018.
 */

public class CorrigirBuscarEnunciadoProfessor extends AppCompatActivity{
    //botões
    private FloatingActionButton continuarFuncao;
    private ImageView imagemEnunciado;
    private File imageFile;
    private String realPath;
    private String latex;
    private WebView mWebView;
    private Button fotoEnunciado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.layout_enunciado);
        fotoEnunciado = (Button) findViewById(R.id.fotoEnunciado);
        imagemEnunciado = (ImageView) findViewById(R.id.fotoEnunciadoMostra);
        mWebView = (WebView) findViewById(R.id.webViewEnun);
        fotoEnunciado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. on Upload click call ACTION_GET_CONTENT intent
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                // 2. pick image only
                intent.setType("image/*");
                // 3. start activity
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(CorrigirBuscarEnunciadoProfessor.this,MenuProfessor.class);
        startActivity(intent);
    }
}
