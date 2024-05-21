package com.sagiadinos.garlic.launcher.services;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.Optional;

public class MyAccessibilityService extends AccessibilityService {
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Toast t = Toast.makeText(getApplicationContext(), "Launcher Service is running now", Toast.LENGTH_SHORT);
        t.show();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        try {
            Log.d("###", AccessibilityEvent.eventTypeToString(event.getEventType()) + " - " + (event.getText().isEmpty() ? "0" : event.getText().get(0).toString()) + " - " + event.getPackageName());
            Log.d("###", "Source:" + (event.getSource() == null ? "" : event.getSource().getClassName()));

            if (!event.getText().isEmpty() && event.getText().get(0).toString().contains("debugging?")) {
                if (event.getSource() == null) {
                    Log.i("###", "Executing Home");
                    performGlobalAction(GLOBAL_ACTION_HOME);
                    performGlobalAction(GLOBAL_ACTION_HOME);
                } else {
                    if (!event.getSource().getChild(2).isChecked()) {
                        event.getSource().getChild(2).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        event.getSource().getChild(3).getChild(1).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("###", ex.getMessage());
        }

    }

    @Override
    public void onInterrupt() {
    }

//    public void enumerate(AccessibilityNodeInfo ac, String tb){
//        if(ac == null)
//        {
//            Log.i("###", "Node is null");
//            Log.i("###", getRootInActiveWindow().toString());
//            ac = getRootInActiveWindow();
//            for (int i = 0; i < getWindows().stream().count(); i++) {
//                enumerate(ac, "");
//            }
//            return;
//        }
//        tb = tb + "    ";
//        if(ac.getClassName() != null && !ac.getClassName().toString().isEmpty()){
//            Log.i("###", tb + ac.getClassName().toString() + " -- " + ac.getText());
//        }
//
//        for (int i = 0; i < ac.getChildCount(); i++) {
//            enumerate(ac.getChild(i),tb);
//        }
//    }
}