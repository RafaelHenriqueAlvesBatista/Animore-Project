package com.example.animoreproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import com.example.animoreproject.classes.AdapterAnimalTelaInicial;
import com.example.animoreproject.classes.AdapterMeuAnimal;
import com.example.animoreproject.classes.ItemAnimalTelaInicial;
import com.example.animoreproject.classes.ItemMeuAnimal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TelaInicial extends AppCompatActivity {

    // VARIAVEIS DA ACTIVITY
    private List<ItemAnimalTelaInicial> animalInicial;
    private AdapterAnimalTelaInicial animalAdapter;
    private String[] dadosListaAnimal;

    // COMPONENTES TOOLBAR
    private ImageButton botaoMenu, botaoCompartilhar;

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
    private MenuItem mnuMensagens;
    private MenuItem mnuOpcoes;
    private MenuItem mnuSair;

    // ATRIBUTOS USUARIO
    private TextView txvNomeUsuario, atributoValorUsuario1, atributoValorUsuario2, atributoValorUsuario3;
    private ImageView imvFotoUsuario;

    // CAMPO BUSCA
    private ImageButton imbProcurarAnimais, imbProcurarMicrofone;
    private TextInputEditText edtBusca;

    // ANIMAIS EM DESTAQUE
    private RecyclerView rcvAnimaisTelaInicial;

    // CAMPOS ITENS EM ALTA
    private ConstraintLayout clyAcessorio1, clyAcessorio2, clyAcessorio3, clyAcessorio4, clyAcessorio5, clyAcessorio6, clyAcessorio7, clyAcessorio8, clyAcessorio9, clyAcessorio10;
    private TextView txvNomeAcessorio1, txvNomeAcessorio2, txvNomeAcessorio3, txvNomeAcessorio4, txvNomeAcessorio5, txvNomeAcessorio6, txvNomeAcessorio7, txvNomeAcessorio8, txvNomeAcessorio9, txvNomeAcessorio10;
    private ImageView imvAcessorio1, imvAcessorio2, imvAcessorio3, imvAcessorio4, imvAcessorio5, imvAcessorio6, imvAcessorio7, imvAcessorio8, imvAcessorio9, imvAcessorio10;

    // ATRIBUTOS ITENS EM ALTA
    private ImageButton imvBolsaAcessorio1, imvBolsaAcessorio2, imvBolsaAcessorio3, imvBolsaAcessorio4, imvBolsaAcessorio5, imvBolsaAcessorio6, imvBolsaAcessorio7, imvBolsaAcessorio8, imvBolsaAcessorio9, imvBolsaAcessorio10;
    private ImageButton imvInfoAcessorio1, imvInfoAcessorio2, imvInfoAcessorio3, imvInfoAcessorio4, imvInfoAcessorio5, imvInfoAcessorio6, imvInfoAcessorio7, imvInfoAcessorio8, imvInfoAcessorio9, imvInfoAcessorio10;

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
        for (int i = 0; i <= 4; i++) {
            depurarListaAcessorios(i);
        }
        atualizarEstatisticas();
        popularLista();
    }

    private void procurarUsuarioAtual() {
        usuarioID                     = FirebaseAuth.getInstance().getCurrentUser().getUid();

        documentReference             = db.collection("Usuarios").document(usuarioID);
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

        txvNomeUsuario            = findViewById(R.id.txvNomeUsuario);
        atributoValorUsuario1     = findViewById(R.id.atributoValorUsuario1);
        atributoValorUsuario2     = findViewById(R.id.atributoValorUsuario2);
        atributoValorUsuario3     = findViewById(R.id.atributoValorUsuario3);
        imvFotoUsuario            = findViewById(R.id.imvFotoUsuario);

        imbProcurarAnimais        = findViewById(R.id.imbProcurarAnimais);
        imbProcurarMicrofone      = findViewById(R.id.imbProcurarMicrofone);
        edtBusca                  = findViewById(R.id.edtBusca);

        rcvAnimaisTelaInicial     = findViewById(R.id.rcvAnimaisTelaInicial);

        clyAcessorio1             = findViewById(R.id.clyAcessorio1);
        txvNomeAcessorio1         = findViewById(R.id.txvNomeAcessorio1);
        imvAcessorio1             = findViewById(R.id.imvAcessorio1);
        imvBolsaAcessorio1        = findViewById(R.id.imvBolsaAcessorio1);
        imvInfoAcessorio1         = findViewById(R.id.imvInfoAcessorio1);
        clyAcessorio2             = findViewById(R.id.clyAcessorio2);
        txvNomeAcessorio2         = findViewById(R.id.txvNomeAcessorio2);
        imvAcessorio2             = findViewById(R.id.imvAcessorio2);
        imvBolsaAcessorio2        = findViewById(R.id.imvBolsaAcessorio2);
        imvInfoAcessorio2         = findViewById(R.id.imvInfoAcessorio2);
        clyAcessorio3             = findViewById(R.id.clyAcessorio3);
        txvNomeAcessorio3         = findViewById(R.id.txvNomeAcessorio3);
        imvAcessorio3             = findViewById(R.id.imvAcessorio3);
        imvBolsaAcessorio3        = findViewById(R.id.imvBolsaAcessorio3);
        imvInfoAcessorio3         = findViewById(R.id.imvInfoAcessorio3);
        clyAcessorio4             = findViewById(R.id.clyAcessorio4);
        txvNomeAcessorio4         = findViewById(R.id.txvNomeAcessorio4);
        imvAcessorio4             = findViewById(R.id.imvAcessorio4);
        imvBolsaAcessorio4        = findViewById(R.id.imvBolsaAcessorio4);
        imvInfoAcessorio4         = findViewById(R.id.imvInfoAcessorio4);
        clyAcessorio5             = findViewById(R.id.clyAcessorio5);
        txvNomeAcessorio5         = findViewById(R.id.txvNomeAcessorio5);
        imvAcessorio5             = findViewById(R.id.imvAcessorio5);
        imvBolsaAcessorio5        = findViewById(R.id.imvBolsaAcessorio5);
        imvInfoAcessorio5         = findViewById(R.id.imvInfoAcessorio5);
        clyAcessorio6             = findViewById(R.id.clyAcessorio6);
        txvNomeAcessorio6         = findViewById(R.id.txvNomeAcessorio6);
        imvAcessorio6             = findViewById(R.id.imvAcessorio6);
        imvBolsaAcessorio6        = findViewById(R.id.imvBolsaAcessorio6);
        imvInfoAcessorio6         = findViewById(R.id.imvInfoAcessorio6);
        clyAcessorio7             = findViewById(R.id.clyAcessorio7);
        txvNomeAcessorio7         = findViewById(R.id.txvNomeAcessorio7);
        imvAcessorio7             = findViewById(R.id.imvAcessorio7);
        imvBolsaAcessorio7        = findViewById(R.id.imvBolsaAcessorio7);
        imvInfoAcessorio7         = findViewById(R.id.imvInfoAcessorio7);
        clyAcessorio8             = findViewById(R.id.clyAcessorio8);
        txvNomeAcessorio8         = findViewById(R.id.txvNomeAcessorio8);
        imvAcessorio8             = findViewById(R.id.imvAcessorio8);
        imvBolsaAcessorio8        = findViewById(R.id.imvBolsaAcessorio8);
        imvInfoAcessorio8         = findViewById(R.id.imvInfoAcessorio8);
        clyAcessorio9             = findViewById(R.id.clyAcessorio9);
        txvNomeAcessorio9         = findViewById(R.id.txvNomeAcessorio9);
        imvAcessorio9             = findViewById(R.id.imvAcessorio9);
        imvBolsaAcessorio9        = findViewById(R.id.imvBolsaAcessorio9);
        imvInfoAcessorio9         = findViewById(R.id.imvInfoAcessorio9);
        clyAcessorio10            = findViewById(R.id.clyAcessorio10);
        txvNomeAcessorio10        = findViewById(R.id.txvNomeAcessorio10);
        imvAcessorio10            = findViewById(R.id.imvAcessorio10);
        imvBolsaAcessorio10       = findViewById(R.id.imvBolsaAcessorio10);
        imvInfoAcessorio10        = findViewById(R.id.imvInfoAcessorio10);

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

        mnuMensagens.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                Toast.makeText(TelaInicial.this, "Funcionalidade ainda não implementada!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        mnuOpcoes.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                drlPagina.closeDrawer(GravityCompat.START);
                Intent telaOpcoes = new Intent(TelaInicial.this, TelaOpcoes.class);
                startActivity(telaOpcoes);
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
                    atributoValorUsuario2.setText(documentSnapshot.getString("curtidas"));
                    atributoValorUsuario3.setText(documentSnapshot.getString("seguidores"));

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

    private void depurarListaAcessorios(int numListaAcessorio) {
        switch (numListaAcessorio) {
            case 1:
                clyAcessorio1.setVisibility(View.VISIBLE);
                imvAcessorio1.setBackground(AppCompatResources.getDrawable(TelaInicial.this, R.drawable.ic_launcher_background));
                txvNomeAcessorio1.setText(R.string.test_placeholderText);
                break;
            case 2:
                clyAcessorio2.setVisibility(View.VISIBLE);
                imvAcessorio2.setBackground(AppCompatResources.getDrawable(TelaInicial.this, R.drawable.ic_launcher_background));
                txvNomeAcessorio2.setText(R.string.test_placeholderText);
                break;
            case 3:
                clyAcessorio3.setVisibility(View.VISIBLE);
                imvAcessorio3.setBackground(AppCompatResources.getDrawable(TelaInicial.this, R.drawable.ic_launcher_background));
                txvNomeAcessorio3.setText(R.string.test_placeholderText);
                break;
            case 4:
                clyAcessorio4.setVisibility(View.VISIBLE);
                imvAcessorio4.setBackground(AppCompatResources.getDrawable(TelaInicial.this, R.drawable.ic_launcher_background));
                txvNomeAcessorio4.setText(R.string.test_placeholderText);
                break;
            case 5:
                clyAcessorio5.setVisibility(View.VISIBLE);
                imvAcessorio5.setBackground(AppCompatResources.getDrawable(TelaInicial.this, R.drawable.ic_launcher_background));
                txvNomeAcessorio5.setText(R.string.test_placeholderText);
                break;
            case 6:
                clyAcessorio6.setVisibility(View.VISIBLE);
                imvAcessorio6.setBackground(AppCompatResources.getDrawable(TelaInicial.this, R.drawable.ic_launcher_background));
                txvNomeAcessorio6.setText(R.string.test_placeholderText);
                break;
            case 7:
                clyAcessorio7.setVisibility(View.VISIBLE);
                imvAcessorio7.setBackground(AppCompatResources.getDrawable(TelaInicial.this, R.drawable.ic_launcher_background));
                txvNomeAcessorio7.setText(R.string.test_placeholderText);
                break;
            case 8:
                clyAcessorio8.setVisibility(View.VISIBLE);
                imvAcessorio8.setBackground(AppCompatResources.getDrawable(TelaInicial.this, R.drawable.ic_launcher_background));
                txvNomeAcessorio8.setText(R.string.test_placeholderText);
                break;
            case 9:
                clyAcessorio9.setVisibility(View.VISIBLE);
                imvAcessorio9.setBackground(AppCompatResources.getDrawable(TelaInicial.this, R.drawable.ic_launcher_background));
                txvNomeAcessorio9.setText(R.string.test_placeholderText);
                break;
            case 10:
                clyAcessorio10.setVisibility(View.VISIBLE);
                imvAcessorio10.setBackground(AppCompatResources.getDrawable(TelaInicial.this, R.drawable.ic_launcher_background));
                txvNomeAcessorio10.setText(R.string.test_placeholderText);
                break;
            default:
                System.out.println("ERRO AO DEPURAR LISTA!");
                System.out.println("NUMLISTAACESSORIO: " + numListaAcessorio);
                break;
        }
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

    private void popularLista() {
        animalInicial = new ArrayList<>();
        Map<String, List<ItemAnimalTelaInicial>> animaisPorDono = new HashMap<>();

        db.collection("Animais")
                .orderBy("likes", Query.Direction.DESCENDING)
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
                                String like          = document.getString("likes");
                                String deslike       = document.getString("deslikes");
                                ImageView imvNaoNulo = findViewById(R.id.imvFotoAnimal);

                                String[] fotos = {foto1, foto2, foto3, foto4, foto5};
                                String fotoNaoVazia = null;

                                for (String foto : fotos) {
                                    if (foto != null && !foto.isEmpty()) {
                                        fotoNaoVazia = foto;
                                        break;
                                    }
                                }

                                animaisDoDono.add(new ItemAnimalTelaInicial(animalId, nome, idade, like, deslike, fotoNaoVazia, imvNaoNulo));
                            }
                        }
                        for (List<ItemAnimalTelaInicial> animaisDoDono : animaisPorDono.values()) {
                            animalInicial.addAll(animaisDoDono);
                        }

                        montarLista();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TelaInicial.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void montarLista() {
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

    @Override
    public void onBackPressed(){
        if (drlPagina.isDrawerOpen(GravityCompat.START)) {
            drlPagina.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
        }
    }
}