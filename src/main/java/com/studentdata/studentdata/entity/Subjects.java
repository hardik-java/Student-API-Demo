package com.studentdata.studentdata.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Subjects {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
//	private Integer maths;
//	private Integer physics;
	
	private String subjectName;
	private Double marks;
	
//	@ManyToMany
//	@JoinColumn(name = "student_id")
//	private StudentEntity studentEntity;
	private Long studentId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
//	public Integer getMaths() {
//		return maths;
//	}
//	public void setMaths(Integer maths) {
//		this.maths = maths;
//	}
//	public Integer getPhysics() {
//		return physics;
//	}
//	public void setPhysics(Integer physics) {
//		this.physics = physics;
//	}
	
	public String getSubjectName() {
		return subjectName;
	}
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public Double getMarks() {
		return marks;
	}
	public void setMarks(Double marks) {
		this.marks = marks;
	}

	
	
}
