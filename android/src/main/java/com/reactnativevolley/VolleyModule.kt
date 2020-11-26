package com.reactnativevolley

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableMapKeySetIterator
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.Arguments

import com.android.volley.Response
import com.android.volley.Request
import com.android.volley.NetworkResponse
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.HttpHeaderParser
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class VolleyModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
      return "Volley"
    }

    // /**
    //  * Make a HTTP request.
    //  */
    // @ReactMethod
    // fun fetch(url: String, opts: ReadableMap, promise: Promise) {

    //   // Retrieve values from options...

    //   // TODO: Is there a cleaner way to do this in Kotlin?
    //   val method = when (opts.getString("method")) {
    //     "GET" -> Request.Method.GET
    //     "POST" -> Request.Method.POST
    //     "PUT" -> Request.Method.PUT
    //     "DELETE" -> Request.Method.DELETE
    //     "HEAD" -> Request.Method.HEAD
    //     "OPTIONS" -> Request.Method.OPTIONS
    //     "TRACE" -> Request.Method.TRACE
    //     "PATCH" -> Request.Method.PATCH
    //     else -> Request.Method.GET
    //   }

    //   val shouldCache = when (opts.getString("cache")) {
    //     "no-cache" -> false
    //     "no-store" -> false
    //     "force-cache" -> true
    //     // "reload" (not supported)
    //     // "only-if-cached" (not supported)
    //     else -> true
    //   }

    //   // Instantiate the RequestQueue.
    //   val requestQueue = Volley.newRequestQueue(getReactApplicationContext())

    //   val stringRequest = CreateRequest(
    //     url,
    //     Response.Listener<String> { response ->
    //       promise.resolve(response)
    //     },
    //     Response.ErrorListener {
    //       // promise.reject()
    //     }
    //   )

    //   // // Request a string response from the provided URL.
    //   // val stringRequest = object: Request<WritableMap>(method, url,
    //   //   Response.Listener<String> { response ->
    //   //     promise.resolve(response)
    //   //   },
    //   //   Response.ErrorListener {
    //   //     // promise.reject()
    //   //   }
    //   // )
    //   // {
    //   //   // Set headers.
    //   //   // See: https://stackoverflow.com/questions/51819176/how-to-add-custom-header-in-volley-request-with-kotlin
    //   //   override fun getHeaders(): MutableMap<String, String> {
    //   //     val optsHeaderMap = opts!!.getMap("headers")!!.toHashMap()
    //   //     val headers = HashMap<String, String>()

    //   //     for ((key, value) in optsHeaderMap) {
    //   //       if (value is String) {
    //   //         headers.set(key, value)
    //   //       }
    //   //     }
    //   //     return headers
    //   //   }

    //   //   // Set headers.
    //   //   // See: https://stackoverflow.com/questions/51819176/how-to-add-custom-header-in-volley-request-with-kotlin
    //   //   override fun parseNetworkResponse(response: NetworkResponse): Response<WritableMap> {
    //   //     val responseMap: WritableMap = Arguments.createMap()
    //   //     val status = response.statusCode

    //   //     try {
    //   //       var charset: Charset = Charset.forName(HttpHeaderParser.parseCharset(response.headers))
    //   //       val body: String = String(response.data, charset)
    //   //     } catch (e: UnsupportedEncodingException) {
    //   //       val body: String = String(response.data, Charset.forName("UTF-8"))
    //   //     }

    //   //     responseMap.putInt("status", status)
    //   //     // responseMap.putString("body", body)
    //   //     // responseMap.putMap("headers", HttpHeaderParser.toAllHeaderList(response.header))
    //   //     return Response.success(responseMap, HttpHeaderParser.parseCacheHeaders(response))
    //   //   }

    //     // TODO: body
    //     // // Set body content type
    //     // // If we don't overwrite this Volley will fall back to "application/x-www-form-urlencoded"
    //     // override fun getBodyContentType(): MutableMap<String, String> {
    //     //   val contentType: String = if opts?.getMap("header")?.getString("Content-Type") else "application/x-www-form-urlencoded"
    //     //   return contentType + "; charset=" + getParamsEncoding()
    //     // }

    //     // overwrite fun getBody(): Array<Byte> {
    //     //   return opts.getString("body").getBytes(getParamsEncoding());
    //     // }
    //   }

    //   // Set caching settings.
    //   // stringRequest.setShouldCache(shouldCache)

    //   // Add the request to the RequestQueue.
    //   requestQueue.add(stringRequest)

    // }



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

      val listener = Response.Listener<WritableMap> { response ->
        promise.resolve(response)
      }
      val errorListener = Response.ErrorListener { error ->
        promise.reject(error)
      }

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

          override fun deliverResponse(response: WritableMap) = listener.onResponse(response)

          override fun parseNetworkResponse(response: NetworkResponse?): Response<WritableMap> {
            val resMap: WritableMap = Arguments.createMap()
            val status = response?.statusCode
            if (status is Int) {
              resMap.putInt("status", status)
            } else {
              resMap.putNull("status")
            }
            try {
              val body = String(
                response?.data ?: ByteArray(0),
                Charset.forName(HttpHeaderParser.parseCharset(response?.headers))
              )
              resMap.putString("body", body)
            } catch (error: UnsupportedEncodingException) {
              promise.reject(error)
            }
            return Response.success(
              resMap,
              HttpHeaderParser.parseCacheHeaders(response)
            )
          }
      }

      // // Request a string response from the provided URL.
      // val stringRequest = object: Request<WritableMap>(method, url,
      //   Response.Listener<String> { response ->
      //     promise.resolve(response)
      //   },
      //   Response.ErrorListener {
      //     // promise.reject()
      //   }
      // )
      // {
      //   // Set headers.
      //   // See: https://stackoverflow.com/questions/51819176/how-to-add-custom-header-in-volley-request-with-kotlin
      //   override fun getHeaders(): MutableMap<String, String> {
      //     val optsHeaderMap = opts!!.getMap("headers")!!.toHashMap()
      //     val headers = HashMap<String, String>()

      //     for ((key, value) in optsHeaderMap) {
      //       if (value is String) {
      //         headers.set(key, value)
      //       }
      //     }
      //     return headers
      //   }

      //   // Set headers.
      //   // See: https://stackoverflow.com/questions/51819176/how-to-add-custom-header-in-volley-request-with-kotlin
      //   override fun parseNetworkResponse(response: NetworkResponse): Response<WritableMap> {
      //     val responseMap: WritableMap = Arguments.createMap()
      //     val status = response.statusCode

      //     try {
      //       var charset: Charset = Charset.forName(HttpHeaderParser.parseCharset(response.headers))
      //       val body: String = String(response.data, charset)
      //     } catch (e: UnsupportedEncodingException) {
      //       val body: String = String(response.data, Charset.forName("UTF-8"))
      //     }

      //     responseMap.putInt("status", status)
      //     // responseMap.putString("body", body)
      //     // responseMap.putMap("headers", HttpHeaderParser.toAllHeaderList(response.header))
      //     return Response.success(responseMap, HttpHeaderParser.parseCacheHeaders(response))
      //   }

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
      // }

      // Set caching settings.
      request.setShouldCache(shouldCache)

      // Add the request to the RequestQueue.
      requestQueue.add(request)

    }

}
