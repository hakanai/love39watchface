package org.trypticon.dozenalwatchface;

import android.content.Context;

/**
 * Collection of Android SDK workarounds.
 */
class Workarounds {

    /**
     * Prevents instantiation.
     */
    private Workarounds() {
    }

    /**
     * Looks up a colour in the context.
     * Method exists solely as an Android SDK workaround for deprecation warnings.
     *
     * @param context the context.
     * @param id the ID of the resource.
     * @return the colour.
     */
    @SuppressWarnings("deprecation")
    static int getColor(Context context, int id) {
        return context.getResources().getColor(id);
    }

}
