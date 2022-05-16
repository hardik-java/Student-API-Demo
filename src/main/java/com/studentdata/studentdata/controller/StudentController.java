package com.studentdata.studentdata.controller;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.studentdata.studentdata.entity.StudentDto;
import com.studentdata.studentdata.service.StudentService;

@RestController
@RequestMapping("/StudentDetails")
public class StudentController {

	@Autowired
	StudentService studentService;

	/**
	 * This API will save student details and their marks according to their subjects.
	 * 
	 * @param studentDto
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@PostMapping("/saveStudentData")
	public ResponseEntity<Map<String, Object>> saveStudentData(@RequestBody StudentDto studentDto) throws InterruptedException, ExecutionException {
		CompletableFuture<Map<String, Object>> studentData = studentService.saveStudentData(studentDto);
		return new ResponseEntity<Map<String,Object>>(studentData.get(), HttpStatus.OK);
	}

	/**
	 * This API will get all Students and their subjects marks
	 * 
	 * @param name
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@GetMapping("/getData")
	public ResponseEntity<Map<String, Object>> getStudentData(@RequestParam("studentName") String name) throws InterruptedException, ExecutionException {
		CompletableFuture<Map<String, Object>> studentData = studentService.getStudentData(name);
		return new ResponseEntity<Map<String,Object>>(studentData.get(), HttpStatus.OK);
	}
}
