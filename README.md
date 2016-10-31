####Introduction

A batch process usually is a process with large amount of data, this concept is called 'Job'.
Each Job is processed for a concept called 'Step'.
A Step is an operation that want to run for:
* Read(Input) from file, jdbc source, etc...
* Process(Transform) define a task to realize for each row of data obtained
* Write(Output) to file, database, etc...

Usually the flow of Steps are Input|Transform|Output

In this examples we go to learn to build:
* Read from xml file, csv comma separated file, txt custom separated file or jdbc source(MYSQL in my case)
* Transform fields to uppercase and write to in-memory hsqldb database
* Write to xml file, csv comma separated file, txt custom separated file or jdbc source(MYSQL in my case)

***

### How to make batch jobs with spring-batch framework and runs in spring-boot.

Now we know what is a Batch process go to specific point, in this examples, we go to learn [spring-batch](http://projects.spring.io/spring-batch/), that is part of [Spring framework](https://projects.spring.io/spring-framework/) and this examples is based on spring maven archetype (build over it)

Spring batch have the same components explained above, Jobs, Steps and Tasks and are represented for Java Classes.
It's important to note that spring batch projects power is in configuration, and there are 2 ways to configure:
* XML based configuration (Classic)
* Annotation based configuration

>Surfing the web I appreciate that there are tons of examples in xml based configuration but not from annotated configuration, this is the purpose of examples,show with annotations how to make batch processes.

All configuration is annotation based, that means not xml configuration is present,instead was created a properties file that contains all configuration properties:
* paths to import or export files
* database properties

We can create more properties if our jobs grow up.

***

### Project structure

![structure](http://shoecillo.drivehq.com/images/ProjectStructure.png)

Image above represents all project resources, is a maven project, go to see pom.xml file:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.sh</groupId>
    <artifactId>spring-batch-examples</artifactId>
    <version>1.0.0</version>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.3.5.RELEASE</version>
    </parent>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-batch</artifactId>
        </dependency>
        <dependency>
            <groupId>org.hsqldb</groupId>
            <artifactId>hsqldb</artifactId>
        </dependency>
        
        <dependency>
		    <groupId>org.springframework</groupId>
		    <artifactId>spring-oxm</artifactId>
		</dependency>
   		
   		<dependency>
   			<groupId>mysql</groupId>
    		<artifactId>mysql-connector-java</artifactId>	
		</dependency>
        
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>  
</project>
```
Here we can see the parent project used for this example:
* org.springframework.boot:spring-boot-starter-parent

Here we can see the libraries that we can use in the project:
* `org.springframework.boot:spring-boot-starter-batch` is main library for run jobs
* `org.hsqldb:hsqldb` is in-memory database hsqldb (Could be Derby or h2)
* `org.springframework:spring-oxm` is spring xml support
* `mysql:mysql-connector-java` is mysql connector (Could be any database vendor)

Only with this small pom we can build all examples, if you need any libraries you can include it in pom for use it.

Project have 2 main directories:
* `src/main/java` - Java source
* `src/main/resources` - Resources(properties, xml,etc)

***

### Resources

We need some resources for run jobs, in this examples are:

![resources](http://shoecillo.drivehq.com/images/resources.png)

In root directory are 2 files:
* `schema-all.sql` - SQL script for init in-memory database.Suffix -all can be replaced for any vendor(h2,Derby...),if is -all check it automatically.
* `application.properties` - Configuration properties (Paths,database configurations...)

In import directory are 3 files:
* `persons.csv` - Input csv for read
* `persons.txt` - Input txt for read
* `persons.xml` - Input xml for read

***

## Java

### Packages

I divided the application in this packages:

![packages](http://shoecillo.drivehq.com/images/packages.png)

Maybe for this example not is very useful this division because in each package only is 1 file, but for more complex job it's a good guide.

#### How Spring-batch works

`com.sh.app.Application` class is the main class of application, only register application as spring-boot regular project. 

There is `@ComponentScan` annotation, we scan `com.sh.config` package, where are all configuration classes.

`com.sh.config.*` classes configure jobs,steps and tasks.It`s divided in db,readers,writers and batch packages:

* `db` package contains the databases configuration, we could need more than 1 data source (i.e: copy one table in other db)
* `readers` package contains methods for read from source (xml,jdbc,txt,csv)
* `writers` package contains methods for write to source (xml,jdbc,txt,csv)
* `batch` package contains the jobs and steps flow configuration

`com.sh.model` contains Java Beans that represent one data row.

`com.sh.processor` contains the logic applies for each row in each step u want to run, for that, this package is outside config package.

`com.sh.notification` contains the listeners for stream task results


