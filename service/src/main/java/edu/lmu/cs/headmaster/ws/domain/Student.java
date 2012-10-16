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
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import edu.lmu.cs.headmaster.ws.types.ResidencyCode;
import edu.lmu.cs.headmaster.ws.types.Sex;
import edu.lmu.cs.headmaster.ws.types.Term;
import edu.lmu.cs.headmaster.ws.util.DateTimeXmlAdapter;

/**
 * A Student is someone who is studying at a particular institution.
 */
@Entity
@XmlRootElement
public class Student {

    private Long id;
    private Boolean active = Boolean.TRUE;
    private String lastName;
    private String firstName;
    private String middleName;
    private String primaryEmail;
    private String secondaryEmail;
    private String campusBox;
    private Boolean compactSigned;
    private Boolean inLivingLearningCommunity;
    private String advisor;
    private Integer entryYear;
    private DateTime honorsEntryDate;
    private Integer expectedGraduationYear;
    private Boolean transferStudent;
    private List<Major> majors = new ArrayList<Major>();
    private List<String> minors = new ArrayList<String>();
    private Boolean hasStudiedAbroad;
    private String scholarship;
    private Sex sex;
    private String raceOrEthnicity;
    private ResidencyCode residencyCode;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String mainPhone;
    private String cellPhone;
    private String notes;
    private Boolean thesisInMajor;
    private String thesisAdvisor;
    private String thesisCourse;
    private Term thesisTerm;
    private Integer thesisYear;
    private String thesisTitle;
    private DateTime thesisSubmissionDate;
    private String thesisNotes;
    private List<Event> attendance = new ArrayList<Event>();
    private List<Grant> grants = new ArrayList<Grant>();
    private StudentRecord record = new StudentRecord();
    private List<String> foodPreference = new ArrayList<String>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlAttribute
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Indicates whether a student is still in the program or institution under
     * which this installation of Headmaster is running. Better approach than
     * having to delete the student.
     */
    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    @Lob
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Lob
    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    @Lob
    public String getSecondaryEmail() {
        return secondaryEmail;
    }

    public void setSecondaryEmail(String secondaryEmail) {
        this.secondaryEmail = secondaryEmail;
    }

    public String getCampusBox() {
        return campusBox;
    }

    public void setCampusBox(String campusBox) {
        this.campusBox = campusBox;
    }

    public Boolean isCompactSigned() {
        return compactSigned;
    }

    public void setCompactSigned(Boolean compactSigned) {
        this.compactSigned = compactSigned;
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    @OrderColumn
    public List<Major> getMajors() {
        return majors;
    }

    public void setMajors(List<Major> majors) {
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

    @Lob
    public String getScholarship() {
        return scholarship;
    }

    public void setScholarship(String scholarship) {
        this.scholarship = scholarship;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    @Lob
    public String getRaceOrEthnicity() {
        return raceOrEthnicity;
    }

    public void setRaceOrEthnicity(String raceOrEthnicity) {
        this.raceOrEthnicity = raceOrEthnicity;
    }

    public ResidencyCode getResidencyCode() {
        return residencyCode;
    }

    public void setResidencyCode(ResidencyCode residencyCode) {
        this.residencyCode = residencyCode;
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

    @Lob
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    @Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
    @XmlJavaTypeAdapter(value=DateTimeXmlAdapter.class)
    public DateTime getThesisSubmissionDate() {
        return thesisSubmissionDate;
    }

    public void setThesisSubmissionDate(DateTime thesisSubmissionDate) {
        this.thesisSubmissionDate = thesisSubmissionDate;
    }

    @Lob
    public String getThesisNotes() {
        return thesisNotes;
    }

    public void setThesisNotes(String thesisNotes) {
        this.thesisNotes = thesisNotes;
    }

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "attendees")
    @OrderBy("dateTime")
    // Event objects reference back to their student attendees, producing a
    // cycle in the data structure. Cycles will not ship out of the web service,
    // so we have to break the cycle and design our API around this limitation.
    @XmlTransient
    public List<Event> getAttendance() {
        return attendance;
    }

    public void setAttendance(List<Event> attendance) {
        this.attendance = attendance;
    }

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "students")
    @OrderBy("submissionDate")
    // Same here for grant objects.
    @XmlTransient
    public List<Grant> getGrants() {
        return grants;
    }

    public void setGrants(List<Grant> grants) {
        this.grants = grants;
    }

    // This keeps the record from going down the wire; it will have to be sent
    // out separately.
    @XmlTransient
    public StudentRecord getRecord() {
        return record;
    }

    public void setRecord(StudentRecord record) {
        this.record = record;
    }

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @Lob
    @OrderColumn
	public List<String> getFoodPreference() {
		return foodPreference;
	}

	public void setFoodPreference(List<String> foodPreference) {
		this.foodPreference = foodPreference;
	}

}
