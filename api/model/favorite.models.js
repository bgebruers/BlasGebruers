const sql = require("../db/db.js");


const Favorite = function(favoritos){
    this.id_cliente = favoritos.id_cliente,
    this.id_contenido = favoritos.id_contenido
}

Favorite.create = (newFavorite, result) =>{
    sql.query("INSERT INTO favorite SET ?", newFavorite, (err, res)=>{
        if (err) {
            console.log("error: ", err);
            result(err, null);
            return;
        }
        console.log("Favorito creado: ", { id: res.insertId, ...newFavorite });
        result(null, { id: res.insertId, ...newFavorite });
    })
}

Favorite.getAll = (result) => {
    let query = "SELECT * FROM favorite";
    sql.query(query, (err, res) => {
        if (err) {
            console.log("error: ", err);
            result(null, err);
            return;
        }
        console.log("Favoritos: ", res);
        result(null, res);
    });
}

Favorite.remove = (id, result) => {
    sql.query("DELETE FROM favorite WHERE id_cliente = ?", id, (err, res) => {
        if (err) {
            console.log("error: ", err);
            result(null, err);
            return;
        }
        if (res.affectedRows == 0) {
            result({ kind: "not found" }, null);
            return;
        }
        console.log("favorito borrado id: ", id);
        result(null, res);
    });
}



module.exports = Favorite;