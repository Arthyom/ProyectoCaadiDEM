/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ProyectoCaadiDEM.Fachadas;

import com.ProyectoCaadiDEM.Entidades.Visit;
import com.ProyectoCaadiDEM.Entidades.Visits;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author frodobang
 */
@Stateless
public class VisitFacade extends AbstractFacade<Visit> {

    @PersistenceContext(unitName = "com.ProyectoCaadiDEM_ProyectoCaadiDEM_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<Visit> visitasParaStd ( String nua ){
        List<Visit> ls = this.em.createNamedQuery("Visit.findByNUA").
                setParameter("nua", nua).getResultList();
        return ls;
    }
    
    public List<Visit> visitasParaHblParaStd ( String skill, String nua ){
        List<Visit> ls = this.em.createNamedQuery("Visit.findBySkillByNUA")
                .setParameter("nua", nua)
                .setParameter("skill", skill)
                .getResultList();
                
        return ls;
    }

    public VisitFacade() {
        super(Visit.class);
    }
    
}
