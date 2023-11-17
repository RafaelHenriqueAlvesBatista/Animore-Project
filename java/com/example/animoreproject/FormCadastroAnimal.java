package com.example.animoreproject;

import androidx.annotation.NonNull;
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
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animoreproject.classes.AdapterVacina;
import com.example.animoreproject.classes.CustomScrollView;
import com.example.animoreproject.classes.ItemVacina;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormCadastroAnimal extends AppCompatActivity {

    // VARIAVEIS DA ACTIVITY
    private int telaFormulario;
    private int[] fotosAnimal;
    private int[] fotosAcessorio;
    private int fotoAnimalSelecionada;
    private int fotoAcessorioSelecionada;
    private boolean modoAdicionar;
    private List<ItemVacina> itemVacina;
    private AdapterVacina adapterVacina;
    private String[] vacinasDisponiveis;
    private Uri uriFoto;
    private String[] uriFotosAnimal;
    private String[] uriFotosAcessorio;
    private String local;
    private Uri uriArquivo;
    private int totalUploads;
    private int uploadsConcluidos;

    // REALIZA A VERIFICACAO DA IMAGEM ESCOLHIDA NO DISPOSITIVO, AO INVES DE COLOCAR UM NUMERO MAGICO
    private static final int PICK_IMAGE_REQUEST = 1;

    // REALIZA A ESCOLHA DOS FORMULARIOS
    private TextView txvTituloAnimal, txvTituloAcessorio;

    // COMPONENTES DO FORMULARIO DO ANIMAL
    private LinearLayout llyFormularioAnimal;
    private Spinner spnTipoAnimal, spnRacaAnimal;
    private RecyclerView rcvVacinaAnimal;
    private TextInputEditText edtNomeAnimal, edtIdadeAnimal, edtPesoAnimal;
    private EditText edtDescricaoAnimal;
    private ImageView imvFotoAnimal1, imvFotoAnimal2, imvFotoAnimal3, imvFotoAnimal4, imvFotoAnimal5;
    private Button btnAdicionarFotoAnimal, btnRegistrarAnimal;
    private RadioGroup rdgSexoAnimal;
    private RadioButton rdbSexoAnimalMacho, rdbSexoAnimalFemea, rdbSexoAnimalDesconhecido;

    // COMPONENTES DO FORMULARIO DO ACESSORIO
    private LinearLayout llyFormularioAcessorio;
    private Spinner spnTipoAcessorio;
    private TextInputEditText edtNomeAcessorio;
    private EditText edtDescricaoAcessorio;
    private ImageView imvFotoAcessorio1, imvFotoAcessorio2, imvFotoAcessorio3, imvFotoAcessorio4, imvFotoAcessorio5;
    private Button btnAdicionarFotoAcessorio, btnRegistrarAcessorio;

    // COMPONENTES DAS FOTOS
    private ConstraintLayout clyFoto;
    private TextView txvTituloFotoAnimal;
    private ImageView imvFotoBig;
    private Button btnSairFoto, btnImportarFoto;

    // COMPONENTE DAS TELAS
    private DrawerLayout drlPagina;
    private View headerView;
    private CustomScrollView scvTela;
    private ConstraintLayout clyTelaCarregando;
    private FloatingActionButton fabVoltar;

    // COMPONENTES TOOLBAR
    private ImageButton botaoMenu, botaoCompartilhar;

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

    // VARIAVEIS DO FIREBASE
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;
    private DatabaseReference databaseRef;
    private StorageReference storageRef;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_animal);
        instanciarVariaveis();
        instanciarComponentes();
        programarComponentes();
        procurarUsuarioAtual();
        recuperarDadosUsuario();
    }

    private void instanciarVariaveis() {
        telaFormulario           = 1;

        fotosAnimal              = new int[5];
        fotosAcessorio           = new int[5];

        fotoAnimalSelecionada    = -1;
        fotoAcessorioSelecionada = -1;

        modoAdicionar            = false;

        // VALOR DEFAULT QUANDO A ACTIVITY E INICIADA
        vacinasDisponiveis       = getResources().getStringArray(R.array.animal_vacinesDog);

        uriFotosAnimal           = new String[5];
        uriFotosAcessorio        = new String[5];

        totalUploads = 0;
        uploadsConcluidos = 0;
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

        scvTela                   = findViewById(R.id.scvTela);

        fabVoltar                 = findViewById(R.id.fabVoltar);

        txvTituloAnimal           = findViewById(R.id.txvTituloAnimal);
        txvTituloAcessorio        = findViewById(R.id.txvTituloAcessorio);

        llyFormularioAnimal       = findViewById(R.id.llyFormularioAnimal);

        spnTipoAnimal             = findViewById(R.id.spnTipoAnimal);
        spnRacaAnimal             = findViewById(R.id.spnRacaAnimal);
        edtNomeAnimal             = findViewById(R.id.edtNomeAnimal);
        edtIdadeAnimal            = findViewById(R.id.edtIdadeAnimal);
        edtPesoAnimal             = findViewById(R.id.edtPesoAnimal);
        rcvVacinaAnimal           = findViewById(R.id.rcvVacinaAnimal);
        rdgSexoAnimal             = findViewById(R.id.rdgSexoAnimal);
        rdbSexoAnimalMacho        = findViewById(R.id.rdbSexoAnimalMacho);
        rdbSexoAnimalFemea        = findViewById(R.id.rdbSexoAnimalFemea);
        rdbSexoAnimalDesconhecido = findViewById(R.id.rdbSexoAnimalDesconhecido);
        edtDescricaoAnimal        = findViewById(R.id.edtDescricaoAnimal);

        btnAdicionarFotoAnimal    = findViewById(R.id.btnAdicionarFotoAnimal);
        imvFotoAnimal1            = findViewById(R.id.imvFotoAnimal1);
        imvFotoAnimal2            = findViewById(R.id.imvFotoAnimal2);
        imvFotoAnimal3            = findViewById(R.id.imvFotoAnimal3);
        imvFotoAnimal4            = findViewById(R.id.imvFotoAnimal4);
        imvFotoAnimal5            = findViewById(R.id.imvFotoAnimal5);

        btnRegistrarAnimal        = findViewById(R.id.btnRegistrarAnimal);

        llyFormularioAcessorio    = findViewById(R.id.llyFormularioAcessorio);

        spnTipoAcessorio          = findViewById(R.id.spnTipoAcessorio);
        edtNomeAcessorio          = findViewById(R.id.edtNomeAcessorio);
        edtDescricaoAcessorio     = findViewById(R.id.edtDescricaoAcessorio);

        btnAdicionarFotoAcessorio = findViewById(R.id.btnAdicionarFotoAcessorio);
        imvFotoAcessorio1         = findViewById(R.id.imvFotoAcessorio1);
        imvFotoAcessorio2         = findViewById(R.id.imvFotoAcessorio2);
        imvFotoAcessorio3         = findViewById(R.id.imvFotoAcessorio3);
        imvFotoAcessorio4         = findViewById(R.id.imvFotoAcessorio4);
        imvFotoAcessorio5         = findViewById(R.id.imvFotoAcessorio5);

        btnRegistrarAcessorio     = findViewById(R.id.btnRegistrarAcessorio);

        clyFoto                   = findViewById(R.id.clyFoto);
        txvTituloFotoAnimal       = findViewById(R.id.txvTituloFotoAnimal);
        imvFotoBig                = findViewById(R.id.imvFotoBig);
        btnSairFoto               = findViewById(R.id.btnSairFoto);
        btnImportarFoto           = findViewById(R.id.btnImportarFoto);

        clyTelaCarregando         = findViewById(R.id.clyTelaCarregando);
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
                Intent telaInicial = new Intent(FormCadastroAnimal.this, TelaInicial.class);
                startActivity(telaInicial);
                return true;
            }
        });

        mnuPerfil.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                Intent perfilUsuario = new Intent(FormCadastroAnimal.this, PerfilUsuario.class);
                startActivity(perfilUsuario);
                return true;
            }
        });

        mnuAnimais.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                Intent perfilUsuario = new Intent(FormCadastroAnimal.this, ListaMeusAnimais.class);
                startActivity(perfilUsuario);
                return true;
            }
        });

        mnuMensagens.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                Toast.makeText(FormCadastroAnimal.this, "Funcionalidade ainda não implementada!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        mnuOpcoes.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                Intent telaOpcoes = new Intent(FormCadastroAnimal.this, TelaOpcoes.class);
                startActivity(telaOpcoes);
                return true;
            }
        });

        mnuSair.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FormCadastroAnimal.this);
                builder.setMessage("Deseja realmente encerrar a sessão?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                drlPagina.closeDrawer(GravityCompat.START);
                                FirebaseAuth.getInstance().signOut();
                                Intent enviarFeedback = new Intent(FormCadastroAnimal.this, FormLogin.class);
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

        txvTituloAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (telaFormulario != 1){
                    abrirFormularioAnimal();
                }
            }
        });

        txvTituloAcessorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (telaFormulario == 1){
                    abrirFormularioAcessorio();
                }
            }
        });

        spnTipoAnimal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setAdapterRacaAnimal(i);
                setAdapterVacinaAnimal(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        rdbSexoAnimalMacho.setChecked(true);

        edtDescricaoAnimal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                scvTela.setScrollingEnabled(!hasFocus);
            }
        });

        llyFormularioAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtDescricaoAnimal.clearFocus();
            }
        });

        btnAdicionarFotoAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                txvTituloFotoAnimal.setText(R.string.title_photoAnimal);
                clyFoto.setVisibility(View.VISIBLE);
                modoAdicionar = true;
                sobrescreverPrevia();
            }
        });

        btnSairFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(true);
                clyFoto.setVisibility(View.GONE);
                modoAdicionar = false;
                imvFotoBig.setImageResource(R.color.text_300);
                imvFotoBig.setBackgroundColor(getResources().getColor(R.color.transparent));
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

        imvFotoAnimal1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                txvTituloFotoAnimal.setText(R.string.title_photoAnimal);
                clyFoto.setVisibility(View.VISIBLE);
                fotoAnimalSelecionada = 0;
                sobrescreverPrevia();
            }
        });
        imvFotoAnimal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                txvTituloFotoAnimal.setText(R.string.title_photoAnimal);
                clyFoto.setVisibility(View.VISIBLE);
                fotoAnimalSelecionada = 1;
                sobrescreverPrevia();
            }
        });
        imvFotoAnimal3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                txvTituloFotoAnimal.setText(R.string.title_photoAnimal);
                clyFoto.setVisibility(View.VISIBLE);
                fotoAnimalSelecionada = 2;
                sobrescreverPrevia();
            }
        });
        imvFotoAnimal4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                txvTituloFotoAnimal.setText(R.string.title_photoAnimal);
                clyFoto.setVisibility(View.VISIBLE);
                fotoAnimalSelecionada = 3;
                sobrescreverPrevia();
            }
        });
        imvFotoAnimal5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                txvTituloFotoAnimal.setText(R.string.title_photoAnimal);
                clyFoto.setVisibility(View.VISIBLE);
                fotoAnimalSelecionada = 4;
                sobrescreverPrevia();
            }
        });

        edtDescricaoAcessorio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                scvTela.setScrollingEnabled(!hasFocus);
            }
        });

        llyFormularioAcessorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtDescricaoAcessorio.clearFocus();
            }
        });

        btnAdicionarFotoAcessorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                txvTituloFotoAnimal.setText(R.string.title_photoAccesory);
                clyFoto.setVisibility(View.VISIBLE);
                modoAdicionar = true;
                sobrescreverPrevia();
            }
        });

        imvFotoAcessorio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                txvTituloFotoAnimal.setText(R.string.title_photoAccesory);
                clyFoto.setVisibility(View.VISIBLE);
                fotoAcessorioSelecionada = 0;
                sobrescreverPrevia();
            }
        });
        imvFotoAcessorio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                txvTituloFotoAnimal.setText(R.string.title_photoAccesory);
                clyFoto.setVisibility(View.VISIBLE);
                fotoAcessorioSelecionada = 1;
                sobrescreverPrevia();
            }
        });
        imvFotoAcessorio3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                txvTituloFotoAnimal.setText(R.string.title_photoAccesory);
                clyFoto.setVisibility(View.VISIBLE);
                fotoAcessorioSelecionada = 2;
                sobrescreverPrevia();
            }
        });
        imvFotoAcessorio4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                txvTituloFotoAnimal.setText(R.string.title_photoAccesory);
                clyFoto.setVisibility(View.VISIBLE);
                fotoAcessorioSelecionada = 3;
                sobrescreverPrevia();
            }
        });
        imvFotoAcessorio5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativacaoBotoes(false);
                txvTituloFotoAnimal.setText(R.string.title_photoAccesory);
                clyFoto.setVisibility(View.VISIBLE);
                fotoAcessorioSelecionada = 4;
                sobrescreverPrevia();
            }
        });

        btnRegistrarAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avisoRegistrar();
            }
        });

        btnRegistrarAcessorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avisoRegistrar();
            }
        });
    }

    private void procurarUsuarioAtual() {
        usuarioID                     = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseRef                   = FirebaseDatabase.getInstance().getReference("uploads");
        storageRef                    = FirebaseStorage.getInstance().getReference("uploads");
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

    private void abrirFormularioAnimal() {
        telaFormulario = 1;
        llyFormularioAnimal.setVisibility(View.VISIBLE);
        llyFormularioAcessorio.setVisibility(View.GONE);
        txvTituloAnimal.setBackgroundResource(R.drawable.layout_escolha_doacao_background_left);
        txvTituloAcessorio.setBackgroundResource(R.color.white);
        txvTituloAnimal.setTextColor(getResources().getColor(R.color.white));
        txvTituloAcessorio.setTextColor(getResources().getColor(R.color.text_700));
    }
    private void abrirFormularioAcessorio() {
        telaFormulario = 2;
        llyFormularioAnimal.setVisibility(View.GONE);
        llyFormularioAcessorio.setVisibility(View.VISIBLE);
        txvTituloAnimal.setBackgroundResource(R.color.white);
        txvTituloAcessorio.setBackgroundResource(R.drawable.layout_escolha_doacao_background_right);
        txvTituloAnimal.setTextColor(getResources().getColor(R.color.text_700));
        txvTituloAcessorio.setTextColor(getResources().getColor(R.color.white));
    }

    private void ativacaoBotoes(boolean marca) {
        txvTituloAnimal.setEnabled(marca);
        txvTituloAcessorio.setEnabled(marca);

        spnTipoAnimal.setEnabled(marca);
        spnRacaAnimal.setEnabled(marca);
        edtNomeAnimal.setEnabled(marca);
        edtIdadeAnimal.setEnabled(marca);
        edtPesoAnimal.setEnabled(marca);
        rcvVacinaAnimal.setEnabled(marca);
        rdgSexoAnimal.setEnabled(marca);
        rdbSexoAnimalMacho.setEnabled(marca);
        rdbSexoAnimalFemea.setEnabled(marca);
        rdbSexoAnimalDesconhecido.setEnabled(marca);
        edtDescricaoAnimal.setEnabled(marca);
        btnAdicionarFotoAnimal.setEnabled(marca);
        imvFotoAnimal1.setEnabled(marca);
        imvFotoAnimal2.setEnabled(marca);
        imvFotoAnimal3.setEnabled(marca);
        imvFotoAnimal4.setEnabled(marca);
        imvFotoAnimal5.setEnabled(marca);
        btnRegistrarAnimal.setEnabled(marca);

        spnTipoAcessorio.setEnabled(marca);
        edtNomeAcessorio.setEnabled(marca);
        edtDescricaoAcessorio.setEnabled(marca);
        btnAdicionarFotoAcessorio.setEnabled(marca);
        imvFotoAcessorio1.setEnabled(marca);
        imvFotoAcessorio2.setEnabled(marca);
        imvFotoAcessorio3.setEnabled(marca);
        imvFotoAcessorio4.setEnabled(marca);
        imvFotoAcessorio5.setEnabled(marca);
        btnRegistrarAcessorio.setEnabled(marca);

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

    private void setAdapterRacaAnimal(int item) {
        ArrayAdapter<String> adapterRaca;
        switch (item){
            case 0:
                adapterRaca = new ArrayAdapter<>(FormCadastroAnimal.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.animal_breedDog));
                adapterRaca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnRacaAnimal.setAdapter(adapterRaca);
                break;
            case 1:
                adapterRaca = new ArrayAdapter<>(FormCadastroAnimal.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.animal_breedCat));
                adapterRaca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnRacaAnimal.setAdapter(adapterRaca);
                break;
            case 2:
                adapterRaca = new ArrayAdapter<>(FormCadastroAnimal.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.animal_breedBird));
                adapterRaca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnRacaAnimal.setAdapter(adapterRaca);
                break;
            case 3:
                adapterRaca = new ArrayAdapter<>(FormCadastroAnimal.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.animal_breedRat));
                adapterRaca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnRacaAnimal.setAdapter(adapterRaca);
                break;
            case 4:
                adapterRaca = new ArrayAdapter<>(FormCadastroAnimal.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.animal_breedOther));
                adapterRaca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnRacaAnimal.setAdapter(adapterRaca);
                break;
            default:
                break;
        }
    }

    private void setAdapterVacinaAnimal(int item) {
        switch (item){
            case 0:
                vacinasDisponiveis = getResources().getStringArray(R.array.animal_vacinesDog);
                break;
            case 1:
                vacinasDisponiveis = getResources().getStringArray(R.array.animal_vacinesCat);
                break;
            case 2:
                vacinasDisponiveis = getResources().getStringArray(R.array.animal_vacinesBird);
                break;
            case 3:
                vacinasDisponiveis = getResources().getStringArray(R.array.animal_vacinesRat);
                break;
            case 4:
                vacinasDisponiveis = getResources().getStringArray(R.array.animal_vacinesOther);
                break;
            default:
                break;
        }
        montarListaVacinas();
    }

    private void montarListaVacinas() {
        itemVacina = new ArrayList<>();
        for (String nomeVacinas : vacinasDisponiveis) {
            itemVacina.add(new ItemVacina(nomeVacinas));
        }
        adapterVacina = new AdapterVacina(itemVacina);
        rcvVacinaAnimal.setLayoutManager(new LinearLayoutManager(this));
        rcvVacinaAnimal.setAdapter(adapterVacina);
    }

    private int checarFotosVazias(){
        int fotoVazia = -1;
        if (telaFormulario == 1){
            for (int i = 0; i < fotosAnimal.length; i++) {
                if (fotosAnimal[i] == 0) {
                    fotoVazia = i;
                    break;
                } else if (i == 4) {
                    fotoVazia = 4;
                }
            }
        } else {
            for (int i = 0; i < fotosAcessorio.length; i++) {
                if (fotosAcessorio[i] == 0) {
                    fotoVazia = i;
                    break;
                } else if (i == 4) {
                    fotoVazia = 4;
                }
            }
        }
        return fotoVazia;
    }

    private void selecionarCampoFoto (int fotoVazia){
        if (telaFormulario == 1){
            switch (fotoVazia) {
                case 0:
                    Picasso.get().load(uriFoto).into(imvFotoAnimal1);
                    uriFotosAnimal[0] = uriFoto.toString();
                    break;
                case 1:
                    Picasso.get().load(uriFoto).into(imvFotoAnimal2);
                    uriFotosAnimal[1] = uriFoto.toString();
                    break;
                case 2:
                    Picasso.get().load(uriFoto).into(imvFotoAnimal3);
                    uriFotosAnimal[2] = uriFoto.toString();
                    break;
                case 3:
                    Picasso.get().load(uriFoto).into(imvFotoAnimal4);
                    uriFotosAnimal[3] = uriFoto.toString();
                    break;
                case 4:
                    Picasso.get().load(uriFoto).into(imvFotoAnimal5);
                    uriFotosAnimal[4] = uriFoto.toString();
                    break;
                default:
                    System.out.println("ERRO AO SELECIONAR FOTO!");
                    break;
            }
            if (fotoVazia > -1 && fotoVazia < 5) {
                fotosAnimal[fotoVazia] = 1;
            }
        } else {
            switch (fotoVazia) {
                case 0:
                    Picasso.get().load(uriFoto).into(imvFotoAcessorio1);
                    uriFotosAcessorio[0] = uriFoto.toString();
                    break;
                case 1:
                    Picasso.get().load(uriFoto).into(imvFotoAcessorio2);
                    uriFotosAcessorio[1] = uriFoto.toString();
                    break;
                case 2:
                    Picasso.get().load(uriFoto).into(imvFotoAcessorio3);
                    uriFotosAcessorio[2] = uriFoto.toString();
                    break;
                case 3:
                    Picasso.get().load(uriFoto).into(imvFotoAcessorio4);
                    uriFotosAcessorio[3] = uriFoto.toString();
                    break;
                case 4:
                    Picasso.get().load(uriFoto).into(imvFotoAcessorio5);
                    uriFotosAcessorio[4] = uriFoto.toString();
                    break;
                default:
                    System.out.println("ERRO AO SELECIONAR FOTO!");
                    break;
            }
            if (fotoVazia > -1 && fotoVazia < 5) {
                fotosAcessorio[fotoVazia] = 1;
            }
        }
    }

    private void sobrescreverPrevia() {
        if (modoAdicionar) {
            imvFotoBig.setImageResource(R.color.text_300);
            imvFotoBig.setBackgroundColor(getResources().getColor(R.color.transparent));
        } else {
            if (telaFormulario == 1){
                switch (fotoAnimalSelecionada) {
                    case 0:
                        if (imvFotoAnimal1.getDrawable() != null) {
                            imvFotoBig.setImageResource(R.color.transparent);
                            imvFotoBig.setBackground(imvFotoAnimal1.getDrawable());
                        }
                        break;
                    case 1:
                        if (imvFotoAnimal2.getDrawable() != null) {
                            imvFotoBig.setImageResource(R.color.transparent);
                            imvFotoBig.setBackground(imvFotoAnimal2.getDrawable());
                        }
                        break;
                    case 2:
                        if (imvFotoAnimal3.getDrawable() != null) {
                            imvFotoBig.setImageResource(R.color.transparent);
                            imvFotoBig.setBackground(imvFotoAnimal3.getDrawable());
                        }
                        break;
                    case 3:
                        if (imvFotoAnimal4.getDrawable() != null) {
                            imvFotoBig.setImageResource(R.color.transparent);
                            imvFotoBig.setBackground(imvFotoAnimal4.getDrawable());
                        }
                        break;
                    case 4:
                        if (imvFotoAnimal5.getDrawable() != null) {
                            imvFotoBig.setImageResource(R.color.transparent);
                            imvFotoBig.setBackground(imvFotoAnimal5.getDrawable());
                        }
                        break;
                    default:
                        break;
                }
            } else {
                switch (fotoAcessorioSelecionada) {
                    case 0:
                        if (imvFotoAcessorio1.getDrawable() != null) {
                            imvFotoBig.setImageResource(R.color.transparent);
                            imvFotoBig.setBackground(imvFotoAcessorio1.getDrawable());
                        }
                        break;
                    case 1:
                        if (imvFotoAcessorio2.getDrawable() != null) {
                            imvFotoBig.setImageResource(R.color.transparent);
                            imvFotoBig.setBackground(imvFotoAcessorio2.getDrawable());
                        }
                        break;
                    case 2:
                        if (imvFotoAcessorio3.getDrawable() != null) {
                            imvFotoBig.setImageResource(R.color.transparent);
                            imvFotoBig.setBackground(imvFotoAcessorio3.getDrawable());
                        }
                        break;
                    case 3:
                        if (imvFotoAcessorio4.getDrawable() != null) {
                            imvFotoBig.setImageResource(R.color.transparent);
                            imvFotoBig.setBackground(imvFotoAcessorio4.getDrawable());
                        }
                        break;
                    case 4:
                        if (imvFotoAcessorio5.getDrawable() != null) {
                            imvFotoBig.setImageResource(R.color.transparent);
                            imvFotoBig.setBackground(imvFotoAcessorio5.getDrawable());
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void avisoRegistrar() {
        String mensagem;
        if (telaFormulario == 1){
            mensagem = "\nAo cadastrar o seu animal, o aplicativo vai disponibilizá-lo para qualquer um adotá-lo.\n";
        } else {
            mensagem = "\nAo cadastrar o acessório, o aplicativo vai disponibilizá-lo para qualquer um que deseje obtê-lo.\n";
        }
        mensagem += "Prosseguir com o cadastro?";
        AlertDialog.Builder builder = new AlertDialog.Builder(FormCadastroAnimal.this);
        builder.setIcon(R.drawable.ic_info);
        builder.setTitle("Informação");
        builder.setMessage(mensagem)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checarCamposVazios(telaFormulario);
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .create()
                .show();
    }

    private void checarCamposVazios(int tela) {
        if (tela == 1) {
            String tipo      = spnTipoAnimal.getSelectedItem().toString();
            String raca      = spnRacaAnimal.getSelectedItem().toString();

            String nome      = edtNomeAnimal.getText().toString();
            String idade     = edtIdadeAnimal.getText().toString();
            String peso      = edtPesoAnimal.getText().toString();

            String vacina    = obterCodigoVacinasSelecionadas();
            String sexo      = checarSexoSelecionado();

            String descricao = edtDescricaoAnimal.getText().toString();

            //depurarVariaveis();

            if (nome.isEmpty() || idade.isEmpty() || peso.isEmpty()) {
                Toast.makeText(FormCadastroAnimal.this, R.string.message_emptyInputs, Toast.LENGTH_SHORT).show();
            } else if (checarFotosVaziasParaCadastro()) {
                Toast.makeText(FormCadastroAnimal.this, R.string.exception_emptyPhotos, Toast.LENGTH_SHORT).show();
            } else {
                cadastrarAnimal(tipo, raca, nome, idade, peso, vacina, sexo, descricao);
            }
        }
    }

    private boolean checarFotosVaziasParaCadastro() {
        return uriFotosAnimal[0] == null && uriFotosAnimal[1] == null && uriFotosAnimal[2] == null && uriFotosAnimal[3] == null && uriFotosAnimal[4] == null;
    }

    private void depurarVariaveis() {
        String tipo      = spnTipoAnimal.getSelectedItem().toString();
        String raca      = spnRacaAnimal.getSelectedItem().toString();
        String nome      = edtNomeAnimal.getText().toString();
        String idade     = edtIdadeAnimal.getText().toString();
        String peso      = edtPesoAnimal.getText().toString();
        String vacina    = obterCodigoVacinasSelecionadas();
        String sexo      = checarSexoSelecionado();
        String descricao = edtDescricaoAnimal.getText().toString();
        String foto1     = uriFotosAnimal[0];
        String foto2     = uriFotosAnimal[1];
        String foto3     = uriFotosAnimal[2];
        String foto4     = uriFotosAnimal[3];
        String foto5     = uriFotosAnimal[4];

        System.out.println("DADOS CADASTRADOS:\n" +
                "\nTIPO:      " + tipo +
                "\nRACA:      " + raca +
                "\nNOME:      " + nome +
                "\nIDADE:     " + idade +
                "\nPESO:      " + peso +
                "\nVACINAS:   " + vacina +
                "\nSEXO:      " + sexo +
                "\nDESCRICAO: " + descricao +
                "\nFOTOS:     " + foto1 +
                "\n           " + foto2 +
                "\n           " + foto3 +
                "\n           " + foto4 +
                "\n           " + foto5);

        Toast.makeText(FormCadastroAnimal.this, "- Dados foram mostrados no terminal -", Toast.LENGTH_SHORT).show();
    }

    private String obterCodigoVacinasSelecionadas() {
        String codigoVacinas = "";
        for (ItemVacina vacinasSelecionadas : itemVacina) {
            if (vacinasSelecionadas.isSelected()) {
                codigoVacinas += "1";
            } else {
                codigoVacinas += "0";
            }
        }
        return codigoVacinas;
    }

    private String checarSexoSelecionado() {
        int idRadioButton = rdgSexoAnimal.getCheckedRadioButtonId();
        if (idRadioButton != -1) {
            RadioButton radioButtonSelecionado = findViewById(idRadioButton);
            return radioButtonSelecionado.getText().toString();
        } else {
            return null;
        }
    }

    private void cadastrarAnimal(String tipo, String raca,
                                 String nome, String idade, String peso,
                                 String vacina, String sexo,
                                 String descricao) {
        ativarProgresso();

        // ATRIBUI O LOCAL ONDE O ANIMAL MORA, COM O LOCAL DO DONO
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null) {
                    local = documentSnapshot.getString("cidade");
                    local += " - " + documentSnapshot.getString("estado");

                    // MONTA A TABELA DO ANIMAL COM OS DADOS JA PREENCHIDOS
                    Map<String,Object> animais = new HashMap<>();

                    // VALORES QUE O USUARIO ESCREVEU NO FORMULARIO, E VALORES FIXOS
                    animais.put("tipo",      tipo);
                    animais.put("raca",      raca);
                    animais.put("nome",      nome);
                    animais.put("idade",     idade);
                    animais.put("peso",      peso);
                    animais.put("vacina",    vacina);
                    animais.put("sexo",      sexo);
                    animais.put("descricao", descricao);

                    // VALORES QUE SAO ATRIBUIDOS POR PADRAO
                    animais.put("foto1",     "");
                    animais.put("foto2",     "");
                    animais.put("foto3",     "");
                    animais.put("foto4",     "");
                    animais.put("foto5",     "");
                    animais.put("refFoto1",  "");
                    animais.put("refFoto2",  "");
                    animais.put("refFoto3",  "");
                    animais.put("refFoto4",  "");
                    animais.put("refFoto5",  "");
                    animais.put("likes",     "0");
                    animais.put("deslikes",  "0");
                    animais.put("local",     local);
                    animais.put("dono",      usuarioID);

                    db.collection("Animais").add(animais).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String IDanimal = documentReference.getId();
                            DocumentReference documentReferenceAnimal = db.collection("Animais").document(IDanimal);
                            salvarFotos(IDanimal, documentReferenceAnimal, 0);
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

    private void salvarFotos(String IDanimal, DocumentReference documentReferenceAnimal, int index) {
        totalUploads = uriFotosAnimal.length;
        if (index < uriFotosAnimal.length) {
            // VERIFICA SE A URI NAO ESTA VAZIA
            if (uriFotosAnimal[index] != null && !uriFotosAnimal[index].isEmpty()) {
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
                StorageReference fileReference = storageRef.child(nomeArquivo);

                switch (index) {
                    case 0:
                        documentReferenceAnimal.update("refFoto1", nomeArquivo);
                        break;
                    case 1:
                        documentReferenceAnimal.update("refFoto2", nomeArquivo);
                        break;
                    case 2:
                        documentReferenceAnimal.update("refFoto3", nomeArquivo);
                        break;
                    case 3:
                        documentReferenceAnimal.update("refFoto4", nomeArquivo);
                        break;
                    case 4:
                        documentReferenceAnimal.update("refFoto5", nomeArquivo);
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
                                databaseRef.child(IDanimal).child(fotoField).setValue(uri.toString());
                                documentReferenceAnimal.update(fotoField, uri.toString());

                                uploadsConcluidos++;

                                if (uploadsConcluidos == totalUploads) {
                                    atualizarNumeroAnimais();
                                }

                                salvarFotos(IDanimal, documentReferenceAnimal, index + 1);
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
            } else {
                salvarFotos(IDanimal, documentReferenceAnimal, index + 1);
            }
        } else {
            atualizarNumeroAnimais();
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
        Toast.makeText(FormCadastroAnimal.this, exception, Toast.LENGTH_SHORT).show();
        desativarProgresso();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void irTelaMeusAnimais() {
        desativarProgresso();

        Intent enviarFeedback = new Intent(FormCadastroAnimal.this, ListaMeusAnimais.class);
        enviarFeedback.putExtra("enviarFeedback", 1);
        startActivity(enviarFeedback);
        finish();
    }

    private void atualizarNumeroAnimais() {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null) {
                    int numAnimaisInt = Integer.parseInt(documentSnapshot.getString("numAnimais"));
                    documentReference.update("numAnimais", String.valueOf(numAnimaisInt += 1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            incrementarEstatisticas();
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

    private void incrementarEstatisticas() {
        DocumentReference documentReferenceEstatisticas;
        documentReferenceEstatisticas = db.collection("Estatisticas").document("Estatisticas");
        documentReferenceEstatisticas.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int animaisInt = Integer.parseInt(documentSnapshot.getString("animais"));
                documentReferenceEstatisticas.update("animais", String.valueOf(animaisInt += 1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        irTelaMeusAnimais();
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
                if (telaFormulario == 1){
                    selecionarCampoFoto(fotoAnimalSelecionada);
                } else {
                    edtDescricaoAcessorio.clearFocus();
                    selecionarCampoFoto(fotoAcessorioSelecionada);
                }
            }
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