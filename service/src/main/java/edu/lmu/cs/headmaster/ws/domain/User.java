package edu.lmu.cs.headmaster.ws.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * A user is an entity who may interact with Headmaster.
 */
@Entity
@Table(name="serviceuser")
@XmlRootElement
public class User implements Serializable {

    private Long id;
    private String login;
    private String email;
    private String challenge;
    private Boolean active;
    private String challengeRequest;
    private List<UserRole> roles;
    private Student student;

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
    @Column(unique = true)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Lob
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Lob
    @XmlTransient
    // The challenge should not go out the wire.
    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    @Transient
    // To service changes in password, we have a separate property
    // for a requested challenge.  This property, however, never
    // goes to the database.
    public String getChallengeRequest() {
        return challengeRequest;
    }

    public void setChallengeRequest(String challengeRequest) {
        this.challengeRequest = challengeRequest;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

    /**
     * A user *may* be associated with a student. Coupled with a STUDENT role,
     * this facilitates a student-centric view of the application.
     */
    @OneToOne(cascade = CascadeType.ALL)
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

}
