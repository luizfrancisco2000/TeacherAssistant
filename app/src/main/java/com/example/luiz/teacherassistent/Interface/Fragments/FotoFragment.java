package com.example.luiz.teacherassistent.Interface.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.luiz.teacherassistent.Helper.ContextParse;
import com.example.luiz.teacherassistent.Helper.OCR.OCRClass;
import com.example.luiz.teacherassistent.Helper.ProcessSingleImageTask;
import com.example.luiz.teacherassistent.Helper.RealPathUtil;
import com.example.luiz.teacherassistent.Helper.api.DetectionResult;
import com.example.luiz.teacherassistent.Interface.CadastroQuestao.CadastrarEnunciado;
import com.example.luiz.teacherassistent.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;

public class FotoFragment extends Fragment implements View.OnClickListener {

    private Button fotoEnunciado;
    private String realPath;
    private File imageFile;
    private ViewGroup viewGroup;
    public static String latex;
    private WebView mWebView;
    private Activity activity;
    private ImageView imagemEnunciado;
    private final int PERMISSAO_REQUEST = 2;
    private Context applicationContext;
    private Activity application;

    public FotoFragment() {
        // Required empty public constructor
        Log.d("TESTE FRAGMENT", "EAE");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = container;
        applicationContext = ContextParse.getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_foto, container, false);
        fotoEnunciado = (Button) view.findViewById(R.id.fotoEnunciado);
        imagemEnunciado = (ImageView) view.findViewById(R.id.fotoEnunciadoMostra);
        mWebView = (WebView) view.findViewById(R.id.webViewEnun);
        fotoEnunciado.setOnClickListener(this);
        return view;

    }

    /**
     * Parte de processamento de texto
     * Por favor não mexa
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {

            // SDK < API11
            if (Build.VERSION.SDK_INT < 11)
                realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(applicationContext, data.getData());

                // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19)
                realPath = RealPathUtil.getRealPathFromURI_API11to18(applicationContext, data.getData());

                // SDK > 19 (Android 4.4)
            else
                realPath = RealPathUtil.getRealPathFromURI_API19(applicationContext, data.getData());


            setTextViews(Build.VERSION.SDK_INT, data.getData().getPath(), realPath);
        }
    }
    private void setTextViews(int sdk, String uriPath, String realPath) {

        imageFile = new File(realPath);
        Uri uriFromPath = Uri.fromFile(imageFile);
        String resultFile = realPath.substring(realPath.lastIndexOf(System.getProperty("file.separator")) + 1, realPath.length());
        // you have two ways to display selected image

        // ( 1 ) imageView.setImageURI(uriFromPath);
        // ( 2 ) imageView.setImageBitmap(bitmap);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(applicationContext.getContentResolver().openInputStream(uriFromPath));
            if (imageFile.exists()) {
                DetectionResult detectionResult = new ProcessSingleImageTask().execute(imageFile).get();
                Log.d("Mostra", detectionResult.latex);
                latex = detectionResult.latex;
                if (latex.equals("")) {
                    OCRClass ocr = new OCRClass(applicationContext,bitmap);
                    latex = String.valueOf(ocr.readToImage());
                    String test = loadLocalContent();
                } else {

                    Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                    imagemEnunciado.setImageBitmap(bitmapReduzido);
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

    public void validarPermissao() {
        if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(application, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(application, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSAO_REQUEST);
            }
        }

    }

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
        String htmlString = localHTML(applicationContext);
        String newHTML = procuraP(htmlString);
        mWebView.loadDataWithBaseURL(localURL, newHTML, "text/html", "UTF-8", null);
        return latex;
    }

    public String procuraP(String p) {
        String aux = null;
        for (int i = 0; i < p.length(); i++) {
            if (p.charAt(i) == '<' && p.charAt(i + 1) == 'p' && p.charAt(i + 2) == '>') {
                aux = p.substring(0, i + 3);
                aux += "\\[ " + latex + " \\]";
            }
            if (p.charAt(i) == '<' && p.charAt(i + 1) == '/' && p.charAt(i + 2) == 'p' && p.charAt(i + 3) == '>') {
                aux += p.substring(i, p.length());
            }
        }
        if (aux != null) {
            Log.d("NOVO HTML", aux);
            return aux;
        } else {
            return "TEXT NOT FOUND";
        }
    }

    public String localHTML(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream json;
        try {
            if (context.getAssets().open("test/index.html") == null) {
                Log.d("Dont", "Existe");
            } else {
                Log.w("Funcionou", "oooooooooooo");
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
        Toast.makeText(applicationContext, errMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fotoEnunciado: {
                Log.d("Click", "CLICK");
                // 1. on Upload click call ACTION_GET_CONTENT intent
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                // 2. pick image only
                intent.setType("image/*");
                // 3. start activity
                startActivityForResult(intent, 0);
                break;
            }
        }
    }
}
