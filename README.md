RES LDAP Lab
============

This is the Labo9, about LDAP from the RES course, 2014, HEIG-VD.

Made by the group Bignens Julien & Brito Carvalho Bruno.

#Tasks#

Here, we will be explaining what we have done in this lab.

## First task ##
This is the first task, the objectif is to obtain the users.csv file. To do so, there's a java program that was provided. Here is a short example of the users.csv file.

![alt tag](https://raw.githubusercontent.com/bbcnt/RES_LDAP_Lab/master/images/Extract_Users.csv.PNG?token=3993580__eyJzY29wZSI6IlJhd0Jsb2I6YmJjbnQvUkVTX0xEQVBfTGFiL21hc3Rlci9pbWFnZXMvRXh0cmFjdF9Vc2Vycy5jc3YuUE5HIiwiZXhwaXJlcyI6MTQwMzk3MDEyNn0%3D--2c50578e6603f511f32766fc16bda9640cd35150)

##Task 2 ##
This second task is all about the DIT's structure. After a few discussions with the teacher, we decided to implement a structure as flat as possible. This is the one, as shown in OpenDJ : 

![alt tag](https://raw.githubusercontent.com/bbcnt/RES_LDAP_Lab/master/images/Tree.png?token=3993580__eyJzY29wZSI6IlJhd0Jsb2I6YmJjbnQvUkVTX0xEQVBfTGFiL21hc3Rlci9pbWFnZXMvVHJlZS5wbmciLCJleHBpcmVzIjoxNDAzOTcwMTM5fQ%3D%3D--51b479b0a6183986394b7462fab0e201189d7ca2)

In a more visual way, this is the one we made "on paper":

![alt tag](https://raw.githubusercontent.com/bbcnt/RES_LDAP_Lab/master/images/DIT_Structure.png?token=3993580__eyJzY29wZSI6IlJhd0Jsb2I6YmJjbnQvUkVTX0xEQVBfTGFiL21hc3Rlci9pbWFnZXMvRElUX1N0cnVjdHVyZS5wbmciLCJleHBpcmVzIjoxNDAzOTcwMDg5fQ%3D%3D--2d03bd7a068ff14464fa38f616aee18fcba169a0)

The fact that our "tree" is as flat as possible makes it easier to treat certain "exceptions", such as having a teacher that also is part of administration, or someone teaching in two different departments. In this fashion, our people can easily switch from TIC to TIN, from Student to Assistant, etc.

Now there was an other problem. To represent users, we use the class inetOrgPerson, the thing is, we want to store the "gender" of the person, but this generic classObject does not possess such a field. We had to add our own custom attribute, called "sexType", that we added to the class heigPeople. To do this, we used OpenDJ, to modifiy the Object class heigPeople (which is where our people are stored, and add a new attribute, which was sexType). This attribute was also created with the OpenDJ assistant. It created this ldif file:
    
    dn: cn=schema
     changetype: modify
     add: objectClasses
     objectClasses: ( heigpeople-oid NAME 'heigPeople' DESC 'People from HEIG-VD' SUP ( inetOrgPerson $ top ) STRUCTURAL MAY sexType X-SCHEMA-FILE 'heig-user.ldif'
 
![alt tag](https://raw.githubusercontent.com/bbcnt/RES_LDAP_Lab/master/images/ADD_sexType.png?token=3993580__eyJzY29wZSI6IlJhd0Jsb2I6YmJjbnQvUkVTX0xEQVBfTGFiL21hc3Rlci9pbWFnZXMvQUREX3NleFR5cGUucG5nIiwiZXhwaXJlcyI6MTQwMzk3MDE1OH0%3D--9d470661648b52dbae128dba0882beb9c2889167)   

To create the heigPeople class, this file was used:

    dn: cn=schema
     changetype: modify
     add: objectClasses
     objectClasses: ( heigPeople-oid NAME 'heigPeople' DESC 'People from HEIG-VD' SUP ( inetOrgPerson $ top ) STRUCTURAL MAY sexType X-SCHEMA-FILE 'heig-user.ldif' )




There was an other problem, LDAP does not tolerate accents in mails, so we found a method that "converts" accents to normal characters. It is called when we generate the ldif file.

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


![alt tag](https://raw.githubusercontent.com/bbcnt/RES_LDAP_Lab/master/images/Manage_Entries.png?token=3993580__eyJzY29wZSI6IlJhd0Jsb2I6YmJjbnQvUkVTX0xEQVBfTGFiL21hc3Rlci9pbWFnZXMvTWFuYWdlX0VudHJpZXMucG5nIiwiZXhwaXJlcyI6MTQwMzk3MDE3NX0%3D--14be356e75bb31701a6bab7b917622f71aa80025)

On the left, all the "People" can be seen and some filters can also be applied here. Talking about filters, this is what the new task is all about.

## Task 4 ##

This task is meant to explore some of the filters available.

**What is the number (not the list!) of people stored in the directory?**


    ./ldapsearch --hostname 127.0.0.1 --port 1389 --bindDN "cn=Directory Manager" --bindPassword 123456 --countentries --baseDN "dc=heigvd,dc=ch" "(objectclass=heigPeople)" ldapentrycount


> Result is 10'000

**What is the number of departments stored in the directory?**

    ./ldapsearch --hostname 127.0.0.1 --port 1389 --bindDN "cn=Directory Manager" --bindPassword 123456 --countentries --baseDN "dc=heigvd,dc=ch" "(objectclass=organizationalUnit)" ldapentrycount

> Result is 8 (it also takes Departments and People into account).

**What is the list of people who belong to the TIC Department?**

    ./ldapsearch --hostname 127.0.0.1 --port 1389 --bindDN "cn=Directory Manager" --bindPassword 123456 --baseDN "dc=heigvd,dc=ch" "(departmentNumber=TIC)"

**What is the list of students in the directory?**

    ./ldapsearch --hostname 127.0.0.1 --port 1389 --bindDN "cn=Directory Manager" --bindPassword 123456 --baseDN "dc=heigvd,dc=ch" "(employeeType=Student)"

**What is the list of students in the TIC Department?**

    ./ldapsearch --hostname 127.0.0.1 --port 1389 --bindDN "cn=Directory Manager" --bindPassword 123456 --baseDN "dc=heigvd,dc=ch" "(&(departmentNumber=TIC)(employeeType=student))" 

So these were the easy ones. Now for the dynamic groups :

**What command do you run to define a dynamic group that represents all members of the TIN Department?**

    dn: cn=TINPeople,ou=People,dc=heigvd,dc=ch
    cn: TINPeople
    objectClass: top
    objectClass: groupOfURLs
    ou: People
    memberURL: ldap:///ou=People,dc=heigvd,dc=ch??sub?(departmentNumber=TIN)

And then : 

    ./ldapmodify --port 1389 --bindDN "cn=Directory Manager" --bindPassword 123456 --defaultAdd --filename /home/bruno/TINPeople.ldif 

**What command do you run to get the list of all members of the TIN Department?**   
 
    ./ldapsearch --hostname 127.0.0.1 --port 1389 --bindDN "cn=Directory Manager" --bindPassword 123456 --baseDN "dc=heigvd,dc=ch" "(&(objectclass=heigPeople)(isMemberOf=cn=TINPeople,ou=People,dc=heigvd,dc=ch))"

> dn: uid=EID_109975,ou=people,dc=heigvd,dc=ch
> objectClass: person
> objectClass: organizationalPerson
> objectClass: inetOrgPerson
> objectClass: top
> objectClass: heigPeople
> sexType: MALE
> givenName: Edgar
> uid: EID_109975
> cn: Edgar.De Decker
> sn: De Decker
> telephoneNumber: (024) 777 705 519
> mail: edgar.de decker@heig-vd.ch
> departmentNumber: **TIN**
> employeeType: Professor
> 
> dn: uid=EID_109976,ou=people,dc=heigvd,dc=ch
> objectClass: person
> objectClass: organizationalPerson
> objectClass: inetOrgPerson
> objectClass: top
> objectClass: heigPeople
> sexType: MALE
> givenName: Dylan
> uid: EID_109976
> cn: Dylan.Bauer
> sn: Bauer
> telephoneNumber: (024) 777 832 310
> mail: dylan.bauer@heig-vd.ch
> departmentNumber: **TIN**
> employeeType: Student
> 
> dn: uid=EID_109990,ou=people,dc=heigvd,dc=ch
> objectClass: person
> objectClass: organizationalPerson
> objectClass: inetOrgPerson
> objectClass: top
> objectClass: heigPeople
> sexType: MALE
> givenName: Guillaume
> uid: EID_109990
> cn: Guillaume.Durant
> sn: Durant
> telephoneNumber: (024) 777 836 858
> mail: guillaume.durant@heig-vd.ch
> departmentNumber: **TIN**
> employeeType: Admin

**What command do you run to define a dynamic group that represents all students with a last name starting with the letter 'A'?**
    
    dn: cn=StartingByA,ou=People,dc=heigvd,dc=ch
    cn: StartingByA
    objectClass: top
    objectClass: groupOfURLs
    ou: People
    memberURL: ldap:///ou=People,dc=heigvd,dc=ch??sub?(&(employeeType=student)(sn=A*))

**Note:** There are no results with A, so in the example, we will use B instead. (Just change the sn=A* in sn=B*)

And then we do this : 
    
    ./ldapmodify --port 1389 --bindDN "cn=Directory Manager" --bindPassword 123456 --defaultAdd --filename /home/bruno/NamesStartingByA.ldif Processing ADD request for cn=StartingByA,ou=People,dc=heigvd,dc=ch

**What command do you run to get the list of these students?**

    ./ldapsearch --hostname 127.0.0.1 --port 1389 --bindDN "cn=Directory Manager" --bindPassword 123456 --baseDN "dc=heigvd,dc=ch" "(&(objectclass=heigPeople)(isMemberOf=cn=StartingByA,ou=People,dc=heigvd,dc=ch))"

> dn: uid=EID_109895,ou=people,dc=heigvd,dc=ch
> objectClass: person
> objectClass: organizationalPerson
> objectClass: inetOrgPerson
> objectClass: top
> objectClass: heigPeople
> sexType: FEMALE
> givenName: Carine
> uid: EID_109895
> cn: Carine.**Bauer**
> sn: Bauer
> telephoneNumber: (024) 777 538 119
> mail: carine.bauer@heig-vd.ch
> departmentNumber: HEG
> employeeType: Student
> 
> dn: uid=EID_109941,ou=people,dc=heigvd,dc=ch
> objectClass: person
> objectClass: organizationalPerson
> objectClass: inetOrgPerson
> objectClass: top
> objectClass: heigPeople
> sexType: FEMALE
> givenName: Faustine
> uid: EID_109941
> cn: Faustine.**Bauer**
> sn: Bauer
> telephoneNumber: (024) 777 412 637
> mail: faustine.bauer@heig-vd.ch
> departmentNumber: TIN
> employeeType: Student
> 
> dn: uid=EID_109947,ou=people,dc=heigvd,dc=ch
> objectClass: person
> objectClass: organizationalPerson
> objectClass: inetOrgPerson
> objectClass: top
> objectClass: heigPeople
> sexType: MALE
> givenName: Henri
> uid: EID_109947
> cn: Henri.**Bauer**
> sn: Bauer
> telephoneNumber: (024) 777 555 170
> mail: henri.bauer@heig-vd.ch
> departmentNumber: TIC
> employeeType: Student
> 
> dn: uid=EID_109976,ou=people,dc=heigvd,dc=ch
> objectClass: person
> objectClass: organizationalPerson
> objectClass: inetOrgPerson
> objectClass: top
> objectClass: heigPeople
> sexType: MALE
> givenName: Dylan
> uid: EID_109976
> cn: Dylan.**Bauer**
> sn: Bauer
> telephoneNumber: (024) 777 832 310
> mail: dylan.bauer@heig-vd.ch
> departmentNumber: TIN
> employeeType: Student

Si this is it !

By the way, this website was plenty useful for dynamic groups: 
> http://docs.oracle.com/cd/E19450-01/820-6169/defining-dynamic-groups.html

