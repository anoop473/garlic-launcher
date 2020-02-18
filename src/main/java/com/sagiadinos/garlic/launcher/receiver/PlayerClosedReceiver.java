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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sagiadinos.garlic.launcher.MainActivity;

/**
 * Needed when a Player closed regulary via exit.
 * In this case we start again the user intrupptable countdown
 * Otherwise the the backgroundservice from Player will
 * recognise, that the player activity do not run in foreground and
 * start the player again.
 */
public class PlayerClosedReceiver extends BroadcastReceiver
{
    MainActivity MyActivity;

    public PlayerClosedReceiver(com.sagiadinos.garlic.launcher.MainActivity act)
    {
        MyActivity = act;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent == null || intent.getAction() == null)
        {
            return;
        }
        if (!intent.getAction().equals("com.sagiadinos.garlic.launcher.receiver.PlayerClosedReceiver") ||
                !context.getPackageName().equals(MyActivity.getPackageName()))
        {
            return;
        }
        MyActivity.startGarlicPlayerDelayed();
    }
}