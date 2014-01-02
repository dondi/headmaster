package edu.lmu.cs.headmaster.ws.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.joda.time.DateTime;

import edu.lmu.cs.headmaster.ws.domain.Course;
import edu.lmu.cs.headmaster.ws.domain.Grant;
import edu.lmu.cs.headmaster.ws.domain.Event;
import edu.lmu.cs.headmaster.ws.domain.User;
import edu.lmu.cs.headmaster.ws.domain.UserRole;
import edu.lmu.cs.headmaster.ws.types.Role;
import edu.lmu.cs.headmaster.ws.types.Term;

/**
 * Holder for utility methods of use to multiple unit tests.
 */
public class DomainObjectUtils {

    /**
     * Helper factory method for creating new user objects.
     */
    public static User createUserObject(String login, String email, String challenge, Role... roles) {
        User user = new User();
        user.setLogin(login);
        user.setEmail(email);
        user.setChallengeRequest(challenge);

        List<UserRole> userRoles = new ArrayList<UserRole>();
        for (Role role: roles) {
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            userRoles.add(userRole);
        }

        user.setRoles(userRoles);
        return user;
    }

    /**
     * Helper factory method for creating new event objects.
     */
    public static Grant createGrantObject(Integer amount, String facultyMentor, String title) {
        Grant grant = new Grant();
        grant.setAmount(amount);
        grant.setFacultyMentor(facultyMentor);
        grant.setTitle(title);
        return grant;
    }

    /**
     * Helper factory method for creating new event objects.
     */
    public static Event createEventObject(String title, String description, DateTime dateTime) {
        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setDateTime(dateTime);
        return event;
    }

    /**
     * Helper factory method for creating new course objects.
     */
    public static Course createCourseObject(String number, String section, String subject, Term term, String title,
            Integer year) {
        Course course = new Course();
        course.setNumber(number);
        course.setSection(section);
        course.setSubject(subject);
        course.setTerm(term);
        course.setTitle(title);
        course.setYear(year);
        return course;
    }

    /**
     * Helper function for asserting the equality of two courses.
     */
    public static void assertSimpleEquality(Course course1, Course course2) {
        Assert.assertEquals(course1.getId(), course2.getId());
        Assert.assertEquals(course1.getNumber(), course2.getNumber());
        Assert.assertEquals(course1.getSection(), course2.getSection());
        Assert.assertEquals(course1.getSubject(), course2.getSubject());
        Assert.assertEquals(course1.getTerm(), course2.getTerm());
        Assert.assertEquals(course1.getTitle(), course2.getTitle());
        Assert.assertEquals(course1.getYear(), course2.getYear());
    }

}
