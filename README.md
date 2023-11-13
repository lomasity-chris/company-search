# Company Search API

## Description

The company search api is used to consolidate calls to the Search for Company and Get Company Officers Apis into a single json response for a specified company identified by name or number.

## Starting the API

The api is a maven spring boot application that can be started with the following command from the root directory.

    ./mvnw spring-boot:run

## Calling the API 

The api is called with a get request to the following url.

    localhost:8080/api/search

The following body content must be supplied specifying the company name and number for the search.  At least one of the properties must be supplied.  If supplied, the company number will be used.      

    {
      "companyName" : "BBC LIMITED",
      "companyNumber" : "06500244"
    }

An optional query parameter can be supplied as shown below.  If this is set to Y then only active companies will be returned.  If it is set to anything else or omitted, then the companies will not be filtered. 

    localhost:8080/api/search?onlyActive=Y

A mandatory API key must be supplied (provided separately) in header property x-api-key

## Development Notes

1. JSON Property Ordering - The api response returns the json properties in the specified order.  However, for simplicity the address properties have been kept to the company address specification order.
2. Test coverage - Test coverage is high due to the integration tests.  With more time, the coverage and quality of the unit tests could be improved.
3. As per clean code principles, commenting has been kept to a minimum and the code has been made as readable as possible.  If the client required javadoc comments then these could be added.
4. The api uses a H2 database to allow caching of company and officer searches.  For simplicity, this has been done at response level rather than for the Companies, Officers and Addresses.  This involved the use of a Spring Data embedded key, due to the fact that the requests could be for all companies or active companies only and these must be saved separately.   