package edu.lmu.cs.headmaster.ws.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import edu.lmu.cs.headmaster.ws.util.DateTimeXmlAdapter;

/**
 * A Student is someone who is studying at a particular institution.
 */
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
    private Boolean inLivingLearningCommunity;
    private String advisor;
    private Integer entryYear;
    private DateTime honorsEntryDate;
    private Integer expectedGraduationYear;
    private Boolean transferStudent;
    private String degree;
    private List<String> majors = new ArrayList<String>();
    private List<String> minors = new ArrayList<String>();
    private Boolean hasStudiedAbroad;
    private Double highSchoolGpa;
    private Double actScore;
    private Integer satVerbalScore;
    private Integer satMathScore;
    private Integer satWritingScore;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String mainPhone;
    private String cellPhone;
    private Double cumulativeGpa;
    private Boolean thesisInMajor;
    private String thesisAdvisor;
    private String thesisCourse;
    private Term thesisTerm;
    private Integer thesisYear;
    private String thesisTitle;
    private String academicStatus;
    private List<GPA> grades = new ArrayList<GPA>();
    private List<Event> attendance = new ArrayList<Event>();
    private List<Grant> grants = new ArrayList<Grant>();

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

    @Lob
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Lob
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

    @Lob
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

    @Lob
    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public Boolean isInLivingLearningCommunity() {
        return inLivingLearningCommunity;
    }

    public void setInLivingLearningCommunity(Boolean inLivingLearningCommunity) {
        this.inLivingLearningCommunity = inLivingLearningCommunity;
    }

    @Lob
    public String getAdvisor() {
        return advisor;
    }

    public void setAdvisor(String advisor) {
        this.advisor = advisor;
    }

    public Integer getEntryYear() {
        return entryYear;
    }

    public void setEntryYear(Integer entryYear) {
        this.entryYear = entryYear;
    }

    @Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
    @XmlJavaTypeAdapter(value=DateTimeXmlAdapter.class)
    public DateTime getHonorsEntryDate() {
        return honorsEntryDate;
    }

    public void setHonorsEntryDate(DateTime honorsEntryDate) {
        this.honorsEntryDate = honorsEntryDate;
    }

    public Integer getExpectedGraduationYear() {
        return expectedGraduationYear;
    }

    public void setExpectedGraduationYear(Integer expectedGraduationYear) {
        this.expectedGraduationYear = expectedGraduationYear;
    }

    public Boolean isTransferStudent() {
        return transferStudent;
    }

    public void setTransferStudent(Boolean transferStudent) {
        this.transferStudent = transferStudent;
    }

    @Lob
    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @Lob
    @OrderColumn
    public List<String> getMajors() {
        return majors;
    }

    public void setMajors(List<String> majors) {
        this.majors = majors;
    }

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @Lob
    @OrderColumn
    public List<String> getMinors() {
        return minors;
    }

    public void setMinors(List<String> minors) {
        this.minors = minors;
    }

    public Boolean isHasStudiedAbroad() {
        return hasStudiedAbroad;
    }

    public void setHasStudiedAbroad(Boolean hasStudiedAbroad) {
        this.hasStudiedAbroad = hasStudiedAbroad;
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

    @Lob
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Lob
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

    public Double getCumulativeGpa() {
        return cumulativeGpa;
    }

    public void setCumulativeGpa(Double cumulativeGpa) {
        this.cumulativeGpa = cumulativeGpa;
    }

    public Boolean isThesisInMajor() {
        return thesisInMajor;
    }

    public void setThesisInMajor(Boolean thesisInMajor) {
        this.thesisInMajor = thesisInMajor;
    }

    @Lob
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

    public Integer getThesisYear() {
        return thesisYear;
    }

    public void setThesisYear(Integer thesisYear) {
        this.thesisYear = thesisYear;
    }

    @Lob
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

    @OneToMany
    public List<GPA> getGrades() {
        return grades;
    }

    public void setGrades(List<GPA> grades) {
        this.grades = grades;
    }

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "attendees")
    public List<Event> getAttendance() {
        return attendance;
    }

    public void setAttendance(List<Event> attendance) {
        this.attendance = attendance;
    }

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "students")
    public List<Grant> getGrants() {
        return grants;
    }

    public void setGrants(List<Grant> grants) {
        this.grants = grants;
    }

}
