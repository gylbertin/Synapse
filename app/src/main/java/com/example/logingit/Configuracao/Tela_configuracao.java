package com.example.logingit.Configuracao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.logingit.Banco.BancoController;
import com.example.logingit.Banco.BancoControllerUsuario;
import com.example.logingit.Login.MainActivity;
import com.example.logingit.R;
import com.example.logingit.Tela_Questao.Tela_GeraQuestoes;
import com.example.logingit.Redacao.Tela_Redacao;
import com.example.logingit.Tela_principal.Tela_Principal;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class Tela_configuracao extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private ImageView profileImage;
    private EditText usernameEdit;
    private ImageButton editButton;
    private Button saveButton;
    private Button deleteButton, btSair;
    private SharedPreferences prefs;
    private Uri imageUri = null;
    private int cod_Usuario, cod_Cronograma;

    BancoControllerUsuario bancoUsuario;

    private BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_configuracao);

        Intent intent = getIntent();
        Bundle dados = intent.getExtras();
        cod_Usuario = dados.getInt("cod_Usuario");
        cod_Cronograma = dados.getInt("cod_Cronograma");



        BancoController banco = new BancoController(this);

        // Conectando os elementos do layout
        profileImage = findViewById(R.id.profile_image);
        usernameEdit = findViewById(R.id.username_edit);
        editButton = findViewById(R.id.edit_button);
        saveButton = findViewById(R.id.save_button);
        deleteButton = findViewById(R.id.delete_account);
        btSair = findViewById(R.id.btSair);
        bottom_navigation = findViewById(R.id.bottom_navigation);

        prefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        loadProfileData();

        // Botão de salvar
        saveButton.setOnClickListener(v -> {
            saveProfileData();
            usernameEdit.setEnabled(false);
            usernameEdit.setBackground(null); // Remove o fundo da borda
            saveButton.setVisibility(View.GONE);
            editButton.setVisibility(View.VISIBLE);


        });

        bottom_navigation.setSelectedItemId(R.id.nav_perfil);

        bottom_navigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_perfil) {
                return true;
            } else if (itemId == R.id.nav_questoes) {
                Intent tela = new Intent(Tela_configuracao.this, Tela_GeraQuestoes.class);
                Bundle parametros = new Bundle();
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                tela.putExtras(parametros);
                startActivity(tela);
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_home) {
                Intent tela = new Intent(Tela_configuracao.this, Tela_Principal.class);
                Bundle parametros = new Bundle();
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                tela.putExtras(parametros);
                startActivity(tela);
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_Redacao) {
                Intent tela = new Intent(Tela_configuracao.this, Tela_Redacao.class);
                Bundle parametros = new Bundle();
                parametros.putInt("cod_Usuario",cod_Usuario);
                parametros.putInt("cod_Cronograma", cod_Cronograma);
                tela.putExtras(parametros);
                startActivity(tela);
                overridePendingTransition(0, 0);
                return true;
            }

            return false;
        });

        // Botão de lápis para editar
        editButton.setOnClickListener(v -> {
            usernameEdit.setEnabled(true);
            usernameEdit.setBackgroundTintList(getColorStateList(android.R.color.darker_gray));
            usernameEdit.requestFocus();
            saveButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.GONE);
        });

        // Botão de deletar conta
        deleteButton.setOnClickListener(v -> {
            String msg = banco.excluirDados(cod_Usuario,cod_Cronograma);
            usernameEdit.setText("");
            profileImage.setImageResource(R.drawable.imagem_perfil);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

            prefs.edit().clear().apply();

            // Redireciona para a tela de login
            Intent tela = new Intent(Tela_configuracao.this, MainActivity.class);
            tela.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | tela.FLAG_ACTIVITY_CLEAR_TASK); // limpa o histórico
            startActivity(tela);
        });

        btSair.setOnClickListener(v -> {
            usernameEdit.setText("");
            profileImage.setImageResource(R.drawable.imagem_perfil);

            prefs.edit().clear().apply();

            Intent tela = new Intent(Tela_configuracao.this, MainActivity.class);
            tela.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | tela.FLAG_ACTIVITY_CLEAR_TASK); // limpa o histórico
            startActivity(tela);
        });

        // Clique na imagem de perfil
        profileImage.setOnClickListener(this::changeProfilePicture);
    }

    public void changeProfilePicture(View view) {
        Intent pick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pick, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
                prefs.edit().putString("imageUri", imageUri.toString()).apply();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveProfileData() {
        String name = usernameEdit.getText().toString();

        BancoControllerUsuario bancoUsuario = new BancoControllerUsuario(this);

        String msg = bancoUsuario.trocaNome(name,cod_Usuario);

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void loadProfileData() {
        BancoControllerUsuario bancoUsuario = new BancoControllerUsuario(this);
        String name = bancoUsuario.puxaNome(cod_Usuario);
        usernameEdit.setText(name);

        // Começa com nome não editável
        usernameEdit.setEnabled(false);
        usernameEdit.setBackground(null);
        saveButton.setVisibility(View.GONE);
        editButton.setVisibility(View.VISIBLE);

        // Carrega imagem, se tiver
        String uriStr = prefs.getString("imageUri", null);
        if (uriStr != null) {
            imageUri = Uri.parse(uriStr);
            profileImage.setImageURI(imageUri);
        }
    }
}
