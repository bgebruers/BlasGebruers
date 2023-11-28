const Pelicula = require("../model/peliculas.models.js");

exports.create = (req, res) => {
    // Validate request
    if (!req.body) {
        res.status(400).send({
            message: "datos vacios!"
        });
    }
    const pelicula = new Pelicula({
        //id: req.body.id,
        titulo: req.body.titulo,
        descripcion: req.body.descripcion,
        fecha_lanzamiento: req.body.fecha_lanzamiento,
        duracion: req.body.duracion,
        producer: req.body.producer,
        director: req.body.director,
        genero: req.body.genero,
        urlPelicula: req.body.urlPelicula,
        banner: req.body.banner,
        image: req.body.image
    });
    Pelicula.create(pelicula, (err, data) => {
        if (err)
            res.status(500).send({
                message:
                    err.message || "error al crear la pelicula."
            });
        else res.send(data);
    });
};

exports.list = (req, res) => {
    console.log("llega al listado");
    Pelicula.getAll((err, data) => {
        if (err) {
            res.status(500).send({
                message:
                    err.message || "error al encontrar las peliculas"
            })
        }  
        try {
            res.send(data);
        } catch (error) {
            console.log("error: " + error);
        }
    });
};

exports.getId = (req, res) => {
    Pelicula.findById(req.params.id, (err, data) => {
        if (err) {
            if (err.kind === "not_found") {
                res.status(404).send({
                    message: `pelicula no encontrada id ${req.params.id}.`
                });
            } else {
                res.status(500).send({
                    message: "error al buscar id " + req.params.id
                });
            }
        } else res.send(data);
    });
};

exports.update = (req, res) => {
    if (!req.body) {
        res.status(400).send({
            message: "sin contenido!"
        });
    };

    console.log(req.body);
    Pelicula.updateById(
        req.params.id,
        new Pelicula(req.body),
        (err, data) => {
            if (err) {
                if (err.kind === "not found") {
                    res.status(404).send({
                        message: `pelicula no encontrada id ${req.params.id}`
                    });
                } else {
                    res.status(500).send({
                        message: "error al actualizar la pelicula id" + req.params.id
                    });
                }
            } else {console.log("update: " + data.status);
                res.send(data);}
        }
    );
}

exports.delete = (req, res) => {
    Pelicula.remove(req.params.id, (err, data) => {
        if (err) {
            if (err.kind === "not found") {
                res.status(404).send({
                    message: `Pelicula no encontrada id ${req.params.id}`
                });
            } else {
                res.status(500).send({
                    message: "no se puede borra la pelicula" + req.params.id
                });
            }
        } else {
            res.send({ message: "Pelicula borrada exitosamente" });
        }
    });
};

exports.deleteAll = (req, res) => {
    Pelicula.removeAll((err, data) => {
        if (err) {
            res.status(500).send({
                message:
                    err.message || "error al encontrar las peliculas"
            })
        } else res.send({ "status": 200, "data": data });
    });
};

exports.searchByGender = (req, res) =>{
    Pelicula.searchByGender(req.body.genero,(err, data) =>{
        if(err){
            console.log("ha ocurrido un error: " + err);
        }else{
            res.send(data);
        }
    })
}

exports.proximamente  = (req, res) => {
    Pelicula.proximamente((err, data) => {
        if (err) {
            res.status(500).send({
                message:
                    err.message || "error no hay peliculas proximas"
            })
        }  
        try {
            res.send(data);
        } catch (error) {
            console.log("error: " + error);
        }
    });
};

exports.pasadas  = (req, res) => {
    Pelicula.pasadas((err, data) => {
        if (err) {
            res.status(500).send({
                message:
                    err.message || "error no hay peliculas proximas"
            })
        }  
        try {
            res.send(data);
        } catch (error) {
            console.log("error: " + error);
        }
    });
};