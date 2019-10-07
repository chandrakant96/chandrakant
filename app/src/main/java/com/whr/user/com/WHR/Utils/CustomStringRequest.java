package com.whr.user.com.WHR.Utils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;

public class CustomStringRequest extends StringRequest {
    public CustomStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.setRetryPolicy(this.getRetryPolicy());
    }

    public CustomStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        this.setRetryPolicy(this.getRetryPolicy());
    }

   /* @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
    }*/

    @Override
    public RetryPolicy getRetryPolicy() {
        // here you can write a custom retry policy
        int socketTimeout = 30000;//5 seconds - change to what you want
        int no_of_try = 2;
        float multiplier = 1.5f;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, no_of_try, multiplier);
        return policy;
        //return super.getRetryPolicy();
    }

}
