import React, { useEffect, useState } from "react";
import { TouchableOpacity, Button, Text, View, StyleSheet, FlatList, Image } from "react-native";
import { useNavigation } from '@react-navigation/native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { API } from '@env'

//screen principal, hay 5 eventos proximos y 5 pasados 
function HomeScreen() {
  const navigation = useNavigation();

  const [dataNextPelis, setDataNextPelis] = useState([]);
  const [dataNextSerie, setDataNextSerie] = useState([]);
  const [dataPastPelis, setDataPastPelis] = useState([]);
  const [dataPastSerie, setDataPastSerie] = useState([]);
  const dataNextEvent = [...dataNextPelis, ...dataNextSerie];
  const dataPastEvent = [...dataPastPelis, ...dataPastSerie];


  const fetchAllNextPelis = async () => {
    const response = await fetch(`${API}/proximamentePelicula`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'x-access-token': await AsyncStorage.getItem('x-access-token'),
      }
    })
    const res = await response.json();
    console.log(res);
    setDataNextPelis(res);
  };

  const fetchAllNextSerie = async () => {
    const response = await fetch(`${API}/proximamenteSerie`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'x-access-token': await AsyncStorage.getItem('x-access-token'),
      }
    })
    const res = await response.json();
    console.log(res);
    setDataNextSerie(res);
  };

  const fetchAllPastPelis = async () => {
    const response = await fetch(`${API}/peliculaPasadas`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'x-access-token': await AsyncStorage.getItem('x-access-token'),
      }
    })
    const res = await response.json();
    console.log(res);
    setDataPastPelis(res);
  };

  const fetchAllPastSerie = async () => {
    const response = await fetch(`${API}/seriePasadas`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'x-access-token': await AsyncStorage.getItem('x-access-token'),
      }
    })
    const res = await response.json();
    console.log(res);
    setDataPastSerie(res);
  };


  useEffect(() => {
    fetchAllNextPelis();
    fetchAllNextSerie();
    fetchAllPastPelis();
    fetchAllPastSerie();
  }, []);



  const handleImagePress = (idMovie, titulo, descripcion, imagen) => {
    navigation.navigate('DetailStackScreen', {
      screen: 'Detail Event',
      params: { idMovie: idMovie, titulo : titulo, descripcion: descripcion, imagen: imagen },
    });
  };

  return (
    <View style={styles.container}>
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Proximamente</Text>
        <FlatList
          data={dataNextEvent}
          keyExtractor={(item) => item.titulo} // Asegúrate de convertir a cadena o número si es necesario
          horizontal={true}
          showsHorizontalScrollIndicator={false}
          renderItem={({ item }) => (
            <View style={{ padding: 5 }}>
              <TouchableOpacity
                onPress={() => handleImagePress(item.idMovie, item.titulo, item.descripcion, item.banner)}
                style={{ padding: 5 }}
              >
                <Image
                  style={styles.img}
                  source={{ uri: item.imagen }}
                />
              </TouchableOpacity>
            </View>
          )}
        />
        <TouchableOpacity style={{ borderColor: 'black', borderWidth: 1, borderRadius: 10, margin: 5 }} onPress={() => navigation.navigate('Proximamente')}>
          <Text style={{ fontSize: 16, margin: 5, borderRadius: 5, textAlign: 'center' }}>Mas a Estrenar</Text>
        </TouchableOpacity>
      </View>
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Estrenos</Text>
        <FlatList
          data={dataPastEvent}
          keyExtractor={(item) => item.id}
          horizontal={true} // Configura la lista como horizontal
          showsHorizontalScrollIndicator={false}
          renderItem={({ item }) => (
            <View style={{ padding: 5 }}>
              <TouchableOpacity
                onPress={() => handleImagePress(item.idMovie, item.titulo, item.descripcion, item.imagen)}
                style={{ padding: 5 }}
              >
                <Image
                  style={styles.img}
                  source={{ uri: item.banner }}
                />
              </TouchableOpacity>
            </View>
          )}
        />
        <TouchableOpacity style={{ borderColor: 'black', borderWidth: 1, borderRadius: 10, margin: 5 }} onPress={() => navigation.navigate('Estrenos')}>
          <Text style={{ fontSize: 16, margin: 5, borderRadius: 5, textAlign: 'center' }}>Mas Estrenos</Text>
        </TouchableOpacity>
      </View>
    </View>

  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 5,
    backgroundColor: '#FFED8D'
  },
  section: {
    margin: 5,
    flex: 1,
    backgroundColor: '#fbd52c',
    borderRadius: 10,
    borderColor: 'black',
    borderWidth: 1,
  },
  sectionTitle: {
    fontSize: 22,
    fontWeight: 'bold',
    margin: 5,
    textAlign: 'center',
  },
  img: {
    width: 181,
    height: 271
  }
});

export default HomeScreen;