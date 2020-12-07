package com.reactnativevolley

import com.android.volley.Response
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.NetworkResponse
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.HttpHeaderParser
import com.facebook.react.bridge.*
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class VolleyModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
      return "Volley"
    }

    // Kotlin Volley example app
    // See: https://github.com/Kotlin/kotlin-examples/tree/master/gradle/android-volley/app/src/main/java/org/example/kotlin/volley

    /**
     * The volly RequestQueue should only be instantiated once.
     */
    val requestQueue: RequestQueue by lazy { Volley.newRequestQueue(reactApplicationContext) }

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

      // Define event listeners.
      val listener = Response.Listener<WritableMap> { response ->
        promise.resolve(response)
      }
      val errorListener = Response.ErrorListener { error ->
        promise.reject(error)
      }

      // Instantiate the request.
      val request = object: Request<WritableMap>(method, url, errorListener) {

        override fun getHeaders(): MutableMap<String, String> {
          val optsHeaderMap = opts.getMap("headers")!!.toHashMap()
          val headers = HashMap<String, String>()

          for ((key, value) in optsHeaderMap) {
            if (value is String) {
              headers.set(key, value)
            }
          }
          return headers
        }

        override fun getBody(): ByteArray? {
          try {
            return opts.getString("body")?.toByteArray(Charset.forName(getParamsEncoding()))
          } catch (error: UnsupportedEncodingException) {
            // promise.reject(error)
            // We ingnore malformed body and return null.
            return null
          }
        }

        override fun deliverResponse(response: WritableMap) = listener.onResponse(response)

        override fun parseNetworkResponse(response: NetworkResponse?): Response<WritableMap> {
          val resMap: WritableMap = Arguments.createMap()

          // Extract headers.
          val headerMap: Map<String, String> = response?.headers ?: emptyMap()
          val headers: WritableMap = Arguments.createMap()
          for ((key, value) in headerMap) {
            headers.putString(key, value)
          }
          resMap.putMap("headers", headers)

          // Extract status.
          val status = response?.statusCode
          if (status is Int) {
            resMap.putInt("status", status)
          } else {
            resMap.putNull("status")
          }

          // Extract body.
          try {
            val body = String(
              response?.data ?: ByteArray(0),
              Charset.forName(HttpHeaderParser.parseCharset(response?.headers))
            )
            resMap.putString("body", body)
          } catch (error: UnsupportedEncodingException) {
            promise.reject(error)
          }

          // Return native response.
          return Response.success(
            resMap,
            HttpHeaderParser.parseCacheHeaders(response)
          )
        }

      }

      // Set caching settings.
      request.setShouldCache(shouldCache)

      // Add request to RequestQueue.
      requestQueue.add(request)

    }

}
