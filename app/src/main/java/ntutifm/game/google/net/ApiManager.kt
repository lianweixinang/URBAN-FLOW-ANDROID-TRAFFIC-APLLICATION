package ntutifm.game.google.net

import android.content.Context
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

class ApiManager constructor(
    private val apiClass: Class<out ApiProcessor>,
    private val callBack: ApiCallBack? = null,
    private val data: String? = null
) {

    constructor(
        callBack: ApiCallBack?
    ) : this(ApiProcessor::class.java, callBack, null)
    constructor(
        callBack: ApiCallBack?,
        data:String
    ) : this(ApiProcessor::class.java, callBack, data)

    private var successData: ArrayList<String> = ArrayList(3)
    private var errorData: ArrayList<String> = ArrayList(3)

    fun execute(fragment: Fragment, apiName: String) {
        val lifecycleScope: LifecycleCoroutineScope? = if (fragment is DialogFragment) {
            fragment.activity?.lifecycleScope
        } else {
            fragment.viewLifecycleOwner.lifecycleScope
        }
        lifecycleScope?.launch(Dispatchers.Main) {
            try {
                //整理資料
                val result = doInBackground(fragment.context, apiName)
            } catch (e: Exception) {
                Log.e("MyLog", e.toString())
            }
        }
    }

    private suspend fun doInBackground(context: Context?, apiName: String): Int =
        withContext(Dispatchers.IO) {
            var result: Int = 13
            successData = ArrayList(3)
            errorData = ArrayList(3)
            successData.add(apiName)
            if (!data.isNullOrEmpty()) {
                successData.add(data)
            }
            errorData.add(apiName)

            val method = apiClass.getMethod(
                apiName,
                Context::class.java,
                ArrayList::class.java,
                ArrayList::class.java
            )
            if (method.name == apiName) {
                result =
                    method.invoke(apiClass.newInstance(), context, successData, errorData) as Int
            }

            if (result == 777) {
                callBack?.doInBackground(result, successData)
            }
            return@withContext result
        }

}