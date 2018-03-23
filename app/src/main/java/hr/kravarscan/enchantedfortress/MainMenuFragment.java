package hr.kravarscan.enchantedfortress;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hr.kravarscan.enchantedfortress.storage.SaveLoad;

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

public class MainMenuFragment extends AAttachableFragment {

    private static final String LOG_TAG = "MainMenuFragment";

    private OnFragmentInteractionListener listener;

    public MainMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        View layout = inflater.inflate(R.layout.fragment_main_menu, container, false);

        View continueButton = layout.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onContinue();
            }
        });

        if (!SaveLoad.get().hasAutosave(container.getContext()))
            continueButton.setVisibility(View.GONE);

        layout.findViewById(R.id.newGameButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onNewGame();
                    }
                }
        );

        layout.findViewById(R.id.scoresButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onScores();
                    }
                }
        );

        layout.findViewById(R.id.helpButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onHelp();
                    }
                }
        );

        layout.findViewById(R.id.aboutButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onAbout();
                    }
                }
        );

        return layout;
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

    interface OnFragmentInteractionListener {
        void onContinue();
        void onNewGame();
        void onScores();
        void onHelp();
        void onAbout();
    }
}
