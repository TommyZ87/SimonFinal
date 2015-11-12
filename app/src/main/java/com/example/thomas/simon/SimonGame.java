package com.example.thomas.simon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Thomas on 10/14/2015.
 */
public class SimonGame
{
    private ArrayList<Integer> moves = new ArrayList<>();
    private Random random = new Random();
    private Button[] buttons = new Button[4];
    private int currentMove = 0;
    private boolean humanTurn = false;
    private Handler handler;
    private Context context;

    private TextView scoreDisplay;
    private TextView roundDisplay;
    private TextView highscoreDisplay;

    private int score;
    private int round;

    private File fileDirectory;

    private MediaPlayer[] mp = new MediaPlayer[4];

    SimonGame(Button[] buttons, Context context)
    {
        constructor(buttons, context, null, null, null);
    }

    SimonGame(Button[] buttons, Context context, TextView scoreDisplay, TextView roundDisplay, TextView highScoreDisplay)
    {
        constructor(buttons, context, scoreDisplay, roundDisplay, highScoreDisplay);
    }

    private void constructor(Button[] buttons, Context context, TextView scoreDisplay, TextView roundDisplay, TextView highScoreDisplay)
    {
        this.buttons=buttons;
        handler = new Handler(context.getMainLooper());
        setButtonsOnTouch();
        this.scoreDisplay = scoreDisplay;
        this.roundDisplay = roundDisplay;
        this.highscoreDisplay = highScoreDisplay;
        fileDirectory = context.getFilesDir();
        this.context = context;
        setHighscoreDisplay();
        setMediaPlayer();
    }

    public void setMediaPlayer()
    {
        mp[0] = MediaPlayer.create(context, R.raw.beep0);
        mp[1] = MediaPlayer.create(context, R.raw.beep1);
        mp[2] = MediaPlayer.create(context, R.raw.beep2);
        mp[3] = MediaPlayer.create(context, R.raw.beep3);

    }

    public void reset()
    {
        moves.clear();
        clearScore();
        currentMove=0;
        setHighscoreDisplay();
        clearRound();
        round();
    }

    public void setHighscoreDisplay()
    {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Integer i = new HighScores(fileDirectory).getHighScore();
                if(i!=null)
                    highscoreDisplay.setText(i +"");
                else
                    highscoreDisplay.setText("NA");
            }
        });
    }

    public void round()
    {
        currentMove=0;
        updateRound();
        generateMove();
        playBack();
    }

    public void generateMove()
    {
        moves.add(random.nextInt(4));
    }

    public void playBack()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int highlightTime = 1000;
                humanTurn=false;
                setButtonsEnabled(humanTurn);
                for(final Integer move: moves)
                {
                    highlight(move);
                    pause(highlightTime);
                    unHighlight(move);
                    pause(highlightTime/5);

                }
                humanTurn=true;
                setButtonsEnabled(humanTurn);
            }
        }).start();
    }

    private void pause(long millis)
    {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    final private static int r1 = Color.parseColor("#FF640000");//Red
    final private static int r2 = Color.parseColor("#FFFF0000");//Red
    final private static int g1 = Color.parseColor("#FF006400");//Green
    final private static int g2 = Color.parseColor("#FF00FF00");//Green
    final private static int b1 = Color.parseColor("#FF000064");//Blue
    final private static int b2 = Color.parseColor("#FF0000FF");//Blue
    final private static int y1 = Color.parseColor("#FF646400");//Yellow
    final private static int y2 = Color.parseColor("#FFFFFF00");//Yellow
    final private static int[] unhighlightColors = {g1,r1,y1,b1};
    final private static int[] highlightColors = {g2,r2,y2,b2};

    public void highlight(final int index)
    {
        mp[index].start();
        handler.post(new Runnable() {
            @Override
            public void run() {
                buttons[index].setBackgroundColor(highlightColors[index]);
            }
        });
    }

    public void unHighlight(final int index)
    {
        mp[index].pause();
        mp[index].seekTo(0);

        handler.post(new Runnable() {
            @Override
            public void run() {
                buttons[index].setBackgroundColor(unhighlightColors[index]);
            }
        });
    }

    public void setButtonsEnabled(final boolean enabled)
    {
        handler.post(new Runnable() {
            @Override
            public void run() {
                for(Button button: buttons)
                {
                    button.setEnabled(enabled);
                }
            }
        });
    }

    public void updateScore()
    {
        score++;
        if(scoreDisplay!=null)
        {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    scoreDisplay.setText(score+"");
                }
            });
        }
    }


    public void updateRound()
    {
        round++;
        if(roundDisplay!=null)
        {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    roundDisplay.setText(round+"");
                }
            });
        }
    }

    public void clearRound()
    {
        round=0;
        if(roundDisplay!=null)
        {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    roundDisplay.setText(round+"");
                }
            });
        }
    }

    public void clearScore()
    {
        score=0;
        if(scoreDisplay!=null)
        {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    scoreDisplay.setText(score+"");
                }
            });
        }
    }

    public void setButtonsOnTouch()
    {
        for(int i = 0; i<4; i++)
        {
            final int q = i;
            buttons[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // PRESSED
                            highlight(q);
                            return true; // if you want to handle the touch event
                        case MotionEvent.ACTION_UP:
                            // RELEASED
                            unHighlight(q);
                            click(q);
                            return true; // if you want to handle the touch event
                    }
                    return true;
                }
            });
        }
    }

    public void click(int q)
    {
        if(humanTurn)
        {
            if(moves.get(currentMove)==q)
            {
                currentMove++;
                updateScore();
                if(currentMove>=moves.size())
                {
                    round();
                }
            }
            else
            {
                onLose();
            }
        }
    }

    public void onLose()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setCancelable(false);

        alert.setTitle("High Score");
        alert.setMessage("Add score to leaderboard");

        // Set an EditText view to get user input
        final EditText input = new EditText(context);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                new HighScores(fileDirectory).addScore(new Score(score, input.getText().toString()));
                reset();
            }
        });
        alert.show();
    }
}