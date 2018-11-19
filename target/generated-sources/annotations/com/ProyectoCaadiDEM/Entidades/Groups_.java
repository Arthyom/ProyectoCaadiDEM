package com.ProyectoCaadiDEM.Entidades;

import com.ProyectoCaadiDEM.Beans.GroupMembers;
import com.ProyectoCaadiDEM.Entidades.Periods;
import com.ProyectoCaadiDEM.Entidades.Students;
import com.ProyectoCaadiDEM.Entidades.Teachers;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

<<<<<<< HEAD
<<<<<<< HEAD
@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2018-11-06T16:04:19")
=======
@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2018-11-08T15:50:16")
>>>>>>> 460c0dab5640b84c48e0dde401fc711a70b376fa
=======
@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2018-11-15T22:24:33")
>>>>>>> 3be2a308c842fb0f569a14da99c574a004a8e46b
@StaticMetamodel(Groups.class)
public class Groups_ { 

    public static volatile SingularAttribute<Groups, String> learningUnit;
    public static volatile SingularAttribute<Groups, String> identifier;
    public static volatile SingularAttribute<Groups, Periods> periodId;
    public static volatile SingularAttribute<Groups, Boolean> visible;
    public static volatile SingularAttribute<Groups, String> idAlterno;
    public static volatile SingularAttribute<Groups, String> level;
    public static volatile CollectionAttribute<Groups, Students> studentsCollection;
    public static volatile CollectionAttribute<Groups, GroupMembers> groupMembersCollection;
    public static volatile SingularAttribute<Groups, Integer> id;
    public static volatile SingularAttribute<Groups, Teachers> employeeNumber;

}