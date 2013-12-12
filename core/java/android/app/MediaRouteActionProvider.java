/*
 * Copyright (C) 2012 The Android Open Source Project
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

package android.app;

<<<<<<< HEAD
import com.android.internal.app.MediaRouteChooserDialogFragment;

import android.content.Context;
import android.content.ContextWrapper;
=======
import android.content.Context;
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
import android.media.MediaRouter;
import android.media.MediaRouter.RouteInfo;
import android.util.Log;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

<<<<<<< HEAD
public class MediaRouteActionProvider extends ActionProvider {
    private static final String TAG = "MediaRouteActionProvider";

    private Context mContext;
    private MediaRouter mRouter;
    private MenuItem mMenuItem;
    private MediaRouteButton mView;
    private int mRouteTypes;
    private View.OnClickListener mExtendedSettingsListener;
    private RouterCallback mCallback;

    public MediaRouteActionProvider(Context context) {
        super(context);
        mContext = context;
        mRouter = (MediaRouter) context.getSystemService(Context.MEDIA_ROUTER_SERVICE);
        mCallback = new RouterCallback(this);
=======
/**
 * The media route action provider displays a {@link MediaRouteButton media route button}
 * in the application's {@link ActionBar} to allow the user to select routes and
 * to control the currently selected route.
 * <p>
 * The application must specify the kinds of routes that the user should be allowed
 * to select by specifying the route types with the {@link #setRouteTypes} method.
 * </p><p>
 * Refer to {@link MediaRouteButton} for a description of the button that will
 * appear in the action bar menu.  Note that instead of disabling the button
 * when no routes are available, the action provider will instead make the
 * menu item invisible.  In this way, the button will only be visible when it
 * is possible for the user to discover and select a matching route.
 * </p>
 */
public class MediaRouteActionProvider extends ActionProvider {
    private static final String TAG = "MediaRouteActionProvider";

    private final Context mContext;
    private final MediaRouter mRouter;
    private final MediaRouterCallback mCallback;

    private int mRouteTypes;
    private MediaRouteButton mButton;
    private View.OnClickListener mExtendedSettingsListener;

    public MediaRouteActionProvider(Context context) {
        super(context);

        mContext = context;
        mRouter = (MediaRouter) context.getSystemService(Context.MEDIA_ROUTER_SERVICE);
        mCallback = new MediaRouterCallback(this);
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1

        // Start with live audio by default.
        // TODO Update this when new route types are added; segment by API level
        // when different route types were added.
        setRouteTypes(MediaRouter.ROUTE_TYPE_LIVE_AUDIO);
    }

<<<<<<< HEAD
    public void setRouteTypes(int types) {
        if (mRouteTypes == types) return;
        if (mRouteTypes != 0) {
            mRouter.removeCallback(mCallback);
        }
        mRouteTypes = types;
        if (types != 0) {
            mRouter.addCallback(types, mCallback);
        }
        if (mView != null) {
            mView.setRouteTypes(mRouteTypes);
=======
    /**
     * Sets the types of routes that will be shown in the media route chooser dialog
     * launched by this button.
     *
     * @param types The route types to match.
     */
    public void setRouteTypes(int types) {
        if (mRouteTypes != types) {
            // FIXME: We currently have no way of knowing whether the action provider
            // is still needed by the UI.  Unfortunately this means the action provider
            // may leak callbacks until garbage collection occurs.  This may result in
            // media route providers doing more work than necessary in the short term
            // while trying to discover routes that are no longer of interest to the
            // application.  To solve this problem, the action provider will need some
            // indication from the framework that it is being destroyed.
            if (mRouteTypes != 0) {
                mRouter.removeCallback(mCallback);
            }
            mRouteTypes = types;
            if (types != 0) {
                mRouter.addCallback(types, mCallback,
                        MediaRouter.CALLBACK_FLAG_PASSIVE_DISCOVERY);
            }
            refreshRoute();

            if (mButton != null) {
                mButton.setRouteTypes(mRouteTypes);
            }
        }
    }

    public void setExtendedSettingsClickListener(View.OnClickListener listener) {
        mExtendedSettingsListener = listener;
        if (mButton != null) {
            mButton.setExtendedSettingsClickListener(listener);
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
        }
    }

    @Override
<<<<<<< HEAD
=======
    @SuppressWarnings("deprecation")
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
    public View onCreateActionView() {
        throw new UnsupportedOperationException("Use onCreateActionView(MenuItem) instead.");
    }

    @Override
    public View onCreateActionView(MenuItem item) {
<<<<<<< HEAD
        if (mMenuItem != null || mView != null) {
=======
        if (mButton != null) {
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
            Log.e(TAG, "onCreateActionView: this ActionProvider is already associated " +
                    "with a menu item. Don't reuse MediaRouteActionProvider instances! " +
                    "Abandoning the old one...");
        }
<<<<<<< HEAD
        mMenuItem = item;
        mView = new MediaRouteButton(mContext);
        mView.setCheatSheetEnabled(true);
        mView.setRouteTypes(mRouteTypes);
        mView.setExtendedSettingsClickListener(mExtendedSettingsListener);
        mView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return mView;
=======

        mButton = new MediaRouteButton(mContext);
        mButton.setCheatSheetEnabled(true);
        mButton.setRouteTypes(mRouteTypes);
        mButton.setExtendedSettingsClickListener(mExtendedSettingsListener);
        mButton.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return mButton;
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
    }

    @Override
    public boolean onPerformDefaultAction() {
<<<<<<< HEAD
        final FragmentManager fm = getActivity().getFragmentManager();
        // See if one is already attached to this activity.
        MediaRouteChooserDialogFragment dialogFragment =
                (MediaRouteChooserDialogFragment) fm.findFragmentByTag(
                MediaRouteChooserDialogFragment.FRAGMENT_TAG);
        if (dialogFragment != null) {
            Log.w(TAG, "onPerformDefaultAction(): Chooser dialog already showing!");
            return false;
        }

        dialogFragment = new MediaRouteChooserDialogFragment();
        dialogFragment.setExtendedSettingsClickListener(mExtendedSettingsListener);
        dialogFragment.setRouteTypes(mRouteTypes);
        dialogFragment.show(fm, MediaRouteChooserDialogFragment.FRAGMENT_TAG);
        return true;
    }

    private Activity getActivity() {
        // Gross way of unwrapping the Activity so we can get the FragmentManager
        Context context = mContext;
        while (context instanceof ContextWrapper && !(context instanceof Activity)) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        if (!(context instanceof Activity)) {
            throw new IllegalStateException("The MediaRouteActionProvider's Context " +
                    "is not an Activity.");
        }

        return (Activity) context;
    }

    public void setExtendedSettingsClickListener(View.OnClickListener listener) {
        mExtendedSettingsListener = listener;
        if (mView != null) {
            mView.setExtendedSettingsClickListener(listener);
        }
=======
        if (mButton != null) {
            return mButton.showDialogInternal();
        }
        return false;
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
    }

    @Override
    public boolean overridesItemVisibility() {
        return true;
    }

    @Override
    public boolean isVisible() {
<<<<<<< HEAD
        return mRouter.getRouteCount() > 1;
    }

    private static class RouterCallback extends MediaRouter.SimpleCallback {
        private WeakReference<MediaRouteActionProvider> mAp;

        RouterCallback(MediaRouteActionProvider ap) {
            mAp = new WeakReference<MediaRouteActionProvider>(ap);
=======
        return mRouter.isRouteAvailable(mRouteTypes,
                MediaRouter.AVAILABILITY_FLAG_IGNORE_DEFAULT_ROUTE);
    }

    private void refreshRoute() {
        refreshVisibility();
    }

    private static class MediaRouterCallback extends MediaRouter.SimpleCallback {
        private final WeakReference<MediaRouteActionProvider> mProviderWeak;

        public MediaRouterCallback(MediaRouteActionProvider provider) {
            mProviderWeak = new WeakReference<MediaRouteActionProvider>(provider);
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
        }

        @Override
        public void onRouteAdded(MediaRouter router, RouteInfo info) {
<<<<<<< HEAD
            final MediaRouteActionProvider ap = mAp.get();
            if (ap == null) {
                router.removeCallback(this);
                return;
            }

            ap.refreshVisibility();
=======
            refreshRoute(router);
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
        }

        @Override
        public void onRouteRemoved(MediaRouter router, RouteInfo info) {
<<<<<<< HEAD
            final MediaRouteActionProvider ap = mAp.get();
            if (ap == null) {
                router.removeCallback(this);
                return;
            }

            ap.refreshVisibility();
=======
            refreshRoute(router);
        }

        @Override
        public void onRouteChanged(MediaRouter router, RouteInfo info) {
            refreshRoute(router);
        }

        private void refreshRoute(MediaRouter router) {
            MediaRouteActionProvider provider = mProviderWeak.get();
            if (provider != null) {
                provider.refreshRoute();
            } else {
                router.removeCallback(this);
            }
>>>>>>> feef9887e8f8eb6f64fc1b4552c02efb5755cdc1
        }
    }
}
