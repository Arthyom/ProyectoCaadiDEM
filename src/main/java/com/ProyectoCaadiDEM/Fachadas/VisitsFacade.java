/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ProyectoCaadiDEM.Fachadas;

import com.ProyectoCaadiDEM.Entidades.Visits;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author frodobang
 */
@Stateless
public class VisitsFacade extends AbstractFacade<Visits> {

    @PersistenceContext(unitName = "com.ProyectoCaadiDEM_ProyectoCaadiDEM_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VisitsFacade() {
        super(Visits.class);
    }
    
}
