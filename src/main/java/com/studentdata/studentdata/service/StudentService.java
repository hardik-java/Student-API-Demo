package com.studentdata.studentdata.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.studentdata.studentdata.entity.StudentDto;
import com.studentdata.studentdata.entity.StudentEntity;
import com.studentdata.studentdata.entity.StudentResponse;
import com.studentdata.studentdata.entity.SubjectDto;

public interface StudentService {
	
	public CompletableFuture<Map<String, Object>> saveStudentData(StudentDto studentDto);

	public StudentResponse getStudentData(String name) throws Exception;

	public CompletableFuture<Map<String, Object>> addSubject(Long studentId, SubjectDto subjects);

}
