const sql = require("../db/db.js");
const moment = require('moment');

const Serie = function (serie) {
    this.titulo = serie.titulo,
    this.descripcion = serie.descripcion,
    this.fecha_lanzamiento = serie.fecha_lanzamiento,
    this.temporadas = serie.temporadas,
    this.producer = serie.producer,
    this.director = serie.director,
    this.genero = serie.genero,
    this.urlSerie = serie.urlSerie,
    this.image = serie.image
};


Serie.create = (newSerie, result) => {
    sql.query("INSERT INTO serie SET ?", newSerie, (err, res) => {
        if (err) {
            console.log("error: ", err);
            result(err, null);
            return;
        }
        console.log("Serie creada: ", { id: res.insertId, ...newSerie });
        result(null, { id: res.insertId, ...newSerie });
    })
};

Serie.findById = (id, result) => {
    sql.query(`SELECT * FROM serie WHERE idSerie = ${id}`, (err, res) => {
        if (err) {
            console.log("error: ", err);
            result(err, null);
            return;
        }
        if (res.length) {
            console.log("serie encontrada: ", res[0]);
            result(null, res[0]);
            return;
        }
        result({ kind: "not-found" }, null);
    });
};

Serie.getAll = (result) => {
    const currentDate = moment().format('YYYY-MM-DD');
    let query = `SELECT * FROM serie WHERE fecha_lanzamiento < ${currentDate}`;
    sql.query(query, (err, res) => {
        if (err) {
            console.log("error: ", err);
            result(null, err);
            return;
        }
        console.log("serie: ", res);
        result(null, res);
    });
};

Serie.updateById = (id, serie, result) => {

    let updateQuery = "UPDATE serie SET ";
    let updateData = [];
    let updateValues = [];

    for (const key in serie) {
        if (serie[key] !== undefined) {
            updateValues.push(`${key} = ?`);
            updateData.push(serie[key]);
        };
    };

    updateQuery += updateValues.join(", ");
    updateQuery += " WHERE idSerie = ? ";
    updateData.push(id);

    sql.query(
        //"UPDATE serie SET titulo=?, descripcion=?, fecha_lanzamiento=?, duracion=?, director=?, genero=? WHERE idMovie = ?",
        //[serie.titulo, serie.descripcion, serie.fecha_lanzamiento, serie.duracion, serie.director, serie.genero, id],
        updateQuery, updateData,
        (err, res) => {
            if (err) {
                console.log("error: ", err);
                result(null, err);
                return;
            }
            if (res.affectedRows == 0) {
                result({ kind: "not_found" }, null);
                return;
            }
            console.log("serie actualizada: ", { id: id, ...serie });
            result(null, { id: id, ...serie });
        }
    );

}

Serie.remove = (id, result) => {
    sql.query("DELETE FROM serie WHERE idSerie = ?", id, (err, res) => {
        if (err) {
            console.log("error: ", err);
            result(null, err);
            return;
        }
        if (res.affectedRows == 0) {
            result({ kind: "not found" }, null);
            return;
        }
        console.log("serie borrada id: ", id);
        result(null, res);
    });
}

Serie.removeAll = result => {
    sql.query("DELETE FROM serie", (err, res) => {
        if (err) {
            console.log("error: ", err);
            result(null, err);
            return;
        }
        console.log(`deleted ${res.affectedRows} serie`)
        result(null, res);
    });

}


Serie.searchByGender = (genero, res) =>{
    if (genero.trim() !== "") {
        
        sql.query(`SELECT * FROM serie WHERE lower(genero) like "%${genero}%"`, (err, data)=>{
            if (err) {
                console.log("error en la sql: " + err);
                res(500, err);
                return;
            }
            if (data.length > 0) {
                res(null, data);
            } else {
                console.log("No se encontraron serie para el género: " + genero);
                res(404, "No se encontraron serie para el género: " + genero);
            }
        });
    }else{
        res(404, "no se puede buscar por un genero vacio");
    }
}

Serie.proximamente = (res) => {
    const currentDate = moment().format('YYYY-MM-DD')

    sql.query(`SELECT * FROM serie WHERE fecha_lanzamiento > '${currentDate}'`, (err, data) => {
        if (err) {
            console.log("Error en la consulta SQL: " + err);
            res(500, err);
            return;
        }

        if (data.length > 0) {
            res(null, data);
        } else {
            console.log("No se encontraron series próximas.");
            res(404, "No se encontraron series próximas.");
        }
    })
}

Serie.pasadas = (res) => {
    const currentDate = moment().format('YYYY-MM-DD')

    sql.query(`SELECT * FROM serie WHERE fecha_lanzamiento <= '${currentDate}'`, (err, data) => {
        if (err) {
            console.log("Error en la consulta SQL: " + err);
            res(500, err);
            return;
        }

        if (data.length > 0) {
            res(null, data);
        } else {
            console.log("No se encontraron series próximas.");
            res(404, "No se encontraron series próximas.");
        }
    })
}

module.exports = Serie;