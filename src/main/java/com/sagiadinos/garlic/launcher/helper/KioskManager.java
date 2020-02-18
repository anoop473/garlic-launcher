/*************************************************************************************
 garlic-launcher: Android Launcher for the Digital Signage Software garlic-player

 Copyright (C) 2020 Nikolaos Sagiadinos <ns@smil-control.com>
 This file is part of the garlic-player source code

 This program is free software: you can redistribute it and/or  modify
 it under the terms of the GNU Affero General Public License, version 3,
 as published by the Free Software Foundation.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *************************************************************************************/

package com.sagiadinos.garlic.launcher.helper;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.sagiadinos.garlic.launcher.BuildConfig;
import com.sagiadinos.garlic.launcher.MainActivity;
import com.sagiadinos.garlic.launcher.receiver.AdminReceiver;

/**
 * This class is responsible for methods which needed to create a Kiosk Mode
 * Kiosk Mode means:
 *
 * Standard Home-Button Activity! this App will become a system launcher and is set
 * LockTask/Pinning, No one can escape from the app.
 *
 */
public class KioskManager
{
    private DeviceOwner MyDeviceOwner;
    private HomeLauncherManager  MyLauncher;
    private LockTaskManager      MyLockTasks;
    private MainActivity         MyActivity;


    public KioskManager(DeviceOwner deviceOwner, HomeLauncherManager hlm,  LockTaskManager ltm, MainActivity ma)
    {
        MyDeviceOwner = deviceOwner;
        MyLauncher    = hlm;
        MyLockTasks   = ltm;
        MyActivity    = ma;
    }


    public void startKioskMode()
    {
        if (canEnterKioskMode())
        {
            MyLockTasks.startLockTask();
            MyLauncher.becomeHomeActivity();
        }
  }

    /**
     * @return boolean returns the status of locktask
     */
    public boolean toggleKioskMode()
    {
        if (canEnterKioskMode())
        {
            return MyLockTasks.toggleLockTask();
        }
        return false;
    }

    public boolean isHomeActivity()
    {
        return MyLauncher.isHomeActivity();
    }

    public void becomeHomeActivity()
    {
        if (canEnterKioskMode())
        {
            MyLauncher.becomeHomeActivity();
        }
    }

    public boolean toggleHomeActivity()
    {
        if (canEnterKioskMode())
        {
           return MyLauncher.toggleHomeActivity();
        }
        return false;
    }


    public static void staticBecomeHomeActivity(Context c)
    {
        ComponentName       deviceAdmin = new ComponentName(c, AdminReceiver.class);
        DevicePolicyManager dpm         = (DevicePolicyManager) c.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (dpm == null)
        {
            return;
        }
        if (!dpm.isAdminActive(deviceAdmin) && !dpm.isDeviceOwnerApp(c.getPackageName()))
        {
            return;
        }
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MAIN);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        intentFilter.addCategory(Intent.CATEGORY_HOME);
        ComponentName activity = new ComponentName(c, MainActivity.class);
        dpm.addPersistentPreferredActivity(deviceAdmin, intentFilter, activity);
    }

    private boolean canEnterKioskMode()
    {
        if (!checkforDeviceRights())
        {
            Toast.makeText(MyActivity, "Kiosk Mode is not permitted", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    private boolean checkforDeviceRights()
    {
        if (!MyDeviceOwner.isAdminActive())
        {
            showToast("This app is not a device admin!");
            return false;
        }
        if (!MyDeviceOwner.isDeviceOwner())
        {
            showToast("This app is not a device admin!");
            return false;
        }
        if (!MyDeviceOwner.isLockTaskPermitted())
        {
            showToast("Lock Task is not permitted");
            return false;
        }
        return true;
    }

    private void showToast(String text)
    {
        if (BuildConfig.DEBUG)
        {
            Toast.makeText(MyActivity, text, Toast.LENGTH_SHORT).show();
        }
    }

}