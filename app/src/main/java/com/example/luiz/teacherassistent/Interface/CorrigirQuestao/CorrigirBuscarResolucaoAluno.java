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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luiz.teacherassistent.Controle.Correcao;
import com.example.luiz.teacherassistent.Helper.ProcessSingleImageTask;
import com.example.luiz.teacherassistent.Helper.RealPathUtil;
import com.example.luiz.teacherassistent.Helper.api.DetectionResult;
import com.example.luiz.teacherassistent.Interface.ForumUsuarios.ForumAluno;
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
    private EditText resolucaoAluno;
    private ImageView fotoResolucaoAluno;
    private FloatingActionButton concluir;
    private Button buttonEscolherFoto;
    private File imageFile;
    private String realPath;
    private String latex;
    private WebView mWebView;
    private final int  PERMISSAO_REQUEST =2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_resolucao);
        validarPermissao();
        resolucaoAluno = (EditText) findViewById(R.id.ResolucaoEdit);
        buttonEscolherFoto = (Button) findViewById(R.id.FotoResolucao);
        fotoResolucaoAluno = (ImageView) findViewById(R.id.imageResourceID);
        concluir = (FloatingActionButton) findViewById(R.id.ConcluirProcesso);
        correcao = Correcao.getInstance();
        Log.d("Teste",correcao.getResolucaoCorreta().get(0).toString());
        mWebView = (WebView) findViewById(R.id.webViewRes);
        buttonEscolherFoto.setOnClickListener(new View.OnClickListener() {
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
        concluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { ;
                String erroString="";
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {

            // SDK < API11
            if (Build.VERSION.SDK_INT < 11)
                realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());

                // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19)
                realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());

                // SDK > 19 (Android 4.4)
            else
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());


            setTextViews(Build.VERSION.SDK_INT, data.getData().getPath(), realPath);
        }
    }

    private void setTextViews(int sdk, String uriPath, String realPath) {
        imageFile = new File(realPath);
        Uri uriFromPath = Uri.fromFile(imageFile);
        String resultFile = realPath.substring(realPath.lastIndexOf(System.getProperty("file.separator"))+1,realPath.length());
        // you have two ways to display selected image

        // ( 1 ) imageView.setImageURI(uriFromPath);
        // ( 2 ) imageView.setImageBitmap(bitmap);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriFromPath));
            if (imageFile.exists()) {
                DetectionResult detectionResult = new ProcessSingleImageTask().execute(imageFile).get();
                Log.d("Mostra", detectionResult.latex);
                latex = detectionResult.latex;
                if (latex.equals("")) {
                    Toast.makeText(this, "Erro ao ler a imagem...\n Por favor verique que o cálculo está aparecendo ou tire outra foto ", Toast.LENGTH_SHORT).show();
                } else {
                    String test = loadLocalContent();
                    Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                    fotoResolucaoAluno.setImageBitmap(bitmapReduzido);
                }
            } else {
                Log.d("a", "arquivo não existe");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Log.d("HMKCODE", "Build.VERSION.SDK_INT:" + sdk);
        Log.d("HMKCODE", "URI Path:" + uriPath);
        Log.d("HMKCODE", "Real Path: " + realPath);
    }
    /**Parte de processamento de texto
     * Por favor não mexa*/

    public String loadLocalContent() {
        mWebView.setVisibility(View.VISIBLE);
        mWebView.getSettings().setJavaScriptEnabled(true);
        WebSettings settings = mWebView.getSettings();
        /*mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (mWebView != null) {
                    //Log.w("seila","aaaaa");
                    StringBuilder stringBuilder = new StringBuilder();
                    InputStream json;
                    try {
                        json = getApplicationContext().getAssets().open("MathJax.js");
                        BufferedReader in = new BufferedReader(new InputStreamReader(json));
                        String str;
                        while ((str = in.readLine()) != null) {
                            stringBuilder.append(str);
                        }
                        mWebView.loadUrl(str);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{

                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });*/
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        if (Build.VERSION.SDK_INT >= 16) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        String localURL = "file:///android_asset";
        String htmlString = localHTML(getApplicationContext());
        String newHTML = procuraP(htmlString);
        mWebView.loadDataWithBaseURL(localURL, newHTML, "text/html", "UTF-8", null);
        return latex;
    }

    public String procuraP(String p){
        String aux=null;
        for(int i=0;i<p.length();i++){
            if(p.charAt(i)=='<' && p.charAt(i+1)=='p' && p.charAt(i+2)=='>'){
                aux = p.substring(0,i+3);
                aux+="\\[ "+latex+" \\]";
            }
            if(p.charAt(i)=='<' && p.charAt(i+1)=='/' && p.charAt(i+2)=='p' && p.charAt(i+3)=='>'){
                aux+=p.substring(i,p.length());
            }
        }
        if(aux!=null){
            Log.d("NOVO HTML",aux);
            return aux;
        }else{
            return "TEXT NOT FOUND";
        }
    }
    public String localHTML(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream json;
        try {
            if(context.getAssets().open("test/index.html")==null){
                Log.d("Dont","Existe");
            }else{
                Log.w("Funcionou","oooooooooooo");
            }
            json = context.getAssets().open("test/index.html");
            BufferedReader in = new BufferedReader(new InputStreamReader(json));
            String str;

            while ((str = in.readLine()) != null) {
                stringBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.w("pagina", stringBuilder.toString());
        return stringBuilder.toString();
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(CorrigirBuscarResolucaoAluno.this,CorrigirBuscarEnunciadoAluno.class);
        startActivity(intent);
    }
}
