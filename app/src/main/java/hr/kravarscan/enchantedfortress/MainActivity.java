package hr.kravarscan.enchantedfortress;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.ViewGroup;

import hr.kravarscan.enchantedfortress.logic.Game;
import hr.kravarscan.enchantedfortress.storage.SaveLoad;

public class MainActivity extends Activity implements MainMenuFragment.OnFragmentInteractionListener, GameFragment.OnFragmentInteractionListener {

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
        boolean isMain = fragment instanceof MainMenuFragment;
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        fragment.attach(this);

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
