## **_Restful API for File Upload and Search on Spring Boot_**

#### **_`Functions are,`_**
- Upload a file with a few metadata - e.g. http://localhost:8080
- Store metadata/files on file system instead of DB
- Search metadata - e.g. http://localhost:8080/listpage
- Download uploaded files


#### **_`JavaScript framework used here is AngularJs.`_**


#### **_`How to Run`_**
- Clone first onto your local machine
- Open and import to your IDE(e.g. eclipse) as a maven project
- If needed, adjust your environment such as Maven User Settings or Installed JREs
- Run directly on eclipse using AppMain class or Run the war which has been generated after executing mvn package(or install)


#### **_`1 parent directory and 2 sub-directories are created`_**
Provided there are no changes on 'applicaition.properties' file, following directories are created.
- `metadataLoc`
- metadataLoc/`metadata` <-- this contains metadata
- metadataLoc/`filesUploaded` <-- this contains uploaded files

* In case of executing 'java -jar' syntax, the above directories will be created in the same directory.
* In case of executing AppMain class on ide, the above directories will be created in the project directory.

#### **_`Default log file name is 'springboot-fileupload.log'`_**
