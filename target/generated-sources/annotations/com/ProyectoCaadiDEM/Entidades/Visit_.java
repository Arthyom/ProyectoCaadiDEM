package com.ProyectoCaadiDEM.Entidades;

import com.ProyectoCaadiDEM.Entidades.Periods;
import com.ProyectoCaadiDEM.Entidades.Students;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2019-08-07T19:17:39")
@StaticMetamodel(Visit.class)
public class Visit_ { 

    public static volatile SingularAttribute<Visit, Periods> periodId;
    public static volatile SingularAttribute<Visit, Boolean> visible;
    public static volatile SingularAttribute<Visit, String> skill;
    public static volatile SingularAttribute<Visit, Date> start;
    public static volatile SingularAttribute<Visit, Date> end;
    public static volatile SingularAttribute<Visit, Integer> id;
    public static volatile SingularAttribute<Visit, Students> nua;

}