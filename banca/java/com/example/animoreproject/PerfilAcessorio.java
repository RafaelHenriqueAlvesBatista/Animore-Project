package com.example.animoreproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class PerfilAcessorio extends AppCompatActivity {

    // VARIAVEIS DA ACTIVITY
    private static final int PICK_IMAGE_REQUEST = 1; // CONSTANTE PARA A SELECAO DE IMAGEM
    private String IDacessorio;
    private boolean textoRevelado;
    private int limiteCaracteres;
    private String descricaoCompleta, descricaoCurta;
    private String IDdono;
    private boolean modoEdicao = false;
    private int feedbackAtualizou = 0;
    private boolean excluindo = false;
    private String nomeAcessorio;
    private int[] statusFotos;
    private String[] uriFotosAcessorio;
    private Uri uriArquivo;
    private int[] fotosAcessorio;
    private Uri uriFoto;
    private boolean modoAdicionar;
    private int fotoAcessorioSelecionada;
    private boolean atualizouFoto = false;
    private boolean travarProgresso = false;
    private String[] refFotosAcessorio;

    // COMPONENTES TOOLBAR
    private ImageButton botaoMenu;

    // COMPONENTES TELA
    private DrawerLayout drlPagina;
    private View headerView;
    private FloatingActionButton fabVoltar;
    private ScrollView scvTela;

    // COMPONENTES MENU
    private NavigationView nvvMenu;
    private TextView txvMenuNomeUsuario;
    private ImageView imvMenuFotoUsuario;
    private Menu menu;
    private MenuItem mnuInicial;
    private MenuItem mnuPerfil;
    private MenuItem mnuAnimais;
    private MenuItem mnuSair;

    // COMPONENTES ACESSORIO
    private LinearLayout llyNomeAcessorio;
    private ImageView imvFotoAcessorio;
    private TextView txvNomeAcessorio, txvTipoAcessorio, txvLocalAcessorio;

    // DESCRICAO ACESSORIO
    private LinearLayout llyDescricaoNormal;
    private TextView txvDescricao, txvLerMais;

    // COMPONENTES DONO ACESSORIO
    private LinearLayout llyDonoAcessorio, llyWhatsappDono;
    private ImageView imvFotoDonoAcessorio;
    private TextView txvNomeDono, txvLocalDono;

    // FOTOS ACESSORIO
    private HorizontalScrollView hsvImagensAcessorio;
    private ImageView imvFotoAcessorio1, imvFotoAcessorio2, imvFotoAcessorio3, imvFotoAcessorio4, imvFotoAcessorio5;

    // OPCOES ACESSORIO
    private LinearLayout llyPerfilAcessorioOpcoes1, llyPerfilAcessorioOpcoes2, llyOpcoesVazio;
    private Button btnEditar, btnExcluir, btnSalvar, btnCancelar;

    // COMPONENTES EDICAO
    private LinearLayout llyNomeAcessorioEdit, llyDescricaoEdit, llyAdicionarFoto;
    private TextInputEditText edtNomeAcessorio;
    private TextView txvTipoAcessorioEdit, txvLocalAcessorioEdit;
    private EditText edtDescricao;
    private Button btnAdicionarFotoAcessorio;
    private ConstraintLayout clyTelaCarregando;
    private HorizontalScrollView hsvImagensAcessorioEdit;
    private ImageView imvFotoAcessorio1Edit, imvFotoAcessorio2Edit, imvFotoAcessorio3Edit, imvFotoAcessorio4Edit, imvFotoAcessorio5Edit;

    // COMPONENTES SUA FOTO
    private ConstraintLayout clyFoto;
    private Button btnSairFoto, btnImportarFoto;
    private ImageView imvFotoBig;

    // VARIAVEIS DO FIREBASE
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;
    private DocumentReference documentRefUsuario;
    private DocumentReference documentRefAcessorio;
    private StorageReference storageRefAcessorio;
    private DatabaseReference databaseRefAcessorio;

    // VARIAVEIS PARA ABRIR O WHATSAPP
    String nomePacoteWhatsApp = "com.whatsapp";
    String nomeDono;
    String numeroDono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_acessorio);
        instanciarVariaveis();
        instanciarComponentes();
        programarComponentes();
        procurarUsuarioAtual();
        recuperarDadosUsuario();
        receberIntent();
        recuperarDadosAcessorio();
    }

    private void instanciarVariaveis() {
        textoRevelado            = false;
        limiteCaracteres         = 200;

        modoAdicionar            = false;
        fotoAcessorioSelecionada = -1;
        fotosAcessorio           = new int[5];
        uriFotosAcessorio        = new String[5];
        statusFotos              = new int[5];
        refFotosAcessorio        = new String[5];
    }

    private void instanciarComponentes() {
        botaoMenu                 = findViewById(R.id.botaoMenu);

        drlPagina                 = findViewById(R.id.drlPagina);

        nvvMenu                   = findViewById(R.id.nvvMenu);
        headerView                = nvvMenu.getHeaderView(0);

        txvMenuNomeUsuario        = headerView.findViewById(R.id.txvMenuNomeUsuario);
        imvMenuFotoUsuario        = headerView.findViewById(R.id.imvMenuFotoUsuario);
        menu                      = nvvMenu.getMenu();
        mnuInicial                = menu.findItem(R.id.menu_paginaInicial);
        mnuPerfil                 = menu.findItem(R.id.menu_perfil);
        mnuAnimais                = menu.findItem(R.id.menu_meusAnimais);
        mnuSair                   = menu.findItem(R.id.menu_sair);

        fabVoltar                 = findViewById(R.id.fabVoltar);

        scvTela                   = findViewById(R.id.scvTela);

        llyNomeAcessorio          = findViewById(R.id.llyNomeAcessorio);
        imvFotoAcessorio          = findViewById(R.id.imvFotoAcessorio);
        txvNomeAcessorio          = findViewById(R.id.txvNomeAcessorio);
        txvTipoAcessorio          = findViewById(R.id.txvTipoAcessorio);
        txvLocalAcessorio         = findViewById(R.id.txvLocalAcessorio);

        llyDescricaoNormal        = findViewById(R.id.llyDescricaoNormal);
        txvDescricao              = findViewById(R.id.txvDescricao);
        txvLerMais                = findViewById(R.id.txvLerMais);

        llyDonoAcessorio          = findViewById(R.id.llyDonoAcessorio);
        imvFotoDonoAcessorio      = findViewById(R.id.imvFotoDonoAcessorio);
        txvNomeDono               = findViewById(R.id.txvNomeDono);
        txvLocalDono              = findViewById(R.id.txvLocalDono);
        llyWhatsappDono           = findViewById(R.id.llyWhatsappDono);

        hsvImagensAcessorio       = findViewById(R.id.hsvImagensAcessorio);
        imvFotoAcessorio1         = findViewById(R.id.imvFotoAcessorio1);
        imvFotoAcessorio2         = findViewById(R.id.imvFotoAcessorio2);
        imvFotoAcessorio3         = findViewById(R.id.imvFotoAcessorio3);
        imvFotoAcessorio4         = findViewById(R.id.imvFotoAcessorio4);
        imvFotoAcessorio5         = findViewById(R.id.imvFotoAcessorio5);

        llyPerfilAcessorioOpcoes1 = findViewById(R.id.llyPerfilAcessorioOpcoes1);
        llyPerfilAcessorioOpcoes2 = findViewById(R.id.llyPerfilAcessorioOpcoes2);
        llyOpcoesVazio            = findViewById(R.id.llyOpcoesVazio);

        btnEditar                 = findViewById(R.id.btnEditar);
        btnExcluir                = findViewById(R.id.btnExcluir);
        btnSalvar                 = findViewById(R.id.btnSalvar);
        btnCancelar               = findViewById(R.id.btnCancelar);

        llyNomeAcessorioEdit      = findViewById(R.id.llyNomeAcessorioEdit);
        llyDescricaoEdit          = findViewById(R.id.llyDescricaoEdit);
        edtNomeAcessorio          = findViewById(R.id.edtNomeAcessorio);
        txvTipoAcessorioEdit      = findViewById(R.id.txvTipoAcessorioEdit);
        txvLocalAcessorioEdit     = findViewById(R.id.txvLocalAcessorioEdit);
        edtDescricao              = findViewById(R.id.edtDescricao);
        llyAdicionarFoto          = findViewById(R.id.llyAdicionarFoto);
        btnAdicionarFotoAcessorio = findViewById(R.id.btnAdicionarFotoAcessorio);
        hsvImagensAcessorioEdit   = findViewById(R.id.hsvImagensAcessorioEdit);
        imvFotoAcessorio1Edit     = findViewById(R.id.imvFotoAcessorio1Edit);
        imvFotoAcessorio2Edit     = findViewById(R.id.imvFotoAcessorio2Edit);
        imvFotoAcessorio3Edit     = findViewById(R.id.imvFotoAcessorio3Edit);
        imvFotoAcessorio4Edit     = findViewById(R.id.imvFotoAcessorio4Edit);
        imvFotoAcessorio5Edit     = findViewById(R.id.imvFotoAcessorio5Edit);

        clyTelaCarregando         = findViewById(R.id.clyTelaCarregando);

        clyFoto                   = findViewById(R.id.clyFoto);
        btnSairFoto               = findViewById(R.id.btnSairFoto);
        btnImportarFoto           = findViewById(R.id.btnImportarFoto);
        imvFotoBig                = findViewById(R.id.imvFotoBig);
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
                Intent telaInicial = new Intent(PerfilAcessorio.this, TelaInicial.class);
                startActivity(telaInicial);
                return true;
            }
        });

        mnuPerfil.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                Intent perfilUsuario = new Intent(PerfilAcessorio.this, PerfilUsuario.class);
                startActivity(perfilUsuario);
                return true;
            }
        });

        mnuAnimais.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                Intent meusAnimais = new Intent(PerfilAcessorio.this, ListaMeusAnimais.class);
                startActivity(meusAnimais);
                return true;
            }
        });

        mnuSair.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PerfilAcessorio.this);
                builder.setMessage("Deseja realmente encerrar a sessão?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                drlPagina.closeDrawer(GravityCompat.START);
                                FirebaseAuth.getInstance().signOut();
                                Intent enviarFeedback = new Intent(PerfilAcessorio.this, FormLogin.class);
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

        fabVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txvLerMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!textoRevelado) {
                    txvDescricao.setText(descricaoCompleta);
                    txvLerMais.setText(getResources().getString(R.string.btn_readLess));
                    textoRevelado = true;
                } else {
                    txvDescricao.setText(descricaoCurta + "...");
                    txvLerMais.setText(getResources().getString(R.string.btn_readMore));
                    textoRevelado = false;
                }
            }
        });

        llyWhatsappDono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avisoWhatsApp();
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativarModoEdicao();
                btnCancelar.setEnabled(true);
                btnSalvar.setEnabled(true);
            }
        });

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PerfilAcessorio.this);
                builder.setIcon(R.drawable.ic_aviso);
                builder.setTitle("Aviso");
                builder.setMessage("\nDeseja realmente excluir?\nEssa operação NÃO poderá ser desfeita.")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                atualizarNumeroAcessorios();
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
                recuperarDadosAcessorio();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // VERIFICA SE ALGUM CAMPO ESTA VAZIO
                if (edtNomeAcessorio.getText().toString().equals("")) {
                    Toast.makeText(PerfilAcessorio.this, R.string.exception_emptyInputs, Toast.LENGTH_SHORT).show();
                } else {
                    ativarProgresso();
                    desativarModoEdicao();
                    salvarFotosAcessorio(0);
                }
            }
        });

        btnAdicionarFotoAcessorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                clyFoto.setVisibility(View.VISIBLE);
                modoAdicionar = true;
                sobrescreverPrevia();
            }
        });

        imvFotoAcessorio1Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                clyFoto.setVisibility(View.VISIBLE);
                fotoAcessorioSelecionada = 0;
                sobrescreverPrevia();
            }
        });
        imvFotoAcessorio2Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                clyFoto.setVisibility(View.VISIBLE);
                fotoAcessorioSelecionada = 1;
                sobrescreverPrevia();
            }
        });
        imvFotoAcessorio3Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                clyFoto.setVisibility(View.VISIBLE);
                fotoAcessorioSelecionada = 2;
                sobrescreverPrevia();
            }
        });
        imvFotoAcessorio4Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                clyFoto.setVisibility(View.VISIBLE);
                fotoAcessorioSelecionada = 3;
                sobrescreverPrevia();
            }
        });
        imvFotoAcessorio5Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                clyFoto.setVisibility(View.VISIBLE);
                fotoAcessorioSelecionada = 4;
                sobrescreverPrevia();
            }
        });

        btnSairFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(true);
                clyFoto.setVisibility(View.INVISIBLE);
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
    }

    private void salvarFotosAcessorio(int index) {
        if (index < 5) {
            // VERIFICA SE A URI NAO ESTA VAZIA
            switch (statusFotos[index]) {
                case 2:
                    travarProgresso = true;
                    if (refFotosAcessorio[index] != null && !refFotosAcessorio[index].isEmpty()) {
                        // EXCLUI A FOTO ANTIGA
                        StorageReference imageRef = storageRefAcessorio.child(refFotosAcessorio[index]);
                        imageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String fotoField = "foto" + (index + 1);
                                databaseRefAcessorio.child(IDacessorio).child(fotoField).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        inserirFotoAcessorio(index);
                                    }
                                });
                            }
                        });
                    } else {
                        inserirFotoAcessorio(index);
                    }
                    break;
                default:
                    System.out.println("ERRO AO SALVAR FOTOS!");
                    break;
            }

            salvarFotosAcessorio(index + 1);
        } else {
            atualizarDados();
        }
    }

    private void inserirFotoAcessorio(int index) {
        // CONVERTE AS STRINGS DA URI DAS FOTOS SALVAS, DE VOLTA PARA URI
        uriArquivo = Uri.parse(uriFotosAcessorio[index]);

        // ATRIBUI A NUMERACAO DAS FOTOS
        String numFoto = "";

        switch (index) {
            case 0:
                numFoto = "_a";
                break;
            case 1:
                numFoto = "_b";
                break;
            case 2:
                numFoto = "_c";
                break;
            case 3:
                numFoto = "_d";
                break;
            case 4:
                numFoto = "_e";
                break;
            default:
                numFoto = "_xxx";
                break;
        }

        // SALVA O NOME DA FOTO COM O ID DO USUARIO MAIS A EXTENSAO NA TABELA DO USUARIO
        String nomeArquivo = IDacessorio + numFoto + "." + getFileExtension(uriArquivo);
        StorageReference fileReference = storageRefAcessorio.child(nomeArquivo);

        switch (index) {
            case 0:
                documentRefAcessorio.update("refFoto1", nomeArquivo);
                break;
            case 1:
                documentRefAcessorio.update("refFoto2", nomeArquivo);
                break;
            case 2:
                documentRefAcessorio.update("refFoto3", nomeArquivo);
                break;
            case 3:
                documentRefAcessorio.update("refFoto4", nomeArquivo);
                break;
            case 4:
                documentRefAcessorio.update("refFoto5", nomeArquivo);
                break;
            default:
                break;
        }

        // FAZ O UPLOAD DA IMAGEM
        fileReference.putFile(uriArquivo).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String fotoField = "foto" + (index + 1);
                        databaseRefAcessorio.child(IDacessorio).child(fotoField).setValue(uri.toString());
                        documentRefAcessorio.update(fotoField, uri.toString());

                        atualizouFoto = true;
                        statusFotos[index] = 1;

                        salvarFotosAcessorio(index + 1);
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

    private void atualizarDados() {
        documentRefAcessorio.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!edtNomeAcessorio.getText().toString().equals("") && !edtNomeAcessorio.getText().toString().equals(documentSnapshot.getString("nome"))) {
                    ativarProgresso();
                    travarProgresso = true;
                    documentRefAcessorio.update("nome", edtNomeAcessorio.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            travarProgresso = false;
                            desativarProgresso();
                            feedbackAtualizou += 1;
                            mandarFeedbackAtualizou();
                        }
                    });
                }
                if (!edtDescricao.getText().toString().equals(documentSnapshot.getString("descricao"))) {
                    ativarProgresso();
                    travarProgresso = true;
                    documentRefAcessorio.update("descricao", edtDescricao.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            travarProgresso = false;
                            desativarProgresso();
                            feedbackAtualizou += 1;
                            mandarFeedbackAtualizou();
                        }
                    });
                }
                if (atualizouFoto) {
                    atualizouFoto = false;
                    travarProgresso = false;
                    desativarProgresso();
                    feedbackAtualizou += 1;
                    mandarFeedbackAtualizou();
                } else if (!travarProgresso) {
                    desativarProgresso();
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
            Toast.makeText(PerfilAcessorio.this, R.string.message_updateSuccess, Toast.LENGTH_SHORT).show();
        }
    }

    private void procurarUsuarioAtual() {
        usuarioID          = FirebaseAuth.getInstance().getCurrentUser().getUid();

        documentRefUsuario = db.collection("Usuarios").document(usuarioID);
    }

    private void recuperarDadosUsuario() {
        documentRefUsuario.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null){
                    txvMenuNomeUsuario.setText(documentSnapshot.getString("nome"));

                    carregarFotoUsuario();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                cancelarOperacao(e.getMessage());
            }
        });
    }

    private void carregarFotoUsuario() {
        documentRefUsuario.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null) {
                    String foto = documentSnapshot.getString("foto");
                    if (foto != null && !foto.isEmpty()){
                        imvMenuFotoUsuario.setBackgroundColor(getResources().getColor(R.color.transparent));
                        Picasso.get().load(documentSnapshot.getString("foto")).into(imvMenuFotoUsuario);
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

    private void ativacaoBotoes(boolean marca) {
        if (modoEdicao) {
            btnCancelar.setEnabled(marca);
            btnSalvar.setEnabled(marca);
        } else {
            btnEditar.setEnabled(marca);
            btnExcluir.setEnabled(marca);
        }
        edtNomeAcessorio.setEnabled(marca);
        edtDescricao.setEnabled(marca);
        btnAdicionarFotoAcessorio.setEnabled(marca);

        if (marca) {
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

    private void cancelarOperacao(String exception) {
        Toast.makeText(PerfilAcessorio.this, exception, Toast.LENGTH_SHORT).show();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void receberIntent() {
        Intent receberIntent = getIntent();
        IDacessorio = receberIntent.getStringExtra("IDacessorio");
    }

    private void recuperarDadosAcessorio() {
        /* -- COMANDO SQL EQUIVALENTE -
            SELECT    ?    FROM    Acessorios;
            ? = IDacessorio;
         */
        documentRefAcessorio = db.collection("Acessorios").document(IDacessorio);
        storageRefAcessorio = FirebaseStorage.getInstance().getReference("uploads");
        databaseRefAcessorio = FirebaseDatabase.getInstance().getReference("uploads");

        documentRefAcessorio.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null && documentSnapshot.exists() && !excluindo) {
                    txvNomeAcessorio.setText(documentSnapshot.getString("nome"));
                    txvTipoAcessorio.setText(documentSnapshot.getString("tipo"));

                    nomeAcessorio = documentSnapshot.getString("nome");

                    String descricao = documentSnapshot.getString("descricao");
                    txvDescricao.setText(descricao);

                    String foto1 = documentSnapshot.getString("foto1");
                    String foto2 = documentSnapshot.getString("foto2");
                    String foto3 = documentSnapshot.getString("foto3");
                    String foto4 = documentSnapshot.getString("foto4");
                    String foto5 = documentSnapshot.getString("foto5");
                    refFotosAcessorio[0] = documentSnapshot.getString("refFoto1");
                    refFotosAcessorio[1] = documentSnapshot.getString("refFoto2");
                    refFotosAcessorio[2] = documentSnapshot.getString("refFoto3");
                    refFotosAcessorio[3] = documentSnapshot.getString("refFoto4");
                    refFotosAcessorio[4] = documentSnapshot.getString("refFoto5");

                    String[] fotos = {foto1, foto2, foto3, foto4, foto5};
                    String fotoNaoVazia = null;

                    for (String foto : fotos) {
                        if (foto != null && !foto.isEmpty()) {
                            fotoNaoVazia = foto;
                            break;
                        }
                    }

                    for (int i = 0; i < fotos.length; i++) {
                        if (fotos[i] != null && !fotos[i].isEmpty()) {
                            statusFotos[i] = 1;
                        }
                    }

                    IDdono     = documentSnapshot.getString("dono");

                    carregarFotosAcessorio(foto1, foto2, foto3, foto4, foto5, fotoNaoVazia);
                    limitarDescricaoGrande(descricao);
                    recuperarDadosDonoAcessorio(IDdono.equals(usuarioID));
                }
            }
        });
    }

    private void carregarFotosAcessorio(String foto1, String foto2, String foto3, String foto4, String foto5, String fotoNaoVazia) {
        if (!excluindo) {
            if (foto1 != null && !foto1.isEmpty()){
                imvFotoAcessorio1.setBackgroundColor(getResources().getColor(R.color.transparent));
                Picasso.get().load(foto1).into(imvFotoAcessorio1);
                imvFotoAcessorio1Edit.setBackgroundColor(getResources().getColor(R.color.transparent));
                Picasso.get().load(foto1).into(imvFotoAcessorio1Edit);
                fotosAcessorio[0] = 1;
            } else {
                imvFotoAcessorio1.setImageResource(R.color.text_300);
                imvFotoAcessorio1.setBackgroundColor(getResources().getColor(R.color.transparent));
                imvFotoAcessorio1Edit.setImageResource(R.color.text_300);
                imvFotoAcessorio1Edit.setBackgroundColor(getResources().getColor(R.color.transparent));
                fotosAcessorio[0] = 0;
            }
            if (foto2 != null && !foto2.isEmpty()){
                imvFotoAcessorio2.setBackgroundColor(getResources().getColor(R.color.transparent));
                Picasso.get().load(foto2).into(imvFotoAcessorio2);
                imvFotoAcessorio2Edit.setBackgroundColor(getResources().getColor(R.color.transparent));
                Picasso.get().load(foto2).into(imvFotoAcessorio2Edit);
                fotosAcessorio[1] = 1;
            } else {
                imvFotoAcessorio2.setImageResource(R.color.text_300);
                imvFotoAcessorio2.setBackgroundColor(getResources().getColor(R.color.transparent));
                imvFotoAcessorio2Edit.setImageResource(R.color.text_300);
                imvFotoAcessorio2Edit.setBackgroundColor(getResources().getColor(R.color.transparent));
                fotosAcessorio[1] = 0;
            }
            if (foto3 != null && !foto3.isEmpty()){
                imvFotoAcessorio3.setBackgroundColor(getResources().getColor(R.color.transparent));
                Picasso.get().load(foto3).into(imvFotoAcessorio3);
                imvFotoAcessorio3Edit.setBackgroundColor(getResources().getColor(R.color.transparent));
                Picasso.get().load(foto3).into(imvFotoAcessorio3Edit);
                fotosAcessorio[2] = 1;
            } else {
                imvFotoAcessorio3.setImageResource(R.color.text_300);
                imvFotoAcessorio3.setBackgroundColor(getResources().getColor(R.color.transparent));
                imvFotoAcessorio3Edit.setImageResource(R.color.text_300);
                imvFotoAcessorio3Edit.setBackgroundColor(getResources().getColor(R.color.transparent));
                fotosAcessorio[2] = 0;
            }
            if (foto4 != null && !foto4.isEmpty()){
                imvFotoAcessorio4.setBackgroundColor(getResources().getColor(R.color.transparent));
                Picasso.get().load(foto4).into(imvFotoAcessorio4);
                imvFotoAcessorio4Edit.setBackgroundColor(getResources().getColor(R.color.transparent));
                Picasso.get().load(foto4).into(imvFotoAcessorio4Edit);
                fotosAcessorio[3] = 1;
            } else {
                imvFotoAcessorio4.setImageResource(R.color.text_300);
                imvFotoAcessorio4.setBackgroundColor(getResources().getColor(R.color.transparent));
                imvFotoAcessorio4Edit.setImageResource(R.color.text_300);
                imvFotoAcessorio4Edit.setBackgroundColor(getResources().getColor(R.color.transparent));
                fotosAcessorio[3] = 0;
            }
            if (foto5 != null && !foto5.isEmpty()){
                imvFotoAcessorio5.setBackgroundColor(getResources().getColor(R.color.transparent));
                Picasso.get().load(foto5).into(imvFotoAcessorio5);
                imvFotoAcessorio5Edit.setBackgroundColor(getResources().getColor(R.color.transparent));
                Picasso.get().load(foto5).into(imvFotoAcessorio5Edit);
                fotosAcessorio[4] = 1;
            } else {
                imvFotoAcessorio5.setImageResource(R.color.text_300);
                imvFotoAcessorio5.setBackgroundColor(getResources().getColor(R.color.transparent));
                imvFotoAcessorio5Edit.setImageResource(R.color.text_300);
                imvFotoAcessorio5Edit.setBackgroundColor(getResources().getColor(R.color.transparent));
                fotosAcessorio[4] = 0;
            }
            imvFotoAcessorio.setBackgroundColor(getResources().getColor(R.color.transparent));
            Picasso.get().load(fotoNaoVazia).into(imvFotoAcessorio);
        }
    }

    private void recuperarDadosDonoAcessorio(boolean usuarioLogado) {
        if (usuarioLogado) {
            documentRefUsuario.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot != null) {
                        txvNomeDono.setText(documentSnapshot.getString("nome"));
                        txvLocalDono.setText(documentSnapshot.getString("rua"));
                        txvLocalAcessorio.setText(
                                documentSnapshot.getString("cidade") +
                                " - " +
                                documentSnapshot.getString("estado"));
                        String foto = documentSnapshot.getString("foto");
                        if (foto != null && !foto.isEmpty()) {
                            imvFotoDonoAcessorio.setBackgroundColor(getResources().getColor(R.color.transparent));
                            Picasso.get().load(foto).into(imvFotoDonoAcessorio);
                        }

                        llyWhatsappDono.setVisibility(View.GONE);
                        llyOpcoesVazio.setVisibility(View.GONE);
                        llyPerfilAcessorioOpcoes1.setVisibility(View.VISIBLE);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    cancelarOperacao(e.getMessage());
                }
            });
        } else {
            db.collection("Usuarios").document(IDdono).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot != null) {
                        txvNomeDono.setText(documentSnapshot.getString("nome"));
                        txvLocalDono.setText(documentSnapshot.getString("rua"));
                        txvLocalAcessorio.setText(
                                documentSnapshot.getString("cidade") +
                                " - " +
                                documentSnapshot.getString("estado"));
                        String foto = documentSnapshot.getString("foto");
                        if (foto != null && !foto.isEmpty()) {
                            imvFotoDonoAcessorio.setBackgroundColor(getResources().getColor(R.color.transparent));
                            Picasso.get().load(foto).into(imvFotoDonoAcessorio);
                        }
                        nomeDono   = documentSnapshot.getString("nome");
                        numeroDono = documentSnapshot.getString("celular");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    cancelarOperacao(e.getMessage());
                }
            });
        }
    }

    private void limitarDescricaoGrande(String descricao) {
        descricaoCompleta = descricao;
        if (descricaoCompleta.length() > 0 && descricaoCompleta.length() > limiteCaracteres) {
            descricaoCurta = descricaoCompleta.substring(0, limiteCaracteres);
            txvDescricao.setText(descricaoCurta + "...");
            txvLerMais.setVisibility(View.VISIBLE);
        } else if (descricaoCompleta.length() == 0) {
            txvDescricao.setText(getResources().getString(R.string.text_noDescription));
            txvDescricao.setTextColor(getResources().getColor(R.color.text_700));
        }
    }

    private void avisoWhatsApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PerfilAcessorio.this);
        builder.setIcon(R.drawable.ic_botao_whatsapp);
        builder.setTitle("Conversar via WhatsApp");
        builder.setMessage("\nIniciar conversa com " + nomeDono + "?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        abrirWhatsApp(numeroDono);
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .create()
                .show();
    }

    private void abrirWhatsApp(String numero) {
        if (aplicativoEstaInstalado(nomePacoteWhatsApp)) {
            Intent abrirWhatsApp = new Intent("android.intent.action.MAIN");
            abrirWhatsApp.setAction(Intent.ACTION_SEND);
            abrirWhatsApp.putExtra("jid", PhoneNumberUtils.stripSeparators(numero) + "@s.whatsapp.net");
            abrirWhatsApp.putExtra(Intent.EXTRA_TEXT,
                    getResources().getString(R.string.text_whatsappMessageStart)
                            + " "
                            + getResources().getString(R.string.text_whatsappMessageStartAcessory)
                            + " "
                            + nomeAcessorio
                            + " "
                            + getResources().getString(R.string.text_whatsappMessageBody)
                            + " "
                            + getResources().getString(R.string.text_whatsappMessageEndAcessory));
            abrirWhatsApp.setType("text/plain");
            abrirWhatsApp.setPackage(nomePacoteWhatsApp);
            startActivity(abrirWhatsApp);
        } else {
            Uri linkPlayStore = Uri.parse("market://details?id=" + nomePacoteWhatsApp);
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, linkPlayStore);
            startActivity(marketIntent);
        }
    }

    private boolean aplicativoEstaInstalado(String nomePacote) {
        PackageManager packageManager = getPackageManager();
        try {
            packageManager.getPackageInfo(nomePacote, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void ativarModoEdicao() {
        modoEdicao = true;
        llyNomeAcessorio.setVisibility(View.GONE);
        llyNomeAcessorioEdit.setVisibility(View.VISIBLE);
        llyDescricaoNormal.setVisibility(View.GONE);
        llyDescricaoEdit.setVisibility(View.VISIBLE);
        llyPerfilAcessorioOpcoes1.setVisibility(View.GONE);
        llyPerfilAcessorioOpcoes2.setVisibility(View.VISIBLE);
        llyAdicionarFoto.setVisibility(View.VISIBLE);
        hsvImagensAcessorio.setVisibility(View.INVISIBLE);
        hsvImagensAcessorio.setEnabled(false);
        hsvImagensAcessorioEdit.setVisibility(View.VISIBLE);

        edtNomeAcessorio.setText(txvNomeAcessorio.getText());
        txvTipoAcessorioEdit.setText(txvTipoAcessorio.getText());
        txvLocalAcessorioEdit.setText(txvLocalAcessorio.getText());
        edtDescricao.setText(descricaoCompleta);
        llyDonoAcessorio.setForeground(getResources().getDrawable(R.drawable.color_brighten_1));
    }

    private void desativarModoEdicao() {
        modoEdicao = false;
        feedbackAtualizou = 0;
        llyNomeAcessorio.setVisibility(View.VISIBLE);
        llyNomeAcessorioEdit.setVisibility(View.GONE);
        llyDescricaoNormal.setVisibility(View.VISIBLE);
        llyDescricaoEdit.setVisibility(View.GONE);
        llyPerfilAcessorioOpcoes1.setVisibility(View.VISIBLE);
        llyPerfilAcessorioOpcoes2.setVisibility(View.GONE);
        llyAdicionarFoto.setVisibility(View.GONE);
        hsvImagensAcessorio.setVisibility(View.VISIBLE);
        hsvImagensAcessorio.setEnabled(true);
        hsvImagensAcessorioEdit.setVisibility(View.GONE);
        llyDonoAcessorio.setForeground(getResources().getDrawable(R.drawable.color_transparent));
    }

    private int checarFotosVazias(){
        int fotoVazia = -1;
        for (int i = 0; i < fotosAcessorio.length; i++) {
            if (fotosAcessorio[i] == 0) {
                fotoVazia = i;
                break;
            } else if (i == 4) {
                fotoVazia = 4;
            }
        }
        return fotoVazia;
    }

    private void selecionarCampoFoto (int fotoVazia){
        switch (fotoVazia) {
            case 0:
                Picasso.get().load(uriFoto).into(imvFotoAcessorio1Edit);
                uriFotosAcessorio[0] = uriFoto.toString();
                statusFotos[0] = 2;
                break;
            case 1:
                Picasso.get().load(uriFoto).into(imvFotoAcessorio2Edit);
                uriFotosAcessorio[1] = uriFoto.toString();
                statusFotos[1] = 2;
                break;
            case 2:
                Picasso.get().load(uriFoto).into(imvFotoAcessorio3Edit);
                uriFotosAcessorio[2] = uriFoto.toString();
                statusFotos[2] = 2;
                break;
            case 3:
                Picasso.get().load(uriFoto).into(imvFotoAcessorio4Edit);
                uriFotosAcessorio[3] = uriFoto.toString();
                statusFotos[3] = 2;
                break;
            case 4:
                Picasso.get().load(uriFoto).into(imvFotoAcessorio5Edit);
                uriFotosAcessorio[4] = uriFoto.toString();
                statusFotos[4] = 2;
                break;
            default:
                System.out.println("ERRO AO SELECIONAR FOTO!");
                break;
        }
        if (fotoVazia > -1 && fotoVazia < 5) {
            fotosAcessorio[fotoVazia] = 1;
        }
    }

    private void sobrescreverPrevia() {
        if (modoAdicionar) {
            imvFotoBig.setImageResource(R.color.text_300);
            imvFotoBig.setBackgroundColor(getResources().getColor(R.color.transparent));
        } else {
            switch (fotoAcessorioSelecionada) {
                case 0:
                    if (imvFotoAcessorio1Edit.getDrawable() != null) {
                        imvFotoBig.setImageResource(R.color.transparent);
                        imvFotoBig.setBackground(imvFotoAcessorio1Edit.getDrawable());
                    }
                    break;
                case 1:
                    if (imvFotoAcessorio2Edit.getDrawable() != null) {
                        imvFotoBig.setImageResource(R.color.transparent);
                        imvFotoBig.setBackground(imvFotoAcessorio2Edit.getDrawable());
                    }
                    break;
                case 2:
                    if (imvFotoAcessorio3Edit.getDrawable() != null) {
                        imvFotoBig.setImageResource(R.color.transparent);
                        imvFotoBig.setBackground(imvFotoAcessorio3Edit.getDrawable());
                    }
                    break;
                case 3:
                    if (imvFotoAcessorio4Edit.getDrawable() != null) {
                        imvFotoBig.setImageResource(R.color.transparent);
                        imvFotoBig.setBackground(imvFotoAcessorio4Edit.getDrawable());
                    }
                    break;
                case 4:
                    if (imvFotoAcessorio5Edit.getDrawable() != null) {
                        imvFotoBig.setImageResource(R.color.transparent);
                        imvFotoBig.setBackground(imvFotoAcessorio5Edit.getDrawable());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void atualizarNumeroAcessorios() {
        ativarProgresso();
        documentRefUsuario.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null) {
                    int numAcessoriosInt = Integer.parseInt(documentSnapshot.getString("numAcessorios"));
                    documentRefUsuario.update("numAcessorios", String.valueOf(numAcessoriosInt -= 1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            decrementarEstatisticas();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            cancelarOperacao(e.getMessage());
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

    private void decrementarEstatisticas() {
        DocumentReference documentReferenceEstatisticas = db.collection("Estatisticas").document("Estatisticas");
        documentReferenceEstatisticas.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int acessoriosInt = Integer.parseInt(documentSnapshot.getString("acessorios"));
                documentReferenceEstatisticas.update("acessorios", String.valueOf(acessoriosInt -= 1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        excluirRegistroAcessorio();
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

    private void excluirRegistroAcessorio() {
        excluindo = true;

        documentRefAcessorio.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null) {
                    for (int i = 0; i < 5; i++) {
                        if (refFotosAcessorio[i] != null && !refFotosAcessorio[i].isEmpty()) {
                            int index = i;
                            // EXCLUI AS REFERENCIAS DAS FOTOS
                            StorageReference imageRef = storageRefAcessorio.child(refFotosAcessorio[i]);
                            imageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String fotoField = "foto" + (index + 1);
                                    databaseRefAcessorio.child(IDacessorio).child(fotoField).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {}
                                    });
                                }
                            });
                        }
                    }
                    documentRefAcessorio.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            desativarProgresso();
                            Toast.makeText(PerfilAcessorio.this, R.string.message_deleteSuccess, Toast.LENGTH_SHORT).show();
                            finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uriFoto = data.getData();

            Picasso.get().load(uriFoto).into(imvFotoBig);

            if (modoAdicionar) {
                selecionarCampoFoto(checarFotosVazias());
            } else {
                selecionarCampoFoto(fotoAcessorioSelecionada);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drlPagina.isDrawerOpen(GravityCompat.START)) {
            drlPagina.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }
}