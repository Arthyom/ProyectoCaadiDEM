/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ProyectoCaadiDEM.Beans;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author frodo
 */
@Embeddable
public class GroupMembersPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nua")
    private String nua;
    @Basic(optional = false)
    @NotNull
    @Column(name = "groupId")
    private int groupId;

    public GroupMembersPK() {
    }

    public GroupMembersPK(String nua, int groupId) {
        this.nua = nua;
        this.groupId = groupId;
    }

    public String getNua() {
        return nua;
    }

    public void setNua(String nua) {
        this.nua = nua;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nua != null ? nua.hashCode() : 0);
        hash += (int) groupId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupMembersPK)) {
            return false;
        }
        GroupMembersPK other = (GroupMembersPK) object;
        if ((this.nua == null && other.nua != null) || (this.nua != null && !this.nua.equals(other.nua))) {
            return false;
        }
        if (this.groupId != other.groupId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ProyectoCaadiDEM.Beans.GroupMembersPK[ nua=" + nua + ", groupId=" + groupId + " ]";
    }
    
}
