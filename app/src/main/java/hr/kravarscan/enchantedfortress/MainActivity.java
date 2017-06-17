package hr.kravarscan.enchantedfortress;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import hr.kravarscan.enchantedfortress.logic.Game;
import hr.kravarscan.enchantedfortress.storage.SaveLoad;

public class MainActivity extends FragmentActivity implements MainMenuFragment.OnFragmentInteractionListener, GameFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportFragmentManager().getFragments() == null) {
            MainMenuFragment mainMenu = new MainMenuFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mainMenu).commit();
        }
    }

    private void switchMainView(Fragment fragment)
    {
        boolean isMain = fragment instanceof MainMenuFragment;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        for (int i = 0; isMain && i < manager.getBackStackEntryCount(); i++)
            manager.popBackStack();

        transaction.replace(R.id.fragment_container, fragment);
        if (!isMain)
            transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onContinue() {
        GameFragment gameFragment = new GameFragment();
        Game game = new Game();
        SaveLoad.get().deserialize(game, SaveLoad.get().load(this));
        gameFragment.setGame(game);

        this.switchMainView(gameFragment);
    }

    @Override
    public void onNewGame() {
        GameFragment gameFragment = new GameFragment();

        this.switchMainView(gameFragment);
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
