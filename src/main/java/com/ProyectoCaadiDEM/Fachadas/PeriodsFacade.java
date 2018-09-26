/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ProyectoCaadiDEM.Fachadas;

import com.ProyectoCaadiDEM.Entidades.Periods;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author frodobang
 */
@Stateless
public class PeriodsFacade extends AbstractFacade<Periods> {

    @PersistenceContext(unitName = "com.ProyectoCaadiDEM_ProyectoCaadiDEM_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PeriodsFacade() {
        super(Periods.class);
    }
    
    
    public boolean buscarPrdActual (){
        List<Periods> lp = this.em.createNamedQuery("Periods.findByActual")
                .getResultList();
                
        if(lp.isEmpty())
            return false;
        
        return true;
    }
    
    public Periods conseguirPrdActual (){
        
        Periods p;
        try {
            p =(Periods) this.em.createNamedQuery("Periods.findByActual")
                .getSingleResult();
        }
        catch (Exception e) 
        {
            p = null;
        }
        return p;
    }
}
