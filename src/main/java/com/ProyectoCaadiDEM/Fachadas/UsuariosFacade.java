
package com.ProyectoCaadiDEM.Fachadas;

import com.ProyectoCaadiDEM.Entidades.Usuarios;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UsuariosFacade extends AbstractFacade<Usuarios> {

    @PersistenceContext(unitName = "com.ProyectoCaadiDEM_ProyectoCaadiDEM_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Usuarios buscarPorNombreYPass (String nombre, String pass){
        
        Usuarios usr ;
        try {
            usr = (Usuarios) this.em.createNamedQuery("Usuarios.findByPassAndName")
                .setParameter("pass", pass)
                .setParameter("nombre",nombre).getSingleResult();
            
        } catch (Exception e) {
            return null;
        }
        return usr;
    }
    
    public UsuariosFacade() {
        super(Usuarios.class);
    }
    
        public EntityManager getEm() {
        return em;
    }
}
