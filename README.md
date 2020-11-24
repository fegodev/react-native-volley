# react-native-volley

React Native module that wraps [Google's Volley HTTP library for Android](https://github.com/google/volley).

> Background: On Android in React Native `[Fetch API](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API)` and `[XMLHttpRequest](https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest)` depend on `google.webkit` API and `WebView`. On some devices, such as 'Wear OS by Google' smartwatches, `webkit` is not supported. With Volley you can make `webkit` independent HTTP requests.

**Work in progress. ONLY use for testing.**

## Installation

```sh
npm install react-native-volley
// or
yarn add react-native-volley
```
<details>
  <summary>Working with ReNative? Click here...</summary>

  In your `renative.json` file add the following:
  ```js
  //...
  "plugins": {
      // ...
      "react-native-volley": {
          "version": "github:fegodev/react-native-volley#0.0.1",
          "android": {
              "package": "com.reactnativevolley.VolleyPackage",
              "implementations": [
                  "'com.android.volley:volley:1.1.1'"
              ]
          },
          "androidwear": {
              "package": "com.reactnativevolley.VolleyPackage",
              "implementations": [
                  "'com.android.volley:volley:1.1.1'"
              ]
          }
      }
  }
  ```
</details>

## Usage

`react-native-volley` mimics the structure of [Fetch API](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API) but with limitations (see example options below).

```js
import Volley from 'react-native-volley';
// ...
const response = await Volley.fetch('https://reactnative.dev/movies.json')
const movies = await response.json().movies
// ...
```

`react-native-volley` is built for Android as a workaround for missing `webkit` support. However, you can safely use it on iOS as well. This works because `react-native-volley` simply returns a standard Fetch API promise when calling `Volley.fetch` on iOS.


## Example with options

```js
// Example POST method implementation:
async function postData(url = '', data = {}) {
  // Default options are marked with *
  const response = await Volley.fetch(url, {
    method: 'POST', // *GET, POST, PUT, DELETE, OPTIONS
    cache: 'no-cache', // *default, no-cache, force-cache (NOT SUPPORTED: reload, only-if-cached)
    headers: {
      'Content-Type': 'application/json'
      // 'Content-Type': 'application/x-www-form-urlencoded',
    },
    body: JSON.stringify(data) // body data type must match "Content-Type" header
    // Other options - that might be available on Fetch API - are ignored
  });
  return response.json(); // Parses JSON response into native JavaScript objects
}
```

Then somewhere in an `async` function in your code...
```js
const data = await postData('https://example.com/answer', { answer: 42 })
console.log(data); // JSON data parsed by `response.json()` call
```

## Cross platform best practices

If you like to use Volley on Android only when needed, best practice is to build a little request service that handles the different cases for you.

something like services/api.ts
```ts
// ...
export default {
  call: function(url: string, opts: object = {}) {
    if (isWearOs) { // device needs Volley
      return Volley.fetch(url, opts)
    } else { // we can use Fetch API
      return fetch(url, opts)
    }
  }
}
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
