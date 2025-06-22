package com.example.logingit.Login;

import android.content.Intent;
import android.content.SharedPreferences;
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
// loginapi
import com.example.logingit.Banco.BancoControllerUsuario;
import com.example.logingit.Banco.DataBaseBanco;
import com.example.logingit.Cronograma.Tela_Cronograma;
import com.example.logingit.R;
import com.example.logingit.Cadastro.Tela_Cadastro;
import com.example.logingit.Tela_principal.Tela_Principal;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
//firebase
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //variavel
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    EditText txtEmail, txtSenha;
    MaterialButton btnLogin, btnCadastro, googleLoginBtn;
    BancoControllerUsuario bd;
    Integer _Usuario, cod_Cronograma, cod_Exame;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        SharedPreferences prefs = getSharedPreferences("app", MODE_PRIVATE);
        if (!prefs.getBoolean("dados_inseridos", false)) {
            DataBaseBanco.carregarDadosDoJson(this);
            prefs.edit().putBoolean("dados_inseridos", true).apply();
        }

        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        btnLogin = findViewById(R.id.btnLogin);
        btnCadastro = findViewById(R.id.btnCadastro);

        btnLogin.setOnClickListener(this);
        btnCadastro.setOnClickListener(this);



        // firebase
        mAuth = FirebaseAuth.getInstance();

        // login com google
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

    public boolean validaLogin() {
        boolean retorno = true;

        String _email = txtEmail.getText().toString();
        String _senha = txtSenha.getText().toString();
        String msg = "";

        if (_email.isEmpty()){
            msg = "O campo de E-mail ou usuario deve ser preenchido!";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            retorno = false;
        }
        if (_senha.isEmpty()){
            msg = "O campo SENHA não foi preenchido!";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            retorno = false;
        }
        bd = new BancoControllerUsuario(this);

        Cursor dados = bd.ConsultaDadosLogin(_email, _senha);

        if(dados.moveToFirst() && dados != null) {
            retorno = true;
        } else {
            msg = "O E-mail / Senha não estão cadastrados no sistema, CADASTRE-SE";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            retorno = false;
        }

        if (dados != null) dados.close();
        bd.close();
        return retorno;
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

                                Intent tela = new Intent(MainActivity.this, Tela_Principal.class);
                                Bundle parametros = new Bundle();
                                parametros.putInt("cod_Cronograma", cod_Cronograma);
                                parametros.putInt("cod_Exame", cod_Exame);
                                tela.putExtras(parametros);
                                startActivity(tela);

                            } else {
                                bd.insereDados(email, nome, uid);

                                Intent tela = new Intent(MainActivity.this, Tela_Cronograma.class);
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
        if (v.getId()==R.id.btnLogin){
            if (validaLogin()) {
                Cursor exame = bd.ConsultaCodigo(txtEmail.getText().toString(), txtSenha.getText().toString());
                if (exame.moveToFirst()) {
                    int seletor = exame.getColumnIndex("cod_Usuario");
                    int seletor2 = exame.getColumnIndex("cod_Exame");
                    _Usuario = exame.getInt(seletor);
                    cod_Exame = exame.getInt(seletor2);
                }
                Cursor cronograma = bd.ConsultaCodigoCronograma(_Usuario);
                if (cronograma.moveToFirst()) {
                    int seletor = cronograma.getColumnIndex("cod_Cronograma");
                    cod_Cronograma = cronograma.getInt(seletor);
                }

                Intent tela = new Intent(MainActivity.this, Tela_Principal.class);
                Bundle parametros = new Bundle();
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                parametros.putInt("cod_Exame", cod_Exame);
                parametros.putInt("cod_Usuario", _Usuario);
                tela.putExtras(parametros);
                startActivity(tela);
            }
        }
        if (v.getId()==R.id.btnCadastro) {
            Intent tela = new Intent(MainActivity.this, Tela_Cadastro.class);
            startActivity(tela);
        }
    }
}
