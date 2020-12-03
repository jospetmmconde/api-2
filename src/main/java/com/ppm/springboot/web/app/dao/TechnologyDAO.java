package com.ppm.springboot.web.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppm.springboot.web.app.entity.Technology;

public interface TechnologyDAO extends JpaRepository<Technology, Long>{

}
