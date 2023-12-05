package com.example.animoreproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animoreproject.classes.AdapterMeuAcessorio;
import com.example.animoreproject.classes.AdapterMeuAnimal;
import com.example.animoreproject.classes.AdapterViewPager;
import com.example.animoreproject.classes.FragmentListaAnimais;
import com.example.animoreproject.classes.FragmentListaAcessorios;
import com.example.animoreproject.classes.ItemMeuAcessorio;
import com.example.animoreproject.classes.ItemMeuAnimal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListaMeusAnimais extends AppCompatActivity {

    // VARIAVEIS DA ACTIVITY
    private List<ItemMeuAnimal> meuAnimal;
    private AdapterMeuAnimal animalAdapter;
    private List<ItemMeuAcessorio> meuAcessorio;
    private AdapterMeuAcessorio acessorioAdapter;
    private int posicaoTab;


    // COMPONENTES TOOLBAR
    private ImageButton botaoMenu;

    // COMPONENTES TELA
    private DrawerLayout drlPagina;
    private View headerView;
    private FloatingActionButton fabAdicionar;

    // COMPONENTES MENU
    private NavigationView nvvMenu;
    private TextView txvMenuNomeUsuario;
    private ImageView imvMenuFotoUsuario;
    private Menu menu;
    private MenuItem mnuInicial;
    private MenuItem mnuPerfil;
    private MenuItem mnuAnimais;
    private MenuItem mnuSair;

    // COMPONENTES VIEWPAGER
    private ViewPager vprListas;
    private TabLayout tblListas;

    // ATRIBUTOS DO FIREBASE
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_animais);
        instanciarComponentes();
        programarComponentes();
        procurarUsuarioAtual();
        recuperarDadosUsuario();
        adicionarTagsFragments();
        montarPaginaListas(vprListas);
    }

    @Override
    protected void onResume() {
        super.onResume();

        popularListaAnimais();
        popularListaAcessorios();
        receberFeedback();
    }

    private void instanciarComponentes() {
        botaoMenu             = findViewById(R.id.botaoMenu);

        drlPagina             = findViewById(R.id.drlPagina);

        nvvMenu               = findViewById(R.id.nvvMenu);
        headerView            = nvvMenu.getHeaderView(0);

        txvMenuNomeUsuario    = headerView.findViewById(R.id.txvMenuNomeUsuario);
        imvMenuFotoUsuario    = headerView.findViewById(R.id.imvMenuFotoUsuario);
        menu                  = nvvMenu.getMenu();
        mnuInicial            = menu.findItem(R.id.menu_paginaInicial);
        mnuPerfil             = menu.findItem(R.id.menu_perfil);
        mnuAnimais            = menu.findItem(R.id.menu_meusAnimais);
        mnuSair               = menu.findItem(R.id.menu_sair);

        fabAdicionar          = findViewById(R.id.fabAdicionar);

        vprListas             = findViewById(R.id.vprListas);
        tblListas             = findViewById(R.id.tblListas);
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
                Intent telaInicial = new Intent(ListaMeusAnimais.this, TelaInicial.class);
                startActivity(telaInicial);
                return true;
            }
        });

        mnuPerfil.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                Intent perfilUsuario = new Intent(ListaMeusAnimais.this, PerfilUsuario.class);
                startActivity(perfilUsuario);
                return true;
            }
        });

        mnuAnimais.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        mnuSair.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListaMeusAnimais.this);
                builder.setMessage("Deseja realmente encerrar a sessão?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                drlPagina.closeDrawer(GravityCompat.START);
                                FirebaseAuth.getInstance().signOut();
                                Intent enviarFeedback = new Intent(ListaMeusAnimais.this, FormLogin.class);
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

        fabAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irTelaCadastro();
            }
        });

        tblListas.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                posicaoTab = tab.getPosition();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void popularListaAnimais() {
        meuAnimal = new ArrayList<>();
        /* -- COMANDO SQL EQUIVALENTE -
            SELECT    *    FROM    Animais    WHERE    Animais.dono = Usuarios.id;
         */
        db.collection("Animais").whereEqualTo("dono", usuarioID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String animalId      = document.getId();
                    String foto1         = document.getString("foto1");
                    String foto2         = document.getString("foto2");
                    String foto3         = document.getString("foto3");
                    String foto4         = document.getString("foto4");
                    String foto5         = document.getString("foto5");
                    String nome          = document.getString("nome");
                    String idade         = document.getString("idade");
                    String raca          = document.getString("raca");
                    ImageView imvNaoNulo = findViewById(R.id.imvMeuAnimal);

                    String[] fotos = {foto1, foto2, foto3, foto4, foto5};
                    String fotoNaoVazia = null;

                    for (String foto : fotos) {
                        if (foto != null && !foto.isEmpty()) {
                            fotoNaoVazia = foto;
                            break;
                        }
                    }

                    meuAnimal.add(new ItemMeuAnimal(animalId, fotoNaoVazia, nome, idade, raca, imvNaoNulo));
                }

                if (!meuAnimal.isEmpty()) {
                    montarListaAnimais();
                } else {
                    programarBotaoListaVazia(1);
                }
            }
        });
    }

    private void montarListaAnimais() {
        FragmentListaAnimais fragmentListaAnimais = (FragmentListaAnimais) getSupportFragmentManager().findFragmentByTag("fragmentListaAnimaisTAG");
        if (fragmentListaAnimais != null) {
            if (fragmentListaAnimais.getView() != null) {
                animalAdapter = new AdapterMeuAnimal(meuAnimal, new AdapterMeuAnimal.OnAnimalClickListener() {
                    @Override
                    public void onAnimalClick(String IDanimal) {
                        Intent abrirPerfil = new Intent(ListaMeusAnimais.this, PerfilAnimal.class);
                        abrirPerfil.putExtra("IDanimal", IDanimal);
                        startActivity(abrirPerfil);
                    }
                });

                LinearLayout llyListaVazia1     = findViewById(R.id.llyListaVazia1);
                RecyclerView rcvListaAnimais    = findViewById(R.id.rcvListaAnimais);

                rcvListaAnimais.setLayoutManager(new LinearLayoutManager(this));
                rcvListaAnimais.setAdapter(animalAdapter);
                rcvListaAnimais.setVisibility(View.VISIBLE);
                llyListaVazia1.setVisibility(View.GONE);
            } else {
                Toast.makeText(ListaMeusAnimais.this, "Falha ao encontrar a View do Fragment!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ListaMeusAnimais.this, "Tag do fragment não encontrado!", Toast.LENGTH_SHORT).show();
        }
    }

    private void popularListaAcessorios() {
        meuAcessorio = new ArrayList<>();
        /* -- COMANDO SQL EQUIVALENTE -
            SELECT    *    FROM   Acessorios   WHERE   Acessorios.dono = Usuarios.id;
         */
        db.collection("Acessorios").whereEqualTo("dono", usuarioID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String animalId      = document.getId();
                    String foto1         = document.getString("foto1");
                    String foto2         = document.getString("foto2");
                    String foto3         = document.getString("foto3");
                    String foto4         = document.getString("foto4");
                    String foto5         = document.getString("foto5");
                    String nome          = document.getString("nome");
                    String tipo          = document.getString("tipo");
                    ImageView imvNaoNulo = findViewById(R.id.imvMeuAnimal);

                    String[] fotos = {foto1, foto2, foto3, foto4, foto5};
                    String fotoNaoVazia = null;

                    for (String foto : fotos) {
                        if (foto != null && !foto.isEmpty()) {
                            fotoNaoVazia = foto;
                            break;
                        }
                    }

                    meuAcessorio.add(new ItemMeuAcessorio(animalId, fotoNaoVazia, nome, tipo, imvNaoNulo));
                }

                if (!meuAcessorio.isEmpty()) {
                    montarListaAcessorios();
                } else {
                    programarBotaoListaVazia(2);
                }
            }
        });
    }

    private void montarListaAcessorios() {
        FragmentListaAcessorios fragmentListaAcessorios = (FragmentListaAcessorios) getSupportFragmentManager().findFragmentByTag("fragmentListaAcessoriosTAG");
        if (fragmentListaAcessorios != null) {
            if (fragmentListaAcessorios.getView() != null) {
                acessorioAdapter = new AdapterMeuAcessorio(meuAcessorio, new AdapterMeuAcessorio.OnAcessorioClickListener() {
                    @Override
                    public void onAcessorioClick(String IDacessorio) {
                        Intent abrirPerfil = new Intent(ListaMeusAnimais.this, PerfilAcessorio.class);
                        abrirPerfil.putExtra("IDacessorio", IDacessorio);
                        startActivity(abrirPerfil);
                    }
                });

                LinearLayout llyListaVazia2        = findViewById(R.id.llyListaVazia2);
                RecyclerView rcvListaAcessorios    = findViewById(R.id.rcvListaAcessorios);

                rcvListaAcessorios.setLayoutManager(new LinearLayoutManager(this));
                rcvListaAcessorios.setAdapter(acessorioAdapter);
                rcvListaAcessorios.setVisibility(View.VISIBLE);
                llyListaVazia2.setVisibility(View.GONE);
            } else {
                Toast.makeText(ListaMeusAnimais.this, "Falha ao encontrar a View do Fragment!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ListaMeusAnimais.this, "Tag do fragment não encontrado!", Toast.LENGTH_SHORT).show();
        }
    }

    private void programarBotaoListaVazia(int tela) {
        if (tela == 1) {
            FragmentListaAnimais fragmentListaAnimais = (FragmentListaAnimais) getSupportFragmentManager().findFragmentByTag("fragmentListaAnimaisTAG");
            if (fragmentListaAnimais != null) {
                if (fragmentListaAnimais.getView() != null) {
                    Button btnCadastrarAnimal = findViewById(R.id.btnCadastrarAnimal);

                    LinearLayout llyListaVazia1     = findViewById(R.id.llyListaVazia1);
                    RecyclerView rcvListaAnimais    = findViewById(R.id.rcvListaAnimais);
                    rcvListaAnimais.setVisibility(View.GONE);
                    llyListaVazia1.setVisibility(View.VISIBLE);

                    btnCadastrarAnimal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            irTelaCadastro();
                        }
                    });
                } else {
                    Toast.makeText(ListaMeusAnimais.this, "Falha ao encontrar a View do Fragment!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ListaMeusAnimais.this, "Tag do fragment não encontrado!", Toast.LENGTH_SHORT).show();
            }
        } else {
            FragmentListaAcessorios fragmentListaAcessorios = (FragmentListaAcessorios) getSupportFragmentManager().findFragmentByTag("fragmentListaAcessoriosTAG");
            if (fragmentListaAcessorios != null) {
                if (fragmentListaAcessorios.getView() != null) {
                    Button btnCadastrarAcessorio = findViewById(R.id.btnCadastrarAcessorio);

                    LinearLayout llyListaVazia2        = findViewById(R.id.llyListaVazia2);
                    RecyclerView rcvListaAcessorios    = findViewById(R.id.rcvListaAcessorios);
                    rcvListaAcessorios.setVisibility(View.GONE);
                    llyListaVazia2.setVisibility(View.VISIBLE);

                    btnCadastrarAcessorio.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            irTelaCadastro();
                        }
                    });
                } else {
                    Toast.makeText(ListaMeusAnimais.this, "Falha ao encontrar a View do Fragment!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ListaMeusAnimais.this, "Tag do fragment não encontrado!", Toast.LENGTH_SHORT).show();
            }
        }
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
                Toast.makeText(ListaMeusAnimais.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ListaMeusAnimais.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void adicionarTagsFragments() {
        FragmentListaAnimais fragmentAnimais = new FragmentListaAnimais();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentListaAnimais, (Fragment) fragmentAnimais, "fragmentListaAnimaisTAG")
                .commit();

        FragmentListaAcessorios fragmentAcessorios = new FragmentListaAcessorios();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentListaAcessorios, (Fragment) fragmentAcessorios, "fragmentListaAcessoriosTAG")
                .commit();
    }

    private void montarPaginaListas(ViewPager vprListas) {
        AdapterViewPager adapterViewPager = new AdapterViewPager(getSupportFragmentManager());

        adapterViewPager.addFragment(new FragmentListaAnimais(), "Animais");
        adapterViewPager.addFragment(new FragmentListaAcessorios(), "Acessórios");

        vprListas.setAdapter(adapterViewPager);
        tblListas.setupWithViewPager(vprListas);
    }

    private void irTelaCadastro() {
        Intent cadastroAnimal = new Intent(ListaMeusAnimais.this, FormCadastroAnimal.class);
        cadastroAnimal.putExtra("telaFormulario", posicaoTab);
        startActivity(cadastroAnimal);
    }

    private void receberFeedback(){
        Intent receberFeedback = getIntent();
        if(receberFeedback.getIntExtra("enviarFeedback", 0) == 1){
            Toast.makeText(ListaMeusAnimais.this, R.string.message_registerAnimalSuccess, Toast.LENGTH_SHORT).show();
        }
        if(receberFeedback.getIntExtra("enviarFeedback", 0) == 2){
            Toast.makeText(ListaMeusAnimais.this, R.string.message_registerAcessorySuccess, Toast.LENGTH_SHORT).show();
        }
        receberFeedback.removeExtra("enviarFeedback");
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