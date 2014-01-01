package edu.lmu.cs.headmaster.ws.service;

import java.util.List;

import edu.lmu.cs.headmaster.ws.dao.CourseDao;
import edu.lmu.cs.headmaster.ws.domain.Course;

public class CourseServiceImpl extends AbstractService implements CourseService {

    private CourseDao courseDao;
    
    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Override
    public Course getCourseById(Long id) {
        return courseDao.getCourseById(id);
    }

    @Override
    public List<Course> getCourses(String query, int skip, int year) {
        return courseDao.getCourses(query, skip, year);
    }

    @Override
    public Course createCourse(Course course) {
        return courseDao.createCourse(course);
    }

    @Override
    public void createOrUpdateCourse(Course course) {
        courseDao.createOrUpdateCourse(course);
    }

}
