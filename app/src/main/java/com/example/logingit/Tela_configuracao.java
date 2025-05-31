package com.example.logingit;

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

import java.io.IOException;

public class Tela_configuracao extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private ImageView profileImage;
    private EditText usernameEdit;
    private ImageButton editButton;
    private Button saveButton;
    private Button deleteButton;
    private SharedPreferences prefs;
    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_configuracao);

        // Conectando os elementos do layout
        profileImage = findViewById(R.id.profile_image);
        usernameEdit = findViewById(R.id.username_edit);
        editButton = findViewById(R.id.edit_button);
        saveButton = findViewById(R.id.save_button);
        deleteButton = findViewById(R.id.delete_account);

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
            prefs.edit().clear().apply();
            usernameEdit.setText("");
            profileImage.setImageResource(R.drawable.imagem_perfil);
            Toast.makeText(this, "Conta removida", Toast.LENGTH_SHORT).show();
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
        prefs.edit().putString("username", name).apply();
        Toast.makeText(this, "Salvo!", Toast.LENGTH_SHORT).show();
    }

    private void loadProfileData() {
        String name = prefs.getString("username", "User.123");
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
