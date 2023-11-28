const User = require("../model/usuario-model.js");
const Tarjeta = require("../model/tarjeta-model.js");

exports.registro = (req, res) => {
    // Validate request
    if (!req.body) {
        res.status(400).send({
            message: "datos vacios!"
        });
    }
    const user = new User({
      nombre : req.body.nombre,
      mail : req.body.mail,
      password : req.body.password,
      tipoPlan : req.body.tipoPlan
    });
    
    const tarjeta = new Tarjeta({
        nroTarjeta : req.body.nroTarjeta,
        vencimiento :  req.body.vencimiento,
        ccv : req.body.ccv,
        idUsuario: '',   //cambiar por la consulta a la bd para saber cual es el usaurio que la carga
    });
    User.registro(user, tarjeta, (err, data) => {
        if (err)
            res.status(500).send({
                message: err.message || "error al crear el nuevo usuario."
            });
        else res.send(data);
    });
    
};

exports.login = (req, res) =>{
    const user = new User({
        nombre : req.body.nombre,
        mail: req.body.mail,
        password: req.body.password
    });
    User.login(user,(err, data) => {
        if (err) {
            if (err.kind === "not_found") {
                res.status(404).send({
                    message: `usuario no encontrado`
                });
            } else {
                res.status(500).send({
                    message: "error al buscar nombre"
                });
            }
        } else {
            //res.sendStatus(data);
            res.status(200).json({ message: "Inicio de sesión exitoso", data });

        }
    });

}