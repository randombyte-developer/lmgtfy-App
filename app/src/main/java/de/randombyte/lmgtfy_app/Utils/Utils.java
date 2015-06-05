package de.randombyte.lmgtfy_app.Utils;

import android.view.ViewGroup;

public class Utils {

    public static void setEnabledRecursive(ViewGroup viewGroup, boolean enabled) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).setEnabled(enabled);
        }
    }

}
