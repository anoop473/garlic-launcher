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
import android.content.IntentFilter;
import android.widget.Toast;

import com.sagiadinos.garlic.launcher.BuildConfig;
import com.sagiadinos.garlic.launcher.MainActivity;
import com.sagiadinos.garlic.launcher.receiver.AdminReceiver;

/**
 *  DeviceOwner handles the methods to check for device owner
 *  and encapsulates some simple functiosn like reboot.
 *
 */
public class DeviceOwner
{
    private DevicePolicyManager dpm;
    private ComponentName       deviceAdmin;
    private Context             ctx               = null;

    public static final String GARLIC_PLAYER_PACKAGE_NAME = "com.sagiadinos.garlic.player";
    public static final String GARLIC_PLAYER_PACKAGE_NAME_ALT = "com.sagiadinos.smilcontrol";
    public static final String QT_TEST_PACKAGE_NAME = "com.sagiadinos.garlic.qttest";
    public static final String SECOND_APP_PACKAGE_NAME = "com.sagiadinos.garlic.secondapp";
    public static final String TEST_APP_PACKAGE_NAME = "com.sagiadinos.garlic.nativetest";

    public DeviceOwner(Context c)
    {
        ctx         = c;
        deviceAdmin = new ComponentName(ctx, AdminReceiver.class);
        dpm         = (DevicePolicyManager) ctx.getSystemService(Context.DEVICE_POLICY_SERVICE);

        if (deviceAdmin == null || dpm == null)
        {
            showToast("handle device owner is null");
            return;
        }
        if (!isAdminActive())
        {
            showToast("This app is not a device admin!");
            return;
        }
        if (isDeviceOwner())
        {
            determinePermittedLockTaskPackages("");
        }
        else
        {
            showToast("This app is not the device owner!");
        }
    }

    public boolean isLockTaskPermitted()
    {
        return dpm.isLockTaskPermitted(ctx.getPackageName());
    }

    public boolean isAdminActive()
    {
        return dpm.isAdminActive(deviceAdmin);
    }

    public boolean isDeviceOwner()
    {
        String s = ctx.getPackageName();
        return dpm.isDeviceOwnerApp(s);
    }

    public void reboot()
    {
        if (isAdminActive())
        {
            dpm.reboot(deviceAdmin);
        }
        else
        {
            showToast("This app is not a device owner!");
        }
    }

    public void addPersistentPreferredActivity(IntentFilter intentFilter)
    {
        ComponentName activity = new ComponentName(ctx, MainActivity.class);
        dpm.addPersistentPreferredActivity(deviceAdmin, intentFilter, activity);

    }

    public void clearMainPackageFromPersistent()
    {
        if (isAdminActive())
        {
            dpm.clearPackagePersistentPreferredActivities(deviceAdmin, ctx.getPackageName());
        }
        else
        {
            showToast("This app is not the device owner!");
        }
    }

    /**
     *
     * @param second_app_name String
     */
    public void determinePermittedLockTaskPackages(String second_app_name)
    {
        if (second_app_name.equals(""))
        {
            dpm.setLockTaskPackages(deviceAdmin, new String[]{ctx.getPackageName(), GARLIC_PLAYER_PACKAGE_NAME, QT_TEST_PACKAGE_NAME});

        }
        else
        {
            // Todo Clear or add
            dpm.setLockTaskPackages(deviceAdmin, new String[]{ctx.getPackageName(), GARLIC_PLAYER_PACKAGE_NAME, QT_TEST_PACKAGE_NAME, second_app_name});
        }
    }

    private void showToast(String text)
    {
        if (BuildConfig.DEBUG)
        {
            Toast.makeText(ctx, text, Toast.LENGTH_LONG).show();
        }
    }

}