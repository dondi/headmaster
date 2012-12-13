package edu.lmu.cs.headmaster.ws.service;

import java.util.List;

import edu.lmu.cs.headmaster.ws.dao.StudentDao;
import edu.lmu.cs.headmaster.ws.domain.Event;
import edu.lmu.cs.headmaster.ws.domain.Student;
import edu.lmu.cs.headmaster.ws.domain.StudentRecord;
import edu.lmu.cs.headmaster.ws.types.Term;

public class StudentServiceImpl implements StudentService {
    
    StudentDao studentDao;
    
    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public List<Student> getStudents(String query, Boolean active, Boolean transferStudent, Integer expectedGraduationYearFrom, 
            Integer expectedGraduationYearTo, Double minCumulativeGpa, Double maxCumulativeGpa, Double minTermGpa, 
            Double maxTermGpa, Term term, Integer year, int skip, int max) {
        
        return studentDao.getStudents(
                query, active, transferStudent, expectedGraduationYearFrom, expectedGraduationYearTo,
                minCumulativeGpa, maxCumulativeGpa, minTermGpa, maxTermGpa, term, year,
                skip, max
            );
    }

    @Override
    public void createStudent(Student student) {
        studentDao.createStudent(student);
        
    }

    @Override
    public void createOrUpdateStudent(Long id, Student student) {
        // The student record does not travel with the incoming student, so we set it here.
        Student currentStudent = studentDao.getStudentById(id);
        if (currentStudent != null) {
            student.setRecord(currentStudent.getRecord());
        }
        
        studentDao.createOrUpdateStudent(student);
    }

    @Override
    public Student getStudentById(Long id) {
        Student student = studentDao.getStudentById(id);
        return student;
    }

    @Override
    public List<Event> getStudentAttendanceById(Long id) {
        return studentDao.getStudentAttendanceById(id);
    }

    @Override
    public StudentRecord getStudentRecordById(Long id) {
        return getStudentById(id).getRecord();
    }

    @Override
    public void updateStudentRecord(Long id, StudentRecord studentRecord) {
        // Retrieve the full student, assign this record, then save.
        Student student = getStudentById(id);
        student.setRecord(studentRecord);

        // Dao problems will filter up as exceptions.
        studentDao.createOrUpdateStudent(student);
    }
}
