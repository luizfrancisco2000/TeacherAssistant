package com.example.luiz.teacherassistent.Interface.CadastroQuestao;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luiz.teacherassistent.Controle.Professor;
import com.example.luiz.teacherassistent.Controle.Questao;
import com.example.luiz.teacherassistent.Helper.ProcessSingleImageTask;
import com.example.luiz.teacherassistent.Helper.RealPathUtil;
import com.example.luiz.teacherassistent.Helper.api.DetectionResult;
import com.example.luiz.teacherassistent.Interface.LoginUsuarios.LoginProfessor;
import com.example.luiz.teacherassistent.Interface.Menus.MenuProfessor;
import com.example.luiz.teacherassistent.R;
import com.example.luiz.teacherassistent.Servidor.ConfiguracaoDataBase;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Chico on 28/02/2018.
 */

public class CadastrarEnunciado extends AppCompatActivity{
    //botões
    private Button fotoEnunciado;
    private EditText editEnunciado;
    private Spinner assuntos;
    private  ImageView imagemEnunciado;
    private final int GALERIA_IMAGENS = 1;
    private  FloatingActionButton continuarCadastro;
    private RadioButton radioFisica;
    private RadioButton radioQuimica;
    private RadioButton radioMatematica;
    private RadioGroup materiasRadio;
    private TextView disciplina;
    private AlertDialog alert;
    //constantes e variaveis
    private final int  PERMISSAO_REQUEST =2;
    Questao questao;
    Professor professor;
    // ferramentas
    private TextRecognizer ocrEnunciado;
    private Bitmap imageGaleria;
    private String latex;
    // ferramentas
    private TextRecognizer ocrResolucao;
    private File imageFile;
    private String realPath;
    private WebView mWebView;
    @Override
    protected void onCreate(final Bundle onSaveInstanceState){
        super.onCreate(onSaveInstanceState);
        super.setContentView(R.layout.layout_enunciado);
        fotoEnunciado = (Button) findViewById(R.id.fotoEnuciado);
        editEnunciado = (EditText) findViewById(R.id.enunciadoEditText);
        continuarCadastro = (FloatingActionButton) findViewById(R.id.ContinuarProcessoCad);
        imagemEnunciado = (ImageView) findViewById(R.id.fotoEnunciadoMostra);
        radioFisica = (RadioButton) findViewById(R.id.radioFisica);
        radioMatematica = (RadioButton) findViewById(R.id.radioMatematica);
        radioQuimica = (RadioButton) findViewById(R.id.radioQuimica);
        materiasRadio = (RadioGroup) findViewById(R.id.radioMaterias);
        disciplina = (TextView) findViewById(R.id.DisciplinaProf);
        assuntos = (Spinner) findViewById(R.id.assunto);
        mWebView = (WebView) findViewById(R.id.webViewEnun);
        validarPermissao();
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
        professor = Professor.getInstance();

        Log.d("Teste",professor.getMateria());
        if(!professor.equals(null)){
            if(professor.getMateria().equals("fisica")){
                radioFisica.setChecked(true);
            }else if(professor.getMateria().equals("quimica")){
                radioFisica.setChecked(true);
            }else if(professor.getMateria().equals("matematica")){
                radioFisica.setChecked(true);
            }
        }
        else{
            Intent intent = new Intent(CadastrarEnunciado.this, MenuProfessor.class);
            startActivity(intent);
        }
        if(radioFisica.isChecked()){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CadastrarEnunciado.this,R.array.fisica_assuntos,R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            assuntos.setAdapter(adapter);
        }else if(radioQuimica.isChecked()){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CadastrarEnunciado.this,R.array.quimica_assuntos,R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            assuntos.setAdapter(adapter);
        }else if(radioMatematica.isChecked()){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CadastrarEnunciado.this,R.array.matematica_assuntos,R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            assuntos.setAdapter(adapter);
        }
        radioQuimica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    radioQuimica.setChecked(true);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CadastrarEnunciado.this,R.array.quimica_assuntos,R.layout.support_simple_spinner_dropdown_item);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    assuntos.setAdapter(adapter);
            }
        });
        radioFisica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioFisica.setChecked(true);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CadastrarEnunciado.this,R.array.fisica_assuntos,R.layout.support_simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                assuntos.setAdapter(adapter);
            }
        });
        radioMatematica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioMatematica.setChecked(true);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CadastrarEnunciado.this,R.array.matematica_assuntos,R.layout.support_simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                assuntos.setAdapter(adapter);
            }
        });
        continuarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.stopLoading();
                questao = new Questao();
                questao.setEnunciado(latex);
                //professor.setAtivo(false);
                if (radioFisica.isChecked()) {
                    questao.setMateria("fisica");
                    int aux;
                    questao.setAssunto(assuntos.getSelectedItem().toString());
                    procurarQuestao();
                }else if (radioMatematica.isChecked()) {
                    questao.setMateria("matematica");
                    questao.setAssunto(assuntos.getSelectedItem().toString());
                    procurarQuestao();
                } else if (radioQuimica.isChecked()) {
                    questao.setAssunto(assuntos.getSelectedItem().toString());
                    questao.setMateria("quimica");
                    procurarQuestao();
                } else {
                    disciplina.setText(disciplina.getText() + "     Campo Obrigatório");
                    disciplina.setTextColor(Color.RED);
                    onCreate(onSaveInstanceState);
                }
            }
        });
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
                imagemEnunciado.setImageBitmap(bitmapReduzido);
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
    public void validarPermissao(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){

            }else{
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSAO_REQUEST);
            }
        }

    }
    public void buscarBancoDeDados(){
        DatabaseReference mRef =  FirebaseDatabase.getInstance().getReference();
        Log.d("Erro1", questao.getEnunciado());
        mRef.child("questao").child(questao.getMateria()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Questao questao = dataSnapshot.getValue(Questao.class);
                    erroDeBusca(questao);
                }
                else{
                    Intent intent = new Intent(CadastrarEnunciado.this, CadastrarResolucao.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void erroDeBusca(Questao questao) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(CadastrarEnunciado.this);
        alerta.setTitle("Atenção").setMessage("Questao já cadsatrada" + questao.getMateria() + "\nDeseja continuar?");
        alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(CadastrarEnunciado.this, CadastrarResolucao.class);
                startActivity(intent);
            }
        });
        alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
    }



    private void procurarQuestao() {
        DatabaseReference salve = ConfiguracaoDataBase.getFirebase();
        salve.child("questao").child(String.valueOf(questao.getMateria())).child(
                String.valueOf(questao.getAssunto())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        try{
                            if(dataSnapshot.getValue(Questao.class).getEnunciado()!=null){
                                if(dataSnapshot.getValue(Questao.class).getEnunciado().equals(questao.getEnunciado())){
                                    AlertDialog.Builder alerta = new AlertDialog.Builder(CadastrarEnunciado.this);
                                    alerta.setTitle("Atenção").setMessage("Questao já cadsatrada: " + questao.getMateria() + "\n Deseja continuar?");
                                    alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            questao = dataSnapshot.getValue(Questao.class);
                                            abrirTelaPrincipal();
                                        }
                                    });
                                    alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                    alert = alerta.create();
                                    alert.show();
                                }
                            }
                            else{
                                abrirTelaPrincipal();
                            }
                        }catch(Exception e){
                            abrirTelaPrincipal();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
        });
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

    private void abrirTelaPrincipal() {
        Questao.setInstance(questao);
        Intent intent = new Intent(CadastrarEnunciado.this,CadastrarResolucao.class);
        startActivity(intent);
    }
}
