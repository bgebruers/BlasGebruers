const bcrypt = require('bcryptjs');
const sql = require("../db/db.js");
//para generar token
const jwt = require("jsonwebtoken");
const { KEY_APP } = process.env;


// constructor
const User = function (user) {
  this.idUser = user.idUser;
  this.nombre = user.nombre;
  this.mail = user.mail;
  this.password = user.password;
  this.tipoPlan = user.tipoPlan;
};


User.login = async (user, callback) => {
  const password = user.password;

  await sql.query(`SELECT * FROM usuarios WHERE mail = '${user.mail}'`, async (err, resSql) => {    
    if (err) {
      callback(err, null);
      return;
    }

    if (resSql.length === 0) {
      callback(null, { status: 404, message: "Usuario no encontrado", token: null });
      return;
    }

    const usuario = resSql[0];
    if (await bcrypt.compare(password, usuario.password)) {
      const token = jwt.sign({ user_name: usuario.nombre }, KEY_APP);
      usuario.token = token;

      // Actualiza el usuario con el token correcto
      sql.query(
        "UPDATE usuarios SET token = ? WHERE nombre = ?",
        [usuario.token, usuario.nombre],
        (err, res) => {
          if (err) {
            console.log("Error:", err);
            callback(err, null);
          } else {
            if (res.affectedRows === 0) {

              callback({ kind: "not_found" }, null);
            } else {
              callback(null, { status: 200, message: "Inicio de sesión exitoso", userData: usuario });
            }
          }
        }
      );
    } else {
      callback(null, { status: 401, message: "Contraseña incorrecta", token: null });
    }
  });
};


User.registro = (user, tarjeta, result) => {

  bcrypt.hash(user.password, 10, (err, mihash) => {
    if (err) {
      console.log("Error al encriptar la contraseña: ", err);
      result(err, null);
      return;
    }

    // Reemplazar la contraseña original con el hash en newUser
    user.password = mihash;

    sql.query("INSERT INTO usuarios SET ?", user, (err, res) => {
      if (err) {
        console.log("Error al crear el nuevo usario: ", err);
        result(err, null);
        return;
      }
      console.log("Usuario creado: ", { id: res.insertId, ...user });
    });


    sql.query(`SELECT idUser FROM usuarios WHERE nombre = '${user.nombre}'`, (err, res) => {
      if (err) {
        console.log("error: ", err);
        result(err, null);
        return;
      }
      if (res.length) {
        tarjeta.idUsuario = res[0].idUser;
        sql.query("INSERT INTO tarjeta SET ?", tarjeta, (err, res) => {
          console.log(tarjeta.idUsuario);
          if (err) {
            console.log("Error al cargar la tarjeta: ", err);
            result(err, null);
            return;
          }
          console.log("Tarjeta añadida: ", { id: res.insertId, ...tarjeta });
          result(null, { id: res.insertId, ...user });
        });
      }
    });


  });

};


User.cerrarSesion = (idUser, res) => {
  sql.query(`UPDATE usuarios SET token ='' WHERE nombre= '${idUser}'`, (err, result) => {
    if (err) {
      console.log("hubo un error al intentar cerrar sesion");
      res(err, null);
      return;
    }
    console.log("Sesion cerrada correctamente: " + res);
    res(null, 200);

  });

}




module.exports = User;