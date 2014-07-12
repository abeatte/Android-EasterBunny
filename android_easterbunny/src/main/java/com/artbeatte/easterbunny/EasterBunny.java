package com.artbeatte.easterbunny;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by art.beatte on 7/10/14.
 */
public class EasterBunny {

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
     * @param unlockListener The {@link com.artbeatte.easterbunny.EasterBunny.UnlockListener} to receive callbacks.
     * @return a new {@link com.artbeatte.easterbunny.EasterBunny} instance
     */
    public static EasterBunny Create(Activity activity, UnlockListener unlockListener) {
        EasterBunny eb =  new EasterBunny(activity);
        eb.setUnlockListener(unlockListener);
        return eb;
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
    private int mCombinationStep;
    MotionEvent mPreviousTouch;
    private FrameLayout mOverlay;

    private EasterBunny(Activity activity) {
        mActivity = activity;
        mCombination = new ArrayList<UnlockGesture>();
        mCombinationStep = -1;
    }

    private void deployOverlay() {
        Window w = mActivity.getWindow();
        mOverlay = new FrameLayout(mActivity);
        mOverlay.setId(R.id.easterbunny_overlay);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);
        w.addContentView(mOverlay, params);
    }

    private void removeOverlay() {
        ((ViewGroup)mOverlay.getParent()).removeView(mOverlay);
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
        return mOverlay.findViewById(R.id.easterbunny_controller) != null;
    }

    private void hideController() {
        mOverlay.removeView(mOverlay.findViewById(R.id.easterbunny_controller));
    }

    private void showController(boolean withDPad) {
        View controller = mActivity.getLayoutInflater().inflate(R.layout.easterbunny_controller, null);
        controller.findViewById(R.id.controller_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processGesture(UnlockGesture.BUTTON_B);
            }
        });
        controller.findViewById(R.id.controller_a).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processGesture(UnlockGesture.BUTTON_A);
            }
        });
        controller.setId(R.id.easterbunny_controller);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_APPLICATION);
        params.gravity = Gravity.CENTER;
        mOverlay.addView(controller, params);
    }

    private void unlock() {
        mCombinationStep = -1;
        mOverlay.setOnTouchListener(null);
        if (mUnlockListener != null) mUnlockListener.unlock();
        removeOverlay();
    }

    public void unlockFailed() {
        mCombinationStep = 0;
        if (mUnlockListener != null) mUnlockListener.unlockFailed();
    }

    /**
     * Locks the {@link com.artbeatte.easterbunny.EasterBunny} with the currently build sequence of {@link com.artbeatte.easterbunny.UnlockGesture}s
     * @return the {@link com.artbeatte.easterbunny.EasterBunny} instance.
     */
    public EasterBunny lock() {
        deployOverlay();
        mCombinationStep = 0;
        mOverlay.setOnTouchListener(new OnSwipeListener(mOverlay) {
            @Override
            public void onSwipe(Direction direction) {
                UnlockGesture unlockGesture = null;
                switch (direction) {
                    case LEFT:
                        unlockGesture = UnlockGesture.SWIPE_LEFT;
                        break;
                    case RIGHT:
                        unlockGesture = UnlockGesture.SWIPE_RIGHT;
                        break;
                    case UP:
                        unlockGesture = UnlockGesture.SWIPE_UP;
                        break;
                    case DOWN:
                        unlockGesture = UnlockGesture.SWIPE_DOWN;
                        break;
                }

                processGesture(unlockGesture);
            }
        });
        if (isButtonGesture(mCombination.get(mCombinationStep))) showController(false);
        return this;
    }

    private void processGesture(UnlockGesture unlockGesture) {
        if (unlockGesture == null) return;

        if (isButtonGesture(unlockGesture)) hideController();

        if (mCombination.get(mCombinationStep) == unlockGesture) {
            mCombinationStep ++;
            if (mCombinationStep == mCombination.size()) {
                unlock();
                return;
            }

            UnlockGesture step = mCombination.get(mCombinationStep);
            if (isButtonGesture(step)) {
                showController(false);
            }
        } else {
            unlockFailed();
        }
    }

    private boolean isButtonGesture(UnlockGesture unlockGesture) {
        return unlockGesture == UnlockGesture.BUTTON_A || unlockGesture == UnlockGesture.BUTTON_B;
    }
}
