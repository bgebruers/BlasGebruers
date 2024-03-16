package com.mycompany.porkycakes.controllers;

import utility.PasswordAuthentication;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.porkycakes.porkyMain;
import com.mycompany.porkycakes.daos.generics.GenericDao;
import com.mycompany.porkycakes.daos.sqlImplementations.PresupuestoDAOImpl;
import com.mycompany.porkycakes.model.Presupuesto;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.templates.velocity.VelocityTemplateEngine;
import utility.Logging;

public class PresupuestoController {
	Logger logger = LoggerFactory.getLogger(PresupuestoController.class);
    private GenericDao<Presupuesto> presupuestoDAO;

    public PresupuestoController(GenericDao<Presupuesto> presupuestoDAO) {
        this.presupuestoDAO = presupuestoDAO;
    }

	HashMap<String, Object> model = new HashMap<String, Object>();
	public Route cargarPresupuestoForm = (Request req, Response res) -> {
		if(!porkyMain.loggedIn) {
			PasswordAuthentication.notLoggedInRedirection(req, res);
			logger.info(Logging.getMessage(req, res));
			return res;
		}
		
		model.put("dontShow", true);
		model.put("temp", "templates/cargarPresup.vsl");
		
		logger.info(Logging.getMessage(req, res));
		return new VelocityTemplateEngine().render(new ModelAndView(model, "templates/layouts.vsl"));
	};

	public Route cargarPresupuesto = (Request req, Response res) -> {
		ObjectMapper mapper = new ObjectMapper();
		model.put("color", "#bd0606");

		try {
			Presupuesto presup = mapper.readValue(req.body(), Presupuesto.class);

			if (!presup.isValid()) {
				res.status(400);
				model.put("mensaje", "Complete todos los campos obligatorios!");
			} else {
				presupuestoDAO.cargar(presup);
				res.status(201);
				model.put("color", "#06bd43");
				model.put("mensaje", "Presupuesto enviado con exito!");
			}

			logger.info(Logging.getMessage(req, res));
		} catch (Exception e) {
			res.status(500);
			model.put("mensaje", "Error del servidor!");
			
			logger.error(Logging.getMessage(req, res) + " | " + Logging.getException(e));
		}

		return res;
	};

	public Route mostrarPresupuesto = (Request req, Response res) -> {
		if(!porkyMain.loggedIn) {
			PasswordAuthentication.notLoggedInRedirection(req, res);
			logger.info(Logging.getMessage(req, res));
			return res;
		}
		
		HashMap<String, Object> model = new HashMap<String, Object>();

		Presupuesto presup = presupuestoDAO.buscarPorId(req.queryParams("titulo"));
		
		model.put("dontShow", true);
        model.put("temp", "templates/mostrarPresup.vsl");
		model.put("presup", presup);
		
		logger.info(Logging.getMessage(req, res));
		return new VelocityTemplateEngine().render(new ModelAndView(model, "templates/layouts.vsl"));
	};
    
}
