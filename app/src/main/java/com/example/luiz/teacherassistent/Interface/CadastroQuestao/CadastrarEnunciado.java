package com.example.luiz.teacherassistent.Interface.CadastroQuestao;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luiz.teacherassistent.Controle.Professor;
import com.example.luiz.teacherassistent.Controle.Questao;
import com.example.luiz.teacherassistent.Helper.ConnectionTest;
import com.example.luiz.teacherassistent.Helper.ContextParse;
import com.example.luiz.teacherassistent.Helper.ProcessSingleImageTask;
import com.example.luiz.teacherassistent.Helper.RealPathUtil;
import com.example.luiz.teacherassistent.Helper.api.DetectionResult;
import com.example.luiz.teacherassistent.Interface.CorrigirQuestao.CorrigirBuscarEnunciadoAluno;
import com.example.luiz.teacherassistent.Interface.ForumUsuarios.ForumAluno;
import com.example.luiz.teacherassistent.Interface.ForumUsuarios.ForumProfessor;
import com.example.luiz.teacherassistent.Interface.Fragments.EnunciadoFragment;
import com.example.luiz.teacherassistent.Interface.Fragments.FotoFragment;
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

public class CadastrarEnunciado extends AppCompatActivity {
    private String latex;
    //botões
   // private Button fotoEnunciado;
  //  private EditText editEnunciado;
    private Spinner assuntos;
    private Spinner insersao;
    private ImageView imagemEnunciado;
    private final int GALERIA_IMAGENS = 1;
    private FloatingActionButton continuarCadastro;
    private RadioButton radioFisica;
    private RadioButton radioQuimica;
    private RadioButton radioFoto;
    private RadioButton radioTeclado;
    private RadioButton radioMatematica;
    private RadioGroup materiasRadio, tipoInsersao;
    private TextView disciplina;
    private AlertDialog alert;
    private ProgressBar barrinha;
    //constantes e variaveis
    private final int PERMISSAO_REQUEST = 2;
    Questao questao;
    Professor professor;
    // ferramentas
    private TextRecognizer ocrEnunciado;
    private Bitmap imageGaleria;
    // ferramentas
    private File imageFile;
    private WebView mWebView;
    private boolean fotos;
    @Override
    protected void onCreate(final Bundle onSaveInstanceState) {
        super.onCreate(onSaveInstanceState);
        super.setContentView(R.layout.layout_enunciado);

        ContextParse.setContext(getApplicationContext());
        fotos=false;
      //  fotoEnunciado = (Button) findViewById(R.id.fotoEnuciado);
      //  editEnunciado = (EditText) findViewById(R.id.enunciadoEditText);
        continuarCadastro = (FloatingActionButton) findViewById(R.id.ContinuarProcessoCad);
       // imagemEnunciado = (ImageView) findViewById(R.id.fotoEnunciadoMostra);
        barrinha = findViewById(R.id.barrinhaenunc);
        radioFisica = (RadioButton) findViewById(R.id.radioFisica);
        radioMatematica = (RadioButton) findViewById(R.id.radioMatematica);
        radioQuimica = (RadioButton) findViewById(R.id.radioQuimica);
        radioFoto = (RadioButton) findViewById(R.id.radioFoto);
        radioTeclado = (RadioButton) findViewById(R.id.radioTeclado);
        materiasRadio = (RadioGroup) findViewById(R.id.radioMaterias);
        disciplina = (TextView) findViewById(R.id.DisciplinaProf);
        assuntos = (Spinner) findViewById(R.id.assunto);
        if(ConnectionTest.isOnline()){
            AlertDialog.Builder alerta = new AlertDialog.Builder(this);
            alerta.setTitle("Atenção").setMessage("Dispositivc desconectado\n Deseja encerrar?");
            alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(CadastrarEnunciado.this, "Sessão Finalizada... \n Retornando ao menu principal", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    }
                }
            });
            alerta.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alerta.create().show();
        }
        final FrameLayout frame = (FrameLayout) findViewById(R.id.containerForFragment);
      //  mWebView = (WebView) findViewById(R.id.webViewEnun);
       /* fotoEnunciado.setOnClickListener(new View.OnClickListener() {
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
        professor = Professor.getInstance();
        Log.d("Teste", professor.getMateria());
        if (!professor.equals(null)) {
            if (professor.getMateria().equals("fisica")) {
                radioFisica.setChecked(true);
            } else if (professor.getMateria().equals("quimica")) {
                radioFisica.setChecked(true);
            } else if (professor.getMateria().equals("matematica")) {
                radioFisica.setChecked(true);
            }
        } else {
            Intent intent = new Intent(CadastrarEnunciado.this, MenuProfessor.class);
            startActivity(intent);
        }
        if (radioFisica.isChecked()) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CadastrarEnunciado.this, R.array.fisica_assuntos, R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            assuntos.setAdapter(adapter);
        } else if (radioQuimica.isChecked()) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CadastrarEnunciado.this, R.array.quimica_assuntos, R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            assuntos.setAdapter(adapter);
        } else if (radioMatematica.isChecked()) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CadastrarEnunciado.this, R.array.matematica_assuntos, R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            assuntos.setAdapter(adapter);
        }
        radioQuimica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioQuimica.setChecked(true);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CadastrarEnunciado.this, R.array.quimica_assuntos, R.layout.support_simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                assuntos.setAdapter(adapter);
            }
        });
        radioFisica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioFisica.setChecked(true);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CadastrarEnunciado.this, R.array.fisica_assuntos, R.layout.support_simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                assuntos.setAdapter(adapter);
                fotos=false;
            }
        });
        radioFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frame.removeAllViews();
                FotoFragment fotoFragment = new FotoFragment();
                managerFragment(fotoFragment,"FOTO_FRAGMENT");
                fotos=true;

            }
        });
        radioTeclado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frame.removeAllViews();
                EnunciadoFragment tecladoFragment = new EnunciadoFragment();
                managerFragment(tecladoFragment,"TECLADO_FRAGMENT");
            }
        });
        radioMatematica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioMatematica.setChecked(true);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CadastrarEnunciado.this, R.array.matematica_assuntos, R.layout.support_simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                assuntos.setAdapter(adapter);
            }
        });
        continuarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barrinha.setVisibility(View.VISIBLE);
                barrinha.setActivated(true);
                questao = new Questao();
                if(fotos){
                    latex = FotoFragment.latex;
                    questao.setEnunciado(latex);
                }else{
                    String texto = EnunciadoFragment.newEdit.getText().toString();
                    questao.setEnunciado(texto);
                }
                //professor.setAtivo(false);
                if (radioFisica.isChecked()) {
                    questao.setMateria("fisica");
                    questao.setAssunto(assuntos.getSelectedItem().toString());

                } else if (radioMatematica.isChecked()) {
                    questao.setMateria("matematica");
                    questao.setAssunto(assuntos.getSelectedItem().toString());

                } else if (radioQuimica.isChecked()) {
                    questao.setAssunto(assuntos.getSelectedItem().toString());
                    questao.setMateria("quimica");

                } else {
                    disciplina.setText(disciplina.getText() + "     Campo Obrigatório");
                    disciplina.setTextColor(Color.RED);

                }
                Log.d("GORDO", questao.getEnunciado());
                if (questao.getAssunto() == null || questao.getEnunciado() == null || questao.getMateria() == null) {
                    Log.d("Para ae", questao.getAssunto());
                    aviso();
                } else {
                    Log.d("eee", questao.getAssunto());
                    procurarQuestao();
                }
            }
        });
    }
    private void aviso() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(CadastrarEnunciado.this);
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




    private void procurarQuestao() {
        DatabaseReference salve = ConfiguracaoDataBase.getFirebase();
        salve.child("questao").child(String.valueOf(questao.getMateria())).child(
                String.valueOf(questao.getAssunto())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue(Questao.class).getEnunciado() != null) {
                        if (dataSnapshot.getValue(Questao.class).getEnunciado().equals(questao.getEnunciado())) {
                            Log.d("Repetido","reapet");
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
                                    latex="";
                                    questao = new Questao();
                                }
                            });
                            alerta.create().show();
                        }else{
                            Log.d("Caralho","oq");
                            abrirTelaPrincipal();
                        }
                    } else {
                        Log.d("Repetido","oq");
                        abrirTelaPrincipal();
                    }
                } catch (Exception e) {
                    Log.d("Repetido","oq");
                    abrirTelaPrincipal();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Cancel","Serio?");

            }
        });
    }


    private void abrirTelaPrincipal() {
        Questao.setInstance(questao);
        Log.d("passar","reso");
        Intent intent = new Intent(CadastrarEnunciado.this, CadastrarResolucao.class);
        startActivity(intent);
    }

    private void managerFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.containerForFragment, fragment, tag);
        fragmentTransaction.commit();
    }
}
