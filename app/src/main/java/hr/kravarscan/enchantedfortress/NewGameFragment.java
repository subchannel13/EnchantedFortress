package hr.kravarscan.enchantedfortress;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import hr.kravarscan.enchantedfortress.logic.Difficulty;

/**
 * Copyright 2018 Ivan Kravarščan
 *
 * This file is part of Enchanted Fortress.
 *
 * Enchanted Fortress is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Enchanted Fortress is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Enchanted Fortress.  If not, see <http://www.gnu.org/licenses/>.
 */

public class NewGameFragment extends AAttachableFragment {

    private static final String LOG_TAG = "NewGameFragment";

    private NewGameFragment.OnFragmentInteractionListener listener;
    private Spinner difficultySelector;

    private TextView startPopText;
    private TextView demonSpawnText;
    private TextView demonGrowthText;

    public NewGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_new_game, container, false);

        this.startPopText = view.findViewById(R.id.difficultyStartPop);
        this.demonSpawnText = view.findViewById(R.id.difficultyDemonSpawn);
        this.demonGrowthText = view.findViewById(R.id.difficultyDemonGrowth);
        final View[] infoTexts = new View[]
                {
                        this.demonGrowthText,
                        this.demonSpawnText,
                        this.startPopText
                };

        this.difficultySelector = view.findViewById(R.id.difficultySelection);
        difficultySelector.setAdapter(ArrayAdapter.createFromResource(view.getContext(), R.array.difficultyLevels, R.layout.research_item));
        difficultySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(LOG_TAG, "difficultySelector.onItemSelected, difficulty: " + i);
                Difficulty data = Difficulty.Levels[i];

                int visibility = (data == Difficulty.Medium) ? View.GONE : View.VISIBLE;
                for(View infoView : infoTexts)
                    infoView.setVisibility(visibility);

                startPopText.setText(getResources().getString(R.string.difficultyStartingPop, bonusPercent(data.getStartingPop(), Difficulty.Medium.getStartingPop())));
                demonSpawnText.setText(getResources().getString(R.string.difficultyDemonSpawn, bonusPercent(data.getDemonSpawnFactor(), Difficulty.Medium.getDemonSpawnFactor())));
                demonGrowthText.setText(getResources().getString(R.string.difficultyDemonGrowth, bonusPercent(data.getDemonPowerBase() - 1, Difficulty.Medium.getDemonPowerBase() - 1)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d(LOG_TAG, "difficultySelector.onNothingSelected");

                //No operation
            }
        });
        difficultySelector.setSelection(Difficulty.Medium.getIndex());

        view.findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onNewGameStart(difficultySelector.getSelectedItemPosition());
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "onDetach");

        listener = null;
    }

    @Override
    public void attach(Object listener) {
        Log.d(LOG_TAG, "attach");

        if (listener instanceof OnFragmentInteractionListener) {
            this.listener = (OnFragmentInteractionListener) listener;
        } else {
            throw new RuntimeException(listener.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private static String bonusPercent(double factor, double normalFactor)
    {
        factor = (factor - normalFactor) / normalFactor;
        String sign = factor >= 0 ? "+" : "";
        return sign + Integer.toString((int)(factor * 100)) + "%";
    }

    interface OnFragmentInteractionListener {
        void onNewGameStart(int difficulty);
    }
}
