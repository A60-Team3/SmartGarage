## Security
### The project uses Spring Security RBAC, standard login for MVC endpoints and JWT token for REST endpoints for authentication and authorization.
### REST free access - [Login](http://localhost:8080/api/garage/login).
### MVC free access - [Login](http://localhost:8080/garage/login), [Home](http://localhost:8080/garage/) and any link on home page
#### **HR user**:
##### `Username` _john_doe_ `Password` _password123_
#### **CLERK user**
##### `Username` _jane_smith_ `Password` _password123_
#### **MECHANIC user**
##### `Username` _d.marshall_ `Password` _password123_
#### **CUSTOMER user**
##### `Username` _i.clarke_ `Password` _password123_
### Additional info about endpoint access levels - [SecurityConfiguration](https://github.com/A60-Team3/SmartGarage/blob/main/src/main/java/org/example/smartgarage/security/SecurityConfiguration.java)
## Database
### A crude implementation of [Flyway](https://flywaydb.org/) database version control. Only used for database creation.
### Database seeding is carried out automatically on first start of the application through a DataLoader class. 
#### Vehicle model, brands and years are taken from [OpenDataSoft Free API](https://public.opendatasoft.com/explore/dataset/all-vehicles-model/information/) - approximately 24000 entries. It takes some time to load all.
#### Only HR can register a EMPLOYEE role. Only a CLERK can register CUSTOMER role.
### Rest Flow to create a Visit:
    (Optional) 1. Login HR - POST http://localhost:8080/api/garage/login
    (Optional) 2. Register CLERK - POST http://localhost:8080/api/garage/clerks
    3. Login CLERK - POST http://localhost:8080/api/garage/login
    4. Register CUSTOMER - POST http://localhost:8080/api/garage/users
    5. Create VEHICLE - POST http://localhost:8080/api/garage/vehicles
    6. Create VISIT - POST http://localhost:8080/api/garage/visits
    7. Create ORDER(s) - POST http://localhost:8080/api/garage/users/{userId}/visits/{visitId}/orders"
### MVC Flow to create a Visit:
    1. Login CLERK
    2. Create CUSTOMER if new, Create VEHICLE if new
    3. Create VISIT
    4. Add ORDER(s) - automatically after a visit is created
### Features implemented
    1. Anonymous visitor and any customer can send a quotation for a visit and receive answer by email.
    2. A visit status can be changed only by a mechanic.
    3. A visit(s) report can be send via email in PDF format.
### Feature plans
    1. Cloud hosting
    2. Adding optional features
## Read the [Custom Enviroment Variables](https://github.com/A60-Team3/SmartGarage/blob/main/.env.local) file and follow the instructions.
## Image Uploading
### Must have a valid [Cloudinary](https://cloudinary.com/) account. It is free. Input your info into the .env file.
## API documentation
### Swagger project [URL](http://localhost:8080/swagger-ui/index.html) `accessible when the program is running`
