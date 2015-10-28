package org.trypticon.dozenalwatchface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.WindowInsets;

import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Service entry point for the dozenal watch face.
 */
public class DozenalWatchFaceService extends CanvasWatchFaceService {

    // Determined this experimentally, so it can probably still be improved. :(
    private static final long INTERACTIVE_UPDATE_RATE_MS = 20;

    private static final int UPDATE_TIME_MESSAGE = 0;

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private class Engine extends CanvasWatchFaceService.Engine {
        private DozenalWatchFace watchFace;

        private final Handler updateTimeHandler = new HandlerImpl(this);

        private final BroadcastReceiver intentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case Intent.ACTION_LOCALE_CHANGED:
                        watchFace.updateLocale(Locale.forLanguageTag(intent.getStringExtra("locale")));
                        break;

                    case Intent.ACTION_TIMEZONE_CHANGED:
                        watchFace.updateTimeZone(TimeZone.getTimeZone(intent.getStringExtra("time-zone")));
                        break;
                }
            }
        };
        private boolean registeredIntentReceiver = false;

        private boolean lowBitAmbient;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(DozenalWatchFaceService.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .setStatusBarGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                    .setHotwordIndicatorGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                    .build());

            watchFace = new DozenalWatchFace(DozenalWatchFaceService.this);
        }

        @Override
        public void onApplyWindowInsets(WindowInsets insets) {
            super.onApplyWindowInsets(insets);

            //TODO: chin - insets.getSystemWindowInsetBottom()
            watchFace.updateShape(insets.isRound());
        }

        @Override
        public void onDestroy() {
            updateTimeHandler.removeMessages(UPDATE_TIME_MESSAGE);
            super.onDestroy();
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            lowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
            watchFace.updateAmbient(lowBitAmbient, isInAmbientMode());
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            watchFace.updateAmbient(lowBitAmbient, inAmbientMode);
            invalidate();

            maybeStartOrStopTimer();
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            watchFace.setBounds(bounds);
            watchFace.draw(canvas);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerReceiver();

                // Maybe the zone changed while we weren't visible.
                watchFace.updateTimeZone(TimeZone.getDefault());
            } else {
                unregisterReceiver();
            }

            maybeStartOrStopTimer();
        }

        private void registerReceiver() {
            if (!registeredIntentReceiver) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(Intent.ACTION_LOCALE_CHANGED);
                intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
                DozenalWatchFaceService.this.registerReceiver(intentReceiver, intentFilter);
                registeredIntentReceiver = true;
            }
        }

        private void unregisterReceiver() {
            if (registeredIntentReceiver) {
                DozenalWatchFaceService.this.unregisterReceiver(intentReceiver);
                registeredIntentReceiver = false;
            }
        }

        /**
         * Starts the timer if it should be running and isn't currently,
         * or stops it if it shouldn't be running but currently is.
         */
        private void maybeStartOrStopTimer() {
            updateTimeHandler.removeMessages(UPDATE_TIME_MESSAGE);
            if (timerShouldBeRunning()) {
                updateTimeHandler.sendEmptyMessage(UPDATE_TIME_MESSAGE);
            }
        }

        /**
         * Tests whether the timer should be running.
         */
        private boolean timerShouldBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        /**
         * Updates the time periodically in interactive mode.
         */
        private void timeUpdated() {
            invalidate();

            if (timerShouldBeRunning()) {
                long timeMillis = System.currentTimeMillis();
                long delayMillis = INTERACTIVE_UPDATE_RATE_MS
                        - (timeMillis % INTERACTIVE_UPDATE_RATE_MS);
                updateTimeHandler.sendEmptyMessageDelayed(UPDATE_TIME_MESSAGE, delayMillis);
            }
        }
    }

    private static class HandlerImpl extends Handler {
        private final WeakReference<DozenalWatchFaceService.Engine> mWeakReference;

        public HandlerImpl(DozenalWatchFaceService.Engine reference) {
            mWeakReference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            DozenalWatchFaceService.Engine engine = mWeakReference.get();
            if (engine != null) {
                switch (msg.what) {
                    case UPDATE_TIME_MESSAGE:
                        engine.timeUpdated();
                        break;
                }
            }
        }
    }
}
