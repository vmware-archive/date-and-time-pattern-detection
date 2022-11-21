# Date/Time Pattern Detection Overview
Pattern Detection service provides utility for Engineers to verify and convert localized inputs containing
dates, time and temporal values. The service is using Unicode Common Locale Data Repository (CLDR) to process and
validate the localized date/time components. It provides suggestions based on the user input and Unicode standards.

## Prerequisites

* Git
* Java 17 recommended (Any version after 8 may be used)
* Maven
* Docker (optional, depending on the build type)

## Build and Run as local REST-API service using Docker

1. Pull Docker image from GitHub Container Registry: `docker pull ghcr.io/vmware-labs/date-and-time-pattern-detection:main` 
If you get authentication error, you may need to login to chcr.io/vware-labs using Github username and access token.
2. Run on specified port. Example: `docker run -p 8083:8083 ghcr.io/vmware-labs/date-and-time-pattern-detection:main`
3. Open browser at http://localhost:8083/i18n/swagger-ui/index.html#/ (Port may be different, depending on above step).
4. Access the REST-API documentation and test-out the endpoints.

**Note: You can also run the service locally by cloning the project, calling `mvn clean install` and running the Jar file under rest-api-service/target/.** 

### Example Usage

You can either validate localized input containing Dates, times and more temporal types, get CLDR standard patterns or
convert localized input from 1 language to another. Sample request for input validation:

```
curl -X POST "http://localhost:8083/i18n/validate/localizedInput/en-US" -H "accept: */*" -H "Content-Type: application/json" -d "Monday, 25 July, 2022 at 8:14 in the morning"
```  

Sample response for input validation:

```json
{
  "isLocalizedContent": true,
  "detectedPattern": {
    "pattern": "EEEE, d MMMM, yyyy 'at' h:mm B",
    "isStandardFormat": false,
    "isValidDate": true,
    "patternInfoMessage": "The input is valid and non-standard localized date+time."
  },
  "language": "English (United States)",
  "input": "Monday, 25 July, 2022 at 8:14 in the morning",
  "suggestions": {
    "NON_STANDARD_DATE_TIME": "Provided dateTime does not match the standard CLDR dateTime patterns: [M/d/yy, h:mm a, MMM d, y, h:mm:ss a, MMMM d, y 'at' h:mm:ss a z, EEEE, MMMM d, y 'at' h:mm:ss a zzzz]"
  },
  "errors": {}
}
```

## Using the project as Maven Dependency
You can directly use the library part of the project by injecting the dependency in your project.
1. Add the dependency in your Maven pom.xml file:
```
<dependency>
  <groupId>com.vmware.g11n</groupId>
  <artifactId>date-time-pattern-detection-library</artifactId>
  <version>1.0.2</version>
</dependency>
```
2. Specify GitHub Package Registry as Maven Repository in your local settings.xml:
```
<repositories>
    <repository>
        <id>github</id>
        <name>GitHub VMware Apache Maven Packages</name>
        <url>https://maven.pkg.github.com/vmware-labs/date-and-time-pattern-detection</url>
    </repository>
</repositories>
```
3. Run `mvn clean install` to inject the dependencies
4. Access all functionality by initializing Pattern Detection object:
```
DateTimePatternDetection patternDetection = new DateTimePatternDetection();
ValidationResult validationResult = patternDetection.validateLocalizedInput("2016년 9월 1일 목요일 AM 11시 7분 10초 그리니치 표준시", "ko-KR");
```
As the project is multi-module, you can only use the service models for mapping response objects from the Rest-API service (may be useful for using the project in test frameworks). 
## Contributing

The date-and-time-pattern-detection project team welcomes contributions from the community. Before you start working with date-and-time-pattern-detection, please
read our [Developer Certificate of Origin](https://cla.vmware.com/dco). All contributions to this repository must be
signed as described on that page. Your signature certifies that you wrote the patch or have the right to pass it on
as an open-source patch. For more detailed information, refer to [CONTRIBUTING.md](CONTRIBUTING.md).