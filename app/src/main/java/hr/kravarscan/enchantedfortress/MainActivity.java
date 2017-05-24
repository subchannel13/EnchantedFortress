package hr.kravarscan.enchantedfortress;

import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class MainActivity extends FragmentActivity implements MainMenuFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainMenuFragment mainMenu = new MainMenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mainMenu).commit();
    }

    @Override
    public void onNewGame() {
        Log.i("EF main", "new game");
    }
}
