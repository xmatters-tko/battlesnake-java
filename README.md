# xm-battlesnake-java

A simple [BattleSnake AI](http://battlesnake.io) written in Java, which can easily be deployed to Heroku. The Java app uses Spring Boot to and the Actuator Service to implement rest services required by your snake. This tutorial will help you get started (https://spring.io/guides/gs/actuator-service/).

This application supports the [Getting Started with Java on Heroku](https://devcenter.heroku.com/articles/getting-started-with-java) article - check it out.

## Prerequisite Software
Make sure you install the follwing software:
- The [GitHub CLI](https://git-scm.com/downloads)
- The [Heroku CLI](https://cli.heroku.com/).
- [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
- [Maven](https://maven.apache.org/install.html)

## Getting your project started
- Create a free [GitHub account](https://github.com)
- Create a free account on [Heroku](https://www.heroku.com/)
- Fork this [project](https://github.com/xmatters-tko/xm-battlesnake-java/fork)

## Running Locally
Once all the software is installed and you've forked this project, run the following commands:

```sh
$ git clone https://github.com/<your account>/xm-battlesnake-java.git
$ cd xm-battlesnake-java
$ mvn install
$ heroku local:start
```

Your app should now be running on [localhost:5000](http://localhost:5000/).


## Deploying to Heroku

### Create an App
First create a Heroku App and give it a name. This will create a remote git repo for Heroku to use to deploy and run your project.
```sh
$ heroku create [APP NAME]
$ git push heroku master
```
The output should end with the URL endpoint of your snake. Use this URL to add your snake to a game on the server.
```
remote: -----> Launching...
remote:        Released v3
remote:        https://my-snake.herokuapp.com/ deployed to Heroku
remote:
remote: Verifying deploy... done.
```


### Pushing Updates
You have to commit your changes to your git project as part of pushing them to the remote heroku git.
```sh
$ git add --all; git commit -m "Updated"; git push
$ git push heroku master
```

### Tailing LOgs
Once your snake is running, you can tail the logs any time in the console using the command:
```sh
$ heroku logs --tail
```


## Documentation

For more information about using Java on Heroku, see these Dev Center articles:

- [Java on Heroku](https://devcenter.heroku.com/categories/java)
```
