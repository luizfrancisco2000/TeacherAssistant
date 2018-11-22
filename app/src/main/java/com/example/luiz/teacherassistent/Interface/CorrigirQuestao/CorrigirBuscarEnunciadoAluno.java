package com.example.luiz.teacherassistent.Interface.CorrigirQuestao;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luiz.teacherassistent.Controle.Aluno;
import com.example.luiz.teacherassistent.Controle.Correcao;
import com.example.luiz.teacherassistent.Controle.Professor;
import com.example.luiz.teacherassistent.Controle.Questao;
import com.example.luiz.teacherassistent.Helper.ContextParse;
import com.example.luiz.teacherassistent.Helper.ProcessSingleImageTask;
import com.example.luiz.teacherassistent.Helper.RealPathUtil;
import com.example.luiz.teacherassistent.Helper.api.DetectionResult;
import com.example.luiz.teacherassistent.Interface.CadastroQuestao.CadastrarEnunciado;
import com.example.luiz.teacherassistent.Interface.CorrigirQuestao.CorrigirBuscarEnunciadoAluno;
import com.example.luiz.teacherassistent.Interface.CadastroUsuarios.InstrucaoCadastroProfessor;
import com.example.luiz.teacherassistent.Interface.Fragments.EnunciadoFragment;
import com.example.luiz.teacherassistent.Interface.Fragments.FotoFragment;
import com.example.luiz.teacherassistent.Interface.LoginUsuarios.LoginProfessor;
import com.example.luiz.teacherassistent.Interface.Menus.MenuAluno;
import com.example.luiz.teacherassistent.Interface.Menus.MenuProfessor;
import com.example.luiz.teacherassistent.R;
import com.example.luiz.teacherassistent.Servidor.ConfiguracaoDataBase;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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

public class CorrigirBuscarEnunciadoAluno extends AppCompatActivity {
    //botões
    private FloatingActionButton continuarResolucao;
    private RadioButton radioFisica;
    private RadioButton radioQuimica;
    private RadioButton radioFoto;
    private RadioButton radioTeclado;
    private FrameLayout frame;
    private RadioButton radioMatematica;
    private RadioGroup materiasRadio;
    private TextView disciplina;
    private Spinner assuntos;
    //constantes e variaveis
    private final int PERMISSAO_REQUEST = 2;
    private Questao questao;
    private Correcao correcao;
    // ferramentas
    private String latex;
    private boolean fotos;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.layout_enunciado);
        ContextParse.setContext(getApplicationContext());
        continuarResolucao = (FloatingActionButton) findViewById(R.id.ContinuarProcessoCad);
        radioFisica = (RadioButton) findViewById(R.id.radioFisica);
        radioMatematica = (RadioButton) findViewById(R.id.radioMatematica);
        radioQuimica = (RadioButton) findViewById(R.id.radioQuimica);
        materiasRadio = (RadioGroup) findViewById(R.id.radioMaterias);
        radioFoto = (RadioButton) findViewById(R.id.radioFoto);
        final FrameLayout frame = (FrameLayout) findViewById(R.id.containerForFragment);
        radioTeclado = (RadioButton) findViewById(R.id.radioTeclado);
        disciplina = (TextView) findViewById(R.id.DisciplinaEnunciado);
        assuntos = (Spinner) findViewById(R.id.assunto);
        validarPermissao();
        if (radioFisica.isChecked()) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CorrigirBuscarEnunciadoAluno.this, R.array.fisica_assuntos, R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            assuntos.setAdapter(adapter);
        } else if (radioQuimica.isChecked()) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CorrigirBuscarEnunciadoAluno.this, R.array.quimica_assuntos, R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            assuntos.setAdapter(adapter);
        } else if (radioMatematica.isChecked()) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CorrigirBuscarEnunciadoAluno.this, R.array.matematica_assuntos, R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            assuntos.setAdapter(adapter);
        }
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
        radioQuimica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioQuimica.setChecked(true);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CorrigirBuscarEnunciadoAluno.this, R.array.quimica_assuntos, R.layout.support_simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                assuntos.setAdapter(adapter);
            }
        });
        radioFisica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioFisica.setChecked(true);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CorrigirBuscarEnunciadoAluno.this, R.array.fisica_assuntos, R.layout.support_simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                assuntos.setAdapter(adapter);
            }
        });
        radioMatematica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioMatematica.setChecked(true);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(CorrigirBuscarEnunciadoAluno.this, R.array.matematica_assuntos, R.layout.support_simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                assuntos.setAdapter(adapter);
            }
        });
        continuarResolucao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questao = new Questao();
                if(fotos){
                    latex = FotoFragment.latex;
                    questao.setEnunciado(latex);
                    Toast.makeText(CorrigirBuscarEnunciadoAluno.this, ""+"Errado", Toast.LENGTH_SHORT).show();
                    Log.d("Errado", latex);
                }else{
                    String texto = EnunciadoFragment.newEdit.getText().toString();
                    Toast.makeText(CorrigirBuscarEnunciadoAluno.this, ""+texto, Toast.LENGTH_SHORT).show();
                    Log.d("Teste", "onClick: "+texto);
                    questao.setEnunciado(texto);
                }
                if (radioFisica.isChecked()) {
                    questao.setMateria("fisica");
                    questao.setAssunto(assuntos.getSelectedItem().toString());
                    buscar();
                } else if (radioMatematica.isChecked()) {
                    questao.setMateria("matematica");
                    questao.setAssunto(assuntos.getSelectedItem().toString());
                    buscar();
                } else if (radioQuimica.isChecked()) {
                    questao.setMateria("quimica");
                    questao.setAssunto(assuntos.getSelectedItem().toString());
                    buscar();
                } else {
                    disciplina.setText(disciplina.getText().toString() + "     Campo Obrigatório");
                    disciplina.setTextColor(Color.RED);
                }
                if (questao.getAssunto() == null || questao.getEnunciado() == null || questao.getMateria() == null) {
                    aviso();
                } else {
                    buscar();
                }

            }
        });
    }

    private void aviso() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(CorrigirBuscarEnunciadoAluno.this);
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

    @Override
    protected void onStop() {
        super.onStop();
    }


    public void validarPermissao() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSAO_REQUEST);
            }
        }

    }

    public void buscar() {
        DatabaseReference salve = ConfiguracaoDataBase.getFirebase();
        salve.child("questao").child(String.valueOf(questao.getMateria())).child(
                String.valueOf(questao.getAssunto())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.getValue(Questao.class).getEnunciado() != null) {
                            Log.d("Vamos lá", dataSnapshot.getValue(Questao.class).getEnunciado());
                            if (dataSnapshot.getValue(Questao.class).getEnunciado().equals(questao.getEnunciado())) {
                                correcao = dataSnapshot.getValue(Questao.class).exportResolucao();
                                abrirTelaPrincipal();
                            } else {
                                AlertDialog.Builder alerta = new AlertDialog.Builder(CorrigirBuscarEnunciadoAluno.this);
                                alerta.setTitle("Atenção").setMessage("Questao não cadsatrada" + questao.getMateria() + "\nDeseja voltar ao menu?");
                                alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(CorrigirBuscarEnunciadoAluno.this, MenuAluno.class);
                                        startActivity(intent);
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
                        } else {
                            AlertDialog.Builder alerta = new AlertDialog.Builder(CorrigirBuscarEnunciadoAluno.this);
                            alerta.setTitle("Atenção").setMessage("Questao não cadsatrada" + questao.getMateria() + "\nDeseja voltar ao menu?");
                            alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(CorrigirBuscarEnunciadoAluno.this, MenuAluno.class);
                                    startActivity(intent);
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
                    } else {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(CorrigirBuscarEnunciadoAluno.this);
                        alerta.setTitle("Atenção").setMessage("Questao não cadsatrada" + questao.getMateria() + "\nDeseja voltar ao menu?");
                        alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(CorrigirBuscarEnunciadoAluno.this, MenuAluno.class);
                                startActivity(intent);
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
                } catch (Exception e) {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(CorrigirBuscarEnunciadoAluno.this);
                    alerta.setTitle("Atenção").setMessage("Questao não cadsatrada" + questao.getMateria() + "\nDeseja voltar ao menu?");
                    alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(CorrigirBuscarEnunciadoAluno.this, MenuAluno.class);
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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void abrirTelaPrincipal() {
        Log.d("TELA", "PRINCIPAL");
        Correcao.setInstance(correcao);
        Intent intent = new Intent(CorrigirBuscarEnunciadoAluno.this, CorrigirBuscarResolucaoAluno.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CorrigirBuscarEnunciadoAluno.this, MenuAluno.class);
        startActivity(intent);
    }

    private void managerFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.containerForFragment, fragment, tag);
        fragmentTransaction.commit();
    }
}