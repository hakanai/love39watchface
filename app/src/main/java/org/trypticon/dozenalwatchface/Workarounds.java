package org.trypticon.dozenalwatchface;

import android.content.res.Resources;

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
     * Looks up a colour in the resources.
     * Method exists solely as an Android SDK workaround for deprecation warnings.
     *
     * @param resources the resources.
     * @param id the ID of the resource.
     * @return the colour.
     */
    @SuppressWarnings("deprecation")
    static int getColor(Resources resources, int id) {
        return resources.getColor(id);
    }

}
