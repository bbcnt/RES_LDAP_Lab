RES LDAP Lab
============

This is the Labo9, about LDAP from the RES course, 2014, HEIG-VD.

Made by the group Bignens Julien & Brito Carvalho Bruno.

#Tasks#

Here, we will be explaining what we have done in this lab.

## First task ##
This is the first task, the objectif is to obtain the users.csv file. To do so, there's a java program that was provided. Here is a short example of the users.csv file.

![alt tag](https://raw.githubusercontent.com/bbcnt/RES_LDAP_Lab/master/images/Extract_Users.csv.PNG?token=3993580__eyJzY29wZSI6IlJhd0Jsb2I6YmJjbnQvUkVTX0xEQVBfTGFiL21hc3Rlci9pbWFnZXMvRXh0cmFjdF9Vc2Vycy5jc3YuUE5HIiwiZXhwaXJlcyI6MTQwMzI1NTI0Mn0%3D--9613157522dd34b447b6a0502b15f07e8f836264)

##Task 2 ##
This second task is all about the DIT's structure. After a few discussions with the teacher, we decided to implement a structure as flat as possible. This is the one, as shown in OpenDJ : 

![alt tag](https://raw.githubusercontent.com/bbcnt/RES_LDAP_Lab/master/images/Tree.png?token=3993580__eyJzY29wZSI6IlJhd0Jsb2I6YmJjbnQvUkVTX0xEQVBfTGFiL21hc3Rlci9pbWFnZXMvVHJlZS5wbmciLCJleHBpcmVzIjoxNDAzMjU1NjcyfQ%3D%3D--95c7f87ee801a86e88db4cd7c356b869257a6246)

In a more visual way, this is the one we made "on paper":

![alt tag](https://raw.githubusercontent.com/bbcnt/RES_LDAP_Lab/master/images/DIT_Structure.png?token=3993580__eyJzY29wZSI6IlJhd0Jsb2I6YmJjbnQvUkVTX0xEQVBfTGFiL21hc3Rlci9pbWFnZXMvRElUX1N0cnVjdHVyZS5wbmciLCJleHBpcmVzIjoxNDAzMjU2MjU5fQ%3D%3D--b35c1d00171fd87e478992f38fc14e2b228e92c1)

**Note:** People and Departments are OU (Organizational Units) and Under them, Ana, Bob and Marc Are cn (common Names).

The fact that our "tree" is as flat as possible makes it easier to treat certain "exceptions", such as having a teacher that also is part of administration, or someone teaching in two different departments. In this fashion, our people can easily switch from TIC to TIN, from Student to Assistant, etc.

Now there was an other problem. To represent user, we use the class inetOrgPerson, the thing is, we want to store the "gender" of the person, but this generic classObject does not possess such a field. We had to add our own custom attribute, called "sexType". To do this, we used OpenDJ, to modifiy the Object class heigPeople (which is where our people are stored, and add a new attribute, which was sexType).



## Task 3 ##

This next step is the one where we actually import the data to the LDAP directory. To do so, we have decided to parse the csv file and to obtain, an ldif file. With this file, it becomes really easy to import data in a LDAP program.

In the end, we obtain something that looks like this :
    
    dn: uid=EID_100001,ou=people,dc=heigvd,dc=ch
    objectClass: top
    objectClass: inetOrgPerson
    objectClass: top
    objectClass: heigPeople
    uid: EID_100001
    givenName: Amélie
    sn: Smith
    cn: Amélie.Smith
    sexType: FEMALE
    employeeType: Admin
    departmentNumber: EC
    telephoneNumber: (024) 777 439 391
    mail: amelie.smith@heig-vd.ch
    
    dn: uid=EID_100002,ou=people,dc=heigvd,dc=ch
    objectClass: top
    objectClass: inetOrgPerson
    objectClass: top
    objectClass: heigPeople
    uid: EID_100002
    givenName: Dorothé
    sn: Dupont
    cn: Dorothé.Dupont
    sexType: FEMALE
    employeeType: Professor
    departmentNumber: DEPG
    telephoneNumber: (024) 777 152 170
    mail: dorothe.dupont@heig-vd.ch

So there is an entry for each person. The departments are also present at the end of this doc. The java source code and the users.ldif are in the deliverables directory.

Once this was imported in OpenDJ, using the import option, we get this result :













http://docs.oracle.com/cd/E19450-01/820-6169/defining-dynamic-groups.html