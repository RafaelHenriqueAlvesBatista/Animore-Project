package com.example.animoreproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // INSTANCIA O GERENTE QUE CUIDA DO CONTROLADOR DA JANELA DO SO DO CELULAR
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());

        // IMPEDE QUE APARECA A BARRAS ESCONDIDAS DO SISTEMA, QUANDO A TELA E CLICADA PELO USUARIO
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );

        // ESCONDE A JANELA DE STATUS POR ALGUNS SEGUNDOS
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());

        // ESPERA POR 3 SEGUNDOS ANTES DE IR PARA A TELA PRINCIPAL
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        startActivity(new Intent(SplashScreen.this, TelaInicial.class));
                    } else {
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    }
                }
            }, 3000);
    }

    @Override
    public void onBackPressed() {}
}