package hr.kravarscan.enchantedfortress;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import hr.kravarscan.enchantedfortress.logic.Difficulty;
import hr.kravarscan.enchantedfortress.logic.Game;
import hr.kravarscan.enchantedfortress.storage.HighScores;
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

public class MainActivity extends Activity implements MainMenuFragment.OnFragmentInteractionListener, GameFragment.OnFragmentInteractionListener, NewGameFragment.OnFragmentInteractionListener {

    private static final String LOG_TAG = "MainActivity";

    private Fragment lastMainFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_main);

        Log.d(LOG_TAG, "Has saved instance? " + (savedInstanceState == null));
        if (savedInstanceState == null) {
            HighScores.get().load(this);

            MainMenuFragment mainMenu = new MainMenuFragment();
            mainMenu.attach(this);
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, mainMenu).commit();
        }
    }

    private void switchMainView(AAttachableFragment fragment)
    {
        Log.d(LOG_TAG, "switchMainView, fragment: " + fragment);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

        fragment.attach(this);
        this.lastMainFragment = fragment;
    }

    @Override
    public void onContinue() {
        Log.d(LOG_TAG, "onContinue");

        GameFragment gameFragment = new GameFragment();
        Game game = new Game(Difficulty.Medium);
        SaveLoad.get().deserialize(game, SaveLoad.get().load(this));
        gameFragment.setGame(game);

        this.switchMainView(gameFragment);
    }

    @Override
    public void onBackPressed() {
        Log.d(LOG_TAG, "onBackPressed, last fragment: " + this.lastMainFragment);

        if (this.lastMainFragment != null && this.lastMainFragment instanceof MainMenuFragment)
            super.onBackPressed();
        else
            this.switchMainView(new MainMenuFragment());
    }

    @Override
    public void onNewGame() {
        Log.d(LOG_TAG, "onNewGame");

        this.switchMainView(new NewGameFragment());
    }

    @Override
    public void onNewGameStart(int difficulty) {
        Log.d(LOG_TAG, "onNewGameStart, difficulty: " + difficulty);

        GameFragment fragment = new GameFragment();
        Game game = new Game(Difficulty.Levels[difficulty]);
        fragment.setGame(game);

        this.switchMainView(fragment);
    }

    @Override
    public void onScores() {
        Log.d(LOG_TAG, "onScores");

        this.switchMainView(new ScoresFragment());
    }

    @Override
    public void onHelp() {
        Log.d(LOG_TAG, "onHelp");

        this.switchMainView(new HelpFragment());
    }

    @Override
    public void onAbout() {
        Log.d(LOG_TAG, "onAbout");

        this.switchMainView(new AboutFragment());
    }

    @Override
    public void onGameOver() {
        Log.d(LOG_TAG, "onGameOver");

        this.switchMainView(new MainMenuFragment());
    }
}
