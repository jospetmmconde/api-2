package com.ppm.springboot.web.app.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ppm.springboot.web.app.dao.TechnologyDAO;

@Controller
@RequestMapping("/technology")
public class TechnologyController {
	
	@Autowired
	private TechnologyDAO technologyDAO;
	
	

}
