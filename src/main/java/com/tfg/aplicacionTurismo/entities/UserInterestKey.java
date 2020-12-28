package com.tfg.aplicacionTurismo.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserInterestKey implements Serializable {

    @Column(name = "interest_id")
    private long interestId;

    @Column(name = "user_id")
    private long userId;

    public UserInterestKey() {
    }

    public UserInterestKey(long interestId, long userId) {
        this.interestId = interestId;
        this.userId = userId;
    }

    public long getInterestId() {
        return interestId;
    }

    public void setInterestId(long interestId) {
        this.interestId = interestId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInterestKey that = (UserInterestKey) o;
        return Objects.equals(interestId, that.interestId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(interestId, userId);
    }
}
