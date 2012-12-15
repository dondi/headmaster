package edu.lmu.cs.headmaster.ws.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.lmu.cs.headmaster.ws.types.Term;

public class GpaTest {

    @Test
    public void fieldsSetBySettersCanBeRead() {
        
        GPA recordOfGpa = new GPA();
        
        long id = 9000L;
        Term term = Term.SUMMER;
        int year = 2500;
        double gpa = 4.0;
        
        recordOfGpa.setId(id);
        recordOfGpa.setTerm(term);
        recordOfGpa.setYear(year);
        recordOfGpa.setGpa(gpa);
        
        assertEquals((long) recordOfGpa.getId(), (long) id);
        assertEquals(recordOfGpa.getTerm(), term);
        assertEquals(recordOfGpa.getYear(), year);
        assertEquals(recordOfGpa.getGpa(), gpa, 0.001);
               
    }
    
}
