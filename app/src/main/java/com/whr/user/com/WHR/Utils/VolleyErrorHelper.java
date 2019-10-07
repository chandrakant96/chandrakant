package com.whr.user.com.WHR.Utils;

/**
 * Created by lenovo on 12/5/2016.
 */

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.whr.user.R;

public class VolleyErrorHelper {

    public VolleyErrorHelper() {
    }

    /**
     * @param error
     * @param context
     * @return Returns appropriate message which is to be displayed to the user against
     * the specified error object.
     */
    public static String getMessage(Object error, Context context) {
        if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.PleaseTryAgain);
        } else if (isServerProblem(error)) {
            return context.getResources().getString(R.string.PleaseTryAgain);
        } else if (isNetworkProblem(error)) {
            return context.getResources().getString(R.string.no_internet);
        }
        return context.getResources().getString(R.string.PleaseTryAgain);
    }

    /**
     * Determines whether the error is related to network
     *
     * @param error
     * @return
     */
    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }

    /**
     * Determines whether the error is related to server
     *
     * @param error
     * @return
     */
    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }
}