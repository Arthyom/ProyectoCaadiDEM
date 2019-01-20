package com.ProyectoCaadiDEM.Entidades;

import com.ProyectoCaadiDEM.Beans.GroupMembers;
import com.ProyectoCaadiDEM.Entidades.Periods;
import com.ProyectoCaadiDEM.Entidades.Students;
import com.ProyectoCaadiDEM.Entidades.Teachers;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2019-01-20T00:59:25")
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