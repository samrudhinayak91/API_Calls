#  Getting the statistics of API calls from GitHub repositories


This project aims at finding what percentage of the method calls from GitHub repositories are from java.util, java.lang and java.io. We use the REST APIs of the akka toolkit as well as the actor pattern to send and receive messages between actors. It also uses GitHub APIs to stream open source applications from GitHub and clone the repositories into our local directories. We then build an abstract syntax tree of the nodes in the code of the downloaded repositories. We have used the search engine API of the JDT to find the calls made to java.util, java.lang and java.io and compare them to the total number of methods invoked to get the percentage of total calls that come from those libraries. Ours is a console based App and we have used the Scala Swing APIs to build the GUI. You can read about the above mentioned resources through the links given below:

* Akka toolkit : http://akka.io
* AST : https://en.wikipedia.org/wiki/Abstract_syntax_tree
* GitHub APIs: https://developer.github.com/v3/
* Scala Swing APIs: http://www.scala-lang.org/api/rc2/scala/swing/package.html


## Inputs:

* A keyword to search for (Example: "dna" as the keyword will look for all projects within the GitHub repository with the word "dna" in it's name)

## Output :

Once you click on "Download", the repositories with the keyword will be downloaded and the downloaded repositories will be analyzed. The output of the analysis is stored in a text file in the folders of the cloned repositories. You then hit "Refresh" to get the list of downloaded repositories.

To get the statistics of the API call percentages, you hit the "Statistics" button which will populate the statistics onto the console window.

To delete any of the repositories, click on the "Delete folder and Contents" button at the bottom of the screen and hit "Refresh" again to see the updated list of repositories.


## Getting Started

### Prerequisites
* JDK 8 and JRE 8 to be installed on the machine.
* SBT to be installed on the machine.
* Understand® installed on the machine.


### Installing, Testing, and Running

Clone the project to your local repository:
```
git clone 
https://samrudhinayak@bitbucket.org/samrudhinayak/ahmed_eric_samrudhi_fp.git
```
 

To make sure that you build the code from scratch, navigate to the project's main directory, remove any pre-built files:
```
sbt clean
```


Then, build the project using: 
```
sbt compile
```


There are a few test cases implemented in this program. These test cases ensure that every method works as expected. You can test them using:
```
sbt "test-only "
```


To execute the program from command line, use:
```
sbt run
```


Then the program should ask you to enter a keyword to narrow down the search for applications from GitHub and download the repositories to your local system.



The program then uses the keyword to clone the repositories matching the keyword description into the local repository, parse through the AST to calculate the total number of methods invoked and the number of calls from the java.util, java.io and java.lang libraries. The cloning takes a few seconds since we have given a limit of 6 repositories that need to be cloned for each keyword that you enter. It finally gets the statistics and stores it in a text file in the directory containing the cloned repository. When you click on the Statistics button, these statistics from the text file will be displayed on the console. The user can also delete any repository by clicking on the name in the drop down menu and clicking on the delete button.


### Implementation Notes:

* We have used AKKA HTTP to make requests to get the JSON object from the URLs obtained according to a particular language and keyword pair input by the user. We then parse through the JSON object to get the clone_url from which we can clone the application into our local repository.
* We use GitHub developer APIs to clone the repository containing java applications with the keyword input by the user.
* We then build the abstract syntax tree for each of the repositories and parse through it to get the number of method invocations and the method declarations from java.util, java.io and java.lang. We have used the search engine API from the JDT to achieve this functionality.
* We have used the Scala Swing APIs to create the GUI.
* All the test cases are written using JUnit and scala test libraries.