package com.studentdata.studentdata.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.studentdata.studentdata.constant.ApplicationConstants;
import com.studentdata.studentdata.entity.StudentDto;
import com.studentdata.studentdata.entity.StudentEntity;
import com.studentdata.studentdata.entity.Subjects;
import com.studentdata.studentdata.repository.StudentRepository;
import com.studentdata.studentdata.repository.SubjectRepository;
import com.studentdata.studentdata.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService{
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	SubjectRepository subjectRepository;

	/**
	 * This method is used to save student and his/her subject details
	 * @param studentDto
	 * 
	 * @return status with Success/Failure
	 */
	@Override
	@Async
	public CompletableFuture<Map<String, Object>> saveStudentData(StudentDto studentDto) {
		Map<String, Object> map = new HashMap<>();
		StudentEntity studentEntity = new StudentEntity();
		Subjects subjects = new Subjects();
		
		try {
			studentEntity.setAddress(studentDto.getStudentEntity().getAddress());
			studentEntity.setAge(studentDto.getStudentEntity().getAge());
			studentEntity.setName(studentDto.getStudentEntity().getName());
			
			//Save Student's data into the table
			CompletableFuture<StudentEntity> saveStudentData = CompletableFuture.supplyAsync(
		            () -> {
		            	return studentRepository.save(studentEntity);
		            });
			
			StudentEntity student = saveStudentData.get();
			
			if(!studentDto.getStudentEntity().getSubjects().isEmpty()) {
				for (Subjects subs : studentDto.getStudentEntity().getSubjects()) {
					if(subs.getMaths() != null)
						subjects.setMaths(subs.getMaths());
					
					if(subs.getPhysics() != null)
						subjects.setPhysics(subs.getPhysics());
					
					subjects.setStudentEntity(student);
				}
			}
			
			//Save Subject's data into the table
			CompletableFuture<Subjects> saveSubjectData = CompletableFuture.supplyAsync(
		            () -> subjectRepository.save(subjects)
		    );
			
			if(saveSubjectData.get() != null) {
				map.put(ApplicationConstants.RESPONSE_STATUS_STATUS, ApplicationConstants.RESPONSE_STATUS_CODE_OK);
				map.put(ApplicationConstants.RESPONSE_STATUS_MESSAGE, ApplicationConstants.RESPONSE_STATUS_CODE_SUCCESS_MESSAGE);
				map.put(ApplicationConstants.RESPONSE_STATUS_DATA, new ArrayList<>());
				
			} else {
				map.put(ApplicationConstants.RESPONSE_STATUS_STATUS, ApplicationConstants.RESPONSE_STATUS_CODE_BAD);
				map.put(ApplicationConstants.RESPONSE_STATUS_MESSAGE, ApplicationConstants.RESPONSE_STATUS_CODE_FAILUR_MESSAGE);
				map.put(ApplicationConstants.RESPONSE_STATUS_DATA, new ArrayList<>());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put(ApplicationConstants.RESPONSE_STATUS_STATUS, ApplicationConstants.RESPONSE_STATUS_CODE_BAD);
			map.put(ApplicationConstants.RESPONSE_STATUS_MESSAGE, ApplicationConstants.RESPONSE_STATUS_CODE_FAILUR_MESSAGE);
			map.put(ApplicationConstants.RESPONSE_STATUS_DATA, new ArrayList<>());
		}
		
		return CompletableFuture.completedFuture(map);
	}

	
	/**
	 * This method is used to get student and subject list by Student Name
	 * @param name
	 * 
	 * return list of student and subject data
	 */
	@Override
	@Async
	public CompletableFuture<Map<String, Object>> getStudentData(String name) {
		List <Map<String, Integer>> subjectMap = new ArrayList<Map<String,Integer>>();
		Map<String, Object> studentMap = new HashMap<>();
		Map<String, Object> mainStudentMap = new HashMap<>();
		Map<String, Integer> subjectMath = new HashMap<String, Integer>();
		Map<String, Integer> subjectPhysics = new HashMap<String, Integer>();
		Map<String, Object> map = new HashMap<>();

		try {
			List<StudentEntity> list = studentRepository.findByName(name);
			
			if(!CollectionUtils.isEmpty(list)) {
				for (StudentEntity studentEntity : list) {
					studentMap.put("name", studentEntity.getName());
					studentMap.put("age", studentEntity.getAge());
					studentMap.put("address", studentEntity.getAddress());

					//Get all subjects of student 
					Subjects subjects =  subjectRepository.getByStudentId(studentEntity.getId());
					
					if(subjects != null) {
						subjectMath.put("maths", subjects.getMaths());
						subjectPhysics.put("physics", subjects.getPhysics());
						
						subjectMap.add(subjectMath);
						subjectMap.add(subjectPhysics);
						studentMap.put("subjects", subjectMap);
						mainStudentMap.put("student", studentMap);
						map.put(ApplicationConstants.RESPONSE_STATUS_STATUS, ApplicationConstants.RESPONSE_STATUS_CODE_OK);
						map.put(ApplicationConstants.RESPONSE_STATUS_MESSAGE, ApplicationConstants.RESPONSE_STATUS_CODE_SUCCESS_MESSAGE);
						map.put(ApplicationConstants.RESPONSE_STATUS_DATA, mainStudentMap);
					} else {
						studentMap.put("subjects", new ArrayList<>());
					}
					
				}
			} else {
				map.put(ApplicationConstants.RESPONSE_STATUS_STATUS, ApplicationConstants.RESPONSE_STATUS_CODE_OK);
				map.put(ApplicationConstants.RESPONSE_STATUS_MESSAGE, ApplicationConstants.RESPONSE_STATUS_CODE_NO_DATA);
				map.put(ApplicationConstants.RESPONSE_STATUS_DATA, new ArrayList<>());
			}
			
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			map.put(ApplicationConstants.RESPONSE_STATUS_STATUS, ApplicationConstants.RESPONSE_STATUS_CODE_BAD);
			map.put(ApplicationConstants.RESPONSE_STATUS_MESSAGE, ApplicationConstants.RESPONSE_STATUS_CODE_FAILUR_MESSAGE);
			map.put(ApplicationConstants.RESPONSE_STATUS_DATA, new ArrayList<>());
			e.printStackTrace();
		}
		
	    return CompletableFuture.completedFuture(map);
		
	}

}
