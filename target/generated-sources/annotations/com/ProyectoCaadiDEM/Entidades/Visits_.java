package com.ProyectoCaadiDEM.Entidades;

import com.ProyectoCaadiDEM.Entidades.Periods;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2019-02-19T01:29:06")
@StaticMetamodel(Visits.class)
public class Visits_ { 

    public static volatile SingularAttribute<Visits, Periods> periodid;
    public static volatile SingularAttribute<Visits, Boolean> visible;
    public static volatile SingularAttribute<Visits, String> skill;
    public static volatile SingularAttribute<Visits, Date> start;
    public static volatile SingularAttribute<Visits, Date> end;
    public static volatile SingularAttribute<Visits, Long> id;
    public static volatile SingularAttribute<Visits, String> nua;

}