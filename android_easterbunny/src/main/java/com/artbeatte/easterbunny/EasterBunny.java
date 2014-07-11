package com.artbeatte.easterbunny;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by art.beatte on 7/10/14.
 */
public class EasterBunny implements View.OnTouchListener {

    /**
     * Listener used to signal when unlock sequences have been performed correctly or not.
     */
    public interface UnlockListener {
        /**
         * Called when an unlock sequence was performed correctly.
         */
        public void unlock();

        /**
         * Called when an unlock sequence was performed incorrectly.
         */
        public void unlockFailed();
    }

    /**
     * Creator of an {@link com.artbeatte.easterbunny.EasterBunny}.
     * @param activity The {@link android.app.Activity} to listen for {@link com.artbeatte.easterbunny.UnlockGesture}s
     * @return a new {@link com.artbeatte.easterbunny.EasterBunny} instance
     */
    public static EasterBunny Create(Activity activity) {
        return new EasterBunny(activity);
    }

    Activity mActivity;
    private UnlockListener mUnlockListener;
    private List<UnlockGesture> mCombination;
    MotionEvent mPreviousTouch;

    private EasterBunny(Activity activity) {
        mActivity = activity;
        mCombination = new ArrayList<UnlockGesture>();
        init();
    }

    private void init() {
        // TODO: create the FrameLayout for listening to swipes and displaying the "controller".
//        inflate(mActivity, R.layout.easterbunny, this);


        // handles everything below the AB
//        Window w = mActivity.getWindow();
//        w.addContentView(this, new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_APPLICATION,
//                0,
//                PixelFormat.OPAQUE));
    }

    /**
     * Sets the {@link com.artbeatte.easterbunny.EasterBunny.UnlockListener}.
     * @param unlockListener the {@link com.artbeatte.easterbunny.EasterBunny.UnlockListener} to be called on unlock attempts.
     */
    public void setUnlockListener(UnlockListener unlockListener) {
        mUnlockListener = unlockListener;
    }

    /**
     * Clears any combination steps previously set to this {@link com.artbeatte.easterbunny.EasterBunny}
     * @return the {@link com.artbeatte.easterbunny.EasterBunny} instance.
     */
    public EasterBunny clearCombination() {
        mCombination = new ArrayList<UnlockGesture>();
        return this;
    }

    /**
     * Adds the {@link com.artbeatte.easterbunny.UnlockGesture} to the current list of steps needed to unlock this {@link com.artbeatte.easterbunny.EasterBunny}
     * @param unlockGesture the {@link com.artbeatte.easterbunny.UnlockGesture} to add
     * @return the {@link com.artbeatte.easterbunny.EasterBunny} instance.
     */
    public EasterBunny addStep(UnlockGesture unlockGesture) {
        mCombination.add(unlockGesture);
        return this;
    }

    /**
     * Locks the {@link com.artbeatte.easterbunny.EasterBunny} with a pre-configured lock pattern
     * @param unlockPattern the {@link com.artbeatte.easterbunny.UnlockPattern} to be used.
     * @return the {@link com.artbeatte.easterbunny.EasterBunny} instance locked with the provided pattern.
     */
    public EasterBunny setPattern(UnlockPattern unlockPattern) {
        switch (unlockPattern) {
            case KONAMI_CODE:
                clearCombination()
                        .addStep(UnlockGesture.SWIPE_UP)
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
                break;
            default:

                break;
        }
        return this;
    }

    /**
     * Tells if the Button Controller is currently displayed on the screen.
     * @return true if displayed
     */
    public boolean isControllerVisible() {

        return false;
    }

    private void hideController() {

    }

    private void showController() {

    }

    /**
     * Locks the {@link com.artbeatte.easterbunny.EasterBunny} with the currently build sequence of {@link com.artbeatte.easterbunny.UnlockGesture}s
     * @return the {@link com.artbeatte.easterbunny.EasterBunny} instance.
     */
    public EasterBunny lock() {
        // TODO: register listeners to signal when unlocked or needing controller.
        return this;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mPreviousTouch = event;
                break;
            }
            case MotionEvent.ACTION_UP: {
                float currentX = event.getX();
                if (mPreviousTouch.getX() < currentX) {
                    // swiped left
                }
                if (mPreviousTouch.getX() > currentX ) {
                    //swiped right
                }
                break;
            }
        }
        return false;
    }
}
