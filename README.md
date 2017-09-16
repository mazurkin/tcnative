# What this project does?

This project implements Maven'ization of Apache Tomcat Native Library to produce a valid Maven artefact (POM and JAR)

No any changes made to the source code. I just added pom.xml and some test classes. All java source is being copied 
from released source tarball "as is". 

# Why it does that?

Apache Tomcat Native Library has no any Maven artefact nor any proper building toolchain for Java part.

# Why do I need all this?

Apache Portable Library (APR) gives a full access to some tricky system features like memory-mapping files, special socket modes, file locking and other advanced features - just check project Java files.

The cool thing that it does it for all operating systems uniformly by introducing a new level of abstraction. 

# Who uses APR?

* Tomcat web container
* Netty.io library

# Structure of APR / Tomcat Native

```
    [*      Apache Tomcat Native Library (Java classes, JAR)        *]
       java classes and definitions that knows about native part
    
                                 |
                                 | (uses)
                                \|/
                                
    [* Apache Tomcat Native Library (Native part, libtcnative-1.so) *]
       JVM-aware native library that is facade between JVM and APR 

                                 |
                                 | (uses)
                                \|/

    [*     Apache Portable Runtime (Native part, libapr-1.so)       *]
        A level of abstraction that unifies features of different OS

                                 |
                                 | (uses)
                                \|/
        
    [*              Windows/Linux/Unix system API                   *]
```

# Installation on Ubuntu 16.04

On other systems refer to your repositories.

## Apache Portable Runtime (native part)

    sudo apt-get install libapr1
    
Or just build the library from the sources. 

## Apache Tomcat Native Library (native part)

    sudo apt-get install libtcnative-1

Also you can compile and install this library from the [sources](https://github.com/apache/tomcat-native).

## Apache Tomcat Native Library (java part)

To install this project to local Maven repository just install it with:

    mvn clean install

Or/and deploy it to your team's Nexus repository.

## A little tricky part

Check where you JVM is searching for native libraries:

    String nativeLibPath = System.getProperty("java.library.path");
    System.out.printf("JVM is searching native libraries in: %s%n", nativeLibPath);

    # Oracle JDK 1.8 on Ubuntu 16.04 shows
    # /opt/idea/default/bin::/usr/java/packages/lib/amd64:/usr/lib64:/lib64:/lib:/usr/lib
    
Then check where is `libtcnative-1.so` installed:

    $ dpkg-query -L libtcnative-1 | grep libtcnative-1.so
    /usr/lib/x86_64-linux-gnu/libtcnative-1.so.0.1.33
    /usr/lib/x86_64-linux-gnu/libtcnative-1.so
    /usr/lib/x86_64-linux-gnu/libtcnative-1.so.0
                        
So here is problem - by default JDK is not seeing this library because JVM is not aware of that directory
and JVM prints error when loading the native library:
 
    org.apache.tomcat.jni.LibraryNotFoundError: no tcnative-1 in java.library.path, no libtcnative-1 in java.library.path 
 
But you can solve this problem in several ways.

1. make a symlink in some directory that is already known by JVM

```
    sudo mkdir -p /usr/java/packages/lib/amd64
    sudo ln -sfT /usr/lib/x86_64-linux-gnu/libtcnative-1.so /usr/java/packages/lib/amd64/libtcnative-1.so
```
         
2. ... or provide JVM with a special argument that points to the directory with the library

```    
    java ... -Djava.library.path=/usr/lib/x86_64-linux-gnu ...
``` 
   
3. ... or set the search path for native libraries to the process (Linux only)

```
    export LD_LIBRARY_PATH=/usr/lib/x86_64-linux-gnu   
    java ... 
```

# Maven dependency

```
    <dependency>
        <groupId>org.apache.apr</groupId>
        <artifactId>tcnative</artifactId>
        <version>1.2.14</version>
    </dependency>
```

# Usage

```
    boolean apr = Library.initialize(null);
    if (!apr) {
        throw new IllegalStateException("Fail to initialize Apache Tomcat Native library");
    }

    System.out.printf("Process PID=%d%n", Stdlib.getpid());
    System.out.printf("Parent  PID=%d%n", Stdlib.getppid());
```    

# References

## Apache Portable Runtime (APR)

https://apr.apache.org/

https://apr.apache.org/anonsvn.html

https://apr.apache.org/docs/apr/1.6/index.html

## Apache Tomcat Native Library

Native library and java part. 

Repository: https://github.com/apache/tomcat-native

Note that Java part is automatically generated after native library is built.



