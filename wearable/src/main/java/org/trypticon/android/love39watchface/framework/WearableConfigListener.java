/*
 * Copyright © 2016-2017 Trejkaz <trejkaz@trypticon.org>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING.WTFPL file for more details.
 */

package org.trypticon.android.love39watchface.framework;

/**
 * Listener for configuration changes made by a config View.
 */
public interface WearableConfigListener {

    void onConfigCompleted(String key, Object value, boolean finish);
}
