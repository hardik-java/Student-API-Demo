package com.studentdata.studentdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.studentdata.studentdata.entity.Subjects;

@Repository
@Transactional
public interface SubjectRepository extends JpaRepository<Subjects, Long>{

	@Query("SELECT s FROM Subjects s WHERE s.studentEntity.id = :id")
	Subjects getByStudentId(Long id);

}
