package com.artbeatte.easterbunnyapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

        private EasterBunny mEasterBunny;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            rootView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), android.R.string.ok, Toast.LENGTH_LONG).show();
                }
            });

            return rootView;
        }

        @Override
        public void onStart() {
            super.onStart();

            mEasterBunny = new EasterBunny(getActivity())
                    .clearCombination()
                    .addStep(UnlockGesture.SWIPE_UP)
                    .addStep(UnlockGesture.BUTTON_B)
                    .addStep(UnlockGesture.SWIPE_UP)
                    .addStep(UnlockGesture.BUTTON_B)
                    .addStep(UnlockGesture.BUTTON_B)
                    .addStep(UnlockGesture.SWIPE_RIGHT)
                    .addStep(UnlockGesture.SWIPE_DOWN)
                    .addStep(UnlockGesture.BUTTON_A)
                    .lock();

//            EasterBunny.Create(getActivity()).setPattern(UnlockPattern.KONAMI_CODE);

            mEasterBunny.setUnlockListener(new EasterBunny.UnlockListener() {
                @Override
                public void unlock() {
                    Toast.makeText(getActivity(), "EasterEgg Unlocked!!!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void unlockFailed() {
                    // TODO: keep count and provide hint every 5 fails.
                }
            });
        }

        @Override
        public void onStop() {
            super.onStop();
            mEasterBunny.stop();
        }
    }
}
