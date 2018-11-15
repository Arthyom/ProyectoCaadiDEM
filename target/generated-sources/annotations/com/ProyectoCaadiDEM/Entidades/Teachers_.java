package com.ProyectoCaadiDEM.Entidades;

import com.ProyectoCaadiDEM.Entidades.Groups;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2018-11-14T21:11:08")
@StaticMetamodel(Teachers.class)
public class Teachers_ { 

    public static volatile SingularAttribute<Teachers, String> secondLastName;
    public static volatile SingularAttribute<Teachers, String> firstLastName;
    public static volatile SingularAttribute<Teachers, Boolean> visible;
    public static volatile SingularAttribute<Teachers, String> gender;
    public static volatile SingularAttribute<Teachers, String> name;
    public static volatile CollectionAttribute<Teachers, Groups> groupsCollection;
    public static volatile SingularAttribute<Teachers, String> email;
    public static volatile SingularAttribute<Teachers, String> employeeNumber;

}