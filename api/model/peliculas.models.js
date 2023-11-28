const sql = require("../db/db.js");
const moment = require('moment');

const Pelicula = function (pelicula) {
    this.titulo = pelicula.titulo,
        this.descripcion = pelicula.descripcion,
        this.fecha_lanzamiento = pelicula.fecha_lanzamiento,
        this.duracion = pelicula.duracion,
        this.producer = pelicula.producer,
        this.director = pelicula.director,
        this.genero = pelicula.genero,
        this.urlPelicula = pelicula.urlPelicula,
        this.banner = pelicula.banner,
        this.imagen = pelicula.image
};

Pelicula.create = (newPelicula, result) => {
    console.log(newPelicula);
    sql.query("INSERT INTO pelicula SET ?", newPelicula, (err, res) => {
        if (err) {
            console.log("error: ", err);
            result(err, null);
            return;
        }
        console.log("Pelicula creada: ", { id: res.insertId, ...newPelicula });
        result(null, { id: res.insertId, ...newPelicula });
    })
};

Pelicula.findById = (id, result) => {
    sql.query(`SELECT * FROM pelicula WHERE idMovie = ${id}`, (err, res) => {
        if (err) {
            console.log("error: ", err);
            result(err, null);
            return;
        }
        if (res.length) {
            console.log("pelicula encontrada: ", res[0]);
            result(null, res[0]);
            return;
        }
        result({ kind: "not-found" }, null);
    });
};

Pelicula.getAll = (result) => {
    console.log("llega al modelo");
    const  currentDate = moment().format('YYYY-MM-DD');

    let query = `SELECT * FROM pelicula WHERE fecha_lanzamiento < '${currentDate}'`;
    sql.query(query, (err, res) => {
        if (err) {
            console.log("error: ", err);
            result(null, err);
            return;
        }
        result(null, res);
    });
};

Pelicula.updateById = (id, pelicula, result) => {

    let updateQuery = "UPDATE pelicula SET ";
    let updateData = [];
    let updateValues = [];

    for (const key in pelicula) {
        if (pelicula[key] !== undefined) {
            updateValues.push(`${key} = ?`);
            updateData.push(pelicula[key]);
        };
    };

    updateQuery += updateValues.join(", ");
    updateQuery += " WHERE idMovie = ? ";
    updateData.push(id);

    sql.query(
        //"UPDATE pelicula SET titulo=?, descripcion=?, fecha_lanzamiento=?, duracion=?, director=?, genero=? WHERE idMovie = ?",
        //[pelicula.titulo, pelicula.descripcion, pelicula.fecha_lanzamiento, pelicula.duracion, pelicula.director, pelicula.genero, id],
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
            console.log("pelicula actualizada: ", { id: id, ...pelicula });
            result(null, { id: id, ...pelicula });
        }
    );

}

Pelicula.remove = (id, result) => {
    sql.query("DELETE FROM pelicula WHERE idMovie = ?", id, (err, res) => {
        if (err) {
            console.log("error: ", err);
            result(null, err);
            return;
        }
        if (res.affectedRows == 0) {
            result({ kind: "not found" }, null);
            return;
        }
        console.log("torta borrada id: ", id);
        result(null, res);
    });
}

Pelicula.removeAll = result => {
    sql.query("DELETE FROM pelicula", (err, res) => {
        if (err) {
            console.log("error: ", err);
            result(null, err);
            return;
        }
        console.log(`deleted ${res.affectedRows} peliculas`)
        result(null, res);
    });

}

Pelicula.searchByGender = (genero, res) => {
    if (genero.trim() !== "") {

        sql.query(`SELECT * FROM pelicula WHERE lower(genero) like "%${genero}%"`, (err, data) => {
            if (err) {
                console.log("error en la sql: " + err);
                res(500, err);
                return;
            }

            if (data.length > 0) {
                res(null, data);
            } else {
                console.log("No se encontraron películas para el género: " + genero);
                res(404, "No se encontraron películas para el género: " + genero);
            }
        });
    } else {
        res(404, "no se puede buscar por un genero vacio");
    }
}

Pelicula.proximamente = (res) => {
    const currentDate = moment().format('YYYY-MM-DD')

    sql.query(`SELECT * FROM pelicula WHERE fecha_lanzamiento > '${currentDate}'`, (err, data) => {
        if (err) {
            console.log("Error en la consulta SQL: " + err);
            res(500, err);
            return;
        }

        if (data.length > 0) {
            res(null, data);
        } else {
            console.log("No se encontraron películas próximas.");
            res(404, "No se encontraron películas próximas.");
        }
    })
}

Pelicula.pasadas = (res) => {
    const currentDate = moment().format('YYYY-MM-DD')

    sql.query(`SELECT * FROM pelicula WHERE fecha_lanzamiento <= '${currentDate}'`, (err, data) => {
        if (err) {
            console.log("Error en la consulta SQL: " + err);
            res(500, err);
            return;
        }

        if (data.length > 0) {
            res(null, data);
        } else {
            console.log("No se encontraron películas próximas.");
            res(404, "No se encontraron películas próximas.");
        }
    })
}

module.exports = Pelicula;