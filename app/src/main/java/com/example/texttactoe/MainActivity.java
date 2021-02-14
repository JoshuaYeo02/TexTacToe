package com.example.texttactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];

    private boolean player1Turn = true;

    private int roundCount;

    private int player1Points;
    private int player2Points;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
//Phone Number Variable
    private EditText editTextNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
        //SMS AREA
        //Permissions to SEND and Recive and Read SMS
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, PackageManager.PERMISSION_GRANTED);
        //Reading Phone Number
        editTextNumber = findViewById(R.id.editTextNumber);
    }

    @Override
    public void onClick(View v) {
        //localising phone number
        String number = editTextNumber.getText().toString();
        SmsManager mySmsManager = SmsManager.getDefault();

        int position = ((Button)v).getId();
        String x = getResources().getResourceEntryName(v.getId());

        if(!((Button) v).getText().toString().equals("")){
            return;
        }

        if(player1Turn){
            ((Button)v).setText("X");
            mySmsManager.sendTextMessage(number, null, x, null, null);
            Toast.makeText(getApplicationContext(), "Waiting For Response from Opponent",Toast.LENGTH_LONG).show();
        }

        roundCount++;

        if(checkForWin()) {
            if(player1Turn) {
                player1Wins();
            }else {player2Wins();}
        } else if(roundCount == 9){
            draw();
        }else {
            player1Turn = !player1Turn;
        }

        if(!player1Turn) {
            player2Move();
            roundCount++;

            if(checkForWin()) {
                if(player1Turn) {
                    player1Wins();
                }else {player2Wins();}
            } else if(roundCount == 9){
                draw();
            }else {
                player1Turn = !player1Turn;
            }
        }

    }

    private void player2Move(){
        while(true) {
            Cursor cursor = getContentResolver().query(Uri.parse("content://sms"),
                    null, null, null, null);
            cursor.moveToFirst();
            String player2Move = cursor.getString(12);
            if (player2Move.equals("button_00")) {
                Button b = (Button) findViewById(R.id.button_00);
                if (((Button) b).getText().toString().equals("")) {
                    b.setText("O");
                    break;
                }
            } else if (player2Move.equals("button_01")) {
                Button b = (Button) findViewById(R.id.button_01);
                if (((Button) b).getText().toString().equals("")) {
                    b.setText("O");
                    break;
                }
            } else if (player2Move.equals("button_02")) {
                Button b = (Button) findViewById(R.id.button_02);
                if (((Button) b).getText().toString().equals("")) {
                    b.setText("O");
                    break;
                }
            } else if (player2Move.equals("button_10")) {
                Button b = (Button) findViewById(R.id.button_10);
                if (((Button) b).getText().toString().equals("")) {
                    b.setText("O");
                    break;
                }
            } else if (player2Move.equals("button_11")) {
                Button b = (Button) findViewById(R.id.button_11);
                if (((Button) b).getText().toString().equals("")) {
                    b.setText("O");
                    break;
                }
            } else if (player2Move.equals("button_12")) {
                Button b = (Button) findViewById(R.id.button_12);
                if (((Button) b).getText().toString().equals("")) {
                    b.setText("O");
                    break;
                }
            } else if (player2Move.equals("button_20")) {
                Button b = (Button) findViewById(R.id.button_20);
                if (((Button) b).getText().toString().equals("")) {
                    b.setText("O");
                    break;
                }
            } else if (player2Move.equals("button_21")) {
                Button b = (Button) findViewById(R.id.button_21);
                if (((Button) b).getText().toString().equals("")) {
                    b.setText("O");
                    break;
                }
            } else if (player2Move.equals("button_22")) {
                Button b = (Button) findViewById(R.id.button_22);
                if (((Button) b).getText().toString().equals("")) {
                    b.setText("O");
                    break;
                }
            }
        }
    }

    private boolean checkForWin(){
        String[][] field = new String[3][3];

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for(int i = 0; i < 3; i++){
            if(field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }
        for(int i = 0; i < 3; i++){
            if(field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }
        if(field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }
        if(field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }
        return false;
    }
        private void player1Wins(){
            player1Points++;
            Toast.makeText(this, "Player 1 Wins", Toast.LENGTH_SHORT).show();
            updatePointsText();
            resetBoard();
        }
        private void player2Wins(){
            player2Points++;
            Toast.makeText(this, "Player 2 Wins", Toast.LENGTH_SHORT).show();
            updatePointsText();
            resetBoard();
        }
        private void draw(){
            Toast.makeText(this, "Draw", Toast.LENGTH_SHORT).show();
            resetBoard();
        }
        private void updatePointsText(){
            textViewPlayer1.setText("Player 1: " + player1Points);
            textViewPlayer2.setText("Player 2: " + player2Points);
        }
        private void resetBoard(){
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    buttons[i][j].setText("");
                }
            }
            roundCount = 0;
            player1Turn = true;
        }
        private void resetGame(){
            player1Points = 0;
            player2Points = 0;
            updatePointsText();
            resetBoard();
        }
}
