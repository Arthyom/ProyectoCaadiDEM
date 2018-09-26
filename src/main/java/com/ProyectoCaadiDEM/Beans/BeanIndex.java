
package com.ProyectoCaadiDEM.Beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@Named(value = "beanIndex")
@SessionScoped
public class BeanIndex implements Serializable {

    public BeanIndex() {
    }
    
}
