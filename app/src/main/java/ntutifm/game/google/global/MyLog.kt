package ntutifm.game.google.global

import android.util.Log

class MyLog {
    companion object {
        @JvmStatic
        fun e(message: String) {
            Log.e("MyLog", message)
        }

        @JvmStatic
        fun d(message: String) {
            Log.d("MyLog", message)
        }
    }
}