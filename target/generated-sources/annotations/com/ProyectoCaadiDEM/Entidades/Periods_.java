package com.ProyectoCaadiDEM.Entidades;

import com.ProyectoCaadiDEM.Entidades.Groups;
import com.ProyectoCaadiDEM.Entidades.Visit;
import com.ProyectoCaadiDEM.Entidades.Visits;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< HEAD
@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2018-11-06T16:04:20")
=======
@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2018-11-08T15:50:16")
>>>>>>> 460c0dab5640b84c48e0dde401fc711a70b376fa
@StaticMetamodel(Periods.class)
public class Periods_ { 

    public static volatile SingularAttribute<Periods, Boolean> actual;
    public static volatile CollectionAttribute<Periods, Visit> visitCollection;
    public static volatile SingularAttribute<Periods, Boolean> visible;
    public static volatile SingularAttribute<Periods, Date> start;
    public static volatile SingularAttribute<Periods, String> description;
    public static volatile CollectionAttribute<Periods, Visits> visitsCollection;
    public static volatile SingularAttribute<Periods, Date> end;
    public static volatile SingularAttribute<Periods, Integer> id;
    public static volatile CollectionAttribute<Periods, Groups> groupsCollection;

}