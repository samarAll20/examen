package com.example.xo_tournament.ui;
import android.util.Log;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.xo_tournament.R;
import com.example.xo_tournament.model.TournamentResult;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer winSound;

    private static final String TAG = "GameActivity";


    private String playerSymbol;
    private int totalGames;
    private int currentGame = 1;
    private int scoreX = 0, scoreO = 0, draws = 0;

    private TextView tvGameStatus, tvScoreX, tvScoreO, tvDraws;
    private Button[][] buttons = new Button[3][3];

    private String[][] board = new String[3][3];
    private boolean playerTurn = true; // true = X, false = O
    private boolean gameActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Log.d(TAG, "GameActivity créée avec succès!");

        // Récupérer les données
        playerSymbol = getIntent().getStringExtra("playerSymbol");
        totalGames = getIntent().getIntExtra("totalGames", 5);

        Log.d(TAG, "Données reçues - Symbole: " + playerSymbol + ", Parties: " + totalGames);

        // Initialiser les vues
        tvGameStatus = findViewById(R.id.tv_game_status);
        tvScoreX = findViewById(R.id.tvScoreX);
        tvScoreO = findViewById(R.id.tvScoreO);
        tvDraws = findViewById(R.id.tvDraws);

        // CORRECTION : Initialisation manuelle de tous les boutons
        buttons[0][0] = findViewById(R.id.btn00);
        buttons[0][1] = findViewById(R.id.btn01);
        buttons[0][2] = findViewById(R.id.btn02);
        buttons[1][0] = findViewById(R.id.btn10);
        buttons[1][1] = findViewById(R.id.btn11);
        buttons[1][2] = findViewById(R.id.btn12);
        buttons[2][0] = findViewById(R.id.btn20);
        buttons[2][1] = findViewById(R.id.btn21);
        buttons[2][2] = findViewById(R.id.btn22);

        // Initialiser le board et les listeners
        initializeBoard();
        updateGameStatus();

        Toast.makeText(this, "Partie 1 démarrée! C'est au tour de X", Toast.LENGTH_LONG).show();

        winSound = MediaPlayer.create(this,R.raw.winsound);
    }

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = "";
                buttons[i][j].setOnClickListener(this);
                buttons[i][j].setText("");
                buttons[i][j].setTextSize(32);
                buttons[i][j].setTextColor(getColor(android.R.color.white));
            }
        }
        gameActive = true;
        playerTurn = true; // X commence toujours
    }

    @Override
    public void onClick(View v) {
        if (!gameActive) return;

        Button button = (Button) v;
        String tag = (String) button.getTag();

        if (tag == null) {
            Log.e(TAG, "Tag null pour le bouton");
            return;
        }

        int row = Integer.parseInt(tag.substring(0, 1));
        int col = Integer.parseInt(tag.substring(1, 2));

        Log.d(TAG, "Clic sur case: " + row + "," + col);

        // Vérifier si la case est vide
        if (board[row][col].isEmpty()) {
            // Placer le symbole du joueur actuel
            String currentSymbol = playerTurn ? "X" : "O";
            board[row][col] = currentSymbol;
            button.setText(currentSymbol);

            // Changer la couleur selon le symbole
            if (currentSymbol.equals("X")) {
                button.setTextColor(getColor(android.R.color.holo_blue_light));
            } else {
                button.setTextColor(getColor(android.R.color.holo_orange_light));
            }

            // Vérifier s'il y a un gagnant
            if (checkWin(currentSymbol)) {
                if(winSound != null){
                    winSound.start();
                }
                gameActive = false;
                if (currentSymbol.equals("X")) {
                    scoreX++;
                    showRoundResult("Le joueur X a gagné cette partie!");
                } else {
                    scoreO++;
                    showRoundResult("Le joueur O a gagné cette partie!");
                }
            } else if (isBoardFull()) {
                gameActive = false;
                draws++;
                showRoundResult("Partie nulle!");
            } else {
                // Changer de joueur
                playerTurn = !playerTurn;
                updateGameStatus();
            }
        } else {
            Toast.makeText(this, "Case déjà occupée!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkWin(String symbol) {
        // Vérifier les lignes
        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(symbol) && board[i][1].equals(symbol) && board[i][2].equals(symbol)) {
                return true;
            }
        }

        // Vérifier les colonnes
        for (int i = 0; i < 3; i++) {
            if (board[0][i].equals(symbol) && board[1][i].equals(symbol) && board[2][i].equals(symbol)) {
                return true;
            }
        }

        // Vérifier les diagonales
        if (board[0][0].equals(symbol) && board[1][1].equals(symbol) && board[2][2].equals(symbol)) {
            return true;
        }
        if (board[0][2].equals(symbol) && board[1][1].equals(symbol) && board[2][0].equals(symbol)) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void updateGameStatus() {
        tvGameStatus.setText("Partie " + currentGame + " / " + totalGames);
        tvScoreX.setText(String.valueOf(scoreX));
        tvScoreO.setText(String.valueOf(scoreO));
        tvDraws.setText(String.valueOf(draws));
    }

    private void showRoundResult(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Résultat de la partie")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Continuer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (currentGame < totalGames) {
                            currentGame++;
                            initializeBoard();
                            updateGameStatus();
                            String nextPlayer = playerTurn ? "X" : "O";
                            Toast.makeText(GameActivity.this,
                                    "Partie " + currentGame + " - Tour de " + nextPlayer,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            showTournamentResult();
                        }
                    }
                })
                .show();
    }

    private void showTournamentResult() {
        String winner;
        if (scoreX > scoreO) {
            winner = "Joueur X";
        } else if (scoreO > scoreX) {
            winner = "Joueur O";
        } else {
            winner = "Égalité";
        }

        TournamentResult result = new TournamentResult(scoreX, scoreO, draws, totalGames, winner);

        new AlertDialog.Builder(this)
                .setTitle("Tournoi Terminé!")
                .setMessage("Score final:\n\n" +
                        "Joueur X: " + scoreX + " victoires\n" +
                        "Joueur O: " + scoreO + " victoires\n" +
                        "Parties nulles: " + draws + "\n\n" +
                        "Vainqueur: " + winner + "!")
                .setCancelable(false)
                .setPositiveButton("Sauvegarder", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper dbHelper = new DatabaseHelper(GameActivity.this);
                        dbHelper.insertTournament(scoreX, scoreO, draws, totalGames, winner); // ✅ ici
                        Toast.makeText(GameActivity.this, "Tournoi sauvegardé dans la DB!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    private void saveTournament(TournamentResult result) {
        try {
            FileOutputStream fos = openFileOutput("last_tournament.ser", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(result);
            oos.close();
            fos.close();
            Toast.makeText(this, "Tournoi sauvegardé!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Erreur sauvegarde: " + e.getMessage());
            Toast.makeText(this, "Erreur de sauvegarde", Toast.LENGTH_SHORT).show();
        }
    }
}