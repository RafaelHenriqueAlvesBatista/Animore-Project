package com.example.animoreproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class FormLogin extends AppCompatActivity {

    private TextView txvEsqueciSenha, txvFazerCadastro;
    private TextInputEditText edtEmail, edtSenha;
    private Button btnEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txvEsqueciSenha  = findViewById(R.id.txvEsqueciSenha);
        txvFazerCadastro = findViewById(R.id.txvFazerCadastro);
        edtEmail         = findViewById(R.id.edtEmail);
        edtSenha         = findViewById(R.id.edtSenha);
        btnEntrar        = findViewById(R.id.btnEntrar);
    }

    @Override
    protected void onResume() {
        super.onResume();

        receberFeedback();

        txvEsqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent esqueciSenha = new Intent(FormLogin.this, FormEsqueciSenha.class);
                startActivity(esqueciSenha);
            }
        });

        txvFazerCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fazerCadastro = new Intent(FormLogin.this, FormCadastro.class);
                startActivity(fazerCadastro);
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String senha = edtSenha.getText().toString();

                if (email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(FormLogin.this, R.string.message_emptyInputs, Toast.LENGTH_SHORT).show();
                } else {
                    autenticarUsuario();
                }
            }
        });
    }

    private void receberFeedback(){
        Intent receberFeedback = getIntent();
        if(receberFeedback.getIntExtra("enviarFeedback", 0) == 1){
            Toast.makeText(FormLogin.this, R.string.message_registerSuccess, Toast.LENGTH_SHORT).show();
        } else if (receberFeedback.getIntExtra("enviarFeedback", 0) == 2) {
            Toast.makeText(FormLogin.this, R.string.message_deleteSuccess, Toast.LENGTH_SHORT).show();
        } else if (receberFeedback.getIntExtra("enviarFeedback", 0) == 3) {
            Toast.makeText(FormLogin.this, R.string.message_signOutSuccess, Toast.LENGTH_SHORT).show();
        }
        receberFeedback.removeExtra("enviarFeedback");
    }

    private void autenticarUsuario() {
        ConstraintLayout clyTelaCarregando = findViewById(R.id.clyTelaCarregando);
        LinearLayout llyStatusBar = findViewById(R.id.llyStatusBar);
        clyTelaCarregando.setVisibility(View.VISIBLE);
        btnEntrar.setVisibility(View.INVISIBLE);
        llyStatusBar.setBackgroundColor(getColor(R.color.analogous2_800));

        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clyTelaCarregando.setVisibility(View.INVISIBLE);
                            btnEntrar.setVisibility(View.VISIBLE);
                            llyStatusBar.setBackgroundColor(getColor(R.color.analogous2_300));

                            Intent receberCargo = getIntent();
                            String cargoEscolhido = receberCargo.getStringExtra("cargoEscolhido");
                            Intent intent = new Intent(FormLogin.this, TelaInicial.class);
                            intent.putExtra("atualizarCargo", cargoEscolhido);
                            startActivity(intent);
                            finish();
                        }
                    }, 2000);
                } else {
                    String erro;
                    clyTelaCarregando.setVisibility(View.INVISIBLE);
                    btnEntrar.setVisibility(View.VISIBLE);
                    llyStatusBar.setBackgroundColor(getColor(R.color.analogous2_300));

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = getString(R.string.exception_loginInvalidInputs);
                    } catch (Exception e){
                        erro = getString(R.string.exception_loginAnotherError);
                    }

                    Toast.makeText(FormLogin.this, erro, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FormLogin.this);
        builder.setMessage("Deseja realmente sair?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .create()
                .show();
    }
}