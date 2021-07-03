package com.tfg.aplicacionTurismo.entities;

import javax.persistence.*;
import java.util.Objects;

/**
 * Clase que representa las valoraciones dadas por los usuarios a las distintas actividades recomendadas.
 *
 * @author Alba Serena Suárez
 * @version 1.0
 */
@Entity
public class RelUserActivity {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    private int valuation;

    /**
     * Constructor de la clase RelUserActivity
     */
    public RelUserActivity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelUserActivity that = (RelUserActivity) o;
        return Objects.equals(activity, that.activity) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activity, user);
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getValuation() {
        return valuation;
    }

    public void setValuation(int valuation) {
        this.valuation = valuation;
    }
}
