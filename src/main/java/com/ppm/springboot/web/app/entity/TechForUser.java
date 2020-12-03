package com.ppm.springboot.web.app.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tech_forusers")
public class TechForUser implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String porcentaje;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPorcentaje() {
		return porcentaje;
	}
	
	@OneToMany(mappedBy= "tech_forusers",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Technology> technologies;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "member_id", referencedColumnName = "id")
	private Member members;

	public TechForUser() {
		technologies = new ArrayList<Technology>();
	}

	public void setPorcentaje(String porcentaje) {
		this.porcentaje = porcentaje;
	}
	
	
	public List<Technology> getTechnologies() {
		return technologies;
	}

	public void setTechnologies(List<Technology> technologies) {
		this.technologies = technologies;
	}

	public void addTechnology(Technology technology) {
		technologies.add(technology);
	}
	

	public Member getMembers() {
		return members;
	}

	public void setMembers(Member members) {
		this.members = members;
	}

	private static final long serialVersionUID = 1L;

}
