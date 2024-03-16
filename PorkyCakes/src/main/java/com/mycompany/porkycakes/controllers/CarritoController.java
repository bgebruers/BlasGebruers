package com.mycompany.porkycakes.controllers;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycompany.porkycakes.daos.generics.CarritoDao;
import com.mycompany.porkycakes.daos.sqlImplementations.CarritoDAOImpl;
import com.mycompany.porkycakes.model.Producto;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.templates.velocity.VelocityTemplateEngine;
import utility.Logging;
import utility.Path;

public class CarritoController {
   	Logger logger = LoggerFactory.getLogger(CarritoController.class);
	private CarritoDao carritoDAO;

    public CarritoController(CarritoDao carritoDAO) {
        this.carritoDAO = carritoDAO;
    }

	public Route agregarCarrito = (Request req, Response res) -> {
		HashMap<String, Object> model = new HashMap<String, Object>();
        boolean respu = carritoDAO.agregarProducto(req.queryParams("prod"));
		
        if (respu) {
			model.put("temp", Path.Template.RESPUESTA_CARRITO);
		}

		logger.info(Logging.getMessage(req, res));
		return new VelocityTemplateEngine().render(new ModelAndView(model, Path.Template.LAYOUT));
	};

	public Route verCarrito = (Request req, Response res) -> {
		HashMap<String, Object> model = new HashMap<String, Object>();
		Producto p = new Producto();

		List<Producto> respu = carritoDAO.verCarrito();

		model.put("respuesta", respu);
		model.put("prod", p);
		model.put("temp", Path.Template.VER_CARRITO);

		logger.info(Logging.getMessage(req, res));
		return new VelocityTemplateEngine().render(new ModelAndView(model, Path.Template.LAYOUT));
	};

	public Route vaciarCarrito = (Request req, Response res) -> {
		HashMap<String, Object> model = new HashMap<String, Object>();
        Boolean respu = carritoDAO.vaciarCarrito();

		if (respu) {
			res.status(200);
			model.put("temp", Path.Template.CARRITO_VACIO);
			logger.info(Logging.getMessage(req, res));
		} else {
			res.status(503);
			res.body("Tuvimos problemas para vaciar su carrito :(");
			logger.error(Logging.getMessage(req, res));
		}

		return new VelocityTemplateEngine().render(new ModelAndView(model, Path.Template.LAYOUT));
	};

}