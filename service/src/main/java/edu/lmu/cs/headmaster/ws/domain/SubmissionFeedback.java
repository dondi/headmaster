package edu.lmu.cs.headmaster.ws.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import edu.lmu.cs.headmaster.ws.util.DateTimeXmlAdapter;

/**
 * A Submission records a student's fulfillment of an assignment.
 */
@Entity
@XmlRootElement
public class SubmissionFeedback {

    private Long id;
    private DateTime feedbackDate;
    private Student student;
    private Assignment assignment;
    private List<Proficiency> proficiencies = new ArrayList<Proficiency>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlAttribute
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
    @XmlJavaTypeAdapter(value=DateTimeXmlAdapter.class)
    public DateTime getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(DateTime feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    @ManyToOne
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @ManyToOne
    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    @OneToMany(mappedBy = "submissionFeedback", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<Proficiency> getProficiencies() {
        return proficiencies;
    }

    public void setProficiencies(List<Proficiency> proficiencies) {
        this.proficiencies = proficiencies;
    }

}
