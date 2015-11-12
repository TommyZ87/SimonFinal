package com.example.thomas.simon;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thomas on 11/4/2015.
 */
class Score
{
    int score;
    String name;
    Score(String json)
    {
        try {
            JSONObject jObj = new JSONObject(json);
            setScore(jObj.getInt("score"));
            setName(jObj.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    Score(JSONObject json)
    {
        try {
            setScore(json.getInt("score"));
            setName(json.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Score(int score, String name)
    {
        setScore(score);
        setName(name);
    }

    public String getJSONString()
    {
        return "{\"name\":\""+name+"\",\"score\":\""+score+"\"}";
    }
    public void setScore(int score)
    {
        this.score = score;
    }

    public void setName(String name)
    {
        this.name=name;
    }

    public String getName()
    {
        return name;
    }

    public int getScore()
    {
        return score;
    }
    @Override
    public String toString()
    {
        return "Name " + name + " with a score of " + score;
    }

}