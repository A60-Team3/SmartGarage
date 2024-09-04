## Security
### The project uses Spring Security RBAC, standard login for MVC endpoints and JWT token for REST endpoints for authentication and authorization.
### [Login](http://localhost:8080/garage/login.html) MVC endpoint and [Login](http://localhost:8080/garage/login) REST endpoint have free access.
#### **HR user**:
##### `Username` _john_doe_ `Password` _password123_
#### **CLERK user**
##### `Username` _jane_smith_ `Password` _password123_
### Additional info about endpoint access levels - [SecurityConfiguration](https://github.com/A60-Team3/SmartGarage/blob/main/src/main/java/org/example/smartgarage/security/SecurityConfiguration.java)
## Database
### A crude implementation of [Flyway](https://flywaydb.org/) database version control. Only used for database creation.
### Database seeding is carried out automatically on first start of the application through a DataLoader class. 
#### Vehicle model, brands and years are taken from [OpenDataSoft Free API](https://public.opendatasoft.com/explore/dataset/all-vehicles-model/information/) - approximately 24000 entries. It takes some time to load all.
#### Only 2 employees are loaded - HR and a CLERK. 
#### Only HR can register a CLERK. Only a CLERK can register CUSTOMER.
### Rest Flow:
    (Optional) 1. Login HR - POST http://localhost:8080/api/garage/login
    (Optional) 2. Register CLERK - POST http://localhost:8080/api/garage/clerks
    3. Login CLERK - POST http://localhost:8080/api/garage/login
    4. Register CUSTOMER - POST http://localhost:8080/api/garage/users
    5. Create VEHICLE - POST http://localhost:8080/api/garage/vehicles
    6. Create VISIT - POST http://localhost:8080/api/garage/visits
    7. Create ORDER(s) - POST http://localhost:8080/api/garage/users/{userId}/visits/{visitId}/orders"
## Read the [Custom Enviroment Variables](https://github.com/A60-Team3/SmartGarage/blob/main/.env.local) file and follow the instructions.
## API documentation
### Swagger project [URL](http://localhost:8080/swagger-ui/index.html) `accessible when the program is running`