package edu.lmu.cs.headmaster.ws.types;

import org.joda.time.DateTime;

public enum ClassYear {
    FRESHMAN, SOPHOMORE, JUNIOR, SENIOR;

    public Integer getExpectedGraduationYear(DateTime referenceDate) {
        return 0;
    }
}
