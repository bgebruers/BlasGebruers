const express = require("express");
const cors = require("cors");
require("dotenv").config();
const {API_PORT} = process.env;
const bcrypt = require("bcryptjs");

const usuarios = require('./controllers/usuario-controller')
const auth = require("./middleware/auth");

const app = express();

app.use(cors());

// parse requests of content-type - application/json
app.use(express.json());

// parse requests of content-type - application/x-www-form-urlencoded
app.use(express.urlencoded({ extended: true }));

// simple route
app.get("/", (req, res) => {
    res.json({ message: "Hola soy la api de node" });
});

//ruta para ir al registrase
app.post("/registro", usuarios.registro);

//ruta para iniciar sesion
app.post("/login", usuarios.login);

//ruta que se puede acceder si estas loguado
app.get("/solologueado", auth,(req, res) => {

  res.json({ message: "Hola soy la api de node" });
});


// set port, listen for requests
const PORT = process.env.PORT || API_PORT;  //SE ROMPIO AL CAMBIAR AL 8080 PARA USAR CON EL REACT VOLVER AL 80
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}.`);
});