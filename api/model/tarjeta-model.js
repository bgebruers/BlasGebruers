// constructor
const Tarjeta = function (tarjeta) {
  this.nroTarjeta = tarjeta.nroTarjeta;
  this.vencimiento = tarjeta.vencimiento;
  this.ccv= tarjeta.ccv;
  this.idUsuario = tarjeta.idUsuario;
};

module.exports = Tarjeta;