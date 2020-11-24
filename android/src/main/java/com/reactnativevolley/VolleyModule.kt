package com.reactnativevolley

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableMapKeySetIterator
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
     * Make a HTTP request.
     */
    @ReactMethod
    fun fetch(url: String, opts: ReadableMap, promise: Promise) {

      // Retrieve values from options...

      // TODO: Is there a cleaner way to do this in Kotlin?
      val method = when (opts.getString("method")) {
        "GET" -> Request.Method.GET
        "POST" -> Request.Method.POST
        "PUT" -> Request.Method.PUT
        "DELETE" -> Request.Method.DELETE
        "HEAD" -> Request.Method.HEAD
        "OPTIONS" -> Request.Method.OPTIONS
        "TRACE" -> Request.Method.TRACE
        "PATCH" -> Request.Method.PATCH
        else -> Request.Method.GET
      }

      val shouldCache = when (opts.getString("cache")) {
        "no-cache" -> false
        "no-store" -> false
        "force-cache" -> true
        // "reload" (not supported)
        // "only-if-cached" (not supported)
        else -> true
      }

      // Instantiate the RequestQueue.
      val requestQueue = Volley.newRequestQueue(getReactApplicationContext())

      // Request a string response from the provided URL.
      val stringRequest = object: StringRequest(method, url,
        Response.Listener<String> { response ->
          promise.resolve(response)
        },
        Response.ErrorListener {
          // promise.reject()
        }
      )
      {
        // Set headers.
        // See: https://stackoverflow.com/questions/51819176/how-to-add-custom-header-in-volley-request-with-kotlin
        override fun getHeaders(): MutableMap<String, String> {
          val optsHeaderMap = opts!!.getMap("headers")!!.toHashMap()
          val headers = HashMap<String, String>()

          for ((key, value) in optsHeaderMap) {
            if (value is String) {
              headers.set(key, value)
            }
          }
          return headers
        }

        // TODO: body
        // // Set body content type
        // // If we don't overwrite this Volley will fall back to "application/x-www-form-urlencoded"
        // override fun getBodyContentType(): MutableMap<String, String> {
        //   val contentType: String = if opts?.getMap("header")?.getString("Content-Type") else "application/x-www-form-urlencoded"
        //   return contentType + "; charset=" + getParamsEncoding()
        // }

        // overwrite fun getBody(): Array<Byte> {
        //   return opts.getString("body").getBytes(getParamsEncoding());
        // }
      }

      // Set caching settings.
      stringRequest.setShouldCache(shouldCache)

      // Add the request to the RequestQueue.
      requestQueue.add(stringRequest)

    }

}
