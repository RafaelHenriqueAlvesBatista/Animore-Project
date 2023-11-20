package com.example.animoreproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animoreproject.classes.ServiceNotificacoes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private int tela = 0;
    private String cargo;

    private ServiceNotificacoes serviceNotificacoes = new ServiceNotificacoes();

    String nomePacoteWhatsApp = "com.whatsapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if (usuarioAtual != null && tela != 0) {
            tela = 6;
            onResume();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // TELA ZERO - DEBUG
        if (tela == 0) {
            setContentView(R.layout.activity_debug);
            Button botao1 = findViewById(R.id.tela1);
            Button botao2 = findViewById(R.id.tela2);
            Button botao3 = findViewById(R.id.tela3);
            Button botao4 = findViewById(R.id.tela4);
            Button botao5 = findViewById(R.id.tela5);
            Button botao6 = findViewById(R.id.tela6);
            Button botao7 = findViewById(R.id.tela7);
            Button botao8 = findViewById(R.id.tela8);
            Button botao9 = findViewById(R.id.tela9);
            Button botao10 = findViewById(R.id.tela10);
            Button botao11 = findViewById(R.id.tela11);
            Button botao12 = findViewById(R.id.tela12);
            Button botao13 = findViewById(R.id.tela13);
            Button botao14 = findViewById(R.id.tela14);
            Button botao15 = findViewById(R.id.tela15);
            Button botao16 = findViewById(R.id.tela16);
            Button botao17 = findViewById(R.id.tela17);
            Button botao18 = findViewById(R.id.tela18);
            Button notificacao1 = findViewById(R.id.notificacao1);
            Button notificacao2 = findViewById(R.id.notificacao2);
            Button servico1 = findViewById(R.id.servico1);
            Button servico2 = findViewById(R.id.servico2);
            Button deslogar = findViewById(R.id.deslogar);

            botao1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tela = 1;
                    onResume();
                }
            });
            botao2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tela = 2;
                    onResume();
                }
            });
            botao3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tela = 3;
                    onResume();
                }
            });
            botao4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tela = 4;
                    onResume();
                }
            });
            botao5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tela = 5;
                    onResume();
                }
            });
            botao6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tela = 6;
                    onResume();
                }
            });
            botao7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tela = 7;
                    onResume();
                }
            });
            botao8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tela = 8;
                    onResume();
                }
            });
            botao9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tela = 9;
                    onResume();
                }
            });
            botao10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tela = 10;
                    onResume();
                }
            });
            botao11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tela = 11;
                    onResume();
                }
            });
            botao12.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tela = 12;
                    onResume();
                }
            });
            botao13.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tela = 13;
                    onResume();
                }
            });
            botao14.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tela = 14;
                    onResume();
                }
            });
            botao15.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tela = 15;
                    onResume();
                }
            });
            botao16.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tela = 16;
                    onResume();
                }
            });
            botao17.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tela = 17;
                    onResume();
                }
            });
            botao18.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tela = 18;
                    onResume();
                }
            });
            notificacao1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    serviceNotificacoes.construirNotificacaoTeste(
                            "Título da Notificação",
                            "Conteúdo da notificação.");
                    onResume();
                    Toast.makeText(MainActivity.this, "Notificação enviada com sucesso!", Toast.LENGTH_SHORT).show();
                }
            });
            notificacao2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    serviceNotificacoes.construirNotificacaoAdocao(
                            getString(R.string.notificationTitle_adoptionRequest),
                            getString(R.string.notificationBody_adoptionRequest),
                            "Fulano de Tal",
                            "Fulano de Tal",
                            "joao@gmail.com",
                            "1");
                            //"teste@gmail.com"
                    onResume();
                    Toast.makeText(MainActivity.this, "Notificação enviada com sucesso!", Toast.LENGTH_SHORT).show();
                }
            });
            servico1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent abrirWhatsApp = getPackageManager().getLaunchIntentForPackage(nomePacoteWhatsApp);
                        if (abrirWhatsApp != null) {
                            startActivity(abrirWhatsApp);
                        } else {
                            Uri linkPlayStore = Uri.parse("market://details?id=" + nomePacoteWhatsApp);
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW, linkPlayStore);
                            startActivity(marketIntent);
                        }
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }

                    onResume();
                }
            });
            servico2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // CODIGO DO PAIS + DDD + NUMERO DO CELULAR
                    String numeroTelefoneTeste = "5518991391954";

                    if (aplicativoEstaInstalado(nomePacoteWhatsApp)) {
                        Intent abrirWhatsApp = new Intent("android.intent.action.MAIN");
                        abrirWhatsApp.setComponent(new ComponentName(nomePacoteWhatsApp, "com.whatsapp.Conversation"));
                        abrirWhatsApp.putExtra("jid", PhoneNumberUtils.stripSeparators(numeroTelefoneTeste) + "@s.whatsapp.net");
                        startActivity(abrirWhatsApp);
                    } else {
                        Uri linkPlayStore = Uri.parse("market://details?id=" + nomePacoteWhatsApp);
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW, linkPlayStore);
                        startActivity(marketIntent);
                    }

                    onResume();
                }
            });
            deslogar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    onResume();
                    Toast.makeText(MainActivity.this, "Usuário deslogado com sucesso!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // PRIMEIRA TELA - BOAS VINDAS
        if (tela == 1){
            setContentView(R.layout.activity_boas_vindas);
            Button botaoComecar = findViewById(R.id.btnComecar);
            botaoComecar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tela = 3;
                    onResume();
                }
            });
        }

        // SEGUNDA TELA - ESCOLHA DO CARGO
        if (tela == 2){
            setContentView(R.layout.activity_escolha_cargo);

            TextView txvDescricao = findViewById(R.id.txvDescricao);
            Button botaoDoar = findViewById(R.id.btnDoar);
            Button botaoDoarInativo = findViewById(R.id.btnDoarInativo);
            Button botaoAdotar = findViewById(R.id.btnAdotar);
            Button botaoAdotarInativo = findViewById(R.id.btnAdotarInativo);
            Button botaoContinuar1 = findViewById(R.id.btnContinuar1);
            Button botaoContinuar1Ativo = findViewById(R.id.btnContinuar1Ativo);
            Button botaoVoltar = findViewById(R.id.btnVoltar);

            botaoDoar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    executarEscolha();
                    txvDescricao.setText(R.string.text_welcome_2_Donate);
                    cargo = "d";
                }
            });

            botaoAdotar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    executarEscolha();
                    txvDescricao.setText(R.string.text_welcome_2_Adopt);
                    cargo = "a";
                }
            });

            botaoContinuar1Ativo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tela = 3;
                    Intent telaLogin = new Intent(MainActivity.this, FormLogin.class);
                    telaLogin.putExtra("cargoEscolhido", cargo);
                    startActivity(telaLogin);
                    System.out.println("Cargo escolhido: " + cargo);
                }
            });

            botaoVoltar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        // TERCEIRA TELA - LOGIN
        if (tela == 3){
            Intent telaLogin = new Intent(MainActivity.this, FormLogin.class);
            startActivity(telaLogin);
        }

        // QUARTA TELA - REGISTRAR
        if (tela == 4) {
            Intent fazerCadastro = new Intent(MainActivity.this, FormCadastro.class);
            startActivity(fazerCadastro);
        }

        // QUINTA TELA - ESQUECI SENHA
        if (tela == 5) {
            Intent esqueciSenha = new Intent(MainActivity.this, FormEsqueciSenha.class);
            startActivity(esqueciSenha);
        }

        // SEXTA TELA - TELA INICIAL
        if (tela == 6) {
            Intent telaInicial = new Intent(MainActivity.this, TelaInicial.class);
            startActivity(telaInicial);
        }

        // SETIMA TELA - PERFIL DO USUARIO
        if (tela == 7) {
            Intent perfilUsuario = new Intent(MainActivity.this, PerfilUsuario.class);
            startActivity(perfilUsuario);
        }

        // OITAVA TELA - MEUS ANIMAIS
        if (tela == 8) {
            Intent meusAnimais = new Intent(MainActivity.this, ListaMeusAnimais.class);
            startActivity(meusAnimais);
        }

        // NONA TELA - PERFIL DO ANIMAL
        if (tela == 9) {
            Intent perfilAnimal = new Intent(MainActivity.this, PerfilAnimal.class);
            startActivity(perfilAnimal);
        }

        // DECIMA TELA - CADASTRO ANIMAL
        if (tela == 10) {
            Intent cadastroAnimal = new Intent(MainActivity.this, FormCadastroAnimal.class);
            startActivity(cadastroAnimal);
        }

        // DECIMA-PRIMEIRA TELA - CONFIGURACOES APLICATIVO
        if (tela == 11) {
            Intent opcoesApp = new Intent(MainActivity.this, TelaOpcoes.class);
            startActivity(opcoesApp);
        }

        // DECIMA-SEGUNDA TELA - TELA MENSAGEM
        if (tela == 12) {
            Intent telaMensagem = new Intent(MainActivity.this, TelaMensagem.class);
            startActivity(telaMensagem);
        }

        // DECIMA-TERCEIRA TELA - TEMPLATE LAYOUT
        if (tela == 13) {
            setContentView(R.layout.activity_template);
        }

        // DECIMA-QUARTA TELA - SPLASH SCREEN
        if (tela == 14) {
            Intent splashScreen = new Intent(MainActivity.this, SplashScreen.class);
            startActivity(splashScreen);
            finish();
        }

        // DECIMA-QUINTA TELA - LAYOUT CHECKBOX CUSTOM SPINNER
        if (tela == 15) {
            setContentView(R.layout.item_checkbox);
        }

        // DECIMA-SEXTA TELA - LAYOUT LISTA MEUS ANIMAIS
        if (tela == 16) {
            setContentView(R.layout.item_meus_animais);
        }

        // DECIMA-SETIMA TELA - LAYOUT LISTA ANIMAIS TELA INICIO
        if (tela == 17) {
            setContentView(R.layout.item_animais_tela_inicial);
        }

        // DECIMA-OITAVA TELA - LAYOUT MENU
        if (tela == 18) {
            setContentView(R.layout.menu_head);
        }
    }

    private void executarEscolha() {
        Button botaoDoar = findViewById(R.id.btnDoar);
        Button botaoDoarInativo = findViewById(R.id.btnDoarInativo);
        Button botaoAdotar = findViewById(R.id.btnAdotar);
        Button botaoAdotarInativo = findViewById(R.id.btnAdotarInativo);
        Button botaoContinuar1 = findViewById(R.id.btnContinuar1);
        Button botaoContinuar1Ativo = findViewById(R.id.btnContinuar1Ativo);

        botaoDoar.setVisibility(View.INVISIBLE);
        botaoDoar.setEnabled(false);
        botaoDoarInativo.setVisibility(View.VISIBLE);
        botaoAdotar.setVisibility(View.INVISIBLE);
        botaoAdotar.setEnabled(false);
        botaoAdotarInativo.setVisibility(View.VISIBLE);
        botaoContinuar1.setVisibility(View.INVISIBLE);
        botaoContinuar1Ativo.setEnabled(true);
        botaoContinuar1Ativo.setVisibility(View.VISIBLE);
    }

    private boolean aplicativoEstaInstalado(String packageName) {
        PackageManager packageManager = getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public void onBackPressed(){
        if (tela == 2){
            tela = 1;
            onResume();
        }
        else {
            finishAffinity();
        }
    }
}