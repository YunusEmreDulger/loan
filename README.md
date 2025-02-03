Hello,

First of all, clone this branch to your local,

-mvn clean
-mvn install

After downloading the related dependencies with the commands,

you can get it up and running in your local to support minimum java17.

http://localhost:8080/swagger-ui/index.html

You can access Swagger-ui from the link above in your local.

http://localhost:8080/h2-console

You can access the h2 database with the link.

When you search for "Database available at" in your local logs, you will find an address similar to 'jdbc:h2:mem:95b695d0-8d11-462f-893c-38ac3359fa78',

you will need to enter this address in the JDBC URL section.

Example;

DriverClass : org.hibernate.dialect.H2Dialect
JDBC URL: jdbc:h2:mem:95b695d0-8d11-462f-893c-38ac3359fa78

You can log in to the database by filling in the form and leaving the username and password sections blank.

When the application starts up in your local, the scripts in src/main/resources/data.sql will have run and your ready-to-use data will take its place in the relevant tables.

If you wish, you can test different scenarios by changing this script.

Customers with ID 1 and 2 are also admin users. They can perform transactions for all customers.

However, since the others are only customers, they can only perform transactions for themselves.

A total of 9 customers, 2 of them are also admin users,

There are 2 loans in total, one has 6 installments, the other has 1 last installment.

You can perform tests on these scenarios,

You can define and test new loans for all customers with users with ID 1 and 2.


Merhaba,

Öncelikle bu branch'i localinize klonlayıp, 

-mvn clean
-mvn install 

komutlarıyla ilgili dependency'leri indirdikten sonra

minimum java17'yi destekleyecek şekilde localinizde ayağa kaldırabilirsiniz.

http://localhost:8080/swagger-ui/index.html

Localinizde yukarıdaki linkten Swagger-ui'a erişebilirsiniz.

http://localhost:8080/h2-console

linki ile h2 veritabanına erişebilirsiniz.

Local loglarınızda "Database available at" diye arattığınızda  'jdbc:h2:mem:95b695d0-8d11-462f-893c-38ac3359fa78'  benzeri bir adres bulacaksınız,

bu adresi JDBC URL kısmına girmeniz gerekecek.

Örnek;

DriverClass : org.hibernate.dialect.H2Dialect
JDBC URL: jdbc:h2:mem:95b695d0-8d11-462f-893c-38ac3359fa78

şeklinde doldurup username ve password kısmını boş bırakarak veritabanına login olabilirsiniz.

Uygulama localinizde ayağa kalktığında src/main/resources/data.sql içindeki scriptler çalışmış olacak ve kullanıma hazır datalarınız,

ilgili tablolarda yerini alacak.

Dilerseniz bu scripti değiştirerek farklı senaryoları test edebilirsiniz.

1 ve 2 ID'li customerlar aynı zamanda admin userlardır. Bütün customerlar için işlem yapabilirler.

Ancak diğerleri sadece customer oldukları için sadece kendileri ile ilgili işlem yapabilirler.

Toplamda 9 adet customer, bunların 2'si aynı zamanda admin user,

Toplamda 2 adet loan mevcut, birinin 6 taksidi, birinin son 1 taksidi bulunmakta.


Bu senaryolar üzerinden testler yapabilir,

1 ve 2 userNo'lu kullanıcılar ile bütün müşterilere yeni Loan'lar tanımlayıp, test edebilirsiniz.




Db script;


INSERT INTO CUSTOMER VALUES(1,1000000,'Emre','Dülger',0);
INSERT INTO CUSTOMER VALUES(2,1000000,'Dogukan','Sanal',100000);
INSERT INTO CUSTOMER VALUES(3,2000000,'Ali','Yılmaz',0);
INSERT INTO CUSTOMER VALUES(4,1000000,'Veli','Şahin',1000000);
INSERT INTO CUSTOMER VALUES(5,0,'Mehmet','Aslan',0);
INSERT INTO CUSTOMER VALUES(6,5000000,'Fahri','Sever',0);
INSERT INTO CUSTOMER VALUES(7,3000000,'Ayşe','Yılmaz',0);
INSERT INTO CUSTOMER VALUES(8,3000000,'Fatma','Ceylan',200000);
INSERT INTO CUSTOMER VALUES(9,2000000,'Kemal','Sarı',0);

INSERT INTO LOAN_ADMINS VALUES(1,'Emre','Dülger',1);
INSERT INTO LOAN_ADMINS VALUES(2,'Dogukan','Sanal',2);

INSERT INTO LOAN VALUES(1,'2025-02-03 01:00:00',	FALSE, 	1200000.00,	6,	6);

INSERT INTO LOAN_INSTALLMENT  VALUES(1,	200000.00,	'2025-03-01',	FALSE,	0.00,	null,	1);
INSERT INTO LOAN_INSTALLMENT  VALUES(2,	200000.00,	'2025-04-01',	FALSE,	0.00,	null,	1);
INSERT INTO LOAN_INSTALLMENT  VALUES(3,	200000.00,	'2025-05-01',	FALSE,	0.00,	null,	1);
INSERT INTO LOAN_INSTALLMENT  VALUES(4,	200000.00,	'2025-06-01',	FALSE,	0.00,	null,	1);
INSERT INTO LOAN_INSTALLMENT  VALUES(5,	200000.00,	'2025-07-01',	FALSE,	0.00,	null,	1);
INSERT INTO LOAN_INSTALLMENT  VALUES(6,	200000.00,	'2025-08-01',	FALSE,	0.00,	null,	1);


INSERT INTO LOAN VALUES(2,'2025-02-03 01:00:00',	FALSE, 	1200000.00,	6,	9);

INSERT INTO LOAN_INSTALLMENT  VALUES(7,	200000.00,	'2025-03-01',	FALSE,	0.00,	null,	2);



