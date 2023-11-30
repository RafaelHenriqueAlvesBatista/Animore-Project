package com.example.animoreproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class PerfilAnimal extends AppCompatActivity {

    // VARIAVEIS DA ACTIVITY
    private boolean textoRevelado;
    private int limiteCaracteres;
    private String descricaoCompleta, descricaoCurta;
    private String IDdono;
    private boolean modoEdicao = false;
    private String nomeAnimal;

    // COMPONENTES TOOLBAR
    private ImageButton botaoMenu;

    // COMPONENTES TELA
    private DrawerLayout drlPagina;
    private View headerView;
    private ScrollView scvTela;
    private FloatingActionButton fabVoltar;

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
    private LinearLayout llyVacinasNormal, llyVacinaAnimal1, llyVacinaAnimal2, llyVacinaAnimal3, llyVacinaAnimal4;
    private TextView txvVacinaAnimalVazio;

    // FOTOS ANIMAL
    private ImageView imvFotoAnimal1, imvFotoAnimal2, imvFotoAnimal3, imvFotoAnimal4, imvFotoAnimal5;

    // OPCOES ANIMAL
    private LinearLayout llyPerfilAnimalOpcoes1, llyPerfilAnimalOpcoes2, llyOpcoesVazio;
    private Button btnEditar, btnExcluir, btnSalvar, btnCancelar;

    // COMPONENTES EDICAO
    private LinearLayout llyNomeAnimalEdit, llyDescricaoEdit, llyVacinasEdit;
    private TextInputEditText edtNomeAnimal, edtIdadeAnimal;
    private TextView txvRacaAnimalEdit, txvLocalAnimalEdit;
    private EditText edtDescricao;
    private RecyclerView rcvVacinaAnimal;

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
        setContentView(R.layout.activity_perfil_animal);
        instanciarVariaveis();
        instanciarComponentes();
        programarComponentes();
        procurarUsuarioAtual();
        recuperarDadosUsuario();
        recuperarDadosAnimal(receberIntent());
    }

    private void instanciarVariaveis() {
        textoRevelado          = false;
        limiteCaracteres       = 200;
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
        llyVacinaAnimal1       = findViewById(R.id.llyVacinaAnimal1);
        llyVacinaAnimal2       = findViewById(R.id.llyVacinaAnimal2);
        llyVacinaAnimal3       = findViewById(R.id.llyVacinaAnimal3);
        llyVacinaAnimal4       = findViewById(R.id.llyVacinaAnimal4);
        txvVacinaAnimalVazio   = findViewById(R.id.txvVacinaAnimalVazio);

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
        rcvVacinaAnimal        = findViewById(R.id.rcvVacinaAnimal);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(PerfilAnimal.this);
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
        Toast.makeText(PerfilAnimal.this, exception, Toast.LENGTH_SHORT).show();
    }

    private String receberIntent() {
        Intent receberIntent = getIntent();
        return receberIntent.getStringExtra("IDanimal");
    }

    private void recuperarDadosAnimal(String IDanimal) {
        /* -- COMANDO SQL EQUIVALENTE -
            SELECT    ?    FROM    Animais;
            ? = IDanimal;
         */
        db.collection("Animais").document(IDanimal).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    txvNomeAnimal.setText(documentSnapshot.getString("nome"));
                    txvIdadeAnimal.setText(documentSnapshot.getString("idade"));
                    txvRacaAnimal.setText(documentSnapshot.getString("raca"));

                    String descricao = documentSnapshot.getString("descricao");
                    txvDescricao.setText(descricao);

                    nomeAnimal = documentSnapshot.getString("nome");

                    String vacina = documentSnapshot.getString("vacina");

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

                    verificarVacinas(vacina);
                    carregarFotosAnimal(foto1, foto2, foto3, foto4, foto5, fotoNaoVazia);
                    limitarDescricaoGrande(descricao);
                    recuperarDadosDonoAnimal(IDdono.equals(usuarioID));
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

    private void verificarVacinas(String vacina) {
        boolean existeVacina = false;
        for (int i = 0; i < vacina.length(); i++) {
            switch (i) {
                case 0:
                    if (vacina.charAt(i) == '1') {
                        llyVacinaAnimal1.setVisibility(View.VISIBLE);
                        existeVacina = true;
                    }
                    break;
                case 1:
                    if (vacina.charAt(i) == '1') {
                        llyVacinaAnimal2.setVisibility(View.VISIBLE);
                        existeVacina = true;
                    }
                    break;
                case 2:
                    if (vacina.charAt(i) == '1') {
                        llyVacinaAnimal3.setVisibility(View.VISIBLE);
                        existeVacina = true;
                    }
                    break;
                case 3:
                    if (vacina.charAt(i) == '1') {
                        llyVacinaAnimal4.setVisibility(View.VISIBLE);
                        existeVacina = true;
                    }
                    break;
                default:
                    System.out.println("FALHA NO METODO VERIFICAR VACINAS, DENTRO DO SWITCH CASE");
                    break;
            }
        }
        if (existeVacina) {
            txvVacinaAnimalVazio.setVisibility(View.GONE);
        }
    }

    private void carregarFotosAnimal(String foto1, String foto2, String foto3, String foto4, String foto5, String fotoNaoVazia) {
        if (foto1 != null && !foto1.isEmpty()){
            imvFotoAnimal1.setBackgroundColor(getResources().getColor(R.color.transparent));
            Picasso.get().load(foto1).into(imvFotoAnimal1);
        }
        if (foto2 != null && !foto2.isEmpty()){
            imvFotoAnimal2.setBackgroundColor(getResources().getColor(R.color.transparent));
            Picasso.get().load(foto2).into(imvFotoAnimal2);
        }
        if (foto3 != null && !foto3.isEmpty()){
            imvFotoAnimal3.setBackgroundColor(getResources().getColor(R.color.transparent));
            Picasso.get().load(foto3).into(imvFotoAnimal3);
        }
        if (foto4 != null && !foto4.isEmpty()){
            imvFotoAnimal4.setBackgroundColor(getResources().getColor(R.color.transparent));
            Picasso.get().load(foto4).into(imvFotoAnimal4);
        }
        if (foto5 != null && !foto5.isEmpty()){
            imvFotoAnimal5.setBackgroundColor(getResources().getColor(R.color.transparent));
            Picasso.get().load(foto5).into(imvFotoAnimal5);
        }
        imvFotoAnimal.setBackgroundColor(getResources().getColor(R.color.transparent));
        Picasso.get().load(fotoNaoVazia).into(imvFotoAnimal);
    }

    private void recuperarDadosDonoAnimal(boolean usuarioLogado) {
        if (usuarioLogado) {
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
        llyPerfilAnimalOpcoes1.setVisibility(View.GONE);
        llyPerfilAnimalOpcoes2.setVisibility(View.VISIBLE);
        llyVacinasNormal.setVisibility(View.GONE);
        llyVacinasEdit.setVisibility(View.VISIBLE);

        edtNomeAnimal.setText(txvNomeAnimal.getText());
        edtIdadeAnimal.setText(txvIdadeAnimal.getText());
        txvRacaAnimalEdit.setText(txvRacaAnimal.getText());
        txvLocalAnimalEdit.setText(txvLocalAnimal.getText());
        edtDescricao.setText(descricaoCompleta);
        llyDonoAnimal.setForeground(getResources().getDrawable(R.drawable.color_brighten_1));
    }

    private void desativarModoEdicao() {
        modoEdicao = false;
        llyNomeAnimal.setVisibility(View.VISIBLE);
        llyNomeAnimalEdit.setVisibility(View.GONE);
        llyDescricaoNormal.setVisibility(View.VISIBLE);
        llyDescricaoEdit.setVisibility(View.GONE);
        llyVacinasNormal.setVisibility(View.VISIBLE);
        llyVacinasEdit.setVisibility(View.GONE);
        llyPerfilAnimalOpcoes1.setVisibility(View.VISIBLE);
        llyPerfilAnimalOpcoes2.setVisibility(View.GONE);
        llyDonoAnimal.setForeground(getResources().getDrawable(R.drawable.color_transparent));
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