const Favorite = require("../model/favorite.models.js");

exports.create = (req, res) => {
    // Validate request
    if (!req.body) {
        res.status(400).send({
            message: "datos vacios!"
        });
    }
    const favorite = new Favorite({
        id_cliente: req.body.id_cliente,
        id_contenido: req.body.id_contenido
    });
    Favorite.create(favorite, (err, data) => {
        if (err)
            res.status(500).send({
                message:
                    err.message || "error al crear el Favorito."
            });
        else res.send(data);
    });
};

exports.list = (req, res) => {
    Favorite.getAll((err, data) => {
        if (err) {
            res.status(500).send({
                message:
                    err.message || "error al encontrar los Favoritos"
            })
        } else res.send({ "status": 200, "data": data });
    });
};

exports.delete = (req, res) => {
    Favorite.remove(req.params.id, (err, data) => {
        if (err) {
            if (err.kind === "not found") {
                res.status(404).send({
                    message: `Favorito no encontrada id ${req.params.id}`
                });
            } else {
                res.status(500).send({
                    message: "no se puede borra el favorito" + req.params.id
                });
            }
        } else {
            res.send({ message: "Favorito borrado exitosamente" });
        }
    });
};
