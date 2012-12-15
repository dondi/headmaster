package edu.lmu.cs.headmaster.ws.service;

import java.util.List;

import edu.lmu.cs.headmaster.ws.domain.Event;
import edu.lmu.cs.headmaster.ws.domain.Student;
import edu.lmu.cs.headmaster.ws.domain.StudentRecord;
import edu.lmu.cs.headmaster.ws.types.Term;

// TODO Flesh out the documentation for this interface.
public interface StudentService {
    List<Student> getStudents(String query,
                Boolean active, 
                Boolean transferStudent, 
                Integer expectedGraduationYearFrom,
                Integer expectedGraduationYearTo,
                Double minCumulativeGpa,
                Double maxCumulativeGpa,
                Double minTermGpa,
                Double maxTermGpa,
                Term term,
                Integer year,
                int skip,
                int max);

    void createStudent(Student student);

    void createOrUpdateStudent(Long id, Student student);

    Student getStudentById(Long id);

    List<Event> getStudentAttendanceById(Long id);

    StudentRecord getStudentRecordById(Long id);

    void updateStudentRecord(Long id, StudentRecord studentRecord);
}
