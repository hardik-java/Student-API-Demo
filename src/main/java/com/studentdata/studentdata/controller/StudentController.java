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
import com.studentdata.studentdata.entity.StudentResponse;
import com.studentdata.studentdata.entity.SubjectDto;
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
	 * @throws Exception 
	 */
	@GetMapping("/getData")
	public StudentResponse getStudentData(@RequestParam("studentName") String studentName) throws Exception {
		return  studentService.getStudentData(studentName);
	}
	
	
	/**
	 * 
	 * @param studentId
	 * @param subjects
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@PostMapping("/addSubject")
	public ResponseEntity<Map<String, Object>> addSubject(@RequestParam("studentId") Long studentId, @RequestBody SubjectDto subjects) throws InterruptedException, ExecutionException {
		CompletableFuture<Map<String, Object>> studentData = studentService.addSubject(studentId,subjects);
		return new ResponseEntity<Map<String,Object>>(studentData.get(), HttpStatus.OK);
	}
}
