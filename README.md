<!-- 
EasterBunny
=========== 
-->

<img src="https://github.com/abeatte/Android-EasterBunny/raw/master/Icon.png" height="200" />

A Library for laying Easter Eggs.

Try out the <a href="https://play.google.com/store/apps/details?id=com.artbeatte.easterbunnyapp">sample</a> in the Play Store.

Simply add this into your code:

    mEasterBunny = new EasterBunny(getActivity())
                    .clearCombination()
                    .addStep(UnlockGesture.SWIPE_UP)
                    .addStep(UnlockGesture.SWIPE_DOWN)
                    .addStep(UnlockGesture.BUTTON_A)
                    .addStep(UnlockGesture.SWIPE_RIGHT)
                    .lock();
    mEasterBunny.setUnlockListener(new EasterBunny.UnlockListener() {
                @Override
                public void unlock() {
                    Toast.makeText(getActivity(), getString(R.string.unlocked), Toast.LENGTH_LONG).show();
                }
                @Override
                public void unlockFailed() {
                    Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                    // TODO: keep count and provide hint every 5 fails.
                }
            };);

<img src="https://github.com/abeatte/Android-EasterBunny/raw/master/nes_controller.png" height="800" />

