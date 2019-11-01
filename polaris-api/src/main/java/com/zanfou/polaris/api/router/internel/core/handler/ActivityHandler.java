package com.zanfou.polaris.api.router.internel.core.handler;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.zanfou.polaris.api.router.Polaris;
import com.zanfou.polaris.api.router.exception.RouterException;
import com.zanfou.polaris.api.router.token.RouterToken;
import org.json.JSONException;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-17
 * <p>
 * 处理Activity相关的路由
 */
public class ActivityHandler implements RouterHandler {
    public static final int REQUEST_CODE_DEFAULT = -1;

    private RouterToken routerToken;
    private Activity activity;
    private Fragment fragment;

    public ActivityHandler(Activity activity, Fragment fragment) {
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public Object handleRouter(RouterToken routerToken) throws Exception {
        this.routerToken = routerToken;
        try {
            Intent intent = createIntent();
            if (routerToken.getRequestCode() <= REQUEST_CODE_DEFAULT) {
                Context context = activity;
                if (context == null && fragment != null) {
                    context = fragment.requireContext();
                }
                if (context == null){
                    context = Polaris.getInstance().getContext();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                ActivityCompat.startActivity(context, intent, null);
                return true;
            }
            if (activity != null) {
                activity.startActivityForResult(intent, routerToken.getRequestCode());
                return true;
            }
            if (fragment != null) {
                fragment.startActivityForResult(intent, routerToken.getRequestCode());
                return true;
            }
            throw new RouterException(RouterException.ERROR_START_ACTIVITY_FOR_RESULT,
                    "'startActivityFroResult' method must be called by Activity or Fragment instance.");
        } catch (ActivityNotFoundException e) {
            throw new RouterException(RouterException.ERROR_ACTIVITY_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RouterException(RouterException.ERROR_CODE_UNKNOWN, e.getMessage());
        }
    }


    private Intent createIntent() throws JSONException {
        Intent intent = new Intent();
        intent.setClass(Polaris.getInstance().getContext(), routerToken.getTargetClass());
        if (routerToken.getBundle() != null) {
            intent.putExtras(routerToken.getBundle());
        }
        return intent;
    }
}
