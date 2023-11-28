import 'package:flutter/material.dart';
import './home.dart';
import './variableGlobal.dart';


class vlack extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      initialRoute: '/',
      routes: {
          //ruta inicial, cuando se inicia la app comienza desde aqui
        '/': (context) => MyHomePage(),
        //ruta para ir a home, se usa esto para poder enviar el contexto si se ingresaron los datos
        '/home': (context) => Home(),
      },
      debugShowCheckedModeBanner: false,
    );
  }
}

class MyHomePage extends StatefulWidget {
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  

  void _login(BuildContext context) {
    String username = _usernameController.text;
    String password = _passwordController.text;
    
    VariableGlobal.userName = username;

    if (username.isNotEmpty && password.isNotEmpty) {
      //envia el parametro a /home sino estan vacios
      Navigator.pushNamed(context, '/home');
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Debes completar todos los campos'),
          ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
     appBar: AppBar(
        title: const Text("Vlack"),
        titleSpacing: 110.0,  //espaciado de texto, por lo que abajo me lo corria asique arregle a mano 
        leading: Container(), //se usa esto porque al usar navegator.push me agrega la flecha para volver
                              //entonces la hago no visible ni accesible
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
     
            TextField(
              controller: _usernameController,
              decoration: const InputDecoration(
                labelText: 'Nombre de Usuario',
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 16.0),
            TextField(
              controller: _passwordController,
              decoration: const InputDecoration(
                labelText: 'Contraseña',
                border: OutlineInputBorder(),
              ),
            ),
            SizedBox(height: 16.0),
            ElevatedButton(
              onPressed: () => _login(context),
              child: Text('Iniciar Sesión'),
            ),
          ],
        ),
      ),
    );
  }
}
