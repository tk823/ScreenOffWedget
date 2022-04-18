package com.tapps.screenoffwidget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.os.SystemClock
import android.widget.RemoteViews
import android.widget.Toast

/**
 * Implementation of App Widget functionality.
 */
class ScreenOffWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == "CLICK_SCREEN_OFF_WIDGET") {
            val pm = context?.getSystemService(Context.POWER_SERVICE) as PowerManager
            if (pm.isInteractive) {
                pm.goToSleep(
                    SystemClock.uptimeMillis(),
                    PowerManager.GO_TO_SLEEP_REASON_POWER_BUTTON,
                    0
                )
            }
        }
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = ""
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.screen_off_widget)
    views.setTextViewText(R.id.appwidget_text, widgetText)

    val intent = Intent(context, ScreenOffWidget::class.java)
    intent.action = "CLICK_SCREEN_OFF_WIDGET"
    val flag = when {
        Build.VERSION.SDK_INT > 30 -> PendingIntent.FLAG_MUTABLE
        else -> 0
    }
    val pIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, flag)
    views.setOnClickPendingIntent(R.id.appwidget_text, pIntent)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}