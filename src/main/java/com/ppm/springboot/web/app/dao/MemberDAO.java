package com.ppm.springboot.web.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ppm.springboot.web.app.entity.Member;

public interface MemberDAO extends JpaRepository<Member, Long>{

}
