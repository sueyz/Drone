package com.example.drone.view.extensions

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BatteryManagerBroadcastReceiver(
    private val onReceiveIntent: (Intent) -> Unit,
) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        onReceiveIntent(intent)
    }
}