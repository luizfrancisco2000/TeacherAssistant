package com.example.luiz.teacherassistent.Interface.CadastroQuestao;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.example.luiz.teacherassistent.Helper.ProcessSingleImageTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.luiz.teacherassistent.Controle.Questao;
import com.example.luiz.teacherassistent.Helper.Base64Custom;
import com.example.luiz.teacherassistent.Helper.RealPathUtil;
import com.example.luiz.teacherassistent.Helper.api.DetectionResult;
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
    // ferramentas
    private TextRecognizer ocrResolucao;
    private Bitmap imageGaleria;
    private File imageFile;
    private String realPath;

    /**Processamento de textos
     * */
    private WebView mWebView;
    private String latex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_resolucao);
        validarPermissao();
        mWebView = (WebView) findViewById(R.id.webViewRes);
        fotoResolucao = (Button) findViewById(R.id.FotoResolucao);
        editResolucao = (EditText) findViewById(R.id.ResolucaoEdit);
        imagemResolucao = (ImageView) findViewById(R.id.imageResourceID);
        concluirCadastro = (FloatingActionButton) findViewById(R.id.ConcluirProcesso);
        questao = Questao.getInstance();
        fotoResolucao.setOnClickListener(new View.OnClickListener() {
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
        concluirCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questao.convertStringForArray(latex);
                for(ArrayList<String> e:questao.getResolucao()){
                    for(String b: e){
                        Log.d("Texto", b);
                    }
                }
                if (questao.getResolucao().size() == 1) {
                    questao.salvar();
                    Intent intent = new Intent(CadastrarResolucao.this, MenuProfessor.class);
                    Toast.makeText(CadastrarResolucao.this, "Cadastro realizado com sucesso\n retomando ao menu", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    } else {
                      atualizarBanco();
                }

            }
        });
    }

    public void validarPermissao() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSAO_REQUEST);
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
                String test = loadLocalContent();
                Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                imagemResolucao.setImageBitmap(bitmapReduzido);
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

    private File getTestFile(String filename) {
        AssetManager assetManager = getAssets();


        InputStream in;
        OutputStream out;
        try {
            in = assetManager.open(filename);
            File cloneFile = new File("/data/data/" + getPackageName() + "/" + filename);
            if(cloneFile.exists()) return cloneFile;

            out = new FileOutputStream(cloneFile);

            byte[] buffer = new byte[1024];
            int read;
            while((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();

            return cloneFile;

        } catch (Exception e) {
            Log.d("aa",e.getMessage());
            e.printStackTrace();
            return null;
        }
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

    private void showErrorAndReset(String errMessage) {
        Toast.makeText(getApplicationContext(), errMessage, Toast.LENGTH_LONG).show();
    }
}
