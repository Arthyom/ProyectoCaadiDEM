package com.ProyectoCaadiDEM.Beans;

import com.ProyectoCaadiDEM.Beans.GroupMembersPK;
import com.ProyectoCaadiDEM.Entidades.Groups;
import com.ProyectoCaadiDEM.Entidades.Students;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2019-01-11T17:52:51")
@StaticMetamodel(GroupMembers.class)
public class GroupMembers_ { 

    public static volatile SingularAttribute<GroupMembers, GroupMembersPK> groupMembersPK;
    public static volatile SingularAttribute<GroupMembers, Boolean> visible;
    public static volatile SingularAttribute<GroupMembers, Groups> groups;
    public static volatile SingularAttribute<GroupMembers, Students> students;

}