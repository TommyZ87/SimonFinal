package com.example.thomas.simon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class Simon extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simon);
        Button button0 = (Button)findViewById(R.id.button_green);
        Button button1 = (Button)findViewById(R.id.button_red);
        Button button2 = (Button)findViewById(R.id.button_yellow);
        Button button3 = (Button)findViewById(R.id.button_blue);
        Button[] buttons = {button0, button1, button2, button3};

        TextView score = (TextView)findViewById(R.id.score);
        TextView round = (TextView)findViewById(R.id.round);
        TextView highscore = (TextView)findViewById(R.id.highScore);
        new SimonGame(buttons, this,score,round,highscore).round();
    }
}
