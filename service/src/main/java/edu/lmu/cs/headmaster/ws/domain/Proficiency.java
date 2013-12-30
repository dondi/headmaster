package edu.lmu.cs.headmaster.ws.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import edu.lmu.cs.headmaster.ws.types.ProficiencyLevel;

/**
 * A Proficiency is a particular rating for an outcome.
 */
@Entity
@XmlRootElement
public class Proficiency {

    private Long id;
    private SubmissionFeedback submission;
    private LearningOutcome outcome;
    private ProficiencyLevel level;
    private String assessment;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlAttribute
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public SubmissionFeedback getSubmissionFeedback() {
        return submission;
    }

    public void setSubmissionFeedback(SubmissionFeedback submissionFeedback) {
        this.submission = submissionFeedback;
    }

    /**
     * Integrity constraint: the outcome referenced here must be one of the outcomes
     * from the associated feedback's assignment, and must be the only one with this
     * outcome for that particular feedback instance.
     */
    @ManyToOne
    public LearningOutcome getOutcome() {
        return outcome;
    }

    public void setOutcome(LearningOutcome outcome) {
        this.outcome = outcome;
    }

    public ProficiencyLevel getLevel() {
        return level;
    }

    public void setLevel(ProficiencyLevel level) {
        this.level = level;
    }

    @Lob
    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

}
