# Coding Challenge
## What's Provided
A simple [Spring Boot](https://projects.spring.io/spring-boot/) web application has been created and bootstrapped 
with data. The application contains information about all employees at a company. On application start-up, an in-memory 
Mongo database is bootstrapped with a serialized snapshot of the database. While the application runs, the data may be
accessed and mutated in the database without impacting the snapshot.

### How to Run
The application may be executed by running `gradle bootRun`.

### How to Use
The following endpoints are available to use:
```
* CREATE
    * HTTP Method: POST 
    * URL: localhost:8080/employee
    * PAYLOAD: Employee
    * RESPONSE: Employee
* READ
    * HTTP Method: GET 
    * URL: localhost:8080/employee/{id}
    * RESPONSE: Employee
* UPDATE
    * HTTP Method: PUT 
    * URL: localhost:8080/employee/{id}
    * PAYLOAD: Employee
    * RESPONSE: Employee
```
The Employee has a JSON schema of:
```json
{
  "type":"Employee",
  "properties": {
    "employeeId": {
      "type": "string"
    },
    "firstName": {
      "type": "string"
    },
    "lastName": {
          "type": "string"
    },
    "position": {
          "type": "string"
    },
    "department": {
          "type": "string"
    },
    "directReports": {
      "type": "array",
      "items" : "string"
    }
  }
}
```
For all endpoints that require an "id" in the URL, this is the "employeeId" field.

## What to Implement
Clone or download the repository, do not fork it.

### Task 1
Create a new type, ReportingStructure, that has the following JSON schema:
```json
{
  "type":"ReportingStructure",
  "properties": {
    "employee": {
      "type": "employee"
    },
    "numberOfReports": {
      "type": "integer"
    }
    }
  }
}
```
For the field "numberOfReports", this should equal the total number of reports under a given employee. The number of 
reports is determined to be the number of directReports for an employee and all of their direct reports. For example, 
given the following employee structure:
```
                    John Lennon
                /               \
         Paul McCartney         Ringo Starr
                               /        \
                          Pete Best     George Harrison
```
The numberOfReports for employee John Lennon (employeeId: 16a596ae-edd3-4847-99fe-c4518e82c86f) would be equal to 4. 

This new type should have a new REST endpoint created for it: 
1.  The URL is GET - /reportingStructure/{id}. 
2.  The id variable is an employeeId. 
3.  The response body is the fully filled out ReportingStructure for the specified employeeId. 
The values should be computed on each request and do not need to be persisted.

### Task 2
Create a new type, Compensation. A Compensation has the following JSON schema:
```json
{
  "type":"Compensation",
  "properties": {
    "employee": {
      "type": "Employee"
    },
    "salary": {
          "type": "integer"
    },
    "effectiveDate": {
      "type": "string",
      "format": "date-time"
    }
    }
  }
}
```
Create two new Compensation REST endpoints. 
1.  The URL to retrieve is GET - /compensation/employee/{id}
2.  The URL to create is POST - /compensation
3.  The compensations should be stored in a repository like Employees are.
