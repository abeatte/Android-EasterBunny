package com.artbeatte.easterbunny;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

    Activity mActivity;
    private UnlockListener mUnlockListener;
    private List<UnlockGesture> mCombination;
    private int mCombinationStep;
    private FrameLayout mTouchListeningView;
    private OnSwipeListener mSwipeListener;

    /**
     * Creates a {@link com.artbeatte.easterbunny.EasterBunny}.
     * @param activity The {@link android.app.Activity} to listen for {@link com.artbeatte.easterbunny.UnlockGesture}s
     */
    public EasterBunny(Activity activity) {
        mActivity = activity;
        mCombination = new ArrayList<UnlockGesture>();
        mCombinationStep = -1;
    }

    /**
     * Creates a {@link com.artbeatte.easterbunny.EasterBunny}.
     * @param activity The {@link android.app.Activity} to listen for {@link com.artbeatte.easterbunny.UnlockGesture}s
     * @param unlockListener The {@link com.artbeatte.easterbunny.EasterBunny.UnlockListener} to receive callbacks.
     */
    public EasterBunny(Activity activity, UnlockListener unlockListener) {
        this(activity);
        setUnlockListener(unlockListener);
    }

    private void addController(boolean withDPad) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                0,
                PixelFormat.TRANSPARENT);

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
        mActivity.getWindow().addContentView(controller, params);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) controller.getLayoutParams();
        lp.gravity = Gravity.CENTER;
        controller.setLayoutParams(lp);
        controller.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    private void removeController() {
        View controller = mActivity.getWindow().getDecorView().findViewById(R.id.easterbunny_controller);
        if (controller != null) ((ViewGroup)controller.getParent()).removeView(controller);
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
                        .addStep(UnlockGesture.BUTTON_A);
                break;
            default:

                break;
        }
        return this;
    }

    /**
     * Disables the {@link com.artbeatte.easterbunny.EasterBunny}
     */
    public void stop() {
        if (mTouchListeningView == null) return;

        mTouchListeningView.setOnTouchListener(null);
        View decorView = mActivity.getWindow().getDecorView();

        // remove view
        ((ViewGroup)decorView).removeView(mTouchListeningView);
        for (int i = 0; i < mTouchListeningView.getChildCount(); i++) {
            View child = mTouchListeningView.getChildAt(i);
            mTouchListeningView.removeView(child);
            ((ViewGroup)decorView).addView(child);
        }

        decorView.setOnTouchListener(null);
        removeController();
    }

    /**
     * Tells if the Button Controller is currently displayed on the screen.
     * @return true if displayed
     */
    public boolean isControllerVisible() {
        return mActivity.getWindow().getDecorView().findViewById(R.id.easterbunny_controller) != null;
    }

    private void unlock() {
        mCombinationStep = -1;
        stop();
        if (mUnlockListener != null) mUnlockListener.unlock();
    }

    private void unlockFailed() {
        mCombinationStep = 0;
        if (isButtonGesture(mCombination.get(mCombinationStep))) addController(false);
        else removeController();
        if (mUnlockListener != null) mUnlockListener.unlockFailed();
    }

    /**
     * Locks the {@link com.artbeatte.easterbunny.EasterBunny} with the currently build sequence of {@link com.artbeatte.easterbunny.UnlockGesture}s
     * @return the {@link com.artbeatte.easterbunny.EasterBunny} instance.
     */
    public EasterBunny lock() {
        mCombinationStep = 0;

        View decorView = mActivity.getWindow().getDecorView();

        // add listeningView
        mTouchListeningView = new FrameLayout(mActivity) {
            @Override
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                if (!isControllerVisible()) {
                    mSwipeListener.onTouch(null, ev);
                }
                return false;
            }

            @Override
            public boolean onTouchEvent(MotionEvent event) {
                return false;
            }
        };
        mSwipeListener = new OnSwipeListener(mTouchListeningView) {
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
                    case NONE:
                        /* ignore clicks */
                        break;
                    case INCONSISTENT:
                        unlockFailed();
                }

                processGesture(unlockGesture);
            }
        };
        decorView.setOnTouchListener(mSwipeListener);

        ViewGroup.LayoutParams lp = decorView.getLayoutParams();
        int childCount = ((ViewGroup)decorView).getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = ((ViewGroup)decorView).getChildAt(i);
            ((ViewGroup) decorView).removeView(child);
            mTouchListeningView.addView(child, child.getLayoutParams());
        }
        ((ViewGroup) decorView).addView(mTouchListeningView, lp);
        if (isButtonGesture(mCombination.get(mCombinationStep))) addController(false);

        return this;
    }

    private void processGesture(UnlockGesture unlockGesture) {
        if (unlockGesture == null) return;

        if (isButtonGesture(unlockGesture)) removeController();

        if (mCombination.get(mCombinationStep) == unlockGesture) {
            mCombinationStep ++;
            if (mCombinationStep == mCombination.size()) {
                unlock();
                return;
            }

            UnlockGesture step = mCombination.get(mCombinationStep);
            if (isButtonGesture(step)) {
                addController(false);
            }
        } else {
            unlockFailed();
        }
    }

    private boolean isButtonGesture(UnlockGesture unlockGesture) {
        return unlockGesture == UnlockGesture.BUTTON_A || unlockGesture == UnlockGesture.BUTTON_B;
    }
}
