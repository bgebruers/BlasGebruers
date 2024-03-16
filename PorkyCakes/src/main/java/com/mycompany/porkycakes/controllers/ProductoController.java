package com.mycompany.porkycakes.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.porkycakes.porkyMain;
import com.mycompany.porkycakes.daos.generics.ProductoDao;
import com.mycompany.porkycakes.daos.sqlImplementations.ProductoDAOImpl;
import com.mycompany.porkycakes.model.Categorias;
import com.mycompany.porkycakes.model.Producto;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.templates.velocity.VelocityTemplateEngine;
import utility.Logging;
import utility.PasswordAuthentication;
import utility.Path;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductoController {
	Logger logger = LoggerFactory.getLogger(ProductoController.class);
	private ProductoDao productoDAO;

	public ProductoController(ProductoDao productoDAO) {
		this.productoDAO = productoDAO;
	}

	public Route buscarxNombre = (Request req, Response res) -> {
		HashMap<String, Object> model = new HashMap<String, Object>();

		String busqueda = req.queryParams("prod");
		List<Producto> respu = productoDAO.buscarPorNombre(busqueda);

		if (!respu.isEmpty()) {
			model.put("respuesta", respu);
			model.put("busqueda", busqueda);
			model.put("temp", Path.Template.SUGERENCIA);
		} else {
			model.put("temp", Path.Template.SIN_RESULTADO);
		}

		logger.info(Logging.getMessage(req, res));
		return new VelocityTemplateEngine().render(new ModelAndView(model, Path.Template.LAYOUT));
	};

	public Route producto = (Request req, Response res) -> {
		HashMap<String, Object> model = new HashMap<String, Object>();
		Producto p = productoDAO.buscarPorId(req.queryParams("prod"));

		model.put("p", p);
		model.put("temp", Path.Template.MOSTRAR);

		logger.info(Logging.getMessage(req, res));
		return new VelocityTemplateEngine().render(new ModelAndView(model, Path.Template.LAYOUT));
	};

	HashMap<String, Object> model = new HashMap<String, Object>();
	public Route cargarProdForm = (Request req, Response res) -> {
		if(!porkyMain.loggedIn) {
			PasswordAuthentication.notLoggedInRedirection(req, res);
			logger.info(Logging.getMessage(req, res));
			return res;
		}

		model.put("dontShow", true);
		model.put("temp", "templates/cargarProd.vsl");
		model.put("categorias", Categorias.values());
		
		logger.info(Logging.getMessage(req, res));
		return new VelocityTemplateEngine().render(new ModelAndView(model, "templates/layouts.vsl"));
	};

	public Route cargarProd = (Request req, Response res) -> {
		ObjectMapper mapper = new ObjectMapper();
		model.put("color", "#bd0606");

		try {
			Producto prod = mapper.readValue(req.body(), Producto.class);

			if (prod.getNombre() == null || prod.getNombre().trim().isEmpty()) {
				res.status(400);
				model.put("mensaje", "El nombre es obligatorio!");
			} else {
				productoDAO.cargar(prod);
				res.status(201);
				model.put("color", "#06bd43");
				model.put("mensaje", "Producto guardado con exito!");
			}

			logger.info(Logging.getMessage(req, res));
		} catch (Exception e) {
			res.status(500);
			model.put("mensaje", "Error del servidor!");
		
			logger.error(Logging.getMessage(req, res) + " | " + Logging.getException(e));
		}

		return res;
	};

	public Route buscarxCantiadad = (Request req, Response res) -> {
		HashMap<String, Object> model = new HashMap<String, Object>();
		Producto p = new Producto();

		List<Producto> respu = productoDAO.buscarPorCantidad(Integer.parseInt(req.queryParams("stock")));

		model.put("respuesta", respu);
		model.put("prod", p);
		model.put("temp", Path.Template.SUGERENCIA);

		logger.info(Logging.getMessage(req, res));
		return new VelocityTemplateEngine().render(new ModelAndView(model, Path.Template.LAYOUT));
	};

	public Route buscarxPrecio = (Request req, Response res) -> {
		HashMap<String, Object> model = new HashMap<String, Object>();
		Producto p = new Producto();

		int precioMin = Integer.parseInt(req.queryParams("precioMin"));
		int precioMax = Integer.parseInt(req.queryParams("precioMax"));

		// ver como viene sino ingresa nada y chequearlo con un if
		List<Producto> respu = productoDAO.buscarPorPrecio(precioMin, precioMax);

		if (!respu.isEmpty()) {
			model.put("respuesta", respu);
			model.put("prod", p);
			model.put("temp", Path.Template.SUGERENCIA);
		} else {
			model.put("temp", Path.Template.SIN_RESULTADO);
		}

		logger.info(Logging.getMessage(req, res));
		return new VelocityTemplateEngine().render(new ModelAndView(model, Path.Template.LAYOUT));
	};

	public Route buscarxCategoria = (Request req, Response res) -> {
		HashMap<String, Object> model = new HashMap<String, Object>();

		Producto p = new Producto();

		List<Producto> respu = productoDAO.buscarPorCategoria(Categorias.valueOf(req.queryParams("categoria")));

		model.put("respuesta", respu);
		model.put("prod", p);
		model.put("temp", Path.Template.SUGERENCIA);

		logger.info(Logging.getMessage(req, res));
		return new VelocityTemplateEngine().render(new ModelAndView(model, Path.Template.LAYOUT));
	};

	public Route eliminar = (Request req, Response res) -> {
		HashMap<String, Object> model = new HashMap<String, Object>();
		
		Boolean respu = productoDAO.eliminar(req.queryParams("prod"));

		if (respu) {
			res.status(200);
			model.put("temp", Path.Template.PRODUCTO_ELIMINADO);
			logger.info(Logging.getMessage(req, res));
		} else {
			res.status(503);
			res.body("Tuvimos problemas para eliminar el producto :(");
			logger.error(Logging.getMessage(req, res));
		}

		return new VelocityTemplateEngine().render(new ModelAndView(model, Path.Template.LAYOUT));
	};
}
