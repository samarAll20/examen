package com.example.xo_tournament.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xo_tournament.R;

public class MainActivity extends AppCompatActivity {


    RadioGroup radioGroup;
    Spinner spinnerGames;
    Button btnPlay, btnRules, btnLastScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des vues
        radioGroup = findViewById(R.id.radioGroupSymbol);
        spinnerGames = findViewById(R.id.spinnerGames);
        btnPlay = findViewById(R.id.btnPlay);
        btnRules = findViewById(R.id.btnRules);
        btnLastScores = findViewById(R.id.btnLastScores);

        // CORRECTION : Configuration du Spinner avec valeurs par défaut
        String[] gameOptions = {"5 parties", "10 parties", "15 parties"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                gameOptions
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGames.setAdapter(adapter);

        // CORRECTION : Test des RadioButtons
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = findViewById(checkedId);
            if (radioButton != null) {
                Toast.makeText(this, "Symbole sélectionné: " + radioButton.getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // CORRECTION : Vérification que les boutons sont cliquables
        btnPlay.setOnClickListener(v -> {
            Toast.makeText(this, "Bouton Jouer cliqué!", Toast.LENGTH_SHORT).show();
            startGame();
        });

        btnRules.setOnClickListener(v -> {
            Toast.makeText(this, "Règles du jeu", Toast.LENGTH_SHORT).show();
            showRules();
        });

        btnLastScores.setOnClickListener(v -> {
            Toast.makeText(this, "Derniers scores", Toast.LENGTH_SHORT).show();
            showLastScores();
        });

        // CORRECTION : Message de test au démarrage
        Toast.makeText(this, "Application démarrée - Sélectionnez X ou O",
                Toast.LENGTH_LONG).show();


    }

    private void startGame() {
        int selectedSymbolId = radioGroup.getCheckedRadioButtonId();

        if(selectedSymbolId == -1){
            Toast.makeText(this, "Veuillez choisir X ou O", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedButton = findViewById(selectedSymbolId);
        String playerSymbol = selectedButton.getText().toString();

        // CORRECTION : Extraction du nombre depuis le texte
        String selectedGamesText = spinnerGames.getSelectedItem().toString();
        int totalGames = Integer.parseInt(selectedGamesText.replace(" parties", ""));

        Toast.makeText(this,
                "Démarrage avec " + playerSymbol + " - " + totalGames + " parties",
                Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("playerSymbol", playerSymbol);
        intent.putExtra("totalGames", totalGames);
        startActivity(intent);
    }

    private void showRules() {
        // Votre code pour les règles
    }

    private void showLastScores() {
        // Votre code pour les scores
    }
}