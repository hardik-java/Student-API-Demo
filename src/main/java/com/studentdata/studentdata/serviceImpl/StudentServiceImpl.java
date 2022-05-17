package com.studentdata.studentdata.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.studentdata.studentdata.constant.ApplicationConstants;
import com.studentdata.studentdata.entity.StudentDto;
import com.studentdata.studentdata.entity.StudentEntity;
import com.studentdata.studentdata.entity.StudentResponse;
import com.studentdata.studentdata.entity.SubjectDto;
import com.studentdata.studentdata.entity.Subjects;
import com.studentdata.studentdata.repository.StudentRepository;
import com.studentdata.studentdata.repository.SubjectRepository;
import com.studentdata.studentdata.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	SubjectRepository subjectRepository;

	/**
	 * This method is used to save student and his/her subject details
	 * 
	 * @param studentDto
	 * 
	 * @return status with Success/Failure
	 */
	
	@Override
	@Async
	public CompletableFuture<Map<String, Object>> saveStudentData(StudentDto studentDto) {
		Map<String, Object> responseMap = new HashMap<>();
		StudentEntity studentEntity = new StudentEntity();

		CompletableFuture<Subjects> saveSubjectData = new CompletableFuture<>();

		try {
			studentEntity.setAddress(studentDto.getStudentEntity().getAddress());
			studentEntity.setAge(studentDto.getStudentEntity().getAge());
			studentEntity.setName(studentDto.getStudentEntity().getName());

			// Save Student's data into the table
			CompletableFuture<StudentEntity> saveStudentData = CompletableFuture.supplyAsync(() -> {
				return studentRepository.save(studentEntity);
			});

			StudentEntity student = saveStudentData.get();

			if (!studentDto.getStudentEntity().getSubjects().isEmpty()) {
				for (Map<String, Double> subject : studentDto.getStudentEntity().getSubjects()) {

					Subjects subjects = new Subjects();
					Set<String> a = subject.keySet();
					for (String s : a) {
						subjects.setMarks(subject.get(s));
						subjects.setSubjectName(s);
					}

					subjects.setStudentId(student.getId());
					saveSubjectData = CompletableFuture.supplyAsync(() -> subjectRepository.save(subjects));
				}
			}

			// Save Subject's data into the table
			if (saveSubjectData.get() != null) {
				responseMap.put(ApplicationConstants.RESPONSE_STATUS_STATUS, ApplicationConstants.RESPONSE_STATUS_CODE_OK);
				responseMap.put(ApplicationConstants.RESPONSE_STATUS_MESSAGE,
						ApplicationConstants.RESPONSE_STATUS_CODE_SUCCESS_MESSAGE);
				responseMap.put(ApplicationConstants.RESPONSE_STATUS_DATA, new ArrayList<>());

			} else {
				responseMap.put(ApplicationConstants.RESPONSE_STATUS_STATUS, ApplicationConstants.RESPONSE_STATUS_CODE_BAD);
				responseMap.put(ApplicationConstants.RESPONSE_STATUS_MESSAGE,
						ApplicationConstants.RESPONSE_STATUS_CODE_FAILUR_MESSAGE);
				responseMap.put(ApplicationConstants.RESPONSE_STATUS_DATA, new ArrayList<>());
			}

		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put(ApplicationConstants.RESPONSE_STATUS_STATUS, ApplicationConstants.RESPONSE_STATUS_CODE_BAD);
			responseMap.put(ApplicationConstants.RESPONSE_STATUS_MESSAGE,
					ApplicationConstants.RESPONSE_STATUS_CODE_FAILUR_MESSAGE);
			responseMap.put(ApplicationConstants.RESPONSE_STATUS_DATA, new ArrayList<>());
		}

		return CompletableFuture.completedFuture(responseMap);
	}

	/**
	 * This method is used to get student and subject list by Student Name
	 * 
	 * @param name
	 * 
	 *             return list of student and subject data
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@Override
	public StudentResponse getStudentData(String name) throws Exception {
		
		List<Map<String, Double>> subjectMap = new ArrayList<>();
		List<Subjects> subjectsList = new ArrayList<>();
		
		StudentResponse studentResponseDto = new StudentResponse();

		try {
			List<StudentEntity> studentEntityList = studentRepository.findByName(name);

			if (!CollectionUtils.isEmpty(studentEntityList)) {
				for (StudentEntity studentEntity : studentEntityList) {
			
					studentResponseDto.setName(studentEntity.getName());
					studentResponseDto.setAge(studentEntity.getAge());
					studentResponseDto.setAddress(studentEntity.getAddress());
					
					//Get all subjects of student
					subjectsList = subjectRepository.getByStudentId(studentEntity.getId());
				}
				
				//Get subject name with marks
				for (Subjects subjects : subjectsList) {
					Map<String, Double> subjectMapInner = new HashMap<String, Double>();
					if (subjects != null) {
						subjectMapInner.put(subjects.getSubjectName(), subjects.getMarks());
						subjectMap.add(subjectMapInner);
						
						studentResponseDto.setSubjects(subjectMap);
					}
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CompletableFuture.completedFuture(studentResponseDto).get();

	}
	
	
	/**
	 * This method is used to get student and subject list by Student Name
	 * 
	 * @param studentId
	 * @param subjects
	 * 
	 * return list of student and subject data
	 */
	@Override
	@Async
	public CompletableFuture<Map<String, Object>> addSubject(Long studentId, SubjectDto subjects) {
		CompletableFuture<Subjects> savedSubjectData = new CompletableFuture<>();
		Map<String, Object> responseMap = new HashMap<>();
		
		try {
			Optional<StudentEntity> studentEntityData = studentRepository.findById(studentId);
			if (studentEntityData.isPresent()) {
				
				for (Map<String, Double> subs : subjects.getSubjects()) {

					Subjects subject = new Subjects();
					
					Set<String> subjectSet = subs.keySet();
					for (String subjectMapKey : subjectSet) {
						subject.setMarks(subs.get(subjectMapKey));
						subject.setSubjectName(subjectMapKey);
					}

					subject.setStudentId(studentId);
					savedSubjectData = CompletableFuture.supplyAsync(() -> subjectRepository.save(subject));
				}
				responseMap.put(ApplicationConstants.RESPONSE_STATUS_STATUS, ApplicationConstants.RESPONSE_STATUS_CODE_OK);
				responseMap.put(ApplicationConstants.RESPONSE_STATUS_MESSAGE,
						ApplicationConstants.RESPONSE_STATUS_CODE_SUCCESS_MESSAGE);
				responseMap.put(ApplicationConstants.RESPONSE_STATUS_DATA, new ArrayList<>());
			}
			else {
				responseMap.put(ApplicationConstants.RESPONSE_STATUS_STATUS, ApplicationConstants.RESPONSE_STATUS_CODE_BAD);
				responseMap.put(ApplicationConstants.RESPONSE_STATUS_MESSAGE,
						ApplicationConstants.RESPONSE_STATUS_CODE_FAILUR_MESSAGE);
				responseMap.put(ApplicationConstants.RESPONSE_STATUS_DATA, new ArrayList<>());
			}
		} catch (Exception e) {
			responseMap.put(ApplicationConstants.RESPONSE_STATUS_STATUS, ApplicationConstants.RESPONSE_STATUS_CODE_BAD);
			responseMap.put(ApplicationConstants.RESPONSE_STATUS_MESSAGE,
					ApplicationConstants.RESPONSE_STATUS_CODE_FAILUR_MESSAGE);
			responseMap.put(ApplicationConstants.RESPONSE_STATUS_DATA, new ArrayList<>());
		}
		
		return CompletableFuture.completedFuture(responseMap);
	}

}
