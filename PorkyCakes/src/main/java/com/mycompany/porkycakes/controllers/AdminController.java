package com.mycompany.porkycakes.controllers;

import com.mycompany.porkycakes.porkyMain;
import com.mycompany.porkycakes.daos.generics.AdminDao;
import com.mycompany.porkycakes.daos.sqlImplementations.AdminDAOImpl;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.templates.velocity.VelocityTemplateEngine;
import utility.Logging;
import utility.Path;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminController {
	Logger logger = LoggerFactory.getLogger(AdminController.class);
	private AdminDao adminDao;

	public AdminController(AdminDao adminDao) {
		this.adminDao = adminDao;
	}

	public Route logPag = (Request req, Response res) -> {
		HashMap<String, Object> model = new HashMap<String, Object>();
		model.put("temp", Path.Template.INIT);

		String redirectTo = req.queryParams("redirect") != null ? req.queryParams("redirect") : "/porkycakes";

		model.put("redirect", redirectTo);

		logger.info(Logging.getMessage(req, res));
		return new VelocityTemplateEngine().render(new ModelAndView(model, Path.Template.LAYOUT));
	};

	public Route logging = (Request req, Response res) -> {
		if(adminDao.credencialesValidas(
			req.queryParams("user"), req.queryParams("password")
		)) {
			res.status(202);
			porkyMain.loggedIn = true;
		} else {
			res.status(403);
		}

		logger.info(Logging.getMessage(req, res));
		return res;
	};
}
