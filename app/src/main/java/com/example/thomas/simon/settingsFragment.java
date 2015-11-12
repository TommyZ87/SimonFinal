package com.example.thomas.simon;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class settingsFragment extends Fragment {

    public settingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        rootView.findViewById(R.id.Delete_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HighScores(rootView.getContext().getFilesDir()).deleteHighScores();
            }
        });
        return rootView;
    }

    public View.OnClickListener createOnClickListener(final Class next)
    {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), next);
                startActivity(intent);

            }
        } ;
    }
}
