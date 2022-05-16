package com.studentdata.studentdata.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.studentdata.studentdata.entity.StudentDto;

public interface StudentService {
	
	public CompletableFuture<Map<String, Object>> saveStudentData(StudentDto studentDto);

	public CompletableFuture<Map<String, Object>> getStudentData(String name);

}
