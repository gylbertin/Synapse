package com.example.logingit.Cadastro;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.logingit.Banco.BancoControllerUsuario;
import com.example.logingit.Login.MainActivity;
import com.example.logingit.R;
import com.example.logingit.Cronograma.Tela_Cronograma;
import com.example.logingit.Tela_principal.Tela_Principal;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Tela_Cadastro extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    EditText txtEmail, txtUsername, txtSenha, txtConfSenha;
    MaterialButton cadastroBtn, googleLoginBtn;
    Integer _Usuario, cod_Cronograma, cod_Exame;
    BancoControllerUsuario bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtEmail = findViewById(R.id.txtEmail);
        txtUsername = findViewById(R.id.txtUsername);
        txtSenha = findViewById(R.id.txtSenha);
        txtConfSenha = findViewById(R.id.txtConfSenha);
        cadastroBtn = findViewById(R.id.cadastroBtn);

        cadastroBtn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1052748216330-f58eb8mqq224h8a59ivh4upa3rqkn188.apps.googleusercontent.com") // Copie do Google Cloud
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // botao do google
        MaterialButton googleButton = findViewById(R.id.googleLoginBtn);
        googleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getId()==R.id.googleLoginBtn) {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Falha no login: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (user != null) {
                            Toast.makeText(this, "Login com Google bem sucedido!", Toast.LENGTH_SHORT).show();
                            String nome = user.getDisplayName();
                            String email = user.getEmail();
                            String uid = user.getUid();

                            Cursor dados = bd.ConsultaCodigo(email, uid);

                            if (dados.moveToFirst()) {
                                int seletor = dados.getColumnIndex("cod_Usuario");
                                int seletor2 = dados.getColumnIndex("cod_Exame");
                                _Usuario = dados.getInt(seletor);
                                cod_Exame = dados.getInt(seletor2);

                                Cursor cronograma = bd.ConsultaCodigoCronograma(_Usuario);
                                if (cronograma.moveToFirst()) {
                                    int seletor3 = cronograma.getColumnIndex("cod_Cronograma");
                                    cod_Cronograma = cronograma.getInt(seletor);
                                }

                                Intent tela = new Intent(Tela_Cadastro.this, Tela_Principal.class);
                                Bundle parametros = new Bundle();
                                parametros.putInt("cod_Cronograma", cod_Cronograma);
                                parametros.putInt("cod_Exame", cod_Exame);
                                tela.putExtras(parametros);
                                startActivity(tela);

                            } else {
                                bd.insereDados(email, nome, uid);

                                Intent tela = new Intent(Tela_Cadastro.this, Tela_Cronograma.class);
                                Bundle parametros = new Bundle();
                                parametros.putString("email", email);
                                parametros.putString("senha", uid);
                                tela.putExtras(parametros);
                                startActivity(tela);
                            }


                        }
                    } else {
                        Toast.makeText(this, "Erro ao autenticar", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onClick(View v) {
        String _username = txtUsername.getText().toString();
        String _email = txtEmail.getText().toString();
        String _senha = txtSenha.getText().toString();
        String _confsenha = txtConfSenha.getText().toString();
        String msg = "";
        if (_username.isEmpty()) {
            msg = "O campo Usuario deve ser preenchido!";
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } else {
            if (_email.isEmpty()) {
                msg = "O campo Email deve ser preenchido!";
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            } else {
                if(_senha.isEmpty()) {
                    msg = "O campo Senha deve ser preenchido!";
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                } else {
                    if (_confsenha.isEmpty()) {
                        msg = "O campo Confirmar senha deve ser preenchido!";
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    } else {
                        if (!_senha.equals(_confsenha)) {
                            msg = "O campo Senha e Confirmar devem ser iguais!";
                            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                        } else {
                            BancoControllerUsuario bd = new BancoControllerUsuario(getBaseContext());
                            String resultado;

                            resultado = bd.insereDados(_email,_username,_senha);

                            Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();

                            Intent tela = new Intent(Tela_Cadastro.this, Tela_Cronograma.class);
                            Bundle parametros = new Bundle();
                            parametros.putString("email", _email);
                            parametros.putString("senha", _senha);
                            tela.putExtras(parametros);
                            startActivity(tela);
                        }
                    }
                }
            }
        }
    }
}