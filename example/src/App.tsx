import * as React from 'react';
import { StyleSheet, View, Text, Button } from 'react-native';
import Volley from 'react-native-volley';

export default function App() {
  const [result, setResult] = React.useState<string | undefined>();
  const [status, setStatus] = React.useState<number | undefined>();

  const urlAndOptsForMethod: { [index: string]: any } = {
    'GET': {
      url: 'https://reqres.in/api/users/2',
      opts: undefined
    },
    'POST': {
      url: 'https://reqres.in/api/users',
      opts: {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          "name": "morpheus",
          "job": "leader"
        })
      }
    },
    'PUT': {
      url: 'https://reqres.in/api/users/2',
      opts: {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          "name": "morpheus",
          "job": "leader"
        })
      }
    },
    'DELETE': {
      url: 'https://reqres.in/api/users/2',
      opts: {
        method: 'DELETE'
      }
    }
  }

  const fetchData = async function (method: string) {
    try {
      const context = urlAndOptsForMethod[method]
      const res = await Volley.fetch(context.url, context.opts)
      if (res?.ok) {
        console.log(JSON.stringify(res));
        const status = res.status
        const text = await res.text()
        setStatus(status)
        setResult(text)
      }

    } catch (e) {
      console.log(JSON.stringify(e));
    }
  };

  return (
    <View style={styles.container}>
      <Button title="GET" onPress={() => fetchData('GET')} />
      <Button title="POST" onPress={() => fetchData('POST')} />
      <Button title="PUT" onPress={() => fetchData('PUT')} />
      <Button title="DELETE" onPress={() => fetchData('DELETE')} />
      <Text>Status: {status}</Text>
      <Text>Result: {result}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
