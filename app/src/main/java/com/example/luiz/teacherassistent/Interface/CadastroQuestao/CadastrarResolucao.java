package com.example.luiz.teacherassistent.Interface.CadastroQuestao;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.example.luiz.teacherassistent.Helper.ContextParse;
import com.example.luiz.teacherassistent.Helper.ProcessSingleImageTask;

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
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.luiz.teacherassistent.Controle.Questao;
import com.example.luiz.teacherassistent.Helper.Base64Custom;
import com.example.luiz.teacherassistent.Helper.RealPathUtil;
import com.example.luiz.teacherassistent.Helper.api.DetectionResult;
import com.example.luiz.teacherassistent.Interface.CorrigirQuestao.CorrigirBuscarEnunciadoAluno;
import com.example.luiz.teacherassistent.Interface.Fragments.EnunciadoFragment;
import com.example.luiz.teacherassistent.Interface.Fragments.FotoFragment;
import com.example.luiz.teacherassistent.Interface.Menus.MenuProfessor;
import com.example.luiz.teacherassistent.R;
import com.example.luiz.teacherassistent.Servidor.ConfiguracaoDataBase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DatabaseReference;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import junit.framework.Assert;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class CadastrarResolucao extends AppCompatActivity {
    //botões
    private Button fotoResolucao;
    private EditText editResolucao;
    private ImageView imagemResolucao;
    protected final int GALERIA_IMAGENS = 1;
    private FloatingActionButton concluirCadastro;
    //constantes e variaveis
    private final int PERMISSAO_REQUEST = 2;
    private Questao questao;
    private RadioButton radioFoto;
    private RadioButton radioTeclado;
    // ferramentas
    private TextRecognizer ocrResolucao;
    private Bitmap imageGaleria;
    private File imageFile;
    private String realPath;
    private boolean fotos;
    /**
     * Processamento de textos
     */
    private WebView mWebView;
    private String latex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_resolucao);
        ContextParse.setContext(getApplicationContext());
        validarPermissao();
        concluirCadastro = (FloatingActionButton) findViewById(R.id.ConcluirProcesso);
        questao = Questao.getInstance();
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
        concluirCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resposta="";
                if(fotos){
                    resposta = FotoFragment.latex;
                }else{
                    resposta = EnunciadoFragment.texto;
                }
                questao.convertStringForArray(resposta);
                if (resposta == "") {
                    aviso();
                } else {
                    if (questao.getResolucao().size() == 1) {
                        questao.salvar();
                        Intent intent = new Intent(CadastrarResolucao.this, MenuProfessor.class);
                        Toast.makeText(CadastrarResolucao.this, "Cadastro realizado com sucesso\n retomando ao menu", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    } else {
                        atualizarBanco();
                    }
                }

            }
        });
    }
    private void aviso() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(CadastrarResolucao.this);
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
    public void validarPermissao() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSAO_REQUEST);
            }
        }

    }
    public void atualizarBanco() {
        DatabaseReference datbase = ConfiguracaoDataBase.getFirebase();
        Map<String, Object> questaoSalvar = questao.toMap();
        Map<String, Object> questaoAtualizacoes = new HashMap<>();
        questaoAtualizacoes.put("/questao/" + questao.getMateria() + "/" + questao.getAssunto() + "/", questaoSalvar);
        datbase.updateChildren(questaoAtualizacoes).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Atualicao", "Atualização feita com sucesso");
                Intent intent = new Intent(CadastrarResolucao.this, MenuProfessor.class);
                startActivity(intent);
            }
        });
    }

    /*imageGaleria = (BitmapFactory.decodeFile(picturePath));
                MediaType mediaType = MediaType.parse("application/json");

                //RequestBody body = RequestBody.create(mediaType, Base64Custom.codificarBase64(filePath[0]));
                RequestBody body = RequestBody.create(mediaType, "{ \"src\" : \"data:image/jpeg;base64,{BASE64-STRING}\" }");
                Request request = new Request.Builder()
                        .url("https://api.mathpix.com/v3/latex")
                        .addHeader("content-type", "application/json")
                        .addHeader("app_id", "mathpix")
                        .addHeader("app_key", "34f1a4cea0eaca8540c95908b4dc84ab")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    //editResolucao.setText(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    Log.d("aa",e.getMessage());
                }
                ocrResolucao = new TextRecognizer.Builder(imagemResolucao.getContext()).build();
                if(!ocrResolucao.isOperational()){
                    Log.w("Cadastro","Detector dependecies are not yet avaiable");
                }else{
                    Frame frame = new Frame.Builder().setBitmap(imageGaleria).build();
                    SparseArray<TextBlock> itens = ocrResolucao.detect(frame);
                    StringBuilder text = new StringBuilder();
                    for (int i = 0; i < itens.size();++i) {
                        TextBlock item = itens.valueAt(i);
                        text.append(item.getValue());
                        text.append("\n");
                    }
                    editResolucao.setText(text);
                }
                Bitmap bitmapReduzido = Bitmap.createScaledBitmap(imageGaleria, 100, 100, true);
                imagemResolucao.setImageBitmap(bitmapReduzido);
                */

    private void showErrorAndReset(String errMessage) {
        Toast.makeText(getApplicationContext(), errMessage, Toast.LENGTH_LONG).show();
    }

    private void managerFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.containerForFragmentR, fragment, tag);
        fragmentTransaction.commit();
    }
}
