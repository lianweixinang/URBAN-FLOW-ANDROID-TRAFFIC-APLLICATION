package ntutifm.game.google.global

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ntutifm.game.google.MyActivity
import ntutifm.game.google.R

class AppUtil {
    companion object {
        /**
         * 關掉SoftKeyboard 不用指定focus的View
         */
        @JvmStatic
        fun closeSoftKeyboard(activity: Activity?) {
            if (activity != null && activity.currentFocus != null) {
                val inputManager =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager?.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
            }
        }

        /**
         * 關掉SoftKeyboard
         */
        @JvmStatic
        fun closeSoftKeyboard(context: Context?, editText: EditText?) {
            if (context == null) {
                return
            }
            if (editText == null) {
                return
            }
            val im = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im?.hideSoftInputFromWindow(editText.windowToken, 0)
        }

        @JvmStatic
        fun showSoftKeyboard(context: Context?, view: View?) {
            if (context == null) {
                return
            }
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.showSoftInput(view, 0)
        }

        @JvmStatic
        fun showTopToast(context: Context?, message: String?) {
            val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER or Gravity.TOP, 0, 200)
            toast.show()
        }

        /**
         * 啟動新的Fragment使用的method
         */
        @JvmStatic
        fun startFragment(manager: FragmentManager?, from: Int, target: Fragment) {
            val transaction = manager!!.beginTransaction()
            transaction.setReorderingAllowed(true)
            transaction.replace(from, target, target.tag)
            transaction.addToBackStack(target.tag)
            transaction.commitAllowingStateLoss()
        }

        /**
         * 返回到底
         */
        @JvmStatic
        fun popupAllFragment(manager: FragmentManager?) {
            try {
                if (manager != null && !manager.isStateSaved && !manager.isDestroyed) {
                    manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
            } catch (e: Exception) {
                MyLog.e(e.toString())
            }
        }
        /**
         * 返回，預設不立即
         */
        @JvmStatic
        fun popBackStack(manager: FragmentManager?, isImmediate: Boolean = false) {
            try {
                if (manager != null && !manager.isStateSaved && !manager.isDestroyed) {
                    if (isImmediate) {
                        manager.popBackStackImmediate()
                    } else {
                        manager.popBackStack()
                    }
                }
            } catch (e: Exception) {
                MyLog.e(e.toString())
            }
        }
        @JvmStatic
        fun showDialog(message : String?, activity: Activity?){
            message?.run {
                val builder = AlertDialog.Builder(activity)
                builder.setCancelable(false)
                builder.setMessage(this)
                builder.setTitle("交通流量小幫手")
                builder.setPositiveButton("確認", null)
                builder.create().show()
            }
        }
    }

}

