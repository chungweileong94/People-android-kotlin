package com.chungwei.leong.people.helpers

import android.database.Cursor

fun Cursor.getStringValue(column: String): String = this.getString(this.getColumnIndex(column))
