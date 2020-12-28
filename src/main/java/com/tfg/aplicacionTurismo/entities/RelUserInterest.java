package com.tfg.aplicacionTurismo.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "rel_user_interest")
@IdClass(UserInterestId.class)
public class RelUserInterest implements Serializable {

    @Id
    @ManyToOne
    //@MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;

    @Id
    @ManyToOne
    //@MapsId("interestId")
    @JoinColumn(name = "interest_id", referencedColumnName = "id")
    Interest interest;

    int priority;

    public RelUserInterest() {
    }

    public Interest getInterest() {
        return interest;
    }

    public void setInterest(Interest interest) {
        this.interest = interest;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


}
