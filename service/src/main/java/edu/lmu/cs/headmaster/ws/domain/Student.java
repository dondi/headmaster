package edu.lmu.cs.headmaster.ws.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

@Entity
@XmlRootElement
public class Student {

    private Long id;
    private String schoolId;
    private String lastName;
    private String firstName;
    private String middleInitial;
    private String email;
    private String campusBox;
    private String college;
    private boolean inLivingLearningCommunity;
    private String advisor;
    private int entryYear;
    private DateTime honorsEntryDate;
    private int expectedGraduationYear;
    private boolean transferStudent;
    private String degree;
    private List<String> majors;
    private List<String> minors;
    private boolean hasStudiedAbroad;
    private double highSchoolGpa;
    private double actScore;
    private int satVerbalScore;
    private int satMathScore;
    private int satWritingScore;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String mainPhone;
    private String cellPhone;
    private double cumulativeGpa;
    private boolean thesisInMajor;
    private String thesisAdvisor;
    private String thesisCourse;
    private Term thesisTerm;
    private int thesisYear;
    private String thesisTitle;
    private String academicStatus;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlAttribute
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCampusBox() {
        return campusBox;
    }

    public void setCampusBox(String campusBox) {
        this.campusBox = campusBox;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public boolean isInLivingLearningCommunity() {
        return inLivingLearningCommunity;
    }

    public void setInLivingLearningCommunity(boolean inLivingLearningCommunity) {
        this.inLivingLearningCommunity = inLivingLearningCommunity;
    }

    public String getAdvisor() {
        return advisor;
    }

    public void setAdvisor(String advisor) {
        this.advisor = advisor;
    }

    public int getEntryYear() {
        return entryYear;
    }

    public void setEntryYear(int entryYear) {
        this.entryYear = entryYear;
    }

    public DateTime getHonorsEntryDate() {
        return honorsEntryDate;
    }

    public void setHonorsEntryDate(DateTime honorsEntryDate) {
        this.honorsEntryDate = honorsEntryDate;
    }

    public int getExpectedGraduationYear() {
        return expectedGraduationYear;
    }

    public void setExpectedGraduationYear(int expectedGraduationYear) {
        this.expectedGraduationYear = expectedGraduationYear;
    }

    public boolean isTransferStudent() {
        return transferStudent;
    }

    public void setTransferStudent(boolean transferStudent) {
        this.transferStudent = transferStudent;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public List<String> getMajors() {
        return majors;
    }

    public void setMajors(List<String> majors) {
        this.majors = majors;
    }

    public List<String> getMinors() {
        return minors;
    }

    public void setMinors(List<String> minors) {
        this.minors = minors;
    }

    public boolean isHasStudiedAbroad() {
        return hasStudiedAbroad;
    }

    public void setHasStudiedAbroad(boolean hasStudiedAbroad) {
        this.hasStudiedAbroad = hasStudiedAbroad;
    }

    public double getHighSchoolGpa() {
        return highSchoolGpa;
    }

    public void setHighSchoolGpa(double highSchoolGpa) {
        this.highSchoolGpa = highSchoolGpa;
    }

    public double getActScore() {
        return actScore;
    }

    public void setActScore(double actScore) {
        this.actScore = actScore;
    }

    public int getSatVerbalScore() {
        return satVerbalScore;
    }

    public void setSatVerbalScore(int satVerbalScore) {
        this.satVerbalScore = satVerbalScore;
    }

    public int getSatMathScore() {
        return satMathScore;
    }

    public void setSatMathScore(int satMathScore) {
        this.satMathScore = satMathScore;
    }

    public int getSatWritingScore() {
        return satWritingScore;
    }

    public void setSatWritingScore(int satWritingScore) {
        this.satWritingScore = satWritingScore;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getMainPhone() {
        return mainPhone;
    }

    public void setMainPhone(String mainPhone) {
        this.mainPhone = mainPhone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public double getCumulativeGpa() {
        return cumulativeGpa;
    }

    public void setCumulativeGpa(double cumulativeGpa) {
        this.cumulativeGpa = cumulativeGpa;
    }

    public boolean isThesisInMajor() {
        return thesisInMajor;
    }

    public void setThesisInMajor(boolean thesisInMajor) {
        this.thesisInMajor = thesisInMajor;
    }

    public String getThesisAdvisor() {
        return thesisAdvisor;
    }

    public void setThesisAdvisor(String thesisAdvisor) {
        this.thesisAdvisor = thesisAdvisor;
    }

    public String getThesisCourse() {
        return thesisCourse;
    }

    public void setThesisCourse(String thesisCourse) {
        this.thesisCourse = thesisCourse;
    }

    public Term getThesisTerm() {
        return thesisTerm;
    }

    public void setThesisTerm(Term thesisTerm) {
        this.thesisTerm = thesisTerm;
    }

    public int getThesisYear() {
        return thesisYear;
    }

    public void setThesisYear(int thesisYear) {
        this.thesisYear = thesisYear;
    }

    public String getThesisTitle() {
        return thesisTitle;
    }

    public void setThesisTitle(String thesisTitle) {
        this.thesisTitle = thesisTitle;
    }

    public String getAcademicStatus() {
        return academicStatus;
    }

    public void setAcademicStatus(String academicStatus) {
        this.academicStatus = academicStatus;
    }

}
