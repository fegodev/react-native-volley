import { NativeModules, } from 'react-native';

type OptionsType = {
  method?: string;
  headers?: object;
  cache?: string;
  body?: string
}

type ResponseType = {
  ok: boolean;
  status: number;
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

  // Sanitize
  if (opts.method) {
    opts.method = opts.method.toUpperCase()
  }
  if (opts.cache) {
    opts.cache = opts.cache.toLowerCase()
  }

  // Extend defaults
  const fetchOpts = Object.assign({
    method: 'GET',
    cache: 'default',
    headers: {},
    body: null
  }, opts);

  const responseString = await Volley.fetch(url, fetchOpts);

  return {
    ok: true,
    status: 200,
    json: () => {
      return new Promise((resolve, reject) => {
        try { resolve(JSON.parse(responseString)) } catch (err) { reject(err) }
      })
    },
    text: () => {
      return new Promise((resolve, reject) => {
        try { resolve(responseString) } catch (err) { reject(err) }
      })
    }
  }

  // TODO: return Response (if possible)
  // try {
  //   const responseString = await Volley.fetch(url, fetchOpts);
  //   const blob = new Blob(responseString, { type: 'application/json' })

  //   return new Response(blob, {
  //     status: 200,
  //     statusText: "OK"
  //   })
  // } catch (error) {
  //   const blob = new Blob(error)
  //   return new Response(blob, {
  //     status: 504,
  //     statusText: error
  //   })
  // }

}

const volleyApi = {
  fetch: volleyFetch
};

export default volleyApi as VolleyType;
