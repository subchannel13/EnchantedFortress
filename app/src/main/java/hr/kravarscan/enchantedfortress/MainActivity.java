package hr.kravarscan.enchantedfortress;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class MainActivity extends FragmentActivity implements MainMenuFragment.OnFragmentInteractionListener, GameFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainMenuFragment mainMenu = new MainMenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mainMenu).commit();
    }

    @Override
    public void onNewGame() {
        GameFragment gameFragment = new GameFragment();
        Bundle args = new Bundle();
        gameFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, gameFragment);
        transaction.commit();
    }

    @Override
    public void onGameOver() {
        Log.i("EF main", "Game over");
    }
}
