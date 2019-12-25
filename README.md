# Shape Challenge

### Background
The mobile application is meant to assist kids in learning basic geometry.

To start, the ​kid​ would select one of the available ​shape categories​ including but not
limited to ​triangle, square, rectangle, parallelogram, rhombus, kite, trapezium, circle
and ​ellipse​.

On selection, the ​kid​ is required to enter a minimum requirement the ​shape​ requires to
form; for example, a ​square​ would require a single ​size​, whereas a ​rectangle​ requires
both ​width​ and ​length​. If a ​shape​ can be formed using different requirement set, a
selection of requirement sets may be added.

The application will then draw a picture of the said ​shape​, display its ​area​, and identify if
it falls into other ​shape​ categories; for example, a ​rectangle​ can be called a
parallelogram​. An option to save the ​shape​ is available as well.

### Basic Requirements
To accommodate the mobile application, these APIs are needed

● API to list all the ​shape​ ​categories​, each with its corresponding requirement sets
of dimension, length and/or angle.

● API to submit the ​shape​, returning with ​area​ and possible ​categories​.

● API to save the ​shape

● API to list all the saved ​shapes

### Intermediate Requirements
● There is a content management system for the administrator to
list/create/edit/delete

● shapes for each kid

● Administrator can login, logout and create/delete other administrator
○ The first Administrator can be just seeded

● Kid can signup, signin, signout

### Challenging Requirements
The CMS should also allow administrator to manage shape categories and its

● Name

● Requirements

● Area formula

● Conditions that allows the shape to fall into other categories

### Run
./gradlew bootRun

### Running Tests
./gradlew clean test

### Explanation
- There are Restful API for mobile app and CMS for administrators
- CMS: http://localhost:8080
- API documentation: http://localhost:8080/swagger-ui.html
- Every endpoints need authentication
- There are two kinds of users: ADMIN and KID.
- First Admin (username: trung, password: p@ssw0rd) is created at the beginning
- APIs allow view/create/update/delete shapes associated with logged in user (KID)
- Only ADMINs can access CMS views for Administrators, Categories and Shapes

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

### Docker
sudo docker build --tag=shape-challenge:latest --rm=true .
sudo docker run --name=shape-challenge --publish=8080:8080 shape-challenge:latest

