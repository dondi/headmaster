package edu.lmu.cs.headmaster.ws.types;

import org.joda.time.DateTime;

public enum ClassYear {
    FRESHMAN, SOPHOMORE, JUNIOR, SENIOR;

    public Integer getExpectedGraduationYear(DateTime referenceDate) {
        DateTime cutoff = new DateTime(referenceDate.getYear(), 7, 1, 0, 0, 0, 0);
        int freshmanGraduationYear = (referenceDate.getYear() + 3) +
                (referenceDate.compareTo(cutoff) < 0 ? 0 : 1);
        return Integer.valueOf(freshmanGraduationYear - ordinal());
    }
}
