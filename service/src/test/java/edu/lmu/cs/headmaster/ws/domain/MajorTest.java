package edu.lmu.cs.headmaster.ws.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;

public class MajorTest {

    @Test
    public void fieldsSetBySettersCanBeRead() {
        
        Major major = new Major();
        
        long id = 9000L;
        String degree = "Dark Arts Defence System Engineering";
        String discipline = "Spell Creation";
        String collegeOrSchool = "Severus Snape College of Defence Against the Dark Arts ";
        
        major.setId(id);
        major.setDegree(degree);
        major.setDiscipline(discipline);
        major.setCollegeOrSchool(collegeOrSchool);
        
        assertEquals((long) major.getId(), (long) id);
        assertEquals(major.getDegree(), degree);
        assertEquals(major.getDiscipline(), discipline);
        assertEquals(major.getCollegeOrSchool(), collegeOrSchool);
        
    }
    
    @Test
    public void equalsReturnsFalseIfIncorrectClass() {
        
        Major majorComaringObjectAgainst = new Major();      
        Object objectOfWrongClass = new DateTime(0);
        
        assertFalse(majorComaringObjectAgainst.equals(objectOfWrongClass));        
        
    }
    
    @Test
    public void equalsReturnsFalseIfNull() {
        
        Major majorComaringObjectAgainst = new Major();      
        Object nullMajor = null;
        
        assertFalse(majorComaringObjectAgainst.equals(nullMajor));        

    }
    
    @Test
    public void equalsComparesSameTypeById() {
        
        long id = 9000L;
        long diffrentId = 4000L;
        String degree = "Dark Arts Defence System Engineering";
        String differentDegree = "Defence Against the Dark Arts";
        String discipline = "Spell Creation";
        String collegeOrSchool = "Severus Snape College of Defence Against the Dark Arts ";
        
        Major majorComaringObjectAgainst = new Major();
        majorComaringObjectAgainst.setId(id);
        majorComaringObjectAgainst.setDegree(degree);
        majorComaringObjectAgainst.setDiscipline(discipline);
        majorComaringObjectAgainst.setCollegeOrSchool(collegeOrSchool);
              
        Major majorWithDiffrentDegreeButSameId = new Major();
        majorWithDiffrentDegreeButSameId.setId(id);
        majorWithDiffrentDegreeButSameId.setDegree(differentDegree);
        majorWithDiffrentDegreeButSameId.setDiscipline(discipline);
        majorWithDiffrentDegreeButSameId.setCollegeOrSchool(collegeOrSchool);
        
        assertTrue(majorComaringObjectAgainst.equals(majorWithDiffrentDegreeButSameId));
        
        Major majorWithDiffrentDegreeAndDiffrentId = new Major();
        majorWithDiffrentDegreeAndDiffrentId.setId(diffrentId);
        majorWithDiffrentDegreeAndDiffrentId.setDegree(differentDegree);
        majorWithDiffrentDegreeAndDiffrentId.setDiscipline(discipline);
        majorWithDiffrentDegreeAndDiffrentId.setCollegeOrSchool(collegeOrSchool);
        
        assertFalse(majorComaringObjectAgainst.equals(majorWithDiffrentDegreeAndDiffrentId));
        
    }
    
}
