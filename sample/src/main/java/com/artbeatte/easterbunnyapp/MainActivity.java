package com.artbeatte.easterbunnyapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
                    .add(R.id.container, new MainFragment())
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
     * Shows how to use custom and pattern lock combinations with EasterBunny.
     */
    public static class MainFragment extends Fragment {

        private EasterBunny mEasterBunny;
        private EasterBunny.UnlockListener mUnlockListener;
        private TextView mHint;

        public MainFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            mHint = (TextView) rootView.findViewById(R.id.hint);
            mHint.setText(getString(R.string.combination) + " " + getString(R.string.combo_not_set));

            rootView.findViewById(R.id.custom_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deployCustomEasterBunny();
                }
            });
            rootView.findViewById(R.id.pattern_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deployPatternEasterBunny();
                }
            });

            return rootView;
        }

        private void deployCustomEasterBunny() {
            removeOldEasterBunny();
            mEasterBunny = new EasterBunny(getActivity())
                    .clearCombination()
                    .addStep(UnlockGesture.SWIPE_UP)
                    .addStep(UnlockGesture.SWIPE_DOWN)
                    .addStep(UnlockGesture.BUTTON_A)
                    .addStep(UnlockGesture.SWIPE_RIGHT)
                    .lock();

            mEasterBunny.setUnlockListener(mUnlockListener);

            mHint.setText(getString(R.string.combination) + " " + getString(R.string.custom_instructions));
        }

        private void deployPatternEasterBunny() {
            removeOldEasterBunny();
            mEasterBunny = new EasterBunny(getActivity())
                    .setPattern(UnlockPattern.KONAMI_CODE)
                    .lock();

            mEasterBunny.setUnlockListener(mUnlockListener);

            mHint.setText(getString(R.string.combination) + " " + getString(R.string.konami_instructions));
        }

        private void removeOldEasterBunny() {
            if (mEasterBunny != null) mEasterBunny.stop();
        }

        @Override
        public void onStart() {
            super.onStart();

            mUnlockListener = new EasterBunny.UnlockListener() {
                @Override
                public void unlock() {
                    Toast.makeText(getActivity(), getString(R.string.unlocked), Toast.LENGTH_LONG).show();
                }

                @Override
                public void unlockFailed() {
                    Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                    // TODO: keep count and provide hint every 5 fails.
                }
            };
        }

        @Override
        public void onStop() {
            super.onStop();
            if (mEasterBunny != null) mEasterBunny.stop();
        }
    }
}
