# Projekat iz predmeta 'XML i veb servisi'
Članovi tima:
- Đorđe Ivković SW54-2016
- Bojan Vlasonjić SW76-2016 
- Miloš Malidža SW80-2016


# ScientificPublications
In general, an informational system for posting, publishing and reviewing scientific papers.
Scientific publications consist of 3 types of documents:
- scientific papers
- cover letters
- paper reviews

The documents are modeled via an xml schema, and they are persisted in the exist database.
The documents contain may contain metadata modeled by the user, which helps build references between documents and provide search.


There are three types of users:
- Authors
- Editors
- Unregistered users

Authors can upload scientific papers using the xml text editor provided, or by uploading a file. They can also choose to upload a cover letter as well. They can view and download their scientific papers, as well as cancel them. Authors can also review other people's scientific papers if they get a request from the editor.

Editor manages all of the scientific papers. He can view and download them. He can choose reviewers for certain scientific papers, and he decides in the end whether the scientific papers are going to get published or not.

Unregistered users can view and search published papers, and download them in xml, pdf or html.


# Technologies used
- Spring boot
- Angular 8
- Exist database
- Fuseki database
- H2 database


# Getting started
- Firstly clone this repository. Open your command prompt and paste the following command: 'git clone https://github.com/bojanVlasonjic/ScientificPublications.git'
- The next step would be to configure the database. I have created a separate heading called 'Database' that guides you through the process.
- Starting the spring boot server: I would recommend opening the project with 'eclipse' or 'intelliJ'. Open the App/Backend/ScientificPublications project. Open the MBookingApplication class and run it.
- Starting angular: Hopefully you have npm and angular cli installed. If that's the case open App/Backend/Scientific publications folder. Open the command line and run 'npm start'. If the project won't start, try running 'npm install' and 'npm update'.
- After you have started the servers, you may open your browser and navigate to localhost:4200 to view the web app.

# Database
We used tomcat server in order to use the databases.
- Download one of the newer apache tomEE from the following link: https://tomee.apache.org/download-ng.html
- Download exist-4.6.1.war from this link: https://bintray.com/existdb/releases/exist/4.6.1/view.
Rename the file to exist, copy it to the apache tomee webapps folder.
- Download Apache Jena Fuseki SPARQL server: https://jena.apache.org/download/index.cgi#apache-jena-fuseki.
Extract the fuseki.war file to the apache tomee web apps folder.

Database access:
- You may access the exist database by opening your browser and navigating to 'localhost:8080/exist'.
Log in with the following credentials: username='admin', password is empty.
- You may access the fuseki database by navigation in your browser to localhost:8080/fuseki.
- You may access the h2 database by navigating to localhost:8081/h2. Make sure that the JDBC Url contains following content: 'jdbc:h2:file:./data/h2-database'.

**Note** Make sure that tomcat server and spring boot server have started.

# DEMO
https://youtu.be/WKQY81fE8fc

