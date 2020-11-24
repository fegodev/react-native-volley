import * as React from 'react';
import { StyleSheet, View, Text, Button } from 'react-native';
import Volley from 'react-native-volley';

export default function App() {
  const [result, setResult] = React.useState<string | undefined>();

  const fetchData = async function () {
    try {
      const res = await Volley.fetch('https://reactnative.dev/movies.json');
      const text = await res.text()
      setResult(text);
    } catch (e) {
      console.log(JSON.stringify(e));
    }
  };

  return (
    <View style={styles.container}>
      <Button title="Fetch" onPress={fetchData} />
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
