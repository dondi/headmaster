package edu.lmu.cs.headmaster.ws.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * An Assignment is a standards-based task that is performed by a student
 * (and graded by the instructor).
 */
@Entity
@XmlRootElement
public class Assignment {

    private Long id;
    private String instructions;
    private Course course;
    private List<LearningOutcome> outcomes = new ArrayList<LearningOutcome>();;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlAttribute
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Lob
    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @ManyToOne
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @OrderColumn
    @JoinTable(
        joinColumns = @JoinColumn(name = "assignment_id"),
        inverseJoinColumns = @JoinColumn(name = "outcome_id")
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<LearningOutcome> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(List<LearningOutcome> outcomes) {
        this.outcomes = outcomes;
    }

}
