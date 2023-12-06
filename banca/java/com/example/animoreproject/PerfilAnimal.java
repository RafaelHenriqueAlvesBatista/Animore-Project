package com.example.animoreproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.animoreproject.classes.AdapterVacinaCadastro;
import com.example.animoreproject.classes.AdapterVacinaPerfil;
import com.example.animoreproject.classes.ItemVacinaCadastro;
import com.example.animoreproject.classes.ItemVacinaPerfil;
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

import java.util.ArrayList;
import java.util.List;

public class PerfilAnimal extends AppCompatActivity {

    // VARIAVEIS DA ACTIVITY
    private static final int PICK_IMAGE_REQUEST = 1; // CONSTANTE PARA A SELECAO DE IMAGEM
    private String IDanimal;
    private boolean textoRevelado;
    private int limiteCaracteres;
    private String descricaoCompleta, descricaoCurta;
    private String IDdono;
    private boolean modoEdicao = false;
    private int feedbackAtualizou = 0;
    private boolean excluindo = false;
    private String nomeAnimal;
    private int[] statusFotos;
    private String[] uriFotosAnimal;
    private Uri uriArquivo;
    private int[] fotosAnimal;
    private Uri uriFoto;
    private boolean modoAdicionar;
    private int fotoAnimalSelecionada;
    private boolean atualizouFoto = false;
    private boolean travarProgresso = false;
    private String[] refFotosAnimal;
    private List<ItemVacinaPerfil> itemVacinaNormal;
    private AdapterVacinaPerfil adapterVacinaNormal;
    private String[] vacinasDisponiveis;
    private List<ItemVacinaCadastro> itemVacinaEdit;
    private AdapterVacinaCadastro adapterVacinaEdit;

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

    // COMPONENTES ANIMAL
    private LinearLayout llyNomeAnimal;
    private ImageView imvFotoAnimal;
    private TextView txvNomeAnimal, txvRacaAnimal, txvIdadeAnimal, txvLocalAnimal;

    // DESCRICAO ANIMAL
    private LinearLayout llyDescricaoNormal;
    private TextView txvDescricao, txvLerMais;

    // COMPONENTES DONO ANIMAL
    private LinearLayout llyDonoAnimal, llyWhatsappDono;
    private ImageView imvFotoDonoAnimal;
    private TextView txvNomeDono, txvLocalDono;

    // VACINAS ANIMAL
    private LinearLayout llyVacinasNormal;
    private RecyclerView rcvVacinas;
    private TextView txvVacinaAnimalVazio;

    // FOTOS ANIMAL
    private HorizontalScrollView hsvImagensAnimal;
    private ImageView imvFotoAnimal1, imvFotoAnimal2, imvFotoAnimal3, imvFotoAnimal4, imvFotoAnimal5;

    // OPCOES ANIMAL
    private LinearLayout llyPerfilAnimalOpcoes1, llyPerfilAnimalOpcoes2, llyOpcoesVazio;
    private Button btnEditar, btnExcluir, btnSalvar, btnCancelar;

    // COMPONENTES EDICAO
    private LinearLayout llyNomeAnimalEdit, llyDescricaoEdit, llyVacinasEdit, llyAdicionarFoto;
    private TextInputEditText edtNomeAnimal, edtIdadeAnimal;
    private TextView txvRacaAnimalEdit, txvLocalAnimalEdit;
    private EditText edtDescricao;
    private RecyclerView rcvVacinasEdit;
    private Button btnAdicionarFotoAnimal;
    private ConstraintLayout clyTelaCarregando;
    private HorizontalScrollView hsvImagensAnimalEdit;
    private ImageView imvFotoAnimal1Edit, imvFotoAnimal2Edit, imvFotoAnimal3Edit, imvFotoAnimal4Edit, imvFotoAnimal5Edit;

    // COMPONENTES SUA FOTO
    private ConstraintLayout clyFoto;
    private Button btnSairFoto, btnImportarFoto;
    private ImageView imvFotoBig;

    // VARIAVEIS DO FIREBASE
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;
    private DocumentReference documentRefUsuario;
    private DocumentReference documentRefAnimal;
    private StorageReference storageRefAnimal;
    private DatabaseReference databaseRefAnimal;

    // VARIAVEIS PARA ABRIR O WHATSAPP
    String nomePacoteWhatsApp = "com.whatsapp";
    String nomeDono;
    String numeroDono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_animal);
        instanciarVariaveis();
        instanciarComponentes();
        programarComponentes();
        procurarUsuarioAtual();
        recuperarDadosUsuario();
        receberIntent();
        recuperarDadosAnimal();
    }

    private void instanciarVariaveis() {
        textoRevelado         = false;
        limiteCaracteres      = 200;

        modoAdicionar         = false;
        fotoAnimalSelecionada = -1;
        fotosAnimal           = new int[5];
        uriFotosAnimal        = new String[5];
        statusFotos           = new int[5];
        refFotosAnimal        = new String[5];
    }

    private void instanciarComponentes() {
        botaoMenu              = findViewById(R.id.botaoMenu);

        drlPagina              = findViewById(R.id.drlPagina);

        nvvMenu                = findViewById(R.id.nvvMenu);
        headerView             = nvvMenu.getHeaderView(0);

        txvMenuNomeUsuario     = headerView.findViewById(R.id.txvMenuNomeUsuario);
        imvMenuFotoUsuario     = headerView.findViewById(R.id.imvMenuFotoUsuario);
        menu                   = nvvMenu.getMenu();
        mnuInicial             = menu.findItem(R.id.menu_paginaInicial);
        mnuPerfil              = menu.findItem(R.id.menu_perfil);
        mnuAnimais             = menu.findItem(R.id.menu_meusAnimais);
        mnuSair                = menu.findItem(R.id.menu_sair);

        fabVoltar              = findViewById(R.id.fabVoltar);

        scvTela                = findViewById(R.id.scvTela);

        llyNomeAnimal          = findViewById(R.id.llyNomeAnimal);
        imvFotoAnimal          = findViewById(R.id.imvFotoAnimal);
        txvNomeAnimal          = findViewById(R.id.txvNomeAnimal);
        txvRacaAnimal          = findViewById(R.id.txvRacaAnimal);
        txvIdadeAnimal         = findViewById(R.id.txvIdadeAnimal);
        txvLocalAnimal         = findViewById(R.id.txvLocalAnimal);

        llyDescricaoNormal     = findViewById(R.id.llyDescricaoNormal);
        txvDescricao           = findViewById(R.id.txvDescricao);
        txvLerMais             = findViewById(R.id.txvLerMais);

        llyDonoAnimal          = findViewById(R.id.llyDonoAnimal);
        imvFotoDonoAnimal      = findViewById(R.id.imvFotoDonoAnimal);
        txvNomeDono            = findViewById(R.id.txvNomeDono);
        txvLocalDono           = findViewById(R.id.txvLocalDono);
        llyWhatsappDono        = findViewById(R.id.llyWhatsappDono);

        llyVacinasNormal       = findViewById(R.id.llyVacinasNormal);
        rcvVacinas             = findViewById(R.id.rcvVacinas);
        txvVacinaAnimalVazio   = findViewById(R.id.txvVacinaAnimalVazio);

        hsvImagensAnimal       = findViewById(R.id.hsvImagensAnimal);
        imvFotoAnimal1         = findViewById(R.id.imvFotoAnimal1);
        imvFotoAnimal2         = findViewById(R.id.imvFotoAnimal2);
        imvFotoAnimal3         = findViewById(R.id.imvFotoAnimal3);
        imvFotoAnimal4         = findViewById(R.id.imvFotoAnimal4);
        imvFotoAnimal5         = findViewById(R.id.imvFotoAnimal5);

        llyPerfilAnimalOpcoes1 = findViewById(R.id.llyPerfilAnimalOpcoes1);
        llyPerfilAnimalOpcoes2 = findViewById(R.id.llyPerfilAnimalOpcoes2);
        llyOpcoesVazio         = findViewById(R.id.llyOpcoesVazio);

        btnEditar              = findViewById(R.id.btnEditar);
        btnExcluir             = findViewById(R.id.btnExcluir);
        btnSalvar              = findViewById(R.id.btnSalvar);
        btnCancelar            = findViewById(R.id.btnCancelar);

        llyNomeAnimalEdit      = findViewById(R.id.llyNomeAnimalEdit);
        llyDescricaoEdit       = findViewById(R.id.llyDescricaoEdit);
        llyVacinasEdit         = findViewById(R.id.llyVacinasEdit);
        edtNomeAnimal          = findViewById(R.id.edtNomeAnimal);
        edtIdadeAnimal         = findViewById(R.id.edtIdadeAnimal);
        txvRacaAnimalEdit      = findViewById(R.id.txvRacaAnimalEdit);
        txvLocalAnimalEdit     = findViewById(R.id.txvLocalAnimalEdit);
        edtDescricao           = findViewById(R.id.edtDescricao);
        rcvVacinasEdit         = findViewById(R.id.rcvVacinasEdit);
        llyAdicionarFoto       = findViewById(R.id.llyAdicionarFoto);
        btnAdicionarFotoAnimal = findViewById(R.id.btnAdicionarFotoAnimal);
        hsvImagensAnimalEdit   = findViewById(R.id.hsvImagensAnimalEdit);
        imvFotoAnimal1Edit     = findViewById(R.id.imvFotoAnimal1Edit);
        imvFotoAnimal2Edit     = findViewById(R.id.imvFotoAnimal2Edit);
        imvFotoAnimal3Edit     = findViewById(R.id.imvFotoAnimal3Edit);
        imvFotoAnimal4Edit     = findViewById(R.id.imvFotoAnimal4Edit);
        imvFotoAnimal5Edit     = findViewById(R.id.imvFotoAnimal5Edit);

        clyTelaCarregando      = findViewById(R.id.clyTelaCarregando);

        clyFoto                = findViewById(R.id.clyFoto);
        btnSairFoto            = findViewById(R.id.btnSairFoto);
        btnImportarFoto        = findViewById(R.id.btnImportarFoto);
        imvFotoBig             = findViewById(R.id.imvFotoBig);
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
                Intent telaInicial = new Intent(PerfilAnimal.this, TelaInicial.class);
                startActivity(telaInicial);
                return true;
            }
        });

        mnuPerfil.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                Intent perfilUsuario = new Intent(PerfilAnimal.this, PerfilUsuario.class);
                startActivity(perfilUsuario);
                return true;
            }
        });

        mnuAnimais.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                Intent meusAnimais = new Intent(PerfilAnimal.this, ListaMeusAnimais.class);
                startActivity(meusAnimais);
                return true;
            }
        });

        mnuSair.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PerfilAnimal.this);
                builder.setMessage("Deseja realmente encerrar a sessão?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                drlPagina.closeDrawer(GravityCompat.START);
                                FirebaseAuth.getInstance().signOut();
                                Intent enviarFeedback = new Intent(PerfilAnimal.this, FormLogin.class);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(PerfilAnimal.this);
                builder.setIcon(R.drawable.ic_aviso);
                builder.setTitle("Aviso");
                builder.setMessage("\nDeseja realmente excluir?\nEssa operação NÃO poderá ser desfeita.")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                atualizarNumeroAnimais();
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
                recuperarDadosAnimal();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // VERIFICA SE ALGUM CAMPO ESTA VAZIO
                if (edtNomeAnimal.getText().toString().equals("") ||
                    edtIdadeAnimal.getText().toString().equals("")) {
                    Toast.makeText(PerfilAnimal.this, R.string.exception_emptyInputs, Toast.LENGTH_SHORT).show();
                } else {
                    ativarProgresso();
                    desativarModoEdicao();
                    salvarFotosAnimal(0);
                }
            }
        });

        btnAdicionarFotoAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                clyFoto.setVisibility(View.VISIBLE);
                modoAdicionar = true;
                sobrescreverPrevia();
            }
        });

        imvFotoAnimal1Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                clyFoto.setVisibility(View.VISIBLE);
                fotoAnimalSelecionada = 0;
                sobrescreverPrevia();
            }
        });
        imvFotoAnimal2Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                clyFoto.setVisibility(View.VISIBLE);
                fotoAnimalSelecionada = 1;
                sobrescreverPrevia();
            }
        });
        imvFotoAnimal3Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                clyFoto.setVisibility(View.VISIBLE);
                fotoAnimalSelecionada = 2;
                sobrescreverPrevia();
            }
        });
        imvFotoAnimal4Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                clyFoto.setVisibility(View.VISIBLE);
                fotoAnimalSelecionada = 3;
                sobrescreverPrevia();
            }
        });
        imvFotoAnimal5Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                clyFoto.setVisibility(View.VISIBLE);
                fotoAnimalSelecionada = 4;
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

    private void salvarFotosAnimal(int index) {
        if (index < 5) {
            // VERIFICA SE A URI NAO ESTA VAZIA
            if (statusFotos[index] == 2) {
                travarProgresso = true;
                if (refFotosAnimal[index] != null && !refFotosAnimal[index].isEmpty()) {
                    // EXCLUI A FOTO ANTIGA
                    StorageReference imageRef = storageRefAnimal.child(refFotosAnimal[index]);
                    imageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String fotoField = "foto" + (index + 1);
                            databaseRefAnimal.child(IDanimal).child(fotoField).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    inserirFotoAnimal(index);
                                }
                            });
                        }
                    });
                } else {
                    inserirFotoAnimal(index);
                }
            } else {
                System.out.println("ERRO AO SALVAR FOTOS!");
            }

            salvarFotosAnimal(index + 1);
        } else {
            atualizarDados();
        }
    }

    private void inserirFotoAnimal(int index) {
        // CONVERTE AS STRINGS DA URI DAS FOTOS SALVAS, DE VOLTA PARA URI
        uriArquivo = Uri.parse(uriFotosAnimal[index]);

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
        String nomeArquivo = IDanimal + numFoto + "." + getFileExtension(uriArquivo);
        StorageReference fileReference = storageRefAnimal.child(nomeArquivo);

        switch (index) {
            case 0:
                documentRefAnimal.update("refFoto1", nomeArquivo);
                break;
            case 1:
                documentRefAnimal.update("refFoto2", nomeArquivo);
                break;
            case 2:
                documentRefAnimal.update("refFoto3", nomeArquivo);
                break;
            case 3:
                documentRefAnimal.update("refFoto4", nomeArquivo);
                break;
            case 4:
                documentRefAnimal.update("refFoto5", nomeArquivo);
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
                        databaseRefAnimal.child(IDanimal).child(fotoField).setValue(uri.toString());
                        documentRefAnimal.update(fotoField, uri.toString());

                        atualizouFoto = true;
                        statusFotos[index] = 1;

                        salvarFotosAnimal(index + 1);
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
        documentRefAnimal.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!edtNomeAnimal.getText().toString().equals("") && !edtNomeAnimal.getText().toString().equals(documentSnapshot.getString("nome"))) {
                    ativarProgresso();
                    travarProgresso = true;
                    documentRefAnimal.update("nome", edtNomeAnimal.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            travarProgresso = false;
                            desativarProgresso();
                            feedbackAtualizou += 1;
                            mandarFeedbackAtualizou();
                        }
                    });
                }
                if (!edtIdadeAnimal.getText().toString().equals("") && !edtIdadeAnimal.getText().toString().equals(documentSnapshot.getString("idade"))) {
                    ativarProgresso();
                    travarProgresso = true;
                    documentRefAnimal.update("idade", edtIdadeAnimal.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    documentRefAnimal.update("descricao", edtDescricao.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            travarProgresso = false;
                            desativarProgresso();
                            feedbackAtualizou += 1;
                            mandarFeedbackAtualizou();
                        }
                    });
                }
                String codVacina = obterCodigoVacinasSelecionadas();
                if (!codVacina.equals(documentSnapshot.getString("vacina"))) {
                    ativarProgresso();
                    travarProgresso = true;
                    documentRefAnimal.update("vacina", codVacina).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    private String obterCodigoVacinasSelecionadas() {
        String codigoVacinas = "";
        for (ItemVacinaCadastro vacinasSelecionadas : itemVacinaEdit) {
            if (vacinasSelecionadas.isSelected()) {
                codigoVacinas += "1";
            } else {
                codigoVacinas += "0";
            }
        }
        return codigoVacinas;
    }

    private void mandarFeedbackAtualizou() {
        if (feedbackAtualizou == 1) {
            Toast.makeText(PerfilAnimal.this, R.string.message_updateSuccess, Toast.LENGTH_SHORT).show();
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
        edtNomeAnimal.setEnabled(marca);
        edtIdadeAnimal.setEnabled(marca);
        edtDescricao.setEnabled(marca);
        btnAdicionarFotoAnimal.setEnabled(marca);

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
        Toast.makeText(PerfilAnimal.this, exception, Toast.LENGTH_SHORT).show();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void receberIntent() {
        Intent receberIntent = getIntent();
        IDanimal = receberIntent.getStringExtra("IDanimal");
    }

    private void recuperarDadosAnimal() {
        /* -- COMANDO SQL EQUIVALENTE -
            SELECT    ?    FROM    Animais;
            ? = IDanimal;
         */
        documentRefAnimal = db.collection("Animais").document(IDanimal);
        storageRefAnimal = FirebaseStorage.getInstance().getReference("uploads");
        databaseRefAnimal = FirebaseDatabase.getInstance().getReference("uploads");

        documentRefAnimal.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    txvNomeAnimal.setText(documentSnapshot.getString("nome"));
                    txvIdadeAnimal.setText(documentSnapshot.getString("idade"));
                    txvRacaAnimal.setText(documentSnapshot.getString("raca"));

                    nomeAnimal        = documentSnapshot.getString("nome");

                    String descricao  = documentSnapshot.getString("descricao");
                    txvDescricao.setText(descricao);

                    String tipo       = documentSnapshot.getString("tipo");
                    String vacina     = documentSnapshot.getString("vacina");

                    String foto1      = documentSnapshot.getString("foto1");
                    String foto2      = documentSnapshot.getString("foto2");
                    String foto3      = documentSnapshot.getString("foto3");
                    String foto4      = documentSnapshot.getString("foto4");
                    String foto5      = documentSnapshot.getString("foto5");
                    refFotosAnimal[0] = documentSnapshot.getString("refFoto1");
                    refFotosAnimal[1] = documentSnapshot.getString("refFoto2");
                    refFotosAnimal[2] = documentSnapshot.getString("refFoto3");
                    refFotosAnimal[3] = documentSnapshot.getString("refFoto4");
                    refFotosAnimal[4] = documentSnapshot.getString("refFoto5");

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

                    setVacinasDisponiveis(tipo, vacina);
                    recuperarFotosAnimal(foto1, foto2, foto3, foto4, foto5, fotoNaoVazia);
                    limitarDescricaoGrande(descricao);
                    recuperarDadosDonoAnimal(IDdono.equals(usuarioID));
                }
            }
        });
    }

    private void setVacinasDisponiveis(String tipo, String vacina) {
        switch (tipo){
            case "Cachorro":
                vacinasDisponiveis = getResources().getStringArray(R.array.animal_vacinesDog);
                break;
            case "Gato":
                vacinasDisponiveis = getResources().getStringArray(R.array.animal_vacinesCat);
                break;
            case "Pássaro":
                vacinasDisponiveis = getResources().getStringArray(R.array.animal_vacinesBird);
                break;
            case "Rato":
                vacinasDisponiveis = getResources().getStringArray(R.array.animal_vacinesRat);
                break;
            case "Outro":
                vacinasDisponiveis = getResources().getStringArray(R.array.animal_vacinesOther);
                break;
            default:
                break;
        }
        montarListaVacinasNormal(vacina);
        montarListaVacinasEdit(vacina);
    }

    private void montarListaVacinasNormal(String vacina) {
        boolean existeVacina = false;
        itemVacinaNormal = new ArrayList<>();
        for (int i = 0; i < vacina.length(); i++) {
            if (vacina.charAt(i) == '1') {
                itemVacinaNormal.add(new ItemVacinaPerfil(vacinasDisponiveis[i]));
                existeVacina = true;
            }
        }
        if (existeVacina) {
            txvVacinaAnimalVazio.setVisibility(View.GONE);
            rcvVacinas.setVisibility(View.VISIBLE);
            adapterVacinaNormal = new AdapterVacinaPerfil(itemVacinaNormal);
            rcvVacinas.setLayoutManager(new LinearLayoutManager(this));
            rcvVacinas.setAdapter(adapterVacinaNormal);
        } else {
            txvVacinaAnimalVazio.setVisibility(View.VISIBLE);
            rcvVacinas.setVisibility(View.GONE);
        }
    }

    private void montarListaVacinasEdit(String vacina) {
        itemVacinaEdit = new ArrayList<>();
        for (String nomeVacinas : vacinasDisponiveis) {
            itemVacinaEdit.add(new ItemVacinaCadastro(nomeVacinas));
        }
        adapterVacinaEdit = new AdapterVacinaCadastro(itemVacinaEdit);
        adapterVacinaEdit.setVacinasTomadas(vacina);
        rcvVacinasEdit.setLayoutManager(new LinearLayoutManager(this));
        rcvVacinasEdit.setAdapter(adapterVacinaEdit);
    }

    private void carregarFotos(ImageView imageView, String fotoUrl, ImageView imageViewEdit, int index) {
        if (fotoUrl != null && !fotoUrl.isEmpty()) {
            imageView.setBackgroundColor(getResources().getColor(R.color.transparent));
            Picasso.get().load(fotoUrl).into(imageView);
            imageViewEdit.setBackgroundColor(getResources().getColor(R.color.transparent));
            Picasso.get().load(fotoUrl).into(imageViewEdit);
            fotosAnimal[index] = 1;
        } else {
            imageView.setImageResource(R.color.text_300);
            imageView.setBackgroundColor(getResources().getColor(R.color.transparent));
            imageViewEdit.setImageResource(R.color.text_300);
            imageViewEdit.setBackgroundColor(getResources().getColor(R.color.transparent));
            fotosAnimal[index] = 0;
        }
    }

    private void recuperarFotosAnimal(String foto1, String foto2, String foto3, String foto4, String foto5, String fotoNaoVazia) {
        if (!excluindo) {
            carregarFotos(imvFotoAnimal1, foto1, imvFotoAnimal1Edit, 0);
            carregarFotos(imvFotoAnimal2, foto2, imvFotoAnimal2Edit, 1);
            carregarFotos(imvFotoAnimal3, foto3, imvFotoAnimal3Edit, 2);
            carregarFotos(imvFotoAnimal4, foto4, imvFotoAnimal4Edit, 3);
            carregarFotos(imvFotoAnimal5, foto5, imvFotoAnimal5Edit, 4);
            if (fotoNaoVazia != null) {
                imvFotoAnimal.setBackgroundColor(getResources().getColor(R.color.transparent));
                Picasso.get().load(fotoNaoVazia).into(imvFotoAnimal);
            }
        }
    }

    private void recuperarDadosDonoAnimal(boolean usuarioLogado) {
        if (usuarioLogado) {
            documentRefUsuario.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot != null) {
                        txvNomeDono.setText(documentSnapshot.getString("nome"));
                        txvLocalDono.setText(documentSnapshot.getString("rua"));
                        txvLocalAnimal.setText(
                                documentSnapshot.getString("cidade") +
                                " - " +
                                documentSnapshot.getString("estado"));
                        String foto = documentSnapshot.getString("foto");
                        if (foto != null && !foto.isEmpty()) {
                            imvFotoDonoAnimal.setBackgroundColor(getResources().getColor(R.color.transparent));
                            Picasso.get().load(foto).into(imvFotoDonoAnimal);
                        }

                        llyWhatsappDono.setVisibility(View.GONE);
                        llyOpcoesVazio.setVisibility(View.GONE);
                        llyPerfilAnimalOpcoes1.setVisibility(View.VISIBLE);
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
                        txvLocalAnimal.setText(
                                documentSnapshot.getString("cidade") +
                                " - " +
                                documentSnapshot.getString("estado"));
                        String foto = documentSnapshot.getString("foto");
                        if (foto != null && !foto.isEmpty()) {
                            imvFotoDonoAnimal.setBackgroundColor(getResources().getColor(R.color.transparent));
                            Picasso.get().load(foto).into(imvFotoDonoAnimal);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PerfilAnimal.this);
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
                            + getResources().getString(R.string.text_whatsappMessageStartAnimal)
                            + " "
                            + nomeAnimal
                            + " "
                            + getResources().getString(R.string.text_whatsappMessageBody)
                            + " "
                            + getResources().getString(R.string.text_whatsappMessageEndAnimal));
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
        llyNomeAnimal.setVisibility(View.GONE);
        llyNomeAnimalEdit.setVisibility(View.VISIBLE);
        llyDescricaoNormal.setVisibility(View.GONE);
        llyDescricaoEdit.setVisibility(View.VISIBLE);
        llyVacinasNormal.setVisibility(View.GONE);
        llyVacinasEdit.setVisibility(View.VISIBLE);
        llyPerfilAnimalOpcoes1.setVisibility(View.GONE);
        llyPerfilAnimalOpcoes2.setVisibility(View.VISIBLE);
        llyAdicionarFoto.setVisibility(View.VISIBLE);
        hsvImagensAnimal.setVisibility(View.INVISIBLE);
        hsvImagensAnimal.setEnabled(false);
        hsvImagensAnimalEdit.setVisibility(View.VISIBLE);

        edtNomeAnimal.setText(txvNomeAnimal.getText());
        edtIdadeAnimal.setText(txvIdadeAnimal.getText());
        txvRacaAnimalEdit.setText(txvRacaAnimal.getText());
        txvLocalAnimalEdit.setText(txvLocalAnimal.getText());
        edtDescricao.setText(descricaoCompleta);
        llyDonoAnimal.setForeground(getResources().getDrawable(R.drawable.color_brighten_1));
    }

    private void desativarModoEdicao() {
        modoEdicao = false;
        feedbackAtualizou = 0;
        llyNomeAnimal.setVisibility(View.VISIBLE);
        llyNomeAnimalEdit.setVisibility(View.GONE);
        llyDescricaoNormal.setVisibility(View.VISIBLE);
        llyDescricaoEdit.setVisibility(View.GONE);
        llyVacinasNormal.setVisibility(View.VISIBLE);
        llyVacinasEdit.setVisibility(View.GONE);
        llyPerfilAnimalOpcoes1.setVisibility(View.VISIBLE);
        llyPerfilAnimalOpcoes2.setVisibility(View.GONE);
        llyAdicionarFoto.setVisibility(View.GONE);
        hsvImagensAnimal.setVisibility(View.VISIBLE);
        hsvImagensAnimal.setEnabled(true);
        hsvImagensAnimalEdit.setVisibility(View.GONE);
        llyDonoAnimal.setForeground(getResources().getDrawable(R.drawable.color_transparent));
    }

    private int checarFotosVazias(){
        int fotoVazia = -1;
        for (int i = 0; i < fotosAnimal.length; i++) {
            if (fotosAnimal[i] == 0) {
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
                Picasso.get().load(uriFoto).into(imvFotoAnimal1Edit);
                uriFotosAnimal[0] = uriFoto.toString();
                statusFotos[0] = 2;
                break;
            case 1:
                Picasso.get().load(uriFoto).into(imvFotoAnimal2Edit);
                uriFotosAnimal[1] = uriFoto.toString();
                statusFotos[1] = 2;
                break;
            case 2:
                Picasso.get().load(uriFoto).into(imvFotoAnimal3Edit);
                uriFotosAnimal[2] = uriFoto.toString();
                statusFotos[2] = 2;
                break;
            case 3:
                Picasso.get().load(uriFoto).into(imvFotoAnimal4Edit);
                uriFotosAnimal[3] = uriFoto.toString();
                statusFotos[3] = 2;
                break;
            case 4:
                Picasso.get().load(uriFoto).into(imvFotoAnimal5Edit);
                uriFotosAnimal[4] = uriFoto.toString();
                statusFotos[4] = 2;
                break;
            default:
                System.out.println("ERRO AO SELECIONAR FOTO!");
                break;
        }
        if (fotoVazia > -1 && fotoVazia < 5) {
            fotosAnimal[fotoVazia] = 1;
        }
    }

    private void sobrescreverPrevia() {
        if (modoAdicionar) {
            imvFotoBig.setImageResource(R.color.text_300);
            imvFotoBig.setBackgroundColor(getResources().getColor(R.color.transparent));
        } else {
            switch (fotoAnimalSelecionada) {
                case 0:
                    if (imvFotoAnimal1Edit.getDrawable() != null) {
                        imvFotoBig.setImageResource(R.color.transparent);
                        imvFotoBig.setBackground(imvFotoAnimal1Edit.getDrawable());
                    }
                    break;
                case 1:
                    if (imvFotoAnimal2Edit.getDrawable() != null) {
                        imvFotoBig.setImageResource(R.color.transparent);
                        imvFotoBig.setBackground(imvFotoAnimal2Edit.getDrawable());
                    }
                    break;
                case 2:
                    if (imvFotoAnimal3Edit.getDrawable() != null) {
                        imvFotoBig.setImageResource(R.color.transparent);
                        imvFotoBig.setBackground(imvFotoAnimal3Edit.getDrawable());
                    }
                    break;
                case 3:
                    if (imvFotoAnimal4Edit.getDrawable() != null) {
                        imvFotoBig.setImageResource(R.color.transparent);
                        imvFotoBig.setBackground(imvFotoAnimal4Edit.getDrawable());
                    }
                    break;
                case 4:
                    if (imvFotoAnimal5Edit.getDrawable() != null) {
                        imvFotoBig.setImageResource(R.color.transparent);
                        imvFotoBig.setBackground(imvFotoAnimal5Edit.getDrawable());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void atualizarNumeroAnimais() {
        ativarProgresso();
        documentRefUsuario.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null) {
                    int numAnimaisInt = Integer.parseInt(documentSnapshot.getString("numAnimais"));
                    documentRefUsuario.update("numAnimais", String.valueOf(numAnimaisInt -= 1)).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                int animaisInt = Integer.parseInt(documentSnapshot.getString("animais"));
                documentReferenceEstatisticas.update("animais", String.valueOf(animaisInt -= 1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        excluirRegistroAnimal();
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

    private void excluirRegistroAnimal() {
        excluindo = true;

        documentRefAnimal.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null) {
                    for (int i = 0; i < 5; i++) {
                        if (refFotosAnimal[i] != null && !refFotosAnimal[i].isEmpty()) {
                            int index = i;
                            // EXCLUI AS REFERENCIAS DAS FOTOS
                            StorageReference imageRef = storageRefAnimal.child(refFotosAnimal[i]);
                            imageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String fotoField = "foto" + (index + 1);
                                    databaseRefAnimal.child(IDanimal).child(fotoField).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {}
                                    });
                                }
                            });
                        }
                    }
                    documentRefAnimal.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            desativarProgresso();
                            Toast.makeText(PerfilAnimal.this, R.string.message_deleteSuccess, Toast.LENGTH_SHORT).show();
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
                selecionarCampoFoto(fotoAnimalSelecionada);
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