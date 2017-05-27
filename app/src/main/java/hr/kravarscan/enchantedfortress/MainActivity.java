package hr.kravarscan.enchantedfortress;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends FragmentActivity implements MainMenuFragment.OnFragmentInteractionListener, GameFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainMenuFragment mainMenu = new MainMenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mainMenu).commit();
    }

    private void switchMainView(Fragment fragment)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        if (!(fragment instanceof MainMenuFragment))
            transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onNewGame() {
        GameFragment gameFragment = new GameFragment();
        Bundle args = new Bundle();
        gameFragment.setArguments(args);

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
