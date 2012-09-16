package edu.lmu.cs.headmaster.ws.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * A StudentRecord holds student properties or information that should only be
 * seen by more privileged users.
 */
@Embeddable
@XmlRootElement
public class StudentRecord {

    private String schoolId;
    private Double highSchoolGpa;
    private Double actScore;
    private Integer satVerbalScore;
    private Integer satMathScore;
    private Integer satWritingScore;
    private Double cumulativeGpa;
    private String academicStatus;
    private List<GPA> grades = new ArrayList<GPA>();

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public Double getHighSchoolGpa() {
        return highSchoolGpa;
    }

    public void setHighSchoolGpa(Double highSchoolGpa) {
        this.highSchoolGpa = highSchoolGpa;
    }

    public Double getActScore() {
        return actScore;
    }

    public void setActScore(Double actScore) {
        this.actScore = actScore;
    }

    public Integer getSatVerbalScore() {
        return satVerbalScore;
    }

    public void setSatVerbalScore(Integer satVerbalScore) {
        this.satVerbalScore = satVerbalScore;
    }

    public Integer getSatMathScore() {
        return satMathScore;
    }

    public void setSatMathScore(Integer satMathScore) {
        this.satMathScore = satMathScore;
    }

    public Integer getSatWritingScore() {
        return satWritingScore;
    }

    public void setSatWritingScore(Integer satWritingScore) {
        this.satWritingScore = satWritingScore;
    }

    public Double getCumulativeGpa() {
        return cumulativeGpa;
    }

    public void setCumulativeGpa(Double cumulativeGpa) {
        this.cumulativeGpa = cumulativeGpa;
    }

    public String getAcademicStatus() {
        return academicStatus;
    }

    public void setAcademicStatus(String academicStatus) {
        this.academicStatus = academicStatus;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OrderBy("year, term")
    public List<GPA> getGrades() {
        return grades;
    }

    public void setGrades(List<GPA> grades) {
        this.grades = grades;
    }

}
