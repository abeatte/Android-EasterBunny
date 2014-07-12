package com.artbeatte.easterbunnyapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.artbeatte.easterbunny.EasterBunny;
import com.artbeatte.easterbunny.UnlockGesture;
import com.artbeatte.easterbunny.UnlockPattern;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            EasterBunny easterBunny = EasterBunny.Create(getActivity())
                    .clearCombination()
                    .addStep(UnlockGesture.SWIPE_UP)
                    .addStep(UnlockGesture.BUTTON_B)
                    .addStep(UnlockGesture.SWIPE_UP)
                    .addStep(UnlockGesture.SWIPE_DOWN)
                    .addStep(UnlockGesture.SWIPE_DOWN)
                    .addStep(UnlockGesture.SWIPE_LEFT)
                    .addStep(UnlockGesture.SWIPE_RIGHT)
                    .addStep(UnlockGesture.SWIPE_LEFT)
                    .addStep(UnlockGesture.SWIPE_RIGHT)
                    .addStep(UnlockGesture.BUTTON_B)
                    .addStep(UnlockGesture.BUTTON_A)
                    .lock();

//            EasterBunny.Create(getActivity()).setPattern(UnlockPattern.KONAMI_CODE);

            easterBunny.setUnlockListener(new EasterBunny.UnlockListener() {
                @Override
                public void unlock() {
                    // TODO: start easteregg activity.
                }

                @Override
                public void unlockFailed() {
                    // TODO: keep count and hint every 5
                }
            });

            return rootView;
        }
    }
}
