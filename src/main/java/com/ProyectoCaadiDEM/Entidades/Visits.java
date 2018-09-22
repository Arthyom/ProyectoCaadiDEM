/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ProyectoCaadiDEM.Entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author frodobang
 */
@Entity
@Table(name = "Visits")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Visits.findAll", query = "SELECT v FROM Visits v")
    , @NamedQuery(name = "Visits.findById", query = "SELECT v FROM Visits v WHERE v.id = :id")
    , @NamedQuery(name = "Visits.findByEnd", query = "SELECT v FROM Visits v WHERE v.end = :end")
    , @NamedQuery(name = "Visits.findByNua", query = "SELECT v FROM Visits v WHERE v.nua = :nua")
    , @NamedQuery(name = "Visits.findBySkill", query = "SELECT v FROM Visits v WHERE v.skill = :skill")
    , @NamedQuery(name = "Visits.findByStart", query = "SELECT v FROM Visits v WHERE v.start = :start")})
public class Visits implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Column(name = "end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date end;
    @Size(max = 255)
    @Column(name = "nua")
    private String nua;
    @Size(max = 255)
    @Column(name = "skill")
    private String skill;
    @Column(name = "start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date start;
    @JoinColumn(name = "periodid", referencedColumnName = "id")
    @ManyToOne
    private Periods periodid;

    public Visits() {
    }

    public Visits(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getNua() {
        return nua;
    }

    public void setNua(String nua) {
        this.nua = nua;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Periods getPeriodid() {
        return periodid;
    }

    public void setPeriodid(Periods periodid) {
        this.periodid = periodid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Visits)) {
            return false;
        }
        Visits other = (Visits) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ProyectoCaadiDEM.Entidades.Visits[ id=" + id + " ]";
    }
    
}
