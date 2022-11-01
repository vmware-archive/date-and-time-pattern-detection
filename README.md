# Date/Time Pattern Detection Overview
Pattern Detection service provides utility for Engineers to verify and convert localized inputs containing
dates, time and temporal values. The service is using Unicode Common Locale Data Repository (CLDR) to process and
validate the localized date/time components. It provides suggestions based on the user input and Unicode standards.

## Prerequisites

* Git
* Java 17 recommended (Any version after 8 may be used)
* Maven
* Docker (optional, depending on the build type)

## Build and Run as local REST-API service

1. Clone from remote to local machine
2. Pull Docker image from Artifactory
3. Run on specified port

### Example Usage

You can either validate localized input containing Dates, times and more temporal types, get CLDR standard patterns or
convert localized input from 1 language to another. Sample request for input validation:

```
curl -X POST "http://localhost:8083/validate/localizedInput/en-US" -H "accept: */*" -H "Content-Type: application/json" -d "Monday, 25 July, 2022 at 8:14 in the morning"
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

## Contributing

The date-and-time-pattern-detection project team welcomes contributions from the community. Before you start working with date-and-time-pattern-detection, please
read our [Developer Certificate of Origin](https://cla.vmware.com/dco). All contributions to this repository must be
signed as described on that page. Your signature certifies that you wrote the patch or have the right to pass it on
as an open-source patch. For more detailed information, refer to [CONTRIBUTING.md](CONTRIBUTING.md).