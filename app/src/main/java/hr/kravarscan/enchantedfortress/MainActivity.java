package hr.kravarscan.enchantedfortress;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.ViewGroup;

import hr.kravarscan.enchantedfortress.logic.Difficulty;
import hr.kravarscan.enchantedfortress.logic.Game;
import hr.kravarscan.enchantedfortress.storage.SaveLoad;

public class MainActivity extends Activity implements MainMenuFragment.OnFragmentInteractionListener, GameFragment.OnFragmentInteractionListener, NewGameFragment.OnFragmentInteractionListener {

    private Fragment lastMainFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (((ViewGroup)findViewById(R.id.fragment_container)).getChildCount() == 0) {
            MainMenuFragment mainMenu = new MainMenuFragment();
            mainMenu.attach(this);
            getFragmentManager().beginTransaction().add(R.id.fragment_container, mainMenu).commit();
        }
    }

    private void switchMainView(AAttachableFragment fragment)
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

        fragment.attach(this);
        this.lastMainFragment = fragment;
    }

    @Override
    public void onContinue() {
        GameFragment gameFragment = new GameFragment();
        Game game = new Game(Difficulty.Medium);
        SaveLoad.get().deserialize(game, SaveLoad.get().load(this));
        gameFragment.setGame(game);

        this.switchMainView(gameFragment);
    }

    @Override
    public void onBackPressed() {
        if (this.lastMainFragment != null && this.lastMainFragment instanceof MainMenuFragment)
            super.onBackPressed();
        else
            this.switchMainView(new MainMenuFragment());
    }

    @Override
    public void onNewGame() {
        NewGameFragment fragment = new NewGameFragment();

        this.switchMainView(fragment);
    }

    @Override
    public void onNewGameStart(int difficulty) {
        GameFragment fragment = new GameFragment();
        Game game = new Game(Difficulty.Levels[difficulty]);
        fragment.setGame(game);

        this.switchMainView(fragment);
    }

    @Override
    public void onHelp() {
        this.switchMainView(new HelpFragment());
    }

    @Override
    public void onAbout() {
        this.switchMainView(new AboutFragment());
    }

    @Override
    public void onGameOver() {
        this.switchMainView(new MainMenuFragment());
    }
}
