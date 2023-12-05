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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animoreproject.classes.AdapterAcessorioTelaInicial;
import com.example.animoreproject.classes.AdapterAnimalTelaInicial;
import com.example.animoreproject.classes.ItemAcessorioTelaInicial;
import com.example.animoreproject.classes.ItemAnimalTelaInicial;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelaInicial extends AppCompatActivity {

    // VARIAVEIS DA ACTIVITY
    private List<ItemAnimalTelaInicial> animalInicial;
    private AdapterAnimalTelaInicial animalAdapter;
    private List<ItemAcessorioTelaInicial> acessorioInicial;
    private AdapterAcessorioTelaInicial acessorioAdapter;

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

    // ATRIBUTOS USUARIO
    private TextView txvNomeUsuario, atributoValorUsuario1, atributoValorUsuario2;
    private ImageView imvFotoUsuario;

    // LISTA ANIMAIS E ACESSORIOS
    private RecyclerView rcvAnimaisTelaInicial, rcvAcessoriosTelaInicial;

    // ATRIBUTOS ESTATISTICAS
    private TextView txvEstatisticasUsuarios, txvEstatisticasAnimais, txvEstatisticasAcessorios, txvEstatisticasDoacoes;

    // ATRIBUTOS DO FIREBASE
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        procurarUsuarioAtual();
        instanciarComponentes();
        programarComponentes();
        recuperarDados();
    }

    @Override
    protected void onResume() {
        super.onResume();

        atualizarEstatisticas();
        popularListaAnimais();
        popularListaAcessorios();
    }

    private void procurarUsuarioAtual() {
        usuarioID                     = FirebaseAuth.getInstance().getCurrentUser().getUid();

        documentReference             = db.collection("Usuarios").document(usuarioID);
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

        txvNomeUsuario            = findViewById(R.id.txvNomeUsuario);
        atributoValorUsuario1     = findViewById(R.id.atributoValorUsuario1);
        atributoValorUsuario2     = findViewById(R.id.atributoValorUsuario2);
        imvFotoUsuario            = findViewById(R.id.imvFotoUsuario);

        rcvAnimaisTelaInicial     = findViewById(R.id.rcvAnimaisTelaInicial);

        rcvAcessoriosTelaInicial  = findViewById(R.id.rcvAcessoriosTelaInicial);

        txvEstatisticasUsuarios   = findViewById(R.id.txvEstatisticasUsuarios);
        txvEstatisticasAnimais    = findViewById(R.id.txvEstatisticasAnimais);
        txvEstatisticasAcessorios = findViewById(R.id.txvEstatisticasAcessorios);
        txvEstatisticasDoacoes    = findViewById(R.id.txvEstatisticasDoacoes);
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
                return true;
            }
        });

        mnuPerfil.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                Intent perfilUsuario = new Intent(TelaInicial.this, PerfilUsuario.class);
                startActivity(perfilUsuario);
                return true;
            }
        });

        mnuAnimais.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                Intent meusAnimais = new Intent(TelaInicial.this, ListaMeusAnimais.class);
                startActivity(meusAnimais);
                return true;
            }
        });

        mnuSair.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TelaInicial.this);
                builder.setMessage("Deseja realmente encerrar a sessão?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                drlPagina.closeDrawer(GravityCompat.START);
                                FirebaseAuth.getInstance().signOut();
                                Intent enviarFeedback = new Intent(TelaInicial.this, FormLogin.class);
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

        txvNomeUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent perfilUsuario = new Intent(TelaInicial.this, PerfilUsuario.class);
                startActivity(perfilUsuario);
            }
        });
        imvFotoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent perfilUsuario = new Intent(TelaInicial.this, PerfilUsuario.class);
                startActivity(perfilUsuario);
            }
        });
    }

    private void recuperarDados() {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null){
                    txvNomeUsuario.setText(documentSnapshot.getString("nome"));
                    atributoValorUsuario1.setText(documentSnapshot.getString("numAnimais"));
                    atributoValorUsuario2.setText(documentSnapshot.getString("numAcessorios"));

                    txvMenuNomeUsuario.setText(documentSnapshot.getString("nome"));

                    carregarFoto();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TelaInicial.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void carregarFoto() {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null) {
                    String foto = documentSnapshot.getString("foto");
                    if (foto != null && !foto.isEmpty()){
                        imvFotoUsuario.setBackgroundColor(getResources().getColor(R.color.transparent));
                        imvMenuFotoUsuario.setBackgroundColor(getResources().getColor(R.color.transparent));
                        Picasso.get().load(documentSnapshot.getString("foto")).into(imvFotoUsuario);
                        Picasso.get().load(documentSnapshot.getString("foto")).into(imvMenuFotoUsuario);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TelaInicial.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void atualizarEstatisticas() {
        DocumentReference documentReferenceEstatisticas;
        documentReferenceEstatisticas = db.collection("Estatisticas").document("Estatisticas");
        documentReferenceEstatisticas.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null) {
                    txvEstatisticasUsuarios.setText(documentSnapshot.getString("usuarios"));
                    txvEstatisticasAnimais.setText(documentSnapshot.getString("animais"));
                    txvEstatisticasAcessorios.setText(documentSnapshot.getString("acessorios"));
                    txvEstatisticasDoacoes.setText(documentSnapshot.getString("doacoes"));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TelaInicial.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void popularListaAnimais() {
        animalInicial = new ArrayList<>();
        Map<String, List<ItemAnimalTelaInicial>> animaisPorDono = new HashMap<>();

        db.collection("Animais")
                .limit(10)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String dono = document.getString("dono");
                            List<ItemAnimalTelaInicial> animaisDoDono = animaisPorDono.get(dono);

                            if (animaisDoDono == null) {
                                animaisDoDono = new ArrayList<>();
                                animaisPorDono.put(dono, animaisDoDono);
                            }

                            if (animaisDoDono.size() < 2) {
                                String animalId      = document.getId();
                                String foto1         = document.getString("foto1");
                                String foto2         = document.getString("foto2");
                                String foto3         = document.getString("foto3");
                                String foto4         = document.getString("foto4");
                                String foto5         = document.getString("foto5");
                                String nome          = document.getString("nome");
                                String idade         = document.getString("idade");
                                ImageView imvNaoNulo = findViewById(R.id.imvFotoAnimal);

                                String[] fotos = {foto1, foto2, foto3, foto4, foto5};
                                String fotoNaoVazia = null;

                                for (String foto : fotos) {
                                    if (foto != null && !foto.isEmpty()) {
                                        fotoNaoVazia = foto;
                                        break;
                                    }
                                }

                                animaisDoDono.add(new ItemAnimalTelaInicial(animalId, nome, idade, fotoNaoVazia, imvNaoNulo));
                            }
                        }
                        for (List<ItemAnimalTelaInicial> animaisDoDono : animaisPorDono.values()) {
                            animalInicial.addAll(animaisDoDono);
                        }

                        montarListaAnimais();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TelaInicial.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void montarListaAnimais() {
        animalAdapter = new AdapterAnimalTelaInicial(animalInicial, new AdapterAnimalTelaInicial.OnAnimalClickListener() {
            @Override
            public void onAnimalClick(String IDanimal) {
                Intent abrirPerfil = new Intent(TelaInicial.this, PerfilAnimal.class);
                abrirPerfil.putExtra("IDanimal", IDanimal);
                startActivity(abrirPerfil);
            }
        });
        LinearLayoutManager linearLayoutManagerHorizontal = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvAnimaisTelaInicial.setLayoutManager(linearLayoutManagerHorizontal);
        rcvAnimaisTelaInicial.setAdapter(animalAdapter);
    }

    private void popularListaAcessorios() {
        acessorioInicial = new ArrayList<>();
        Map<String, List<ItemAcessorioTelaInicial>> acessoriosPorDono = new HashMap<>();

        db.collection("Acessorios")
                .limit(10)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String dono = document.getString("dono");
                            List<ItemAcessorioTelaInicial> acessoriosDoDono = acessoriosPorDono.get(dono);

                            if (acessoriosDoDono == null) {
                                acessoriosDoDono = new ArrayList<>();
                                acessoriosPorDono.put(dono, acessoriosDoDono);
                            }

                            if (acessoriosDoDono.size() < 2) {
                                String acessorioId      = document.getId();
                                String foto1         = document.getString("foto1");
                                String foto2         = document.getString("foto2");
                                String foto3         = document.getString("foto3");
                                String foto4         = document.getString("foto4");
                                String foto5         = document.getString("foto5");
                                String nome          = document.getString("nome");
                                ImageView imvNaoNulo = findViewById(R.id.imvFotoAcessorio);

                                String[] fotos = {foto1, foto2, foto3, foto4, foto5};
                                String fotoNaoVazia = null;

                                for (String foto : fotos) {
                                    if (foto != null && !foto.isEmpty()) {
                                        fotoNaoVazia = foto;
                                        break;
                                    }
                                }

                                acessoriosDoDono.add(new ItemAcessorioTelaInicial(acessorioId, nome, fotoNaoVazia, imvNaoNulo));
                            }
                        }
                        for (List<ItemAcessorioTelaInicial> acessoriosDoDono : acessoriosPorDono.values()) {
                            acessorioInicial.addAll(acessoriosDoDono);
                        }

                        montarListaAcessorios();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TelaInicial.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void montarListaAcessorios() {
        acessorioAdapter = new AdapterAcessorioTelaInicial(acessorioInicial, new AdapterAcessorioTelaInicial.OnAcessorioClickListener() {
            @Override
            public void onAcessorioClick(String IDacessorio) {
                Intent abrirPerfil = new Intent(TelaInicial.this, PerfilAcessorio.class);
                abrirPerfil.putExtra("IDacessorio", IDacessorio);
                startActivity(abrirPerfil);
            }
        });
        LinearLayoutManager linearLayoutManagerHorizontal = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvAcessoriosTelaInicial.setLayoutManager(linearLayoutManagerHorizontal);
        rcvAcessoriosTelaInicial.setAdapter(acessorioAdapter);
    }

    @Override
    public void onBackPressed(){
        if (drlPagina.isDrawerOpen(GravityCompat.START)) {
            drlPagina.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
        }
    }
}