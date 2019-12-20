package com.planb.sniffer.console

import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager

fun WindowManager.initDragWindow(rootView: View, dragView: View): WindowManager.LayoutParams {
    val layoutParams = WindowManager.LayoutParams()
    // 设置LayoutParam
    layoutParams.width = rootView.resources.displayMetrics.widthPixels -
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, rootView.resources.displayMetrics).toInt()
    layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400f, rootView.resources.displayMetrics).toInt()
    layoutParams.format = PixelFormat.TRANSLUCENT// 支持透明
    // mParams.format = PixelFormat.RGBA_8888;
    layoutParams.flags = layoutParams.flags.or(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { /*android7.0不能用TYPE_TOAST*/
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
    } else { /*以下代码块使得android6.0之后的用户不必再去手动开启悬浮窗权限*/
        if (PackageManager.PERMISSION_GRANTED ==
                rootView.context.packageManager.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", rootView.context.packageName)) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST
        }
    }
    layoutParams.gravity = Gravity.TOP
    layoutParams.x = 0
    layoutParams.y = 0
    dragView.setOnTouchListener(object : View.OnTouchListener {
        private var x: Int = 0
        private var y: Int = 0

        override fun onTouch(view: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = event.rawX.toInt()
                    y = event.rawY.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    val nowX = event.rawX.toInt()
                    val nowY = event.rawY.toInt()
                    val movedY = nowY - y
                    x = nowX
                    y = nowY
                    layoutParams.y = layoutParams.y + movedY

                    // 更新悬浮窗控件布局
                    updateViewLayout(rootView, layoutParams)
                }
                else -> {
                }
            }
            return true
        }
    })
    return layoutParams
}

fun WindowManager.showView(view: View?, layoutParams: WindowManager.LayoutParams) {
    // 将悬浮窗控件添加到WindowManager
    addView(view, layoutParams)
}


fun WindowManager.hideView(view: View) {
    removeView(view)
}