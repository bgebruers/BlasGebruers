import 'package:blackner/src/pages/home.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:blackner/src/base_datos/database-service.dart';
import './variableGlobal.dart';
import 'dart:io';
import 'package:image_picker/image_picker.dart';

class addPost extends StatefulWidget {
  @override
  _addPostState createState() => _addPostState();
}


class addPostForm extends StatefulWidget {
  @override
  _addPostState createState() => _addPostState();
}

class _addPostState extends State<addPost> {
  final DatabaseService databaseService = DatabaseService();

  final TextEditingController _contentController = TextEditingController();
  int maxCharacters = 250; // Límite de caracteres inicial
  String currentDateTime = '';  //variable para almacenar la hora del posteo.
  
  String formattedDateTime = ''; // Variable de instancia para la fecha y hora

  //tengo todo este codigo para pickear una imagen.
  File? _image;

  final picker = ImagePicker();
  //captura la imagen 
  Future getImagen() async {
    final pickedFile = await picker.pickImage(source: ImageSource.gallery);
      setState(() {
      if (pickedFile != null) {
        _image = File(pickedFile.path);
        //si se elige una imagen recorto la cantidad de caracteres
        _reduceCharacterLimit();
        
      } else {
        print('No image selected.');
      }
      });
  }


  //captura la fecha y la hora
  void captureDateTime() {
    DateTime now = DateTime.now();
    formattedDateTime = DateFormat('dd/MM/yyyy HH:mm:ss').format(now);
  }

  //funcion para recortar la cantidad de caracteres permitidos para agregar 
  void _reduceCharacterLimit() {
    setState(() {
      maxCharacters = 100; // cambiar el límite de caracteres a 100
    });
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          //box para agregar el contenido
          Container(
            decoration: BoxDecoration(
              border: Border.all(),
              borderRadius: BorderRadius.circular(10.0),
            ),
            padding: EdgeInsets.symmetric(horizontal: 16.0),
            child: TextFormField(
              //controlador para capturar lo que el usuario ingresa
              controller: _contentController,
              decoration: const InputDecoration(
                hintText: 'Agregar Contenido',
                border: InputBorder.none,
              ),
              maxLines: null, // Permite multiples líneas de texto
              maxLength: maxCharacters, // limite de caracteres dinámico
            ),
          ),
          SizedBox(height: 16.0),
          //row para botones de video e imagen
          Row(
            mainAxisAlignment: MainAxisAlignment.start,
            children: [
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  IconButton(
                    icon: const Icon(Icons.videocam, color: Colors.grey),
                    onPressed: () {
                      // Lógica para agregar video
                    },
                  ),
                ],
              ),
              const SizedBox(width: 8.0), // Espacio entre los botones
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                 TextButton(
                    onPressed: ()=> getImagen(),                  
                      // Muestra la imagen seleccionada
                    child: _image == null
                      ? const Icon(Icons.image, color: Colors.grey)
                      : Image.file(
                          _image!,
                          height: 200,
                          width: 200,
                        ),
                    
                  ),
                ],
              ),
            ],
          ),
          SizedBox(height: 16.0),
          //row para boton de publicar 
          Row(
            mainAxisAlignment: MainAxisAlignment.center, // Centra el botón "Publicar"
            children: [
              ElevatedButton(
                onPressed: () {
                  // Logica para publicar el contenido
                  String contenido = _contentController.text;
                   if (contenido.isNotEmpty) {
                    captureDateTime(); // Captura la fecha y hora
                    String date = formattedDateTime; // Accede a formattedDateTime para la foto
                   
                    //permite navegar a otras paginas, en este caso a la first page
                    void navigateToHomePage(BuildContext context) {
                      //esta funcion se usa para poder esperar 3 segundos para navegar a la otra, es para que 
                      //se vea el mensaje que se muestra por pantalla.
                      Future.delayed(const Duration(seconds: 0, milliseconds: 300), () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => Home(), 
                          ),
                        );
                      });
                    }
                    //esta funcion me permite ver el resultado de la insersion del post a la bd que viene desde dabase-service
                    //es una funcion que espera recivir un bool que viene del callback
                    void handlePostUpload(bool success) {
                      if (success) {
                        //muestra un mensaje y navega a otra página
                        ScaffoldMessenger.of(context).showSnackBar(
                         const SnackBar(
                            content: Text('Post cargado exitosamente'),
                          ),
                        );
                        //ir a la home
                        navigateToHomePage(context); 
                      } else {
                        //Muestra un mensaje de error
                        ScaffoldMessenger.of(context).showSnackBar(
                          const SnackBar(
                            content: Text('Error al cargar el post.'),
                          ),
                        );
                      }
                    }
                    databaseService.cargarPost(VariableGlobal.userName, date, _image, contenido, "",  handlePostUpload);

                  } else {
                      // Muestra un mensaje de error si el campo está vacío
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(
                        content: Text('Por favor, ingresa contenido antes de publicar.'),
                      ),
                    );
                  }
                 
                },
                child: Text('Publicar'),
              ),
            ],
          ),
        ],
      ),
    );
  }

}

