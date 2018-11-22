package com.example.luiz.teacherassistent.Interface.CorrigirQuestao;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luiz.teacherassistent.Controle.Correcao;
import com.example.luiz.teacherassistent.Helper.ProcessSingleImageTask;
import com.example.luiz.teacherassistent.Helper.RealPathUtil;
import com.example.luiz.teacherassistent.Helper.api.DetectionResult;
import com.example.luiz.teacherassistent.Interface.ForumUsuarios.ForumAluno;
import com.example.luiz.teacherassistent.Interface.Fragments.EnunciadoFragment;
import com.example.luiz.teacherassistent.Interface.Fragments.FotoFragment;
import com.example.luiz.teacherassistent.Interface.Menus.MenuAluno;
import com.example.luiz.teacherassistent.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

public class CorrigirBuscarResolucaoAluno extends AppCompatActivity{
    Correcao correcao;
    private FloatingActionButton concluir;
    private RadioButton radioFoto;
    private RadioButton radioTeclado;
    private File imageFile;
    private String realPath;
    private String latex;
    private boolean fotos;
    private final int  PERMISSAO_REQUEST =2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_resolucao);
        validarPermissao();
        concluir = (FloatingActionButton) findViewById(R.id.ConcluirProcesso);
        radioFoto = (RadioButton) findViewById(R.id.radioFotoR);
        final FrameLayout frame = (FrameLayout) findViewById(R.id.containerForFragmentR);
        radioTeclado = (RadioButton) findViewById(R.id.radioTecladoR);
        radioFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frame.removeAllViews();
                FotoFragment fotoFragment = new FotoFragment();
                managerFragment(fotoFragment, "FOTO_FRAGMENT");
                fotos = true;

            }
        });
        radioTeclado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fotos=false;
                frame.removeAllViews();
                EnunciadoFragment tecladoFragment = new EnunciadoFragment();
                managerFragment(tecladoFragment, "TECLADO_FRAGMENT");
            }
        });
        correcao = Correcao.getInstance();
        Log.d("Teste",correcao.getResolucaoCorreta().get(0).toString());
       // mWebView = (WebView) findViewById(R.id.webViewRes);
       /* buttonEscolherFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. on Upload click call ACTION_GET_CONTENT intent
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                // 2. pick image only
                intent.setType("image/*");
                // 3. start activity
                startActivityForResult(intent, 0);
            }
        });*/
        radioFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frame.removeAllViews();
                FotoFragment fotoFragment = new FotoFragment();
                managerFragment(fotoFragment, "FOTO_FRAGMENT");
                fotos = true;

            }
        });
        radioTeclado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fotos=false;
                frame.removeAllViews();
                EnunciadoFragment tecladoFragment = new EnunciadoFragment();
                managerFragment(tecladoFragment, "TECLADO_FRAGMENT");
            }
        });

        concluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resposta="";
                String erroString="";
                /*String erroString="";
                if (latex==null) {
                    aviso();
                } else {
                    correcao.convertStringForArray(latex);
                    erroString = correcao.corrigir();
                    if(!erroString.equals("")){
                        correcao.setErro(erroString);
                        Log.d("IF CERTO", erroString);
                        Correcao.setInstance(correcao);
                        Intent intent = new Intent(CorrigirBuscarResolucaoAluno.this, ErradoAluno.class);
                        startActivity(intent);
                    }
                    else{
                        Log.d("IF ERRADO", erroString);
                        Intent intent = new Intent(CorrigirBuscarResolucaoAluno.this, CorretoAluno.class);
                        startActivity(intent);
                    }
                }

*/
                if(fotos){
                    resposta = FotoFragment.latex;
                }else{
                    resposta = EnunciadoFragment.texto;
                }
                if(resposta==""){
                    aviso();
                }else{
                    correcao.convertStringForArray(resposta);
                    erroString = correcao.corrigir();
                    if(!erroString.equals("")){
                        correcao.setErro(erroString);
                        Log.d("IF CERTO", erroString);
                        Correcao.setInstance(correcao);
                        Intent intent = new Intent(CorrigirBuscarResolucaoAluno.this, ErradoAluno.class);
                        startActivity(intent);
                    }
                    else{
                        Log.d("IF ERRADO", erroString);
                        Intent intent = new Intent(CorrigirBuscarResolucaoAluno.this, CorretoAluno.class);
                        startActivity(intent);
                    }
                }

            }
        });


    }

    private void aviso() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(CorrigirBuscarResolucaoAluno.this);
        alerta.setTitle("Atenção").setMessage("Por favor veja se tem algum campo incorreto");
        alerta.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                onStop();
            }
        });
        alerta.create().show();
    }
    public void validarPermissao(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){

            }else{
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSAO_REQUEST);
            }
        }

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(CorrigirBuscarResolucaoAluno.this,CorrigirBuscarEnunciadoAluno.class);
        startActivity(intent);
    }
    private void managerFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.containerForFragmentR, fragment, tag);
        fragmentTransaction.commit();
    }
}
