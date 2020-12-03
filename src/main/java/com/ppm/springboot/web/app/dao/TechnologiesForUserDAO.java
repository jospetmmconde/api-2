package com.ppm.springboot.web.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppm.springboot.web.app.entity.TechForUser;

public interface TechnologiesForUserDAO extends JpaRepository<TechForUser, Long>{

}
