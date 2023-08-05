package ntutifm.game.google.net

import androidx.annotation.MainThread
import java.util.ArrayList

interface ApiCallBack {

    @MainThread
    fun onSuccess(successData: ArrayList<String>)

    @MainThread
    fun onError(errorCode: Int, errorData: ArrayList<String>)

    fun doInBackground(result: Int, successData: ArrayList<String>)
}