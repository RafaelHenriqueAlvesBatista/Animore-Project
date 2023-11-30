package com.example.animoreproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animoreproject.classes.ServiceNotificacoes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class PerfilAcessorio extends AppCompatActivity {

    // VARIAVEIS DA ACTIVITY
    private boolean textoRevelado;
    private int limiteCaracteres;
    private String descricaoCompleta, descricaoCurta;
    private String IDdono;
    private boolean modoEdicao = false;
    private boolean atualizouFoto = false;
    private int feedbackAtualizou = 0;
    private boolean excluindo = false;
    private String nomeAcessorio;

    // COMPONENTES TOOLBAR
    private ImageButton botaoMenu, botaoCompartilhar;

    // COMPONENTES TELA
    private DrawerLayout drlPagina;
    private View headerView;
    private FloatingActionButton fabVoltar;

    // COMPONENTES MENU
    private NavigationView nvvMenu;
    private TextView txvMenuNomeUsuario;
    private ImageView imvMenuFotoUsuario;
    private Menu menu;
    private MenuItem mnuInicial;
    private MenuItem mnuPerfil;
    private MenuItem mnuAnimais;
    private MenuItem mnuMensagens;
    private MenuItem mnuOpcoes;
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
    private ImageView imvFotoAcessorio1, imvFotoAcessorio2, imvFotoAcessorio3, imvFotoAcessorio4, imvFotoAcessorio5;

    // OPCOES ACESSORIO
    private LinearLayout llyPerfilAcessorioOpcoes1, llyPerfilAcessorioOpcoes2, llyOpcoesVazio;
    private Button btnEditar, btnExcluir, btnSalvar, btnCancelar;

    // COMPONENTES EDICAO
    private LinearLayout llyNomeAcessorioEdit, llyDescricaoEdit;
    private TextInputEditText edtNomeAcessorio;
    private TextView txvTipoAcessorioEdit, txvLocalAcessorioEdit;
    private EditText edtDescricao;

    // VARIAVEIS DO FIREBASE
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;
    private DocumentReference documentReference;

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
        recuperarDadosAcessorio(receberIntent());
    }

    private void instanciarVariaveis() {
        textoRevelado          = false;
        limiteCaracteres       = 200;
    }

    private void instanciarComponentes() {
        botaoMenu                 = findViewById(R.id.botaoMenu);
        botaoCompartilhar         = findViewById(R.id.botaoCompartilhar);

        drlPagina                 = findViewById(R.id.drlPagina);

        nvvMenu                   = findViewById(R.id.nvvMenu);
        headerView                = nvvMenu.getHeaderView(0);

        txvMenuNomeUsuario        = headerView.findViewById(R.id.txvMenuNomeUsuario);
        imvMenuFotoUsuario        = headerView.findViewById(R.id.imvMenuFotoUsuario);
        menu                      = nvvMenu.getMenu();
        mnuInicial                = menu.findItem(R.id.menu_paginaInicial);
        mnuPerfil                 = menu.findItem(R.id.menu_perfil);
        mnuAnimais                = menu.findItem(R.id.menu_meusAnimais);
        mnuMensagens              = menu.findItem(R.id.menu_mensagens);
        mnuOpcoes                 = menu.findItem(R.id.menu_opcoes);
        mnuSair                   = menu.findItem(R.id.menu_sair);

        fabVoltar                 = findViewById(R.id.fabVoltar);

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

        mnuMensagens.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                Toast.makeText(PerfilAcessorio.this, "Funcionalidade ainda não implementada!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        mnuOpcoes.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                Intent telaOpcoes = new Intent(PerfilAcessorio.this, TelaOpcoes.class);
                startActivity(telaOpcoes);
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
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desativarModoEdicao();
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
                                // TODO
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
    }

    private void procurarUsuarioAtual() {
        usuarioID                     = FirebaseAuth.getInstance().getCurrentUser().getUid();

        documentReference             = db.collection("Usuarios").document(usuarioID);
    }

    private void recuperarDadosUsuario() {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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

    private void cancelarOperacao(String exception) {
        Toast.makeText(PerfilAcessorio.this, exception, Toast.LENGTH_SHORT).show();
    }

    private String receberIntent() {
        Intent receberIntent = getIntent();
        return receberIntent.getStringExtra("IDacessorio");
    }

    private void recuperarDadosAcessorio(String IDacessorio) {
        /* -- COMANDO SQL EQUIVALENTE -
            SELECT    ?    FROM    Acessorios;
            ? = IDacessorio;
         */
        db.collection("Acessorios").document(IDacessorio).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
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

                    String[] fotos = {foto1, foto2, foto3, foto4, foto5};
                    String fotoNaoVazia = null;

                    for (String foto : fotos) {
                        if (foto != null && !foto.isEmpty()) {
                            fotoNaoVazia = foto;
                            break;
                        }
                    }

                    IDdono     = documentSnapshot.getString("dono");

                    carregarFotosAcessorio(foto1, foto2, foto3, foto4, foto5, fotoNaoVazia);
                    limitarDescricaoGrande(descricao);
                    recuperarDadosDonoAcessorio(IDdono.equals(usuarioID));
                } else {
                    cancelarOperacao("Algo deu errado. Por favor, tente novamente.");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                cancelarOperacao(e.getMessage());
            }
        });
    }

    private void carregarFotosAcessorio(String foto1, String foto2, String foto3, String foto4, String foto5, String fotoNaoVazia) {
        if (foto1 != null && !foto1.isEmpty()){
            imvFotoAcessorio1.setBackgroundColor(getResources().getColor(R.color.transparent));
            Picasso.get().load(foto1).into(imvFotoAcessorio1);
        }
        if (foto2 != null && !foto2.isEmpty()){
            imvFotoAcessorio2.setBackgroundColor(getResources().getColor(R.color.transparent));
            Picasso.get().load(foto2).into(imvFotoAcessorio2);
        }
        if (foto3 != null && !foto3.isEmpty()){
            imvFotoAcessorio3.setBackgroundColor(getResources().getColor(R.color.transparent));
            Picasso.get().load(foto3).into(imvFotoAcessorio3);
        }
        if (foto4 != null && !foto4.isEmpty()){
            imvFotoAcessorio4.setBackgroundColor(getResources().getColor(R.color.transparent));
            Picasso.get().load(foto4).into(imvFotoAcessorio4);
        }
        if (foto5 != null && !foto5.isEmpty()){
            imvFotoAcessorio5.setBackgroundColor(getResources().getColor(R.color.transparent));
            Picasso.get().load(foto5).into(imvFotoAcessorio5);
        }
        imvFotoAcessorio.setBackgroundColor(getResources().getColor(R.color.transparent));
        Picasso.get().load(fotoNaoVazia).into(imvFotoAcessorio);
    }

    private void recuperarDadosDonoAcessorio(boolean usuarioLogado) {
        if (usuarioLogado) {
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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

        edtNomeAcessorio.setText(txvNomeAcessorio.getText());
        txvTipoAcessorioEdit.setText(txvTipoAcessorio.getText());
        txvLocalAcessorioEdit.setText(txvLocalAcessorio.getText());
        edtDescricao.setText(descricaoCompleta);
        llyDonoAcessorio.setForeground(getResources().getDrawable(R.drawable.color_brighten_1));
    }

    private void desativarModoEdicao() {
        modoEdicao = false;
        atualizouFoto = false;
        feedbackAtualizou = 0;
        llyNomeAcessorio.setVisibility(View.VISIBLE);
        llyNomeAcessorioEdit.setVisibility(View.GONE);
        llyDescricaoNormal.setVisibility(View.VISIBLE);
        llyDescricaoEdit.setVisibility(View.GONE);
        llyPerfilAcessorioOpcoes1.setVisibility(View.VISIBLE);
        llyPerfilAcessorioOpcoes2.setVisibility(View.GONE);
        llyDonoAcessorio.setForeground(getResources().getDrawable(R.drawable.color_transparent));
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