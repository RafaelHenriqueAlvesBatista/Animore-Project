package com.example.animoreproject;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.santalu.maskara.widget.MaskEditText;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FormCadastro extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // CONSTANTE PARA A SELECAO DE IMAGEM
    private ConstraintLayout clyTelaCarregando, clySuaFoto;
    private LinearLayout llyStatusBar, llyFormulario1, llyFormulario2;
    private TextView txvCursorVoltar, txvCursorPosicao, txvCursorAvancar;
    private TextInputEditText edtNome, edtEmail, edtSenha;
    private MaskEditText edtTelefone;
    private TextInputEditText edtRua, edtBairro, edtCidade, edtEstado;
    private ImageView imvFotoSmall, imvFotoBig;
    private Button btnContinuar2, btnVoltar, btnVisualizarFoto, btnSairFoto, btnImportarFoto;
    String usuarioID;
    private boolean segundoFormulario = false;

    private Uri uriFoto;

    private DatabaseReference databaseRef;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        llyStatusBar     = findViewById(R.id.llyStatusBar);

        llyFormulario1   = findViewById(R.id.llyFormulario1);

        edtNome          = findViewById(R.id.edtNome);
        edtTelefone      = findViewById(R.id.edtTelefone);
        edtEmail         = findViewById(R.id.edtEmail);
        edtSenha         = findViewById(R.id.edtSenha);

        llyFormulario2   = findViewById(R.id.llyFormulario2);

        edtRua           = findViewById(R.id.edtRua);
        edtBairro        = findViewById(R.id.edtBairro);
        edtCidade        = findViewById(R.id.edtCidade);
        edtEstado        = findViewById(R.id.edtEstado);
        imvFotoSmall     = findViewById(R.id.imvFotoSmall);
        btnVisualizarFoto  = findViewById(R.id.btnVisualizarFoto);

        btnContinuar2    = findViewById(R.id.btnContinuar2);
        btnVoltar        = findViewById(R.id.btnVoltar);

        txvCursorVoltar  = findViewById(R.id.txvCursorVoltar);
        txvCursorPosicao = findViewById(R.id.txvCursorPosicao);
        txvCursorAvancar = findViewById(R.id.txvCursorAvancar);

        clySuaFoto       = findViewById(R.id.clySuaFoto);

        imvFotoBig       = findViewById(R.id.imvFotoBig);
        btnSairFoto      = findViewById(R.id.btnSairFoto);
        btnImportarFoto = findViewById(R.id.btnImportarFoto);

        clyTelaCarregando = findViewById(R.id.clyTelaCarregando);

        databaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        storageRef = FirebaseStorage.getInstance().getReference("uploads");
    }

    @Override
    protected void onResume() {
        super.onResume();

        txvCursorVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                segundoFormulario = false;

                llyFormulario1.setVisibility(View.VISIBLE);
                llyFormulario2.setVisibility(View.GONE);
                txvCursorVoltar.setEnabled(false);
                txvCursorAvancar.setEnabled(true);
                txvCursorPosicao.setText(R.string.text_one);

                txvCursorVoltar.setTextColor(getResources().getColor(R.color.gray_600));
                txvCursorAvancar.setTextColor(getResources().getColor(R.color.white));
            }
        });

        txvCursorAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                segundoFormulario = true;

                llyFormulario1.setVisibility(View.GONE);
                llyFormulario2.setVisibility(View.VISIBLE);
                txvCursorVoltar.setEnabled(true);
                txvCursorAvancar.setEnabled(false);
                txvCursorPosicao.setText(R.string.text_two);

                txvCursorVoltar.setTextColor(getResources().getColor(R.color.white));
                txvCursorAvancar.setTextColor(getResources().getColor(R.color.gray_600));
            }
        });

        imvFotoSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarFoto();
            }
        });

        btnVisualizarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarFoto();
            }
        });

        btnSairFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sairFoto();
            }
        });

        btnImportarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirSeletorArquivos();
            }
        });

        btnContinuar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome     = edtNome.getText().toString();
                String telefone = edtTelefone.getUnMasked();
                String email    = edtEmail.getText().toString();
                String senha    = edtSenha.getText().toString();
                String rua      = edtRua.getText().toString();
                String bairro   = edtBairro.getText().toString();
                String cidade   = edtCidade.getText().toString();
                String estado   = edtEstado.getText().toString();

                if (nome.isEmpty() || telefone.isEmpty() || email.isEmpty() || senha.isEmpty()){
                    Toast.makeText(FormCadastro.this, R.string.message_emptyInputs, Toast.LENGTH_SHORT).show();
                } else if (telefone.length() < 11) {
                    Toast.makeText(FormCadastro.this, R.string.exception_registerInvalidPhone, Toast.LENGTH_SHORT).show();
                } else if (rua.isEmpty() || bairro.isEmpty() || cidade.isEmpty() || estado.isEmpty()) {
                    if (segundoFormulario) {
                        Toast.makeText(FormCadastro.this, R.string.message_emptyInputs, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FormCadastro.this, R.string.message_emptyInputs2, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    cadastrarUsuario();
                }
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void cadastrarUsuario() {
        clyTelaCarregando.setVisibility(View.VISIBLE);
        btnContinuar2.setEnabled(false);
        btnVoltar.setEnabled(false);
        llyStatusBar.setBackgroundColor(getColor(R.color.analogous2_800));

        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    salvarDados();
                } else {
                    String erro;
                    devolverTela();

                    try {
                        throw task.getException();

                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = getString(R.string.exception_registerWeakPassword);
                    } catch (FirebaseAuthUserCollisionException e) {
                        erro = getString(R.string.exception_registerDuplicateEmail);
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = getString(R.string.exception_registerInvalidEmail);
                    } catch (Exception e){
                        erro = getString(R.string.exception_registerAnotherError);
                    }

                    Toast.makeText(FormCadastro.this, erro, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void devolverTela() {
        clyTelaCarregando.setVisibility(View.INVISIBLE);
        btnContinuar2.setEnabled(true);
        btnVoltar.setEnabled(true);
        llyStatusBar.setBackgroundColor(getColor(R.color.analogous2_300));
    }

    private void salvarDados() {
        // DADOS DO FORMULARIO
        String nome     = edtNome.getText().toString();
        String celular  = "55" + edtTelefone.getUnMasked();
        String rua      = edtRua.getText().toString();
        String bairro   = edtBairro.getText().toString();
        String cidade   = edtCidade.getText().toString();
        String estado   = edtEstado.getText().toString();

        // PEGA A DATA ATUAL, QUE VAI SER ATRIBUIDA NA HORA QUE O USUARIO FOR CADASTRO
        Calendar calendar   = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String dataCadastro = df.format(calendar.getTime());

        // MONTA A TABELA DO USUARIO COM OS DADOS JA PREENCHIDOS
        Map<String,Object> usuarios = new HashMap<>();

        // VALORES QUE O USUARIO ESCREVEU NO FORMULARIO, E VALORES FIXOS
        usuarios.put("nome",         nome);
        usuarios.put("celular",      celular);
        usuarios.put("rua",          rua);
        usuarios.put("bairro",       bairro);
        usuarios.put("cidade",       cidade);
        usuarios.put("estado",       estado);
        usuarios.put("dataCadastro", dataCadastro);

        // VALORES QUE SAO ATRIBUIDOS POR PADRAO
        usuarios.put("foto",       "");
        usuarios.put("refFoto",    "");
        usuarios.put("numAnimais", "0");
        usuarios.put("numAcessorios", "0");

        // INSTANCIA A CLASSE DB PARA VERIFICAR SE O USUARIO FOI CADASTRADO COM SUCESSO
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (uriFoto != null) {
                    // SALVA O NOME DA FOTO COM O ID DO USUARIO MAIS A EXTENSAO NA TABELA DO USUARIO
                    StorageReference fileReference = storageRef.child(usuarioID
                            + "." + getFileExtension(uriFoto));
                    documentReference.update("refFoto", (usuarioID + "." + getFileExtension(uriFoto)));

                    // FAZ O UPLOAD DA IMAGEM
                    StorageTask uploadTask = fileReference.putFile(uriFoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // SALVA A REFERENCIA DA IMAGEM NO BANCO
                                    databaseRef.child(usuarioID).setValue(uri.toString());

                                    // SALVA O LINK DA FOTO NA TABELA DO USUARIO
                                    documentReference.update("foto", uri.toString());

                                    incrementarEstatisticas();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(FormCadastro.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            devolverTela();
                        }
                    });
                } else {
                    incrementarEstatisticas();
                }
            }
        })
         .addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 devolverTela();
             }
         });
    }

    private void mostrarFoto() {
        edtRua.setEnabled(false);
        edtBairro.setEnabled(false);
        edtEstado.setEnabled(false);
        imvFotoSmall.setEnabled(false);
        btnVisualizarFoto.setEnabled(false);
        txvCursorVoltar.setEnabled(false);
        btnVoltar.setEnabled(false);
        btnContinuar2.setEnabled(false);
        clySuaFoto.setVisibility(View.VISIBLE);
        llyStatusBar.setBackgroundColor(getColor(R.color.analogous2_800));
    }

    private void sairFoto() {
        edtRua.setEnabled(true);
        edtBairro.setEnabled(true);
        edtEstado.setEnabled(true);
        imvFotoSmall.setEnabled(true);
        btnVisualizarFoto.setEnabled(true);
        txvCursorVoltar.setEnabled(true);
        btnVoltar.setEnabled(true);
        btnContinuar2.setEnabled(true);
        clySuaFoto.setVisibility(View.INVISIBLE);
        llyStatusBar.setBackgroundColor(getColor(R.color.analogous2_300));
    }

    private void abrirSeletorArquivos() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void irTelaLogin() {
        devolverTela();
        btnVoltar.setEnabled(false);
        btnContinuar2.setEnabled(false);
        FirebaseAuth.getInstance().signOut();

        Intent enviarFeedback = new Intent(FormCadastro.this, FormLogin.class);
        enviarFeedback.putExtra("enviarFeedback", 1);
        startActivity(enviarFeedback);
        finish();
    }

    private void incrementarEstatisticas() {
        DocumentReference documentReferenceEstatisticas;
        documentReferenceEstatisticas = FirebaseFirestore.getInstance().collection("Estatisticas").document("Estatisticas");
        documentReferenceEstatisticas.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int usuariosInt = Integer.parseInt(documentSnapshot.getString("usuarios"));
                documentReferenceEstatisticas.update("usuarios", String.valueOf(usuariosInt += 1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        irTelaLogin();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FormCadastro.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                devolverTela();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uriFoto = data.getData();

            Picasso.get().load(uriFoto).into(imvFotoSmall);
            Picasso.get().load(uriFoto).into(imvFotoBig);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}