/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ProyectoCaadiDEM.Beans;

import com.ProyectoCaadiDEM.Entidades.Groups;
import com.ProyectoCaadiDEM.Entidades.Students;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author frodo
 */
@Entity
@Table(name = "GroupMembers")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GroupMembers.findAll", query = "SELECT g FROM GroupMembers g")
    , @NamedQuery(name = "GroupMembers.findByNua", query = "SELECT g FROM GroupMembers g WHERE g.groupMembersPK.nua = :nua")
    , @NamedQuery(name = "GroupMembers.findByGroupId", query = "SELECT g FROM GroupMembers g WHERE g.groupMembersPK.groupId = :groupId")
    , @NamedQuery(name = "GroupMembers.findByVisible", query = "SELECT g FROM GroupMembers g WHERE g.visible = :visible")})
public class GroupMembers implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GroupMembersPK groupMembersPK;
    @Column(name = "visible")
    private Boolean visible;
    @JoinColumn(name = "groupId", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Groups groups;
    @JoinColumn(name = "nua", referencedColumnName = "nua", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Students students;

    public GroupMembers() {
    }

    public GroupMembers(GroupMembersPK groupMembersPK) {
        this.groupMembersPK = groupMembersPK;
    }

    public GroupMembers(String nua, int groupId) {
        this.groupMembersPK = new GroupMembersPK(nua, groupId);
    }

    public GroupMembersPK getGroupMembersPK() {
        return groupMembersPK;
    }

    public void setGroupMembersPK(GroupMembersPK groupMembersPK) {
        this.groupMembersPK = groupMembersPK;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Groups getGroups() {
        return groups;
    }

    public void setGroups(Groups groups) {
        this.groups = groups;
    }

    public Students getStudents() {
        return students;
    }

    public void setStudents(Students students) {
        this.students = students;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupMembersPK != null ? groupMembersPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupMembers)) {
            return false;
        }
        GroupMembers other = (GroupMembers) object;
        if ((this.groupMembersPK == null && other.groupMembersPK != null) || (this.groupMembersPK != null && !this.groupMembersPK.equals(other.groupMembersPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ProyectoCaadiDEM.Beans.GroupMembers[ groupMembersPK=" + groupMembersPK + " ]";
    }
    
}
