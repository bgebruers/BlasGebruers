import React, { useState, useEffect } from 'react';
import { View, Text, Image, TextInput, TouchableOpacity, FlatList, StyleSheet } from 'react-native';
import { useRoute } from '@react-navigation/native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { API } from '@env'
import { SafeAreaView } from 'react-native-safe-area-context';


//pantalla para ver los detalles del contenido
function EventScren() {
  const route = useRoute();
  const { idMovie, titulo, descripcion, imagen} = route.params;
  console.log("comentario en handle comment " + idMovie);
  
  const [comment, setComment] = useState('');
  const [comments, setComments] = useState([]);
  const [hayComentarios, setHayComentarios] = useState(false)

  const handleComment = () => {
    
    const idContenido = idMovie;
    const comentario = comment;
    const comm = { idContenido, comentario };
    createComment(comm);
    if (comments) {
      setHayComentarios(true);
    }
  };

  const createComment = async (comm) => {
    console.log("nuevo comentario: " +comm.idContenido)
    await fetch(`${API}/newComentario`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'x-access-token': await AsyncStorage.getItem('x-access-token'),
      },
      body: JSON.stringify(comm)
    }).then((response) => {
      if (response.ok) {
        setHayComentarios(true);
        verComentarios(idMovie);
      }
    })
  };

  const verComentarios = async (idMovie) => {
    const response = await fetch(`${API}/verComentarios/${idMovie}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'x-access-token': await AsyncStorage.getItem('x-access-token'),
      },
    })
    const res = await response.json();
    if (res.length > 0) {
      setHayComentarios(true);
    } else {
      setHayComentarios(false);
    }
    setComments(res);
  };

  useEffect(() => {
    verComentarios(idMovie);
  }, []);

  return (
    <SafeAreaView style={{ flex: 1, backgroundColor: '#FFED8D' }}>
      <View style={{ backgroundColor: '#fbd52c', flex: 1, borderColor: 'black', borderWidth: 1, borderRadius: 10, margin: 5, padding: 5 }}>
        <Text style={{ fontSize: 20, fontWeight: 'bold', textAlign: 'center' }}>{titulo}</Text>
        <Image
          source={imagen}
          style={{ width: '100%', height: 200 }}
        />
        <Text style={{ fontSize: 16, marginTop: 10 }}>{descripcion}</Text>
      </View>
      <View style={{ flex: 1, borderColor: 'black', borderWidth: 1, borderRadius: 10, margin: 5, padding: 5 }}>
        <Text style={{ fontSize: 18, marginTop: 20, margin: 10 }}>Comentarios</Text>
        <TextInput
          placeholder="Añadir comentario"
          value={comment}
          onChangeText={(text) => setComment(text)}
          style={{ borderWidth: 1, padding: 10, margin: 10 }}
        />
        <TouchableOpacity style={{ borderColor: 'black', borderWidth: 1, borderRadius: 10, margin: 5, backgroundColor: '#fbd52c' }} onPress={handleComment}>
          <Text style={{ fontSize: 16, margin: 5, borderRadius: 5, textAlign: 'center' }}>Comentar</Text>
        </TouchableOpacity>
        <br />
        <View style={styles.line} />
        {hayComentarios ? (
          <FlatList
            data={comments}
            keyExtractor={(item, index) => index.toString()}
            renderItem={({ item }) => (
              <Text style={styles.comment}>{item.comentario}</Text>
            )}
          />
        ) : (
          <Text style={styles.comment}>No hay comentarios</Text>
        )}
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  line: {
    height: 1,
    width: '100%',
    backgroundColor: 'black', // Puedes ajustar el color según tus preferencias
  },
  comment: {
    padding: 5,
    fontSize: 16
  }
});
export default EventScren;