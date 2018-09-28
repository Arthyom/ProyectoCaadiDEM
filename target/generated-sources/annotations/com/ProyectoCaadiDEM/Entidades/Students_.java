package com.ProyectoCaadiDEM.Entidades;

import com.ProyectoCaadiDEM.Entidades.Groups;
import com.ProyectoCaadiDEM.Entidades.Visit;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2018-09-27T23:19:56")
@StaticMetamodel(Students.class)
public class Students_ { 

    public static volatile SingularAttribute<Students, Date> birthday;
    public static volatile SingularAttribute<Students, String> secondLastName;
    public static volatile SingularAttribute<Students, String> firstLastName;
    public static volatile CollectionAttribute<Students, Visit> visitCollection;
    public static volatile SingularAttribute<Students, String> gender;
    public static volatile SingularAttribute<Students, String> name;
    public static volatile SingularAttribute<Students, String> nua;
    public static volatile SingularAttribute<Students, String> program;
    public static volatile CollectionAttribute<Students, Groups> groupsCollection;
    public static volatile SingularAttribute<Students, String> email;

}