package com.example.thomas.simon;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Thomas on 11/4/2015.
 */
public class HighScores
{
    File highScoresFile;
    ArrayList<Score> scores = new ArrayList<Score>();

    public HighScores(File appDirectory)
    {
        this.highScoresFile = new File(appDirectory,"HighScores.json");
//        Arrays.sort(
    }

    public void readScores()
    {
        scores.clear();

        if(!highScoresFile.exists()||highScoresFile.length()==0)
            return;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(highScoresFile));
            StringBuilder builder = new StringBuilder();
            String line;
            while((line = reader.readLine())!=null)
            {
                builder.append(line);
            }
            reader.close();
            parseJSONIntoScores(builder.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeScores()
    {
        try {
            if(!highScoresFile.exists())
                highScoresFile.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(highScoresFile));
            writer.write("[");
            for(int i = 0; i<scores.size();i++)
            {
                if(i!=0)
                    writer.write(",");
                writer.write(scores.get(i).getJSONString());
            }

            writer.write("]");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseJSONIntoScores(String json)
    {
        try {
            JSONArray jScores = new JSONArray(json);
            for(int i = 0; i< jScores.length(); i++)
            {
                scores.add(new Score(jScores.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private final static Comparator<Score> comparator = new Comparator<Score>() {
        @Override
        public int compare(Score lhs, Score rhs) {
            Integer first = lhs.getScore();
            return -first.compareTo(rhs.getScore());
        }
    };

    public void sortScores()
    {
        Collections.sort(scores,comparator);
    }

    public ArrayList<Score> getScores()
    {
        return scores;
    }

    public void addScore(Score score)
    {
        if(scores.isEmpty())
            readScores();
        scores.add(score);
        sortScores();
        writeScores();
    }

    public Integer getHighScore()
    {
        readScores();
        sortScores();
        if(scores.size()==0) return null;
        return scores.get(0).getScore();
    }

    public void deleteHighScores()
    {
        highScoresFile.delete();
    }
}
