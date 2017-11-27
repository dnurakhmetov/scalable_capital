# Scalable.Capital test assignment

This is a solution for test assignment for interview at Scalable.Capital.

## Description

The goal of assignment is to build webcrawler console application that returns list of top 5 used javascript libraries on websites that were retrieved from Search Engine (like Google) by certain keyword.

## Implementation

The implementation is based on Java 8 and uses minimum of external libraries.
Remote querying is based on URLConnection while strings fetching and processing is mainly based on REGEXP.
In the codebase Java Stream API is used to handle data flow and provide 'multi-threaded' processing.
The deduplication of Javascript libraries problem is solved for now with use of hash algorithms. Namely SHA-256 is used.

## Testing

Solution has unit tests which are developed with use of JUnit 4 and Mockito frameworks.
It doesn't test connectivity, but parsing, fetching and strings processing is tested.
To do so all HTML pages are mocked.

## Known issues

Currently application has significant issues with handling remote data over SSL.
Also it requires additional effort on providing more complex algorithms on parsing Javascript URLs.

## Build & Run

To build application latest Java 8 and Maven 3 are required.

To run application use java -jar webcrawler-X.X.X.jar

## Further improvements

In general to have a production like solution, the application should be splitted into atomic and independent microservices, where crawling, fetching, storing and output would be done by different services.
Another suggestion would be use of message queue to organize data flow.

The microservices architecture would solve problems with speed improvements.