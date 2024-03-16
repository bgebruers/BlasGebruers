package com.mycompany.porkycakes.controllers;

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
import utility.Path;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrincipalController {
	Logger logger = LoggerFactory.getLogger(PrincipalController.class);
	ProductoDao productoDao;

	public PrincipalController(ProductoDao productoDao) {
		this.productoDao = productoDao;
	}

	//para ingresar a pagina principal
	public Route principal = (Request req, Response res)->{
		HashMap<String, Object> model = new HashMap<String, Object>();

		Random rand = new Random();

		int cantidad = productoDao.buscarTodos().size();

		int idProductoMax = rand.nextInt(cantidad);    //numero maximo que puede ser escogido

		while(idProductoMax  < cantidad-1){
			idProductoMax = rand.nextInt(cantidad);
		}

		int idProductoMin = idProductoMax - 8;     //se resta 8 para mostrar 9 prod en pantalla

		List<Producto> respu = productoDao.buscarPorRango(idProductoMin,idProductoMax); //cambiar 1 y 4 por idProducto

		model.put("respuesta", respu);

		model.put("temp", "/templates/segerenciaPrincipal.vsl");

		logger.info(Logging.getMessage(req, res));
		return new VelocityTemplateEngine().render(new ModelAndView(model, Path.Template.LAYOUT));
	};

	//para mostrar listado de productos buscados
	//anda bien, ver el boton porque no redirige
	public Route mostrar = (Request req, Response res)->{
		HashMap<String, Object> model = new HashMap<String, Object>();

		model.put("temp", Path.Template.MOSTRAR);

		logger.info(Logging.getMessage(req, res));
		return new VelocityTemplateEngine().render(new ModelAndView(model, Path.Template.LAYOUT));
	};
}
