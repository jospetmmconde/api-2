package com.ppm.springboot.web.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ppm.springboot.web.app.dao.MemberDAO;
import com.ppm.springboot.web.app.dao.TechnologiesForUserDAO;
import com.ppm.springboot.web.app.entity.Member;
import com.ppm.springboot.web.app.entity.TechForUser;

@Controller
@RequestMapping("/techForUser")
public class TechForUserController {

	@Autowired
	private TechnologiesForUserDAO technologiesForUserDAO;
	
	private MemberDAO memberDAO;
	
	@PostMapping
	TechForUser createTechForUser(@RequestBody TechForUser techForUser) {
		Member member = new Member();
		member.setName("");
		member.setLastName("");
		member.setRol("");
		member.setCreated_by("");
		member.setUpdate_by("");
		techForUser.setMembers(member);
		return technologiesForUserDAO.save(techForUser);
	}
	
	@PutMapping(path = "/techForUser/{id}")
    Optional<TechForUser> updateTechForUser(@PathVariable Long id, @RequestBody TechForUser requestTechForUser){
        return technologiesForUserDAO.findById(id).map(techForUser -> {
            techForUser.setPorcentaje(requestTechForUser.getPorcentaje());

            return technologiesForUserDAO.save(techForUser);
        });
    }
	
	@GetMapping(path = "/forUser")
    public List< TechForUser > getTechForUser() {
        return technologiesForUserDAO.findAll();
    }

    @RequestMapping(path = "/forUser/{id}")// /products/{productId} -> /products/1
    public ResponseEntity<TechForUser> getTechForUserById(@PathVariable("id") Long id){
        Optional<TechForUser> optionalTechForUser= technologiesForUserDAO.findById(id);
        if(optionalTechForUser.isPresent()){
            return ResponseEntity.ok(optionalTechForUser.get());
        }else {
            return ResponseEntity.noContent().build();
        }
    }
	
	

	
}
