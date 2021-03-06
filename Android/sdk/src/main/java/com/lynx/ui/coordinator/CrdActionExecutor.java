// Copyright 2017 The Lynx Authors. All rights reserved.
package com.lynx.ui.coordinator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.lynx.core.base.LynxEvent;
import com.lynx.ui.LynxUI;

import java.util.ArrayList;
import java.util.List;

public class CrdActionExecutor {
    private LynxUI mUI;

    public CrdActionExecutor(LynxUI ui) {
        mUI = ui;
    }

    private void dispatchEvent(CrdResult result) {
        if (!TextUtils.isEmpty(result.getEvent())) {
            String eventName = result.getEvent().toLowerCase();
            LynxEvent event = new LynxEvent(eventName);
            event.set("detail", result.getParamsForEvent());
            mUI.postEvent(eventName, event);
        }
    }

    public void executeAnim(final CrdResult result) {
        View view = mUI.getView();
        long duration = result.getDuration();

        Interpolator interpolator = null;
        if (result.getInterpolatorType() != CrdResult.NOT_SET) {
            switch (result.getInterpolatorType()) {
                case 0://LINEAR
                    interpolator = new LinearInterpolator(); break;
                case 1://EASE_IN
                    interpolator = new AccelerateInterpolator(); break;
                case 2://EASE_OUT
                    interpolator = new DecelerateInterpolator(); break;
                case 3://EASE
                case 4://EASE_IN_OUT
                    interpolator = new AccelerateDecelerateInterpolator(); break;
                default: break;
            }
        }

        List<PropertyAnimHolder> holders = new ArrayList<>();

        if (result.getScaleX() != CrdResult.NOT_SET) {
            holders.add(new PropertyAnimHolder(view, view.getScaleX(), result.getScaleX()) {
                @Override
                void updateValue(View view, float value) {
                    view.setScaleX(value);
                }
            });
        }
        if (result.getScaleY() != CrdResult.NOT_SET) {
            holders.add(new PropertyAnimHolder(view, view.getScaleY(), result.getScaleY()) {
                @Override
                void updateValue(View view, float value) {
                    view.setScaleY(value);
                }
            });
        }
        if (result.getTranslateX() != CrdResult.NOT_SET) {
            holders.add(new PropertyAnimHolder(view, view.getTranslationX(), result.getTranslateX()) {
                @Override
                void updateValue(View view, float value) {
                    view.setTranslationX(value);
                }
            });
        }
        if (result.getRotationX() != CrdResult.NOT_SET) {
            holders.add(new PropertyAnimHolder(view, view.getRotationX(), result.getRotationX()) {
                @Override
                void updateValue(View view, float value) {
                    view.setRotationX(value);
                }
            });
        }
        if (result.getRotationY() != CrdResult.NOT_SET) {
            holders.add(new PropertyAnimHolder(view, view.getRotationY(), result.getRotationY()) {
                @Override
                void updateValue(View view, float value) {
                    view.setRotationY(value);
                }
            });
        }
        if (result.getTranslateY() != CrdResult.NOT_SET) {
            holders.add(new PropertyAnimHolder(view, view.getTranslationY(), result.getTranslateY()) {
                @Override
                void updateValue(View view, float value) {
                    view.setTranslationY(value);
                }
            });
        }
        if (result.getTopOffset() != CrdResult.NOT_SET) {
            holders.add(new PropertyAnimHolder(view, mUI.getOffsetTop(), result.getTopOffset()) {
                @Override
                void updateValue(View view, float value) {
                    mUI.setOffsetTop((int) value);
                }
            });
        }
        if (result.getBottomOffset() != CrdResult.NOT_SET) {
            holders.add(new PropertyAnimHolder(view, mUI.getOffsetBottom(), result.getBottomOffset()) {
                @Override
                void updateValue(View view, float value) {
                    mUI.setOffsetBottom((int) value);
                }
            });
        }
        if (result.getLeftOffset() != CrdResult.NOT_SET) {
            holders.add(new PropertyAnimHolder(view, mUI.getOffsetLeft(), result.getLeftOffset()) {
                @Override
                void updateValue(View view, float value) {
                    mUI.setOffsetLeft((int) value);
                }
            });
        }
        if (result.getRightOffset() != CrdResult.NOT_SET) {
            holders.add(new PropertyAnimHolder(view, mUI.getOffsetRight(), result.getRightOffset()) {
                @Override
                void updateValue(View view, float value) {
                    mUI.setOffsetRight((int) value);
                }
            });
        }
        if (result.getAlpha() != CrdResult.NOT_SET) {
            holders.add(new PropertyAnimHolder(view, view.getAlpha(), result.getAlpha()) {
                @Override
                void updateValue(View view, float value) {
                    view.setAlpha(value);
                }
            });
        }

        if (!holders.isEmpty() || result.getEvent() != null) {
            ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
            animator.setDuration(duration);
            animator.setInterpolator(interpolator);
            animator.addUpdateListener(new PropertyAnimListener(holders));
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    dispatchEvent(result);

                    if (result.getTopOffset() != CrdResult.NOT_SET ||
                        result.getBottomOffset() != CrdResult.NOT_SET ||
                        result.getRightOffset() != CrdResult.NOT_SET ||
                        result.getLeftOffset() != CrdResult.NOT_SET) {
                        mUI.updateFrame();
                    }
                }
            });
            animator.start();
        }
    }

    public void execute(CrdResult result) {
        if (result == null) return;
        View view = mUI.getView();
        if (view == null) return;
        if (result.getDuration() != CrdResult.NOT_SET) {
            executeAnim(result);
            return;
        }
        if (result.getScaleX() != CrdResult.NOT_SET) {
            view.setScaleX(result.getScaleX());
        }
        if (result.getScaleY() != CrdResult.NOT_SET) {
            view.setScaleY(result.getScaleY());
        }
        if (result.getTranslateX() != CrdResult.NOT_SET) {
            view.setTranslationX(result.getTranslateX());
        }
        if (result.getTranslateY() != CrdResult.NOT_SET) {
            view.setTranslationY(result.getTranslateY());
        }
        if (result.getRotationX() != CrdResult.NOT_SET) {
            view.setRotationX(result.getRotationX());
        }
        if (result.getRotationY() != CrdResult.NOT_SET) {
            view.setRotationY(result.getRotationY());
        }
        if (result.getPivotX() != CrdResult.NOT_SET) {
            view.setPivotX(result.getPivotX());
        }
        if (result.getPivotY() != CrdResult.NOT_SET) {
            view.setPivotY(result.getPivotY());
        }
        if (result.getAlpha() != CrdResult.NOT_SET) {
            view.setAlpha(result.getAlpha());
        }
        boolean isLayoutOffsetUpdated = false;
        if (result.getTopOffset() != CrdResult.NOT_SET) {
            mUI.setOffsetTop((int) result.getTopOffset());
            isLayoutOffsetUpdated = true;
        }
        if (result.getBottomOffset() != CrdResult.NOT_SET) {
            mUI.setOffsetBottom((int) result.getBottomOffset());
            isLayoutOffsetUpdated = true;
        }
        if (result.getRightOffset() != CrdResult.NOT_SET) {
            mUI.setOffsetRight((int) result.getRightOffset());
            isLayoutOffsetUpdated = true;
        }
        if (result.getLeftOffset() != CrdResult.NOT_SET) {
            mUI.setOffsetLeft((int) result.getLeftOffset());
            isLayoutOffsetUpdated = true;
        }
        if (isLayoutOffsetUpdated) {
            mUI.updateFrame();
        }
        dispatchEvent(result);
    }


    private static abstract class PropertyAnimHolder {
        private View mView;
        private float mStart;
        private float mEnd;

        public PropertyAnimHolder(View view, float start, float end) {
            mView = view;
            mStart = start;
            mEnd = end;
        }

        void updateValue(ValueAnimator animation) {
            float value = (float) animation.getAnimatedValue();
            updateValue(mView, mStart + value * (mEnd - mStart));
        }

        abstract void updateValue(View view, float value);

    }

    private static class PropertyAnimListener implements ValueAnimator.AnimatorUpdateListener {
        private List<PropertyAnimHolder> mHolders;

        public PropertyAnimListener(List<PropertyAnimHolder> holders) {
            mHolders = holders;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            for (PropertyAnimHolder holder : mHolders) {
                holder.updateValue(animation);
            }
        }
    }
}
