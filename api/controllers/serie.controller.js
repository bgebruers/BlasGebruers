const Serie = require("../model/series.models.js");

exports.create = (req, res) => {
    // Validate request
    if (!req.body) {
        res.status(400).send({
            message: "datos vacios!"
        });
    }
    const serie = new Serie({
        //id: req.body.id,
        titulo: req.body.titulo,
        descripcion: req.body.descripcion,
        fecha_lanzamiento: req.body.fecha_lanzamiento,
        temporadas: req.body.temporadas,
        producer: req.body.producer,
        director: req.body.director,
        genero: req.body.genero,
        urlSerie: req.body.urlSerie,
        image: req.body.image
    });
    Serie.create(serie, (err, data) => {
        if (err)
            res.status(500).send({
                message:
                    err.message || "error al crear la serie."
            });
        else res.send(data);
    });
};

exports.list = (req, res) => {
    Serie.getAll((err, data) => {
        if (err) {
            res.status(500).send({
                message:
                    err.message || "error al encontrar las series"
            })
        } else res.send({ "status": 200, "data": data });
    });
};

exports.getId = (req, res) => {
    Serie.findById(req.params.id, (err, data) => {
        if (err) {
            if (err.kind === "not_found") {
                res.status(404).send({
                    message: `serie no encontrada id ${req.params.id}.`
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
    Serie.updateById(
        req.params.id,
        new Serie(req.body),
        (err, data) => {
            if (err) {
                if (err.kind === "not found") {
                    res.status(404).send({
                        message: `serie no encontrada id ${req.params.id}`
                    });
                } else {
                    res.status(500).send({
                        message: "error al actualizar la serie id" + req.params.id
                    });
                }
            } else res.send(data);
        }
    );
}

exports.delete = (req, res) => {
    Serie.remove(req.params.id, (err, data) => {
        if (err) {
            if (err.kind === "not found") {
                res.status(404).send({
                    message: `Serie no encontrada id ${req.params.id}`
                });
            } else {
                res.status(500).send({
                    message: "no se puede borra la serie" + req.params.id
                });
            }
        } else {
            res.send({ message: "Serie borrada exitosamente" });
        }
    });
};

exports.deleteAll = (req, res) => {
    Serie.removeAll((err, data) => {
        if (err) {
            res.status(500).send({
                message:
                    err.message || "error al encontrar las serie"
            })
        } else res.send({ "status": 200, "data": data });
    });
};

exports.searchByGender = (req, res) =>{
    Serie.searchByGender(req.body.genero,(err, data) =>{
        if(err){
            console.log("ha ocurrido un error: " + err);
        }else{
            res.send(data);
        }
    })
}

exports.proximamente  = (req, res) => {
    Serie.proximamente((err, data) => {
        if (err) {
            res.status(500).send({
                message:
                    err.message || "error no hay series proximas"
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
    Serie.pasadas((err, data) => {
        if (err) {
            res.status(500).send({
                message:
                    err.message || "error no hay series proximas"
            })
        }  
        try {
            res.send(data);
        } catch (error) {
            console.log("error: " + error);
        }
    });
};