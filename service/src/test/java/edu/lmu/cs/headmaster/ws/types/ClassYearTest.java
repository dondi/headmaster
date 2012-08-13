package edu.lmu.cs.headmaster.ws.types;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Test;

/*
 * Tests class year computations.  Year computations have a cutoff of July 1.
 */
public class ClassYearTest {

    @Test
    public void testClassYearsEarly2012() {
        // In 2012 before July 1, seniors are those graduating in 2012, juniors
        // in 2013, sophomores in 2014, and freshmen in 2015.
        DateTime early2012 = new DateTime(2012, 6, 30, 23, 59, 59, 0);
        Assert.assertEquals(Integer.valueOf(2012), ClassYear.SENIOR.getExpectedGraduationYear(early2012));
        Assert.assertEquals(Integer.valueOf(2013), ClassYear.JUNIOR.getExpectedGraduationYear(early2012));
        Assert.assertEquals(Integer.valueOf(2014), ClassYear.SOPHOMORE.getExpectedGraduationYear(early2012));
        Assert.assertEquals(Integer.valueOf(2015), ClassYear.FRESHMAN.getExpectedGraduationYear(early2012));
    }

    @Test
    public void testClassYearsLate2012() {
        // In 2012 after July 1, seniors are those graduating in 2013, juniors
        // in 2014, sophomores in 2015, and freshmen in 2016.
        DateTime late2012 = new DateTime(2012, 7, 1, 0, 0, 0, 0);
        Assert.assertEquals(Integer.valueOf(2013), ClassYear.SENIOR.getExpectedGraduationYear(late2012));
        Assert.assertEquals(Integer.valueOf(2014), ClassYear.JUNIOR.getExpectedGraduationYear(late2012));
        Assert.assertEquals(Integer.valueOf(2015), ClassYear.SOPHOMORE.getExpectedGraduationYear(late2012));
        Assert.assertEquals(Integer.valueOf(2016), ClassYear.FRESHMAN.getExpectedGraduationYear(late2012));
    }

}
