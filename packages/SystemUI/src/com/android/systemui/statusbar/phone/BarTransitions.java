/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.statusbar.phone;

<<<<<<< HEAD
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.ActivityManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.view.View;
=======
import android.animation.TimeInterpolator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1

import com.android.systemui.R;

public class BarTransitions {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_COLORS = false;

    public static final int MODE_OPAQUE = 0;
    public static final int MODE_SEMI_TRANSPARENT = 1;
    public static final int MODE_TRANSLUCENT = 2;
    public static final int MODE_LIGHTS_OUT = 3;

    public static final int LIGHTS_IN_DURATION = 250;
    public static final int LIGHTS_OUT_DURATION = 750;
    public static final int BACKGROUND_DURATION = 200;

    private final String mTag;
    private final View mView;
    private final boolean mSupportsTransitions = ActivityManager.isHighEndGfx();
<<<<<<< HEAD

    private final int mOpaque;
    private final int mSemiTransparent;

    private int mMode;
    private ValueAnimator mColorDrawableAnimator;
    private boolean mColorDrawableShowing;

    private final ColorDrawable mColorDrawable;
    private final TransitionDrawable mTransitionDrawable;
    private final AnimatorUpdateListener mAnimatorListener = new AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animator) {
            mColorDrawable.setColor((Integer) animator.getAnimatedValue());
        }
    };
=======
    private final BarBackgroundDrawable mBarBackground;

    private int mMode;
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1

    public BarTransitions(View view, int gradientResourceId) {
        mTag = "BarTransitions." + view.getClass().getSimpleName();
        mView = view;
<<<<<<< HEAD
        final Resources res = mView.getContext().getResources();

        if (DEBUG_COLORS) {
            mOpaque = 0xff0000ff;
            mSemiTransparent = 0x7f0000ff;
        } else {
            mOpaque = res.getColor(R.color.system_bar_background_opaque);
            mSemiTransparent = res.getColor(R.color.system_bar_background_semi_transparent);
        }

        mColorDrawable = new ColorDrawable(mOpaque);
        mTransitionDrawable = new TransitionDrawable(
                new Drawable[] { res.getDrawable(gradientResourceId), mColorDrawable });
        mTransitionDrawable.setCrossFadeEnabled(true);
        mTransitionDrawable.resetTransition();
        if (mSupportsTransitions) {
            mView.setBackground(mTransitionDrawable);
=======
        mBarBackground = new BarBackgroundDrawable(mView.getContext(), gradientResourceId);
        if (mSupportsTransitions) {
            mView.setBackground(mBarBackground);
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
        }
    }

    public int getMode() {
        return mMode;
    }

    public void transitionTo(int mode, boolean animate) {
        if (mMode == mode) return;
        int oldMode = mMode;
        mMode = mode;
        if (DEBUG) Log.d(mTag, String.format("%s -> %s animate=%s",
                modeToString(oldMode), modeToString(mode),  animate));
        if (mSupportsTransitions) {
            onTransition(oldMode, mMode, animate);
        }
    }

<<<<<<< HEAD
    private Integer getBackgroundColor(int mode) {
        if (mode == MODE_SEMI_TRANSPARENT) return mSemiTransparent;
        if (mode == MODE_OPAQUE) return mOpaque;
        if (mode == MODE_LIGHTS_OUT) return mOpaque;
        return null;
    }

=======
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
    protected void onTransition(int oldMode, int newMode, boolean animate) {
        applyModeBackground(oldMode, newMode, animate);
    }

    protected void applyModeBackground(int oldMode, int newMode, boolean animate) {
<<<<<<< HEAD
        if (DEBUG) Log.d(mTag, String.format("applyModeBackground %s animate=%s",
                modeToString(newMode), animate));
        cancelColorAnimation();
        Integer oldColor = getBackgroundColor(oldMode);
        Integer newColor = getBackgroundColor(newMode);
        if (newColor != null) {
            if (animate && oldColor != null && !oldColor.equals(newColor)) {
                startColorAnimation(oldColor, newColor);
            } else if (!newColor.equals(mColorDrawable.getColor())) {
                if (DEBUG) Log.d(mTag, String.format("setColor = %08x", newColor));
                mColorDrawable.setColor(newColor);
            }
        }
        if (newColor == null && mColorDrawableShowing) {
            if (DEBUG) Log.d(mTag, "Hide color layer");
            if (animate) {
                mTransitionDrawable.reverseTransition(BACKGROUND_DURATION);
            } else {
                mTransitionDrawable.resetTransition();
            }
            mColorDrawableShowing = false;
        } else if (newColor != null && !mColorDrawableShowing) {
            if (DEBUG) Log.d(mTag, "Show color layer");
            mTransitionDrawable.startTransition(animate ? BACKGROUND_DURATION : 0);
            mColorDrawableShowing = true;
        }
    }

    private void startColorAnimation(int from, int to) {
        if (DEBUG) Log.d(mTag, String.format("startColorAnimation %08x -> %08x", from, to));
        mColorDrawableAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), from, to);
        mColorDrawableAnimator.addUpdateListener(mAnimatorListener);
        mColorDrawableAnimator.start();
    }

    private void cancelColorAnimation() {
        if (mColorDrawableAnimator != null && mColorDrawableAnimator.isStarted()) {
            mColorDrawableAnimator.cancel();
            mColorDrawableAnimator = null;
        }
    }

    public static String modeToString(int mode) {
        if (mode == MODE_OPAQUE) return "MODE_OPAQUE";
        if (mode == MODE_SEMI_TRANSPARENT) return "MODE_SEMI_TRANSPARENT";
        if (mode == MODE_TRANSLUCENT) return "MODE_TRANSLUCENT";
        if (mode == MODE_LIGHTS_OUT) return "MODE_LIGHTS_OUT";
        throw new IllegalArgumentException("Unknown mode " + mode);
=======
        if (DEBUG) Log.d(mTag, String.format("applyModeBackground oldMode=%s newMode=%s animate=%s",
                modeToString(oldMode), modeToString(newMode), animate));
        mBarBackground.applyModeBackground(oldMode, newMode, animate);
    }

    public static String modeToString(int mode) {
        if (mode == MODE_OPAQUE) return "MODE_OPAQUE";
        if (mode == MODE_SEMI_TRANSPARENT) return "MODE_SEMI_TRANSPARENT";
        if (mode == MODE_TRANSLUCENT) return "MODE_TRANSLUCENT";
        if (mode == MODE_LIGHTS_OUT) return "MODE_LIGHTS_OUT";
        throw new IllegalArgumentException("Unknown mode " + mode);
    }

    public void finishAnimations() {
        mBarBackground.finishAnimation();
    }

    public void setContentVisible(boolean visible) {
        // for subclasses
    }

    private static class BarBackgroundDrawable extends Drawable {
        private final int mOpaque;
        private final int mSemiTransparent;
        private final Drawable mGradient;
        private final TimeInterpolator mInterpolator;

        private int mMode = -1;
        private boolean mAnimating;
        private long mStartTime;
        private long mEndTime;

        private int mGradientAlpha;
        private int mColor;

        private int mGradientAlphaStart;
        private int mColorStart;

        public BarBackgroundDrawable(Context context, int gradientResourceId) {
            final Resources res = context.getResources();
            if (DEBUG_COLORS) {
                mOpaque = 0xff0000ff;
                mSemiTransparent = 0x7f0000ff;
            } else {
                mOpaque = res.getColor(R.color.system_bar_background_opaque);
                mSemiTransparent = res.getColor(R.color.system_bar_background_semi_transparent);
            }
            mGradient = res.getDrawable(gradientResourceId);
            mInterpolator = new LinearInterpolator();
        }

        @Override
        public void setAlpha(int alpha) {
            // noop
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            // noop
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            mGradient.setBounds(bounds);
        }

        public void applyModeBackground(int oldMode, int newMode, boolean animate) {
            if (mMode == newMode) return;
            mMode = newMode;
            mAnimating = animate;
            if (animate) {
                long now = SystemClock.elapsedRealtime();
                mStartTime = now;
                mEndTime = now + BACKGROUND_DURATION;
                mGradientAlphaStart = mGradientAlpha;
                mColorStart = mColor;
            }
            invalidateSelf();
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        public void finishAnimation() {
            if (mAnimating) {
                mAnimating = false;
                invalidateSelf();
            }
        }

        @Override
        public void draw(Canvas canvas) {
            int targetGradientAlpha = 0, targetColor = 0;
            if (mMode == MODE_TRANSLUCENT) {
                targetGradientAlpha = 0xff;
            } else if (mMode == MODE_SEMI_TRANSPARENT) {
                targetColor = mSemiTransparent;
            } else {
                targetColor = mOpaque;
            }
            if (!mAnimating) {
                mColor = targetColor;
                mGradientAlpha = targetGradientAlpha;
            } else {
                final long now = SystemClock.elapsedRealtime();
                if (now >= mEndTime) {
                    mAnimating = false;
                    mColor = targetColor;
                    mGradientAlpha = targetGradientAlpha;
                } else {
                    final float t = (now - mStartTime) / (float)(mEndTime - mStartTime);
                    final float v = Math.max(0, Math.min(mInterpolator.getInterpolation(t), 1));
                    mGradientAlpha = (int)(v * targetGradientAlpha + mGradientAlphaStart * (1 - v));
                    mColor = Color.argb(
                          (int)(v * Color.alpha(targetColor) + Color.alpha(mColorStart) * (1 - v)),
                          (int)(v * Color.red(targetColor) + Color.red(mColorStart) * (1 - v)),
                          (int)(v * Color.green(targetColor) + Color.green(mColorStart) * (1 - v)),
                          (int)(v * Color.blue(targetColor) + Color.blue(mColorStart) * (1 - v)));
                }
            }
            if (mGradientAlpha > 0) {
                mGradient.setAlpha(mGradientAlpha);
                mGradient.draw(canvas);
            }
            if (Color.alpha(mColor) > 0) {
                canvas.drawColor(mColor);
            }
            if (mAnimating) {
                invalidateSelf();  // keep going
            }
        }
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
    }
}
