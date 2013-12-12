/*
 * Copyright (C) 2008 The Android Open Source Project
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

package com.android.systemui.statusbar.policy;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
<<<<<<< HEAD
import android.graphics.PorterDuff.Mode;
=======
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.hardware.input.InputManager;
import android.os.SystemClock;
<<<<<<< HEAD
import android.os.UserHandle;
import android.provider.Settings;
=======
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
import android.util.AttributeSet;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.InputDevice;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
<<<<<<< HEAD
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;

import com.android.internal.util.slim.ButtonsConstants;
import com.android.internal.util.slim.SlimActions;

=======
import android.view.ViewDebug;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;

>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
import com.android.systemui.R;

public class KeyButtonView extends ImageView {
    private static final String TAG = "StatusBar.KeyButtonView";
    private static final boolean DEBUG = false;

    final float GLOW_MAX_SCALE_FACTOR = 1.8f;
    public static final float DEFAULT_QUIESCENT_ALPHA = 0.70f;

    long mDownTime;
    int mCode;
<<<<<<< HEAD
    String mClickAction;
    String mLongpressAction;
    int mTouchSlop;
    Drawable mGlowBG;
    int mGlowBGColor;
    int mGlowWidth, mGlowHeight;
    float mGlowAlpha = 0f, mGlowScale = 1f, mDrawingAlpha = 1f;
    float mQuiescentAlpha = DEFAULT_QUIESCENT_ALPHA;
    boolean mSupportsLongpress = false;
    boolean mIsLongpressed = false;
    RectF mRect = new RectF(0f,0f,0f,0f);
=======
    int mTouchSlop;
    Drawable mGlowBG;
    int mGlowWidth, mGlowHeight;
    float mGlowAlpha = 0f, mGlowScale = 1f;
    @ViewDebug.ExportedProperty(category = "drawing")
    float mDrawingAlpha = 1f;
    @ViewDebug.ExportedProperty(category = "drawing")
    float mQuiescentAlpha = DEFAULT_QUIESCENT_ALPHA;
    boolean mSupportsLongpress = true;
    RectF mRect = new RectF();
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
    AnimatorSet mPressedAnim;
    Animator mAnimateToQuiescent = new ObjectAnimator();

    Runnable mCheckLongPress = new Runnable() {
        public void run() {
<<<<<<< HEAD
            mIsLongpressed = true;
            if (isPressed()) {
                sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_LONG_CLICKED);
                if (mLongpressAction != null
                    && SlimActions.isActionKeyEvent(mLongpressAction)) {
                    setHapticFeedbackEnabled(false);
                }
                performLongClick();
                setHapticFeedbackEnabled(true);
=======
            if (isPressed()) {
                // Log.d("KeyButtonView", "longpressed: " + this);
                if (mCode != 0) {
                    sendEvent(KeyEvent.ACTION_DOWN, KeyEvent.FLAG_LONG_PRESS);
                    sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_LONG_CLICKED);
                } else {
                    // Just an old-fashioned ImageView
                    performLongClick();
                }
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
            }
        }
    };

    public KeyButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KeyButtonView,
                defStyle, 0);

        mCode = a.getInteger(R.styleable.KeyButtonView_keyCode, 0);

        mSupportsLongpress = a.getBoolean(R.styleable.KeyButtonView_keyRepeat, true);

        mGlowBG = a.getDrawable(R.styleable.KeyButtonView_glowBackground);
<<<<<<< HEAD
        if (mGlowBG != null) {
            setDrawingAlpha(mQuiescentAlpha);
=======
        setDrawingAlpha(mQuiescentAlpha);
        if (mGlowBG != null) {
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
            mGlowWidth = mGlowBG.getIntrinsicWidth();
            mGlowHeight = mGlowBG.getIntrinsicHeight();
        }

        a.recycle();

        setClickable(true);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

<<<<<<< HEAD
    public void setCode(int code) {
        mCode = code;
    }

    public void setClickAction(String action) {
        mClickAction = action;
        setOnClickListener(mClickListener);
    }

    public void setLongpressAction(String action) {
        mLongpressAction = action;
        if (!action.equals(ButtonsConstants.ACTION_NULL)) {
            mSupportsLongpress = true;
            setOnLongClickListener(mLongPressListener);
        }
    }

    public void setGlowBackground(int id) {
        mGlowBG = getResources().getDrawable(id);
        if (mGlowBG != null) {
            setDrawingAlpha(mQuiescentAlpha);
            mGlowWidth = mGlowBG.getIntrinsicWidth();
            mGlowHeight = mGlowBG.getIntrinsicHeight();
            int defaultColor = mContext.getResources().getColor(
                    R.color.navigationbar_button_glow_default_color);
            mGlowBGColor = Settings.System.getIntForUser(mContext.getContentResolver(),
                    Settings.System.NAVIGATION_BAR_GLOW_TINT,
                    -2, UserHandle.USER_CURRENT);

            if (mGlowBGColor == -2) {
                mGlowBGColor = defaultColor;
            }
            mGlowBG.setColorFilter(null);
            mGlowBG.setColorFilter(mGlowBGColor, Mode.SRC_ATOP);

        }
    }

=======
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
    @Override
    protected void onDraw(Canvas canvas) {
        if (mGlowBG != null) {
            canvas.save();
            final int w = getWidth();
            final int h = getHeight();
            final float aspect = (float)mGlowWidth / mGlowHeight;
            final int drawW = (int)(h*aspect);
            final int drawH = h;
            final int margin = (drawW-w)/2;
            canvas.scale(mGlowScale, mGlowScale, w*0.5f, h*0.5f);
            mGlowBG.setBounds(-margin, 0, drawW-margin, drawH);
            mGlowBG.setAlpha((int)(mDrawingAlpha * mGlowAlpha * 255));
            mGlowBG.draw(canvas);
            canvas.restore();
            mRect.right = w;
            mRect.bottom = h;
        }
        super.onDraw(canvas);
    }

    public void setQuiescentAlpha(float alpha, boolean animate) {
        mAnimateToQuiescent.cancel();
        alpha = Math.min(Math.max(alpha, 0), 1);
<<<<<<< HEAD
        if (alpha == mQuiescentAlpha) return;
        mQuiescentAlpha = alpha;
        if (DEBUG) Log.d(TAG, "New quiescent alpha = " + mQuiescentAlpha);
        if (mGlowBG != null) {
            if (animate) {
                mAnimateToQuiescent = animateToQuiescent();
                mAnimateToQuiescent.start();
            } else {
                setDrawingAlpha(mQuiescentAlpha);
            }
=======
        if (alpha == mQuiescentAlpha && alpha == mDrawingAlpha) return;
        mQuiescentAlpha = alpha;
        if (DEBUG) Log.d(TAG, "New quiescent alpha = " + mQuiescentAlpha);
        if (mGlowBG != null && animate) {
            mAnimateToQuiescent = animateToQuiescent();
            mAnimateToQuiescent.start();
        } else {
            setDrawingAlpha(mQuiescentAlpha);
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
        }
    }

    private ObjectAnimator animateToQuiescent() {
        return ObjectAnimator.ofFloat(this, "drawingAlpha", mQuiescentAlpha);
    }

<<<<<<< HEAD
    public float getDrawingAlpha() {
        if (mGlowBG == null) return 0;
=======
    public float getQuiescentAlpha() {
        return mQuiescentAlpha;
    }

    public float getDrawingAlpha() {
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
        return mDrawingAlpha;
    }

    public void setDrawingAlpha(float x) {
<<<<<<< HEAD
        if (mGlowBG == null) return;
=======
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
        // Calling setAlpha(int), which is an ImageView-specific
        // method that's different from setAlpha(float). This sets
        // the alpha on this ImageView's drawable directly
        setAlpha((int) (x * 255));
        mDrawingAlpha = x;
    }

    public float getGlowAlpha() {
        if (mGlowBG == null) return 0;
        return mGlowAlpha;
    }

    public void setGlowAlpha(float x) {
        if (mGlowBG == null) return;
        mGlowAlpha = x;
        invalidate();
    }

    public float getGlowScale() {
        if (mGlowBG == null) return 0;
        return mGlowScale;
    }

    public void setGlowScale(float x) {
        if (mGlowBG == null) return;
        mGlowScale = x;
        final float w = getWidth();
        final float h = getHeight();
        if (GLOW_MAX_SCALE_FACTOR <= 1.0f) {
            // this only works if we know the glow will never leave our bounds
            invalidate();
        } else {
            final float rx = (w * (GLOW_MAX_SCALE_FACTOR - 1.0f)) / 2.0f + 1.0f;
            final float ry = (h * (GLOW_MAX_SCALE_FACTOR - 1.0f)) / 2.0f + 1.0f;
            com.android.systemui.SwipeHelper.invalidateGlobalRegion(
                    this,
                    new RectF(getLeft() - rx,
                              getTop() - ry,
                              getRight() + rx,
                              getBottom() + ry));

            // also invalidate our immediate parent to help avoid situations where nearby glows
            // interfere
            ((View)getParent()).invalidate();
        }
    }

    public void setPressed(boolean pressed) {
        if (mGlowBG != null) {
            if (pressed != isPressed()) {
                if (mPressedAnim != null && mPressedAnim.isRunning()) {
                    mPressedAnim.cancel();
                }
                final AnimatorSet as = mPressedAnim = new AnimatorSet();
                if (pressed) {
                    if (mGlowScale < GLOW_MAX_SCALE_FACTOR)
                        mGlowScale = GLOW_MAX_SCALE_FACTOR;
                    if (mGlowAlpha < mQuiescentAlpha)
                        mGlowAlpha = mQuiescentAlpha;
                    setDrawingAlpha(1f);
                    as.playTogether(
                        ObjectAnimator.ofFloat(this, "glowAlpha", 1f),
                        ObjectAnimator.ofFloat(this, "glowScale", GLOW_MAX_SCALE_FACTOR)
                    );
                    as.setDuration(50);
                } else {
                    mAnimateToQuiescent.cancel();
                    mAnimateToQuiescent = animateToQuiescent();
                    as.playTogether(
                        ObjectAnimator.ofFloat(this, "glowAlpha", 0f),
                        ObjectAnimator.ofFloat(this, "glowScale", 1f),
                        mAnimateToQuiescent
                    );
                    as.setDuration(500);
                }
                as.start();
            }
        }
        super.setPressed(pressed);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        int x, y;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
<<<<<<< HEAD
                mDownTime = SystemClock.uptimeMillis();
                mIsLongpressed = false;
                setPressed(true);
                if (mCode != 0) {
                    sendEvent(KeyEvent.ACTION_DOWN, 0, mDownTime);
=======
                //Log.d("KeyButtonView", "press");
                mDownTime = SystemClock.uptimeMillis();
                setPressed(true);
                if (mCode != 0) {
                    sendEvent(KeyEvent.ACTION_DOWN, 0, mDownTime);
                } else {
                    // Provide the same haptic feedback that the system offers for virtual keys.
                    performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
                }
                if (mSupportsLongpress) {
                    removeCallbacks(mCheckLongPress);
                    postDelayed(mCheckLongPress, ViewConfiguration.getLongPressTimeout());
                }
                break;
            case MotionEvent.ACTION_MOVE:
                x = (int)ev.getX();
                y = (int)ev.getY();
                setPressed(x >= -mTouchSlop
                        && x < getWidth() + mTouchSlop
                        && y >= -mTouchSlop
                        && y < getHeight() + mTouchSlop);
                break;
            case MotionEvent.ACTION_CANCEL:
                setPressed(false);
                if (mCode != 0) {
                    sendEvent(KeyEvent.ACTION_UP, KeyEvent.FLAG_CANCELED);
                }
                if (mSupportsLongpress) {
                    removeCallbacks(mCheckLongPress);
                }
                break;
            case MotionEvent.ACTION_UP:
                final boolean doIt = isPressed();
                setPressed(false);
<<<<<<< HEAD
                if (!mIsLongpressed) {
                    if (mCode != 0) {
                        if (doIt) {
                            sendEvent(KeyEvent.ACTION_UP, 0);
                        } else {
                            sendEvent(KeyEvent.ACTION_UP, KeyEvent.FLAG_CANCELED);
                        }
                    } else {
                        // no key code, it is a custom click action
                        if (doIt) {
                            if (mClickAction != null
                                && !SlimActions.isActionKeyEvent(mClickAction)) {
                                performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                            }
                            performClick();
                        }
                    }
                    if (doIt) {
                        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
=======
                if (mCode != 0) {
                    if (doIt) {
                        sendEvent(KeyEvent.ACTION_UP, 0);
                        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
                        playSoundEffect(SoundEffectConstants.CLICK);
                    } else {
                        sendEvent(KeyEvent.ACTION_UP, KeyEvent.FLAG_CANCELED);
                    }
                } else {
                    // no key code, just a regular ImageView
                    if (doIt) {
                        performClick();
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
                    }
                }
                if (mSupportsLongpress) {
                    removeCallbacks(mCheckLongPress);
                }
                break;
        }

        return true;
    }

<<<<<<< HEAD
    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            SlimActions.processAction(mContext, mClickAction, false);
            return;
        }
    };

    private OnLongClickListener mLongPressListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            SlimActions.processAction(mContext, mLongpressAction, true);
            return true;
        }
    };

=======
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
    void sendEvent(int action, int flags) {
        sendEvent(action, flags, SystemClock.uptimeMillis());
    }

    void sendEvent(int action, int flags, long when) {
        final int repeatCount = (flags & KeyEvent.FLAG_LONG_PRESS) != 0 ? 1 : 0;
        final KeyEvent ev = new KeyEvent(mDownTime, when, action, mCode, repeatCount,
                0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
                flags | KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY,
                InputDevice.SOURCE_KEYBOARD);
        InputManager.getInstance().injectInputEvent(ev,
                InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);
    }
}


