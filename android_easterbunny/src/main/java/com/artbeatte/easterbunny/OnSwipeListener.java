package com.artbeatte.easterbunny;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by art.beatte on 7/11/14.
 */
public abstract class OnSwipeListener implements View.OnTouchListener {

    public enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN,
        NONE,
        INCONSISTENT;
    }

    private MotionEvent mPrevious;
    private int mTouchSlop;
    private Direction mDirection;

    public OnSwipeListener(View view) {
        ViewConfiguration vc = ViewConfiguration.get(view.getContext());
        mTouchSlop = vc.getScaledTouchSlop();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean right, left, up, down;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevious = event;
                mDirection = Direction.NONE;

                break;
            case MotionEvent.ACTION_MOVE:
                right = isRight(mPrevious, event);
                left = isLeft(mPrevious, event);
                up = isUp(mPrevious, event);
                down = isDown(mPrevious, event);

                Direction direction = Direction.NONE;
                if (right && !up && !down) {
                    direction = Direction.RIGHT;
                } else if (left && !up && !down) {
                    direction = Direction.LEFT;
                } else if (up && !right && !left) {
                    direction = Direction.UP;
                } else if (down && !right && !left) {
                    direction = Direction.DOWN;
                }

                if (mDirection == Direction.NONE) {
                    if (direction != Direction.NONE) mDirection = direction;
                } else if (mDirection == Direction.INCONSISTENT) {
                    /* no op */
                } else if (mDirection != direction) {
                    mDirection = Direction.INCONSISTENT;
                }

                break;
            case MotionEvent.ACTION_UP:
                right = isRight(mPrevious, event);
                left = isLeft(mPrevious, event);
                up = isUp(mPrevious, event);
                down = isDown(mPrevious, event);

                if (right && !up && !down && mDirection == Direction.RIGHT) {
                    onSwipe(Direction.RIGHT);
                } else if (left && !up && !down && mDirection == Direction.LEFT) {
                    onSwipe(Direction.LEFT);
                } else if (up && !right && !left && mDirection == Direction.UP) {
                    onSwipe(Direction.UP);
                } else if (down && !right && !left && mDirection == Direction.DOWN) {
                    onSwipe(Direction.DOWN);
                }
                mPrevious = null;

                break;
        }

        return false;
    }

    private boolean isRight(MotionEvent e1, MotionEvent e2) {
        float delta = e1.getX() - e2.getX();
        return Math.abs(delta) > mTouchSlop && delta > 0;
    }

    private boolean isLeft(MotionEvent e1, MotionEvent e2) {
        float delta = e1.getX() - e2.getX();
        return Math.abs(delta) > mTouchSlop && delta < 0;
    }

    private boolean isUp(MotionEvent e1, MotionEvent e2) {
        float delta = e1.getY() - e2.getY();
        return Math.abs(delta) > mTouchSlop && delta > 0;
    }

    private boolean isDown(MotionEvent e1, MotionEvent e2) {
        float delta = e1.getY() - e2.getY();
        return Math.abs(delta) > mTouchSlop && delta < 0;
    }

    public abstract void onSwipe(Direction direction);
}
