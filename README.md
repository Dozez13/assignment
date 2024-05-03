User Validation.postman_collection.json contains collection that you can import into postman and test Web API Validation.
For application run you should set profiles, specifically "dev":
1. Go to Intellij configuration
2. Choose "Modify options" and select "Add VM options"
3. insert into appeared field "-Dspring.profiles.active=dev"
Now you can run application using "dev" profile
For testing other part of Web Api you can you jsondoc:
1. Run application
2. Go to http://localhost:8080/jsondoc-ui.html
3. insert http://localhost:8080/jsondoc in the box and get the documentation.

