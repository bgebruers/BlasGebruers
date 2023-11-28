import 'package:cloud_firestore/cloud_firestore.dart';
import 'dart:io';
import 'package:firebase_storage/firebase_storage.dart';


//tipo de dato para poder usar callback y mandar el exito o el fracaso a la pagina addPost, 
//pq queria mostrar un mensaje por pantalla y no podia hacerlo sin el context
typedef UploadCallback = void Function(bool success);


//esta es una clase singleton que se encarga de conectar con la bd y traer la coleccion.
class DatabaseService {

  final CollectionReference posteoCollection =
      FirebaseFirestore.instance.collection('posteo');
  Stream<QuerySnapshot<Map<String, dynamic>>> get posteosStream {
    //ordena por el mas reciente
    return  FirebaseFirestore.instance.collection('posteo').orderBy('date', descending: true).snapshots();
  }
  


  //esta funcion buscar el id del documento a actualizarle el nuevo valor, y luego actualiza.
  //compara el id del doc y el texto del doc, pq al haber varios nombre de usuarios iguales
  //busca siempre el mismo id y modifica siempre el mismo doc.
  void actualizarValoracion(
      String nombreUsuario, String texto, String nuevoValor) {
    // Busca el ID del documento
    posteoCollection
        .where('nombreUsuario', isEqualTo: nombreUsuario)
        .where('texto', isEqualTo: texto)
        .get()
        .then((querySnapshot) {
          if (querySnapshot.docs.isNotEmpty) {
            final docID = querySnapshot.docs.first.id;
            // Actualiza el campo
            posteoCollection.doc(docID).update({
              'valoracion': nuevoValor,
            });
          } else {
            print("error de id");
          }
        }).catchError((error) {
          print("Error al obtener el ID del documento: $error");
          
        });
  }

  Future<void> cargarPost(String username, String date, File? _image, String texto, String valoracion,  UploadCallback callback,) async {
   
   /* ESTO ESTA COMENTADO PORQUE SINO NO ME DEJA POSTEAR, 
      SERIA LA LOGICA QUE SE APLICARIA PARA CARGAR LAS IMAGENES.
    String media = _image.toString();
    if(media != ''){
      media = cargarImagen(_image!) as String;
    }*/

    //si la imagen es nula carga el posteo, sino carga la imagen y todo el resto
      posteoCollection.add({
        'nombreUsuario': username,
        'date': date,
        'media':'',
        'texto': texto,
        'valoracion': valoracion,
      })
        // exito: los datos se agregaron correctamente  
      .then((_) {
        //devuelve verdadero al addPost.dart
        callback(true); // Éxito
      })
      .catchError((error) {
        // Error: ocurrió un error al agregar los datos
        print('Error al agregar datos: $error');
        callback(false); // fracaso
      });
  
  }

  //carga en la storage y devuelve la url para escribirla en el campo media de la bd
  Future<String> cargarImagen(File file) async{
    final Reference storageReference = FirebaseStorage.instance
          .ref()
          .child('images')
          .child(DateTime.now().toString());
    final UploadTask upTask = storageReference.putFile(file);
    final TaskSnapshot taskSnapshot = await upTask;

    if(taskSnapshot.state == TaskState.success){
      final String fileUrl = await taskSnapshot.ref.getDownloadURL();
      return fileUrl;
    }else{
      return "error al cargar la imagen";
    }

  }

 
}

