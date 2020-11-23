package com.reactnativevolley

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.Promise

import com.android.volley.Response
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class VolleyModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
      return "Volley"
    }

    /**
     * Make an HTTP request.
     */
    @ReactMethod
    fun fetch(url: String, opts: ReadableMap, promise: Promise) {

      // Instantiate the RequestQueue.
      val requestQueue = Volley.newRequestQueue(getReactApplicationContext())

      // Request a string response from the provided URL.
      val stringRequest = StringRequest(Request.Method.GET, url,
        Response.Listener<String> { response ->
          promise.resolve(response)
        },
        Response.ErrorListener {
          // promise.reject()
        }
      )

      // Add the request to the RequestQueue.
      requestQueue.add(stringRequest)

    }

}
