package net.usermd.mcichon.quakemodel.interfaces;

import java.util.HashMap;
import java.util.List;

public interface AsyncCallback {

    void onAsyncStarted();
    void onAsyncCompleted(List<HashMap<String, String>> result);
}
