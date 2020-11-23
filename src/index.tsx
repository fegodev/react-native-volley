import { NativeModules } from 'react-native';

type VolleyType = {
  multiply(a: number, b: number): Promise<number>;
  fetch(url: string, opts: object): Promise<string>;
};

const { Volley } = NativeModules;

export default Volley as VolleyType;
