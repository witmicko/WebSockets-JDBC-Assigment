Michal Ogrodniczak 2005 7303

All done as per spec, with small differences:

- implemented messaging system
this allows greater flexibility when expanding system, rather than sending strings only.

- inputs verifier on client side
checks for decimal and integer inputs on appropriate fields

-client thread get its own JDBC connection
JDBC connection in SQL is treated as transaction, therefore it should not be shared between different threads
source:  https://docs.oracle.com/cd/A87860_01/doc/java.817/a83724/tips1.htm

- application is assuming that AccountNum in the DB is an UNIQUE field