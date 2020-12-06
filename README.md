# react-native-volley

React Native module that wraps [Google's Volley HTTP library for Android](https://github.com/google/volley).

> Background: On Android in React Native `Fetch API` or `XMLHttpRequest` depend on the `google.webkit` API and `WebView`. On some devices, such as smartwatches for 'Wear OS by Google' (formerly 'Android Wear'), `webkit` is not supported. With Volley you can make `webkit` independent HTTP requests on Android.

## Installation

```sh
npm install react-native-volley
```
*- or -*
```sh
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
          "version": "^0.1.1", // <- Replace with latest version
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

`react-native-volley` mimics [Fetch API](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API) but with limitations (see [example with options](#example-with-options) below).

```js
import Volley from 'react-native-volley';

// Somewhere in an `async` function ...
const response = await Volley.fetch('https://reactnative.dev/movies.json')
const movies = await response.json().movies
// ...
```

`react-native-volley` is built for Android as a workaround for missing `webkit` support. If you like to use it cross platform, please read the [cross platform example](#cross-platform-example).


## Example with options

```js
// Example POST method implementation:
async function postData(url = '', data = {}) {
  // Default options are marked with *
  const response = await Volley.fetch(url, {
    method: 'POST', // *GET, POST, PUT, DELETE, ...
    cache: 'no-cache', // *default, no-cache, no-store, force-cache
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

## Cross platform example

If you like to use Volley cross platform and only when needed, best practice is to build a tiny service that handles the different cases for you.

*something like* `../services/HttpService.ts`
```ts
import Volley from 'react-native-volley';

// Add `useVolleyForFetch` logic.
import { Platform } from 'react-native'
const useVolleyForFetch = Platform.OS === 'android'

// Using ReNative? You might want to do this instead...
// import { isAndroidWear } from 'renative'
// const useVolleyForFetch = isAndroidWear

export class HttpService {
  async fetch(url: string, opts: object = {}) {
    if (useVolleyForFetch) {
      // Device should use Volley.
      return Volley.fetch(url, opts)
    } else {
      // We can use Fetch API (or something else).
      return fetch(url, opts)
    }
  }
}
```
*Then in some component...*
```ts
import { HttpService } from '../path/to/services/HttpService.ts'
// In some `async` function...
const response = await HttpService.fetch('https://reactnative.dev/movies.json')
// ...
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT