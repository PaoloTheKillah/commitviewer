# Commit Viewer
Simple application that displays the commits of the default branch of a Github repository.
Both a cli and a REST interface are offered.

## Prerequisites
Java 11 runtime or newer must be installed on the system.
Other dependencies are managed using Gradle, notably:
- Spring Boot for the REST interface
- picocli  for command line parsing
- SL4J, Log4j2 for logging

## Quick start
We can build and run the application using Gradle (starting from the project base directory):

- Display commits for repository https://github.com/PaoloTheKillah/SampleRepo directly in terminal, page 1 and pagesize 100:
```
./gradlew run --args="cli -p 1 -s 100  https://github.com/PaoloTheKillah/SampleRepo"

> Task :run
Commit{hash='673c9749f0ce7da129bac230af176e851c09eb61', author=Developer{name='Paolo Chila', email='paolo.chila@gmail.com', date=2019-12-04T19:36:39Z}, committer=Developer{name='Paolo Chila', email='paolo
.chila@gmail.com', date=2019-12-04T19:36:39Z}, message=Third commit, references=[], parentHashes=[9d96107aa063b5638c1637f3cc822dd65e9c4a14]}
Commit{hash='9d96107aa063b5638c1637f3cc822dd65e9c4a14', author=Developer{name='Paolo Chila', email='paolo.chila@gmail.com', date=2019-12-04T19:33:50Z}, committer=Developer{name='Paolo Chila', email='paolo
.chila@gmail.com', date=2019-12-04T19:33:50Z}, message=Second commit, references=[], parentHashes=[4f34033f8ef31262049c746dba67a2f7652cfee9]}
Commit{hash='4f34033f8ef31262049c746dba67a2f7652cfee9', author=Developer{name='Paolo Chila', email='paolo.chila@gmail.com', date=2019-12-04T19:32:49Z}, committer=Developer{name='Paolo Chila', email='paolo
.chila@gmail.com', date=2019-12-04T19:32:49Z}, message=Initial commit, references=[], parentHashes=[]}

```

- Start REST server and send a request for commits of repository https://github.com/PaoloTheKillah/SampleRepo, page 1 and pagesize 100:
```
./gradlew run --args="server"

> Task :run

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.2.1.RELEASE)



```
```
curl 'http://localhost:8080/viewcommits?repo=https://github.com/PaoloTheKillah/SampleRepo&page=1&pageSize=100' | jq .
...

[
  {
    "hash": "673c9749f0ce7da129bac230af176e851c09eb61",
    "author": {
      "name": "Paolo Chila",
      "email": "paolo.chila@gmail.com",
      "date": "2019-12-04T19:36:39Z"
    },
    "references": [],
    "parentHashes": [
      "9d96107aa063b5638c1637f3cc822dd65e9c4a14"
    ]
  },
  {
    "hash": "9d96107aa063b5638c1637f3cc822dd65e9c4a14",
    "author": {
      "name": "Paolo Chila",
      "email": "paolo.chila@gmail.com",
      "date": "2019-12-04T19:33:50Z"
    },
    "references": [],
    "parentHashes": [
      "4f34033f8ef31262049c746dba67a2f7652cfee9"
    ]
  },
  {
    "hash": "4f34033f8ef31262049c746dba67a2f7652cfee9",
    "author": {
      "name": "Paolo Chila",
      "email": "paolo.chila@gmail.com",
      "date": "2019-12-04T19:32:49Z"
    },
    "references": [],
    "parentHashes": []
  }
]
```


## Build, test, package
Using gradle you can build, test and package the application simply by running
`./gradlew build`

### Redistributable JAR
You can find a distributable jar named commitviewer-1.0-SNAPSHOT.jar in build/libs directory, this file can be copied 
to any directory and run as:
`java -jar commitviewer-1.0-SNAPSHOT.jar cli -p 1 -s 100  https://github.com/PaoloTheKillah/SampleRepo` or `java -jar commitviewer-1.0-SNAPSHOT.jar server`

### Test results
An HTML report for the test run can be found in directory `build/reports/test/test`
