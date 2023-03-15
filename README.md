# lab7
I made a console application, which is the server-client model. The clients make their requests via TCP protocol to the server, that can 
handle many requests at the same time because of multithreading. User must register or log in in the system. They are kept in the DB. I am using JDBC PostgreSQL to 
implement it. User can enter commands from the keyboard in enteractive mode or he can just execute script to generate his own objects. Each user has his own objects,
which are saved after closing the session. Every command is a Java object, because I implemented pattern "Command". 


## Previous versions of the application: 
In the first version there is no remote connection between client and server. And I also kept the user's object in the XML documents, which I parsed.

In the second version of application I add TCP connection and NIO2 Selectors, which allowed to keep connection with many users at the same time, 
but work only with one of them.
