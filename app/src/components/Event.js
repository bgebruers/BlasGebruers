import React from "react";
import { View, FlatList, TouchableOpacity, Image, Text } from "react-native";

function Event(data, { navigation }, horizontal, limit) {
  const handleImagePress = (idMovie) => {
    navigation.navigate('DetailStackScreen', {
      screen: 'Detail Event',
      params: { idMovie: idMovie },
    });
  };

  const limitedData = limit ? data.slice(0, limit) : data;

  return (
    <FlatList
      data={limitedData}
      keyExtractor={(item) => item.titulo}
      horizontal={horizontal} // Configura la lista como horizontal
      showsHorizontalScrollIndicator={false}
      renderItem={({ item }) => (
        <View style={{ padding: 10 }}>
          <Text>Título: {item.titulo}</Text>
          <Text>Fecha: {new Date(item.fecha_lanzamiento).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
          })}</Text>
          <TouchableOpacity
            onPress={() => handleImagePress(item.idMovie)}
            style={{ paddingTop: 5 }}
          >
            <Image
              style={styles.img}
              source={{ uri: item.banner }}
            />
          </TouchableOpacity>
        </View>
      )}
    />
  )
}

export default Event;

const styles = {
  container: {
    flex: 1,
    padding: 16,
    justifyContent: 'center', // Center vertically
    alignItems: 'center', // Center horizontally
  },
  section: {
    marginBottom: 20,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
  },
  img: {
    width: 181,
    height: 271
  }
};