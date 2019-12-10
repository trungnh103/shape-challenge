# Shape Challenge

This is my solution for Java BE Developer - Take-home Test

### Run
./gradlew bootRun

### Running Tests
./gradlew clean test

### Explanation
There are Restful API for mobile app and CMS for administrators
CMS: http://localhost:8080
API documentation: http://localhost:8080/swagger-ui.html
Every endpoints need authentication
There are two kinds of users: ADMIN and KID.
First Admin (username: trung, password: p@ssw0rd) is created at the beginning
APIs allow view/create/update/delete shapes associated with logged in user (KID)
Only ADMINs can access CMS views for Administrators, Categories and Shapes

### Example of Category and Shape
Category {
	"name": "RECTANGLE",
	"requirements": [
      "width", "height"
    ]
    "areaFormula": "width*height",
    "conditionsToBecomeOtherCategories": [
      {
        "category": "SQUARE",
        "condition": "width==height"
      }
    ]
  }
  
 Shape {
  "category": {
    "name": "RECTANGLE"
  },
  "name": "TOKYO RECTANGLE",
  "shapeProperties": [
    {
      "name": "width",
      "value": "6"
    },
	{
      "name": "height",
      "value": "6"
    }
  ],
  "username": "WONDER KID"
}
"requirements" are defined when Category is created. And when creating Shape with this Category, "shapeProperties", whose keys are Category's requirements, need to be entered.
The application will take values of shape properties (width and height) and check if they satisfy "conditionsToBecomeOtherCategories" of corresponding Category (RECTANGLE). 
In above example, since both width and height have value of 6, possible categories of this shape include both SQUARE and RECTANGLE.
For area calculation, "areaFormula" of corresponding Category (RECTANGLE) will be applied to values of shape properties. 
In above example, the result is 36

### Dependencies
JDK 1.8, Spring boot, Spring Security
Thymeleaf, Swagger, JUnit
Spring data Mongo, Flapdoodle Embedded MongoDB
Lombok, Gson

### * Notes
Embedded MongoDB(de.flapdoodle.embed.mongo) is used for integration testing (Repository layer)
Current gradle settings allow to use this for main application also, so that no real MongoDB installation is needed
	implementation('de.flapdoodle.embed:de.flapdoodle.embed.mongo')
In case a real MongoDB is used, we can set the scope of Embedded MongoDB to "test" only by changing above setting to
	testImplementation('de.flapdoodle.embed:de.flapdoodle.embed.mongo')
And the application will connect to MongoDB according to application.yml

