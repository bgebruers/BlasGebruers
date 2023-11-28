const sql = require("../db/db.js");
const Favorito = function(favorito){
    this.idUser = favorito.idUser,
    this.idContenido = favorito.idContenido
}

Favorito.añadir = (fav, result)=>{
   
    sql.query("INSERT INTO favoritos SET ?", fav, (err, res) => {
        if (err) {
            console.log("Error al crear el nuevo usario: ", err);
            result(err, null);
            return;
        }
        console.log("Añadito a favoritos correctamente");
        result(null, 200);
    });
}

module.exports = Favorito;
