package com.studentdata.studentdata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.studentdata.studentdata.constant.ApplicationConstants;
import com.studentdata.studentdata.entity.StudentDto;
import com.studentdata.studentdata.entity.StudentEntity;
import com.studentdata.studentdata.entity.Subjects;
import com.studentdata.studentdata.service.StudentService;

@SpringBootTest
class StudentdataApplicationTests {

	@Autowired
	StudentService studentService;
	
	@Test
	void contextLoads() {
	}
	
	@Test
	public void completableFutureSaveStudentDataTest() throws Exception
	{   
		CompletableFuture<Map<String, Object>> studentResponse = new CompletableFuture<>();
		StudentDto studentDataDto = new StudentDto();
		List<Subjects> subjectsData = new ArrayList<Subjects>();
		Subjects subs = new Subjects();
		StudentEntity studentEntity = new StudentEntity();
		
		studentEntity.setName("Hardik");
		studentEntity.setAge(23);
		studentEntity.setAddress("Ahmedabad, Gujarat");
		
		subs.setMaths(90);
		subs.setPhysics(100);
		
		subjectsData.add(subs);
		studentEntity.setSubjects(subjectsData);
		studentDataDto.setStudentEntity(studentEntity);
		studentResponse = studentService.saveStudentData(studentDataDto);
		
		assertNotEquals(null, studentResponse);
	}
	
	
	@Test
	public void completableFutureGetStudentDataTest() throws Exception
	{   
		CompletableFuture<Map<String, Object>> testStudentData = studentService.getStudentData("Hardik");
		System.out.println("testStudentData : "+testStudentData);
		assertNotEquals(null, testStudentData);
	}
	
	@Test
	public void completableFutureGetNullStudentDataTest() throws Exception
	{   
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put(ApplicationConstants.RESPONSE_STATUS_STATUS, ApplicationConstants.RESPONSE_STATUS_CODE_OK);
		map.put(ApplicationConstants.RESPONSE_STATUS_MESSAGE, ApplicationConstants.RESPONSE_STATUS_CODE_NO_DATA);
		map.put(ApplicationConstants.RESPONSE_STATUS_DATA, new ArrayList<>());
		
		CompletableFuture<Map<String, Object>> testStudentData = studentService.getStudentData("123123");
		assertEquals(map, testStudentData.get());
	}

}