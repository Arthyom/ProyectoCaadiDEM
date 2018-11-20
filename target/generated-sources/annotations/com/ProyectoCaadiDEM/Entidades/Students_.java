package com.ProyectoCaadiDEM.Entidades;

import com.ProyectoCaadiDEM.Beans.GroupMembers;
import com.ProyectoCaadiDEM.Entidades.Groups;
import com.ProyectoCaadiDEM.Entidades.Visit;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< HEAD
@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2018-11-20T00:23:49")
=======
<<<<<<< HEAD
<<<<<<< HEAD
@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2018-11-06T16:04:20")
=======
@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2018-11-08T15:50:16")
>>>>>>> 460c0dab5640b84c48e0dde401fc711a70b376fa
=======
@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2018-11-15T22:24:34")
>>>>>>> 3be2a308c842fb0f569a14da99c574a004a8e46b
>>>>>>> f0ef3a65140e36dc2aeab1a907d193545bd3b943
@StaticMetamodel(Students.class)
public class Students_ { 

    public static volatile SingularAttribute<Students, Date> birthday;
    public static volatile SingularAttribute<Students, String> secondLastName;
    public static volatile SingularAttribute<Students, String> firstLastName;
    public static volatile CollectionAttribute<Students, Visit> visitCollection;
    public static volatile SingularAttribute<Students, Boolean> visible;
    public static volatile SingularAttribute<Students, String> gender;
    public static volatile SingularAttribute<Students, String> name;
    public static volatile CollectionAttribute<Students, GroupMembers> groupMembersCollection;
    public static volatile SingularAttribute<Students, String> program;
    public static volatile SingularAttribute<Students, String> nua;
    public static volatile CollectionAttribute<Students, Groups> groupsCollection;
    public static volatile SingularAttribute<Students, String> email;

}