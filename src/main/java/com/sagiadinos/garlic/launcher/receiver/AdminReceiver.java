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

package com.sagiadinos.garlic.launcher.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import com.sagiadinos.garlic.launcher.helper.KioskManager;


public class AdminReceiver extends DeviceAdminReceiver
{
    @Override
    public void onEnabled(Context ctx, Intent intent)
    {
        KioskManager.staticBecomeHomeActivity(ctx);
   }

    @Override
    public CharSequence onDisableRequested(Context ctx, Intent intent)
    {
        return "Warning: Device Admin is going to be disabled.";
    }

}