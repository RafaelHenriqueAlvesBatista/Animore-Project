package com.example.animoreproject;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.santalu.maskara.widget.MaskEditText;
import com.squareup.picasso.Picasso;

public class PerfilUsuario extends AppCompatActivity {

    // COMPONENTES TOOLBAR
    private ImageButton botaoMenu;

    // COMPONENTES TELA
    private DrawerLayout drlPagina;
    private View headerView;

    // COMPONENTES MENU
    private NavigationView nvvMenu;
    private TextView txvMenuNomeUsuario;
    private ImageView imvMenuFotoUsuario;
    private Menu menu;
    private MenuItem mnuInicial;
    private MenuItem mnuPerfil;
    private MenuItem mnuAnimais;
    private MenuItem mnuSair;

    // COMPONENTES DADOS - NORMAL
    private TextView txvNomeUsuario, txvEmailUsuario, txvCelularUsuario, txvRuaUsuario, txvBairroUsuario, txvEstadoUsuario;
    private TextView txvValorAnimaisUsuario, txvValorAcessoriosUsuario, txvDataLogadoUsuario;

    // COMPONENTES DADOS - EDICAO
    private TextView txvEmailUsuarioEdit, txvValorAnimaisUsuarioEdit, txvValorAcessoriosUsuarioEdit, txvDataLogadoUsuarioEdit;
    private TextInputEditText edtNomeUsuario, edtRuaUsuario, edtBairroUsuario, edtEstadoUsuario;
    private MaskEditText edtCelularUsuario;

    // COMPONENTES DIVERSOS
    private ImageView imvFotoSmall, imvFotoBig;
    private ConstraintLayout clySuaFoto, clyTelaCarregando;
    private LinearLayout llyNomeUsuario, llyNomeUsuarioEdit, llyDadosPerfilUsuario, llyDadosPerfilUsuarioEdit, llyDetalhesPerfilUsuario, llyDetalhesPerfilUsuarioEdit, llyPerfilUsuarioOpcoes1, llyPerfilUsuarioOpcoes2;
    private ScrollView scvTela;
    private Button btnEditar, btnExcluir, btnSalvar, btnCancelar, btnSairFoto, btnImportarFoto;
    private FloatingActionButton fabVoltar, fabEditarFoto;

    // VARIAVEIS DA ACTIVITY
    private static final int PICK_IMAGE_REQUEST = 1; // CONSTANTE PARA A SELECAO DE IMAGEM
    private boolean modoEdicao = false;
    private boolean atualizouFoto = false;
    private int feedbackAtualizou = 0;
    private boolean excluindo = false;
    private Uri uriFoto;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;
    private DatabaseReference databaseRef;
    private StorageReference storageRef;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        procurarUsuarioAtual();
        instanciarComponentes();
        programarComponentes();
        recuperarDados();
    }

    private void procurarUsuarioAtual() {
        usuarioID                     = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseRef                   = FirebaseDatabase.getInstance().getReference("uploads");
        storageRef                    = FirebaseStorage.getInstance().getReference("uploads");
        documentReference             = db.collection("Usuarios").document(usuarioID);
    }

    private void instanciarComponentes() {
        botaoMenu                     = findViewById(R.id.botaoMenu);

        drlPagina                     = findViewById(R.id.drlPagina);

        nvvMenu                       = findViewById(R.id.nvvMenu);
        headerView                    = nvvMenu.getHeaderView(0);

        txvMenuNomeUsuario            = headerView.findViewById(R.id.txvMenuNomeUsuario);
        imvMenuFotoUsuario            = headerView.findViewById(R.id.imvMenuFotoUsuario);
        menu                          = nvvMenu.getMenu();
        mnuInicial                    = menu.findItem(R.id.menu_paginaInicial);
        mnuPerfil                     = menu.findItem(R.id.menu_perfil);
        mnuAnimais                    = menu.findItem(R.id.menu_meusAnimais);
        mnuSair                       = menu.findItem(R.id.menu_sair);

        txvNomeUsuario                = findViewById(R.id.txvNomeUsuario);
        txvEmailUsuario               = findViewById(R.id.txvEmailUsuario);
        txvCelularUsuario             = findViewById(R.id.txvCelularUsuario);
        txvRuaUsuario                 = findViewById(R.id.txvRuaUsuario);
        txvBairroUsuario              = findViewById(R.id.txvBairroUsuario);
        txvEstadoUsuario              = findViewById(R.id.txvEstadoUsuario);
        txvValorAnimaisUsuario        = findViewById(R.id.txvValorAnimaisUsuario);
        txvValorAcessoriosUsuario     = findViewById(R.id.txvValorAcessoriosUsuario);
        txvDataLogadoUsuario          = findViewById(R.id.txvDataLogadoUsuario);

        llyNomeUsuario                = findViewById(R.id.llyNomeUsuario);
        llyDadosPerfilUsuario         = findViewById(R.id.llyDadosPerfilUsuario);
        llyDetalhesPerfilUsuario      = findViewById(R.id.llyDetalhesPerfilUsuario);

        edtNomeUsuario                = findViewById(R.id.edtNomeUsuario);
        edtCelularUsuario             = findViewById(R.id.edtCelularUsuario);
        edtRuaUsuario                 = findViewById(R.id.edtRuaUsuario);
        edtBairroUsuario              = findViewById(R.id.edtBairroUsuario);
        edtEstadoUsuario              = findViewById(R.id.edtEstadoUsuario);

        txvEmailUsuarioEdit           = findViewById(R.id.txvEmailUsuarioEdit);
        txvValorAnimaisUsuarioEdit    = findViewById(R.id.txvValorAnimaisUsuarioEdit);
        txvValorAcessoriosUsuarioEdit = findViewById(R.id.txvValorAcessoriosUsuarioEdit);
        txvDataLogadoUsuarioEdit      = findViewById(R.id.txvDataLogadoUsuarioEdit);

        llyNomeUsuarioEdit            = findViewById(R.id.llyNomeUsuarioEdit);
        llyDadosPerfilUsuarioEdit     = findViewById(R.id.llyDadosPerfilUsuarioEdit);
        llyDetalhesPerfilUsuarioEdit  = findViewById(R.id.llyDetalhesPerfilUsuarioEdit);
        llyPerfilUsuarioOpcoes1       = findViewById(R.id.llyPerfilUsuarioOpcoes1);
        llyPerfilUsuarioOpcoes2       = findViewById(R.id.llyPerfilUsuarioOpcoes2);

        imvFotoSmall                  = findViewById(R.id.imvFotoSmall);
        imvFotoBig                    = findViewById(R.id.imvFotoBig);

        clySuaFoto                    = findViewById(R.id.clySuaFoto);
        clyTelaCarregando             = findViewById(R.id.clyTelaCarregando);

        btnEditar                     = findViewById(R.id.btnEditar);
        btnExcluir                    = findViewById(R.id.btnExcluir);
        btnSalvar                     = findViewById(R.id.btnSalvar);
        btnCancelar                   = findViewById(R.id.btnCancelar);
        btnSairFoto                   = findViewById(R.id.btnSairFoto);
        btnImportarFoto               = findViewById(R.id.btnImportarFoto);

        fabVoltar                     = findViewById(R.id.fabVoltar);
        fabEditarFoto                 = findViewById(R.id.fabEditarFoto);

        scvTela                       = findViewById(R.id.scvTela);

        nvvMenu                       = findViewById(R.id.nvvMenu);
    }

    private void programarComponentes() {
        botaoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drlPagina.openDrawer(GravityCompat.START);
            }
        });

        mnuInicial.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                Intent telaInicial = new Intent(PerfilUsuario.this, TelaInicial.class);
                startActivity(telaInicial);
                return true;
            }
        });

        mnuPerfil.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        mnuAnimais.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                Intent meusAnimais = new Intent(PerfilUsuario.this, ListaMeusAnimais.class);
                startActivity(meusAnimais);
                return true;
            }
        });

        mnuSair.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PerfilUsuario.this);
                builder.setMessage("Deseja realmente encerrar a sessão?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                drlPagina.closeDrawer(GravityCompat.START);
                                FirebaseAuth.getInstance().signOut();
                                Intent enviarFeedback = new Intent(PerfilUsuario.this, FormLogin.class);
                                enviarFeedback.putExtra("enviarFeedback", 3);
                                startActivity(enviarFeedback);
                                finish();
                            }
                        })
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        })
                        .create()
                        .show();
                return true;
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativarModoEdicao();
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PerfilUsuario.this);
                builder.setIcon(R.drawable.ic_aviso);
                builder.setTitle("Aviso");
                builder.setMessage("\nDeseja realmente excluir?\nEssa operação NÃO poderá ser desfeita.")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ativarProgresso();
                                excluindo = true;
                                excluirRegistros("Animais");
                                excluirRegistros("Acessorios");
                                decrementarEstatisticas();
                            }
                        })
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        })
                        .create()
                        .show();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desativarModoEdicao();
                carregarFoto();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // VERIFICA SE ALGUM CAMPO ESTA VAZIO
                        if (edtNomeUsuario.getText().toString().equals("") ||
                                edtCelularUsuario.getUnMasked().equals("") ||
                                edtRuaUsuario.getText().toString().equals("") ||
                                edtBairroUsuario.getText().toString().equals("") ||
                                edtEstadoUsuario.getText().toString().equals("")) {
                            Toast.makeText(PerfilUsuario.this, R.string.exception_emptyInputs, Toast.LENGTH_SHORT).show();
                        } else if (edtCelularUsuario.length() < 19) {
                            Toast.makeText(PerfilUsuario.this, R.string.exception_registerInvalidPhone, Toast.LENGTH_SHORT).show();
                        } else {
                            if (atualizouFoto) {
                                ativarProgresso();

                                // VERIFICA SE HA ALGUMA IMAGEM ADICIONADA ANTES, E A EXCLUI CASO ENCONTRAR
                                if (documentSnapshot != null) {
                                    String foto = documentSnapshot.getString("foto");
                                    String refFoto = documentSnapshot.getString("refFoto");

                                    if (foto != null && !foto.isEmpty()) {
                                        StorageReference imageRef = storageRef.child(refFoto);
                                        imageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                databaseRef.child(usuarioID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                    }
                                                });
                                            }
                                        });
                                    } else {
                                        databaseRef.child(usuarioID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {}
                                        });
                                    }
                                }

                                salvarFoto();
                            } else {
                                atualizarDados();
                            }
                        }
                        desativarModoEdicao();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        cancelarOperacao(e.getMessage());
                    }
                });
            }
        });

        btnSairFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(true);
                fabEditarFoto.setEnabled(true);
                clySuaFoto.setVisibility(View.INVISIBLE);
            }
        });

        btnImportarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        fabVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fabEditarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                fabEditarFoto.setEnabled(false);
                clySuaFoto.setVisibility(View.VISIBLE);
            }
        });
    }

    private void recuperarDados() {
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    if (documentSnapshot != null && !excluindo) {
                        txvNomeUsuario.setText(documentSnapshot.getString("nome"));
                        txvEmailUsuario.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        String celular = documentSnapshot.getString("celular");
                        if (celular != null && !celular.equals("")) {
                            txvCelularUsuario.setText(obterCelularComMascara(celular));
                        }
                        txvRuaUsuario.setText(documentSnapshot.getString("rua"));
                        txvBairroUsuario.setText(documentSnapshot.getString("bairro"));
                        txvEstadoUsuario.setText(documentSnapshot.getString("estado"));

                        txvValorAnimaisUsuario.setText(documentSnapshot.getString("numAnimais"));
                        txvValorAnimaisUsuarioEdit.setText(documentSnapshot.getString("numAnimais"));
                        txvValorAcessoriosUsuario.setText(documentSnapshot.getString("numAcessorios"));
                        txvValorAcessoriosUsuarioEdit.setText(documentSnapshot.getString("numAcessorios"));
                        txvDataLogadoUsuario.setText(documentSnapshot.getString("dataCadastro"));
                        txvDataLogadoUsuarioEdit.setText(documentSnapshot.getString("dataCadastro"));

                        txvMenuNomeUsuario.setText(documentSnapshot.getString("nome"));

                        carregarFoto();
                    }
                }
            });
    }

    private void carregarFoto() {
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot != null) {
                        String foto = documentSnapshot.getString("foto");
                        if (!excluindo) {
                            if (foto != null && !foto.isEmpty()){
                                imvFotoSmall.setBackgroundColor(getResources().getColor(R.color.transparent));
                                imvMenuFotoUsuario.setBackgroundColor(getResources().getColor(R.color.transparent));
                                Picasso.get().load(documentSnapshot.getString("foto")).into(imvFotoSmall);
                                Picasso.get().load(documentSnapshot.getString("foto")).into(imvFotoBig);
                                Picasso.get().load(documentSnapshot.getString("foto")).into(imvMenuFotoUsuario);
                            }
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    cancelarOperacao(e.getMessage());
                }
            });
    }

    private void ativarModoEdicao() {
        modoEdicao = true;
        llyNomeUsuario.setVisibility(View.INVISIBLE);
        llyNomeUsuarioEdit.setVisibility(View.VISIBLE);
        edtNomeUsuario.setEnabled(true);
        llyDadosPerfilUsuario.setVisibility(View.INVISIBLE);
        llyDadosPerfilUsuarioEdit.setVisibility(View.VISIBLE);
        txvEmailUsuarioEdit.setEnabled(true);
        edtCelularUsuario.setEnabled(true);
        edtRuaUsuario.setEnabled(true);
        edtBairroUsuario.setEnabled(true);
        edtEstadoUsuario.setEnabled(true);
        llyDetalhesPerfilUsuario.setVisibility(View.INVISIBLE);
        llyDetalhesPerfilUsuarioEdit.setVisibility(View.VISIBLE);
        llyPerfilUsuarioOpcoes1.setVisibility(View.INVISIBLE);
        llyPerfilUsuarioOpcoes2.setVisibility(View.VISIBLE);
        btnSalvar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
        fabEditarFoto.setEnabled(true);
        fabEditarFoto.setVisibility(View.VISIBLE);

        edtNomeUsuario.setText(txvNomeUsuario.getText());
        edtCelularUsuario.setText(obterCelularSemMascara(txvCelularUsuario.getText().toString()));
        txvEmailUsuarioEdit.setText(txvEmailUsuario.getText());
        edtRuaUsuario.setText(txvRuaUsuario.getText());
        edtBairroUsuario.setText(txvBairroUsuario.getText());
        edtEstadoUsuario.setText(txvEstadoUsuario.getText());
    }

    private void desativarModoEdicao() {
        modoEdicao = false;
        atualizouFoto = false;
        feedbackAtualizou = 0;
        llyNomeUsuario.setVisibility(View.VISIBLE);
        llyNomeUsuarioEdit.setVisibility(View.INVISIBLE);
        edtNomeUsuario.setEnabled(false);
        llyDadosPerfilUsuario.setVisibility(View.VISIBLE);
        llyDadosPerfilUsuarioEdit.setVisibility(View.INVISIBLE);
        txvEmailUsuarioEdit.setEnabled(false);
        edtCelularUsuario.setEnabled(false);
        edtRuaUsuario.setEnabled(false);
        edtBairroUsuario.setEnabled(false);
        edtEstadoUsuario.setEnabled(false);
        llyDetalhesPerfilUsuario.setVisibility(View.VISIBLE);
        llyDetalhesPerfilUsuarioEdit.setVisibility(View.INVISIBLE);
        llyPerfilUsuarioOpcoes1.setVisibility(View.VISIBLE);
        llyPerfilUsuarioOpcoes2.setVisibility(View.INVISIBLE);
        btnSalvar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnEditar.setEnabled(true);
        btnExcluir.setEnabled(true);
        fabEditarFoto.setEnabled(false);
        fabEditarFoto.setVisibility(View.INVISIBLE);
    }

    private void ativacaoBotoes(boolean marca) {
        if (modoEdicao) {
            btnCancelar.setEnabled(marca);
            btnSalvar.setEnabled(marca);
        } else {
            btnEditar.setEnabled(marca);
            btnExcluir.setEnabled(marca);
        }
        edtNomeUsuario.setEnabled(marca);
        txvEmailUsuarioEdit.setEnabled(marca);
        edtCelularUsuario.setEnabled(marca);
        edtRuaUsuario.setEnabled(marca);
        edtBairroUsuario.setEnabled(marca);
        edtEstadoUsuario.setEnabled(marca);

        if (marca){
            scvTela.setOnTouchListener(null);
        } else {
            scvTela.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
        }
    }

    private void ativarProgresso() {
        clyTelaCarregando.setVisibility(View.VISIBLE);
        fabVoltar.setEnabled(false);
        ativacaoBotoes(false);
        nvvMenu.setVisibility(View.GONE);
    }

    private void desativarProgresso() {
        clyTelaCarregando.setVisibility(View.INVISIBLE);
        fabVoltar.setEnabled(true);
        ativacaoBotoes(true);
        nvvMenu.setVisibility(View.VISIBLE);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void salvarFoto() {
        // ENVIA UMA IMAGEM NOVA AO STORAGE
        StorageReference fileReference = storageRef.child(usuarioID
                + "." + getFileExtension(uriFoto));
        documentReference.update("refFoto", (usuarioID + "." + getFileExtension(uriFoto)));

        StorageTask uploadTask = fileReference.putFile(uriFoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        databaseRef.child(usuarioID).setValue(uri.toString());
                        documentReference.update("foto", uri.toString());

                        desativarProgresso();
                        feedbackAtualizou += 1;
                        mandarFeedbackAtualizou();
                        atualizarDados();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        cancelarOperacao(e.getMessage());
                        ativarModoEdicao();
                    }
                });
            }
        });
    }

    private void atualizarDados() {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!edtNomeUsuario.getText().toString().equals("") && !edtNomeUsuario.getText().toString().equals(documentSnapshot.getString("nome"))) {
                    ativarProgresso();
                    documentReference.update("nome", edtNomeUsuario.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            desativarProgresso();
                            feedbackAtualizou += 1;
                            mandarFeedbackAtualizou();
                        }
                    });
                }
                if (!edtCelularUsuario.getUnMasked().equals("") && !edtCelularUsuario.getUnMasked().equals(documentSnapshot.getString("celular"))) {
                    ativarProgresso();
                    documentReference.update("celular", ("55" + edtCelularUsuario.getUnMasked())).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            desativarProgresso();
                            feedbackAtualizou += 1;
                            mandarFeedbackAtualizou();
                        }
                    });
                }
                if (!edtRuaUsuario.getText().toString().equals("") && !edtRuaUsuario.getText().toString().equals(documentSnapshot.getString("rua"))) {
                    ativarProgresso();
                    documentReference.update("rua", edtRuaUsuario.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            desativarProgresso();
                            feedbackAtualizou += 1;
                            mandarFeedbackAtualizou();
                        }
                    });
                }
                if (!edtBairroUsuario.getText().toString().equals("") && !edtBairroUsuario.getText().toString().equals(documentSnapshot.getString("bairro"))) {
                    ativarProgresso();
                    documentReference.update("bairro", edtBairroUsuario.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            desativarProgresso();
                            feedbackAtualizou += 1;
                            mandarFeedbackAtualizou();
                        }
                    });
                }
                if (!edtEstadoUsuario.getText().toString().equals("") && !edtEstadoUsuario.getText().toString().equals(documentSnapshot.getString("estado"))) {
                    ativarProgresso();
                    documentReference.update("estado", edtEstadoUsuario.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            desativarProgresso();
                            feedbackAtualizou += 1;
                            mandarFeedbackAtualizou();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                cancelarOperacao(e.getMessage());
            }
        });
    }

    private void mandarFeedbackAtualizou() {
        if (feedbackAtualizou == 1) {
            Toast.makeText(PerfilUsuario.this, R.string.message_updateSuccess, Toast.LENGTH_SHORT).show();
        }
    }

    private void excluirRegistros(String nomeCollection) {
        db.collection(nomeCollection).whereEqualTo("dono", usuarioID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int quantidadeRegistros = queryDocumentSnapshots.size();
                for (int i = 0; i < quantidadeRegistros; i++) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // FAZ A LEITURA DAS FOTOS PARA REALIZAR A EXCLUSAO
                        String[] refFotosRegistro = new String[5];
                        refFotosRegistro[0] = document.getString("refFoto1");
                        refFotosRegistro[1] = document.getString("refFoto2");
                        refFotosRegistro[2] = document.getString("refFoto3");
                        refFotosRegistro[3] = document.getString("refFoto4");
                        refFotosRegistro[4] = document.getString("refFoto5");
                        StorageReference  storageRefRegistro  = FirebaseStorage.getInstance().getReference("uploads");
                        DatabaseReference databaseRefRegistro = FirebaseDatabase.getInstance().getReference("uploads");
                        for (int j = 0; j < 5; j++) {
                            // VERIFICA SE A FOTO RECUPERADA NAO E NULA
                            if (refFotosRegistro[j] != null && !refFotosRegistro[j].isEmpty()) {
                                int index = j;
                                // EXCLUI AS REFERENCIAS DAS FOTOS
                                StorageReference imageRef = storageRefRegistro.child(refFotosRegistro[j]);
                                imageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        String fotoField = "foto" + (index + 1);
                                        databaseRefRegistro.child(document.getId()).child(fotoField).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {}
                                        });
                                    }
                                });
                            }
                        }
                        // EXCLUI OS DADOS DO ANIMAL / ACESSORIO
                        db.collection(nomeCollection).document(document.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {}
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                cancelarOperacao(e.getMessage());
                            }
                        });
                    }
                }

                // DECREMENTA O NUMERO DAS ESTATISTICAS
                DocumentReference documentReferenceEstatisticas = db.collection("Estatisticas").document("Estatisticas");
                documentReferenceEstatisticas.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot != null) {
                            String campoEstatistica = nomeCollection.toLowerCase();
                            int registroInt = Integer.parseInt(documentSnapshot.getString(campoEstatistica));
                            documentReferenceEstatisticas.update(campoEstatistica, String.valueOf(registroInt -= quantidadeRegistros)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {}
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        cancelarOperacao(e.getMessage());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                cancelarOperacao(e.getMessage());
            }
        });
    }

    private void decrementarEstatisticas() {
        DocumentReference documentReferenceEstatisticas;
        documentReferenceEstatisticas = db.collection("Estatisticas").document("Estatisticas");
        documentReferenceEstatisticas.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int usuariosInt = Integer.parseInt(documentSnapshot.getString("usuarios"));
                documentReferenceEstatisticas.update("usuarios", String.valueOf(usuariosInt -= 1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        excluirContaUsuario();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                cancelarOperacao(e.getMessage());
            }
        });
    }

    private void excluirContaUsuario() {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null) {
                    String foto = documentSnapshot.getString("foto");
                    String refFoto = documentSnapshot.getString("refFoto");

                    if (foto != null && !foto.isEmpty()) {
                        StorageReference imageRef = storageRef.child(refFoto);
                        imageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                databaseRef.child(usuarioID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        irTelaLogin();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    } else {
                        databaseRef.child(usuarioID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                irTelaLogin();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                cancelarOperacao(e.getMessage());
            }
        });
    }

    private void irTelaLogin() {
        desativarProgresso();

        Intent enviarFeedback = new Intent(PerfilUsuario.this, FormLogin.class);
        enviarFeedback.putExtra("enviarFeedback", 2);
        startActivity(enviarFeedback);
        finish();
    }

    private void cancelarOperacao(String exception) {
        Toast.makeText(PerfilUsuario.this, exception, Toast.LENGTH_SHORT).show();
        desativarProgresso();
    }

    private String obterCelularSemMascara(String numero) {
        // numero     = +55 (18) 12345-6789
        // novoNumero = 18123456789
        String novoNumero = "";
        char charNumero;
        for (int i = 0; i < numero.length(); i++) {
            charNumero = numero.charAt(i);
            switch (i) {
                case 5:
                case 6:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 15:
                case 16:
                case 17:
                case 18:
                    novoNumero += charNumero;
                    break;
            }
        }
        return novoNumero;
    }

    private String obterCelularComMascara(String numero) {
        // numero     = 5518123456789
        // novoNumero = +55 (18) 12345-6789
        String novoNumero = "+55 ";
        char charNumero;
        for (int i = 0; i < numero.length(); i++) {
            charNumero = numero.charAt(i);
            switch (i) {
                case 0:
                case 1:
                    break;
                case 2:
                    novoNumero += "("  + charNumero;
                    break;
                case 4:
                    novoNumero += ") " + charNumero;
                    break;
                case 9:
                    novoNumero += "-"  + charNumero;
                    break;
                default:
                    novoNumero += charNumero;
                    break;
            }
        }
        return novoNumero;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uriFoto = data.getData();

            Picasso.get().load(uriFoto).into(imvFotoSmall);
            Picasso.get().load(uriFoto).into(imvFotoBig);
            atualizouFoto = true;
        }
    }

    @Override
    public void onBackPressed(){
        if (drlPagina.isDrawerOpen(GravityCompat.START)) {
            drlPagina.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }
}