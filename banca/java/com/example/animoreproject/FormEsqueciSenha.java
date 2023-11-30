package com.example.animoreproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class FormEsqueciSenha extends AppCompatActivity {

    private TextInputEditText edtEmail;
    private Button btnEnviarNovaSenha, btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_senha);

        edtEmail           = findViewById(R.id.edtEmail);
        btnEnviarNovaSenha = findViewById(R.id.btnEnviarNovaSenha);
        btnVoltar          = findViewById(R.id.btnVoltar);
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnEnviarNovaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email     = edtEmail.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(FormEsqueciSenha.this, R.string.message_emptyInputs, Toast.LENGTH_SHORT).show();
                } else {
                    atualizarSenha();
                }
            }
        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void atualizarSenha() {
        ConstraintLayout clyTelaCarregando = findViewById(R.id.clyTelaCarregando);
        LinearLayout llyStatusBar = findViewById(R.id.llyStatusBar);
        clyTelaCarregando.setVisibility(View.VISIBLE);
        btnEnviarNovaSenha.setVisibility(View.INVISIBLE);
        btnVoltar.setVisibility(View.INVISIBLE);
        llyStatusBar.setBackgroundColor(getColor(R.color.analogous2_800));

        String email = edtEmail.getText().toString();

        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clyTelaCarregando.setVisibility(View.INVISIBLE);
                            btnEnviarNovaSenha.setVisibility(View.VISIBLE);
                            btnVoltar.setVisibility(View.VISIBLE);
                            llyStatusBar.setBackgroundColor(getColor(R.color.analogous2_300));

                            Toast.makeText(FormEsqueciSenha.this, R.string.message_sendEmailSuccess, Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                } else {
                    String erro;
                    clyTelaCarregando.setVisibility(View.INVISIBLE);
                    btnEnviarNovaSenha.setVisibility(View.VISIBLE);
                    btnVoltar.setVisibility(View.VISIBLE);
                    llyStatusBar.setBackgroundColor(getColor(R.color.analogous2_300));

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = getString(R.string.exception_registerInvalidEmail);
                    } catch (Exception e){
                        erro = getString(R.string.exception_anotherError);
                    }

                    Toast.makeText(FormEsqueciSenha.this, erro, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}