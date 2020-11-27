import { NativeModules, } from 'react-native';

type OptionsType = {
  method?: string;
  headers?: { [index: string]: string };
  cache?: string;
  body?: string
}

type ResponseType = {
  ok: boolean;
  status: number;
  statusText: string;
  headers: Headers;
  text: Function;
  json: Function;
}

type VolleyType = {
  fetch(url: string | null, opts?: Partial<OptionsType>): Promise<ResponseType>;
};

const { Volley } = NativeModules;

/**
 * Performs a HTTP request to the provided URL with optional options.
 *
 * @param url
 * @param opts
 */
async function volleyFetch(url: RequestInfo, opts: Partial<OptionsType> = {}) {

  // Sanitize.
  if (opts.method) {
    opts.method = opts.method.toUpperCase()
  }
  if (opts.cache) {
    opts.cache = opts.cache.toLowerCase()
  }

  // Extend defaults.
  const fetchOpts = Object.assign({
    method: 'GET',
    cache: 'default',
    headers: {},
    body: null
  }, opts);

  try {
    // Await native response.
    const nativeResponse = await Volley.fetch(url, fetchOpts);

    // Return response.
    return {
      ok: true,
      status: nativeResponse.status,
      statusText: 'OK',
      headers: new Headers(nativeResponse.headers || {}),
      json: () => {
        return new Promise((resolve, reject) => {
          try { resolve(JSON.parse(nativeResponse.body)) } catch (err) { reject(err) }
        })
      },
      text: () => {
        return new Promise((resolve, reject) => {
          try { resolve(nativeResponse.body) } catch (err) { reject(err) }
        })
      },
      blob: () => {
        return new Promise((resolve, reject) => {
          let blobType: string = ''
          if (opts && opts.headers && opts.headers['Content-Type']) {
            blobType = opts.headers['Content-Type']
          }
          try {
            resolve(new Blob(nativeResponse.body, { type: blobType }))
          } catch (err) { reject(err) }
        })
      }
    }
  } catch (err) {
    // Throw error.
    throw new Error(err)
  }

}

const volleyApi = {
  fetch: volleyFetch
};

export default volleyApi as VolleyType;
