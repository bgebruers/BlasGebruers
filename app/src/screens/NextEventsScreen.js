import React, { useState, useEffect } from "react";
import { View, Text, FlatList, TouchableOpacity, AppRegistry } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { API } from '@env'


//screen para proximos eventos
function NextEventsScreen({ navigation }) {

  const [dataPelis, setDataPelis] = useState([]);
  const [dataSerie, setDataSerie] = useState([]);
  const dataEvent = [...dataPelis, ...dataSerie];

  const fetchAllPelis = async () => {
    const response = await fetch(`${API}/proximamentePelicula`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'x-access-token': await AsyncStorage.getItem('x-access-token'),
      }
    })
    const res = await response.json();
    setDataPelis(res);
  };

  const fetchAllSerie = async () => {
    const response = await fetch(`${API}/proximamenteSerie`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'x-access-token': await AsyncStorage.getItem('x-access-token'),
      }
    })
    const res = await response.json();
    setDataSerie(res);
  };

  useEffect(() => {
    fetchAllPelis();
    fetchAllSerie();

  }, []);



  // Ordenar los eventos del más viejo al más nuevo por año, mes y día
  const eventosOrdenados = dataEvent.sort((a, b) => {
    const fechaA = new Date(a.date);
    const fechaB = new Date(b.date);

    if (fechaA.getFullYear() !== fechaB.getFullYear()) {
      return fechaA.getFullYear() - fechaB.getFullYear();
    }

    if (fechaA.getMonth() !== fechaB.getMonth()) {
      return fechaA.getMonth() - fechaB.getMonth();
    }

    return fechaA.getDate() - fechaB.getDate();
  });

  // Separar los eventos por meses
  const eventosPorMes = eventosOrdenados.reduce((acc, evento) => {
    const mes = new Date(evento.date).toLocaleString('default', { month: 'long' });
    acc[mes] = acc[mes] || [];
    acc[mes].push(evento);
    return acc;
  }, {});


  const handleEventPress = (idMovie, titulo, descripcion, imagen) => {
    navigation.navigate('DetailStackScreen', {
      screen: 'Detail Event',
      params: { idMovie: idMovie, titulo : titulo, descripcion: descripcion, imagen: imagen },
    });
  };

  return (
    <View style={{ backgroundColor: '#ffed8d', flex: 1 }}>
      <Text style={{ fontSize: 20, fontWeight: 'bold', marginBottom: 10, textAlign: 'center' }}>Próximos Estrenos</Text>
      <FlatList
        data={Object.entries(eventosPorMes)}
        keyExtractor={(item) => item[0]}
        renderItem={({ item }) => (
          <View style={{ marginBottom: 15 }}>
            <FlatList
              data={item[1]}
              keyExtractor={(evento) => evento.titulo}
              renderItem={({ item: evento }) => (
                <View style={{ borderWidth: 1, borderColor: 'black', borderRadius: 10, margin: 5, padding: 5 }}>
                  <TouchableOpacity
                    onPress={() => handleEventPress(evento.idMovie, evento.titulo, evento.descripcion, evento.imagen)}
                    style={{ marginBottom: 10 }}
                  >
                    <Text style={{ fontSize: 18, fontWeight: 'bold', marginBottom: 5 }}>{evento.titulo}</Text>
                    <Text>{evento.fecha_lanzamiento}</Text>
                    <Text>{evento.descripcion}</Text>
                  </TouchableOpacity>
                </View>
              )}
            />
          </View>
        )}
      />
    </View>
  );
};

export default NextEventsScreen;