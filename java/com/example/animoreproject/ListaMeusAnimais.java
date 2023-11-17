package com.example.animoreproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.animoreproject.classes.AdapterMeuAnimal;
import com.example.animoreproject.classes.ItemMeuAnimal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListaMeusAnimais extends AppCompatActivity {

    // VARIAVEIS DA ACTIVITY
    private List<ItemMeuAnimal> meuAnimal;
    private AdapterMeuAnimal animalAdapter;
    private String[] dadosListaAnimal;

    // COMPONENTES TOOLBAR
    private ImageButton botaoMenu, botaoCompartilhar;

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
    private MenuItem mnuMensagens;
    private MenuItem mnuOpcoes;
    private MenuItem mnuSair;

    // COMPONENTES LISTA VAZIA
    private LinearLayout llyListaVazia;
    private Button btnCadastrarAnimal;

    // COMPONENTES LISTA ANIMAIS
    private RecyclerView rcvListaAnimais;

    // ATRIBUTOS DO FIREBASE
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;
    private DatabaseReference databaseRef;
    private StorageReference storageRef;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_animais);
        instanciarVariaveis();
        instanciarComponentes();
        programarComponentes();
        procurarUsuarioAtual();
        recuperarDadosUsuario();
    }

    @Override
    protected void onResume() {
        super.onResume();

        popularLista();
        receberFeedback();
    }

    private void instanciarVariaveis() {
        dadosListaAnimal = new String[4];
    }

    private void instanciarComponentes() {
        botaoMenu             = findViewById(R.id.botaoMenu);
        botaoCompartilhar     = findViewById(R.id.botaoCompartilhar);

        drlPagina             = findViewById(R.id.drlPagina);

        nvvMenu               = findViewById(R.id.nvvMenu);
        headerView            = nvvMenu.getHeaderView(0);

        txvMenuNomeUsuario    = headerView.findViewById(R.id.txvMenuNomeUsuario);
        imvMenuFotoUsuario    = headerView.findViewById(R.id.imvMenuFotoUsuario);
        menu                  = nvvMenu.getMenu();
        mnuInicial            = menu.findItem(R.id.menu_paginaInicial);
        mnuPerfil             = menu.findItem(R.id.menu_perfil);
        mnuAnimais            = menu.findItem(R.id.menu_meusAnimais);
        mnuMensagens          = menu.findItem(R.id.menu_mensagens);
        mnuOpcoes             = menu.findItem(R.id.menu_opcoes);
        mnuSair               = menu.findItem(R.id.menu_sair);

        fabAdicionar          = findViewById(R.id.fabAdicionar);

        llyListaVazia         = findViewById(R.id.llyListaVazia);
        btnCadastrarAnimal    = findViewById(R.id.btnCadastrarAnimal);

        rcvListaAnimais       = findViewById(R.id.rcvListaAnimais);
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

        mnuMensagens.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                Toast.makeText(ListaMeusAnimais.this, "Funcionalidade ainda não implementada!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        mnuOpcoes.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                Intent telaOpcoes = new Intent(ListaMeusAnimais.this, TelaOpcoes.class);
                startActivity(telaOpcoes);
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
                irTelaCadastroAnimal();
            }
        });
        btnCadastrarAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irTelaCadastroAnimal();
            }
        });
    }

    private void popularLista() {
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
                    montarLista();
                }
            }
        });
    }

    private void montarLista() {
        animalAdapter = new AdapterMeuAnimal(meuAnimal, new AdapterMeuAnimal.OnAnimalClickListener() {
            @Override
            public void onAnimalClick(String IDanimal) {
                Intent abrirPerfil = new Intent(ListaMeusAnimais.this, PerfilAnimal.class);
                abrirPerfil.putExtra("IDanimal", IDanimal);
                startActivity(abrirPerfil);
            }
        });
        rcvListaAnimais.setLayoutManager(new LinearLayoutManager(this));
        rcvListaAnimais.setAdapter(animalAdapter);
        rcvListaAnimais.setVisibility(View.VISIBLE);
        llyListaVazia.setVisibility(View.GONE);
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

    private void irTelaCadastroAnimal() {
        Intent cadastroAnimal = new Intent(ListaMeusAnimais.this, FormCadastroAnimal.class);
        startActivity(cadastroAnimal);
    }

    private void receberFeedback(){
        Intent receberFeedback = getIntent();
        if(receberFeedback.getIntExtra("enviarFeedback", 0) == 1){
            Toast.makeText(ListaMeusAnimais.this, R.string.message_registerSuccess, Toast.LENGTH_SHORT).show();
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