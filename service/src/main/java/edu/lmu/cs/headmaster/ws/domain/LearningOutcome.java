package edu.lmu.cs.headmaster.ws.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * An Outcome is a specific task/ability for which a student is given a proficiency.
 */
@Entity
@XmlRootElement
public class LearningOutcome {

    private Long id;
    private String description;
    private LearningObjective objective;

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
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne
    public LearningObjective getObjective() {
        return objective;
    }

    public void setObjective(LearningObjective objective) {
        this.objective = objective;
    }
}
