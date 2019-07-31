# Dotsub coding test

Create a simple web project, that provides a form to upload a file and gather some metadata for that file: Title, Description and Creation date

- The UI can be implemented in any SPA framework of your choice.
- The REST back-end will be preferably implemented based on Java/Spring Boot (+ any Spring/Java library/framework of your choice) to store data.
- No user authentication and security features are needed.
- Project can use simple in-memory DB to store data
- Files can be stored in web server file system.
- No need to implement routing or sophisticated state management in UI (e.g. FLUX)
- Project can use any quality assurance mechanisms or build tools of choice
- You may also submit with the project any design considerations, restrictions, or possible future enhancements (if applicable)

## Technical stack 
+ Backend
    * Java (Compiled with openjdk version 11.0.3)
    * Spring-Boot
    * H2 
+ Frontend
    * JavaScript/ES6+
    * React 16.9.0-alpha.0
    * React hooks
    * Axios
    * Fontawesome
    * Tests with Jest

## Features
* Register a new file : title, description and binary data
* Edit a previously registered file
* Delete a previously registered file 
* Search by title or description 
* Download file's binary data
* Pagination

## Frontend design details
* This project uses React 16.9.0-alpha.0 (this alpha version includes some fixes on the new 'act' API)
* Only React functional components
* Pure and simple CSS, no library used
* Modal dialog boxes using React portals
* Full global application state management only using useContext and useReducer React hooks
* Inside each component, state management is achieved through useState hook
* There are tests using Jest to ensure the correct behaviour of App and Paginator components 
* All the interaction with the backend is done through the service **src/services/FileService.js** wich uses the library Axios to trigger http requests 

## Backend design details
* The application preloads the database with images and details of the 7 wonders of the world
* The POST, UPDATE and PATCH endpoints handle binary data associated with the files. Thus, to cope with the requirement of storing binary data, this endpoints were coded in an async way in order to avoid slowing down the http server's worker threads with slow I/O tasks
* This choice for async endpoints was taken into consideration to avoid future scalability issues    
* Binary data are stored by default in the local OS temp directory. The storage mechanism was abstracted away through a service interface (**com.dotsub.challenge.services.DataStorageService**) whose only implementation is **com.dotsub.challenge.services.FileSystemStorageService** used on the default spring boot profile
* The following classes are involved on handling the restful endpoints:
  *   **com.dotsub.challenge.repositories.FileRepository**
      *   Basic spring data rest repository for file, exposing CRUD methods as restful endpoints and with sort and paging capabilities
  *   **com.dotsub.challenge.controller.FileController**
      *   A controller to override the POST, PATCH and PUT endpoint of the previous restful controller. Takes into account the logic to store the binary data
  *   **com.dotsub.challenge.controller.DownloadController**
      *   The controller who allows download files' binary data 
* It was necessary to use the spring security project, in order to properly setup the CORS policy, once that both the frontend and the backend servers run on different ports at localhost (See **com.dotsub.challenge.config.WebSecurityConfig**)
* Each resource was augmented with the link that allows the download of his binary data (See **com.dotsub.challenge.config.FileResourceProcessor**)
* There are integration tests to ensure the correct behaviour of all restful endpoints for File 

## Possible improvements
* Implement a responsive layout
* Implement end-to-end tests with cypress for the frontend
* Show a more detailed upload progress when a new file data is been sent to the server
* Implement a service worker to cache the data allowing a read only browsing mode experience when the network is down
* Create a new **com.dotsub.challenge.services.DataStorageService** implementation, in order to store files in some cloud storage provider
* Use the spring cache abstraction to save database reads when listing files

## How to run
+ Backend: First, ensure you have **JDK 11** at least. Inside the **backend** folder, just run
    `mvn clean spring-boot:run`
+ Frontend: First, ensure you have **node >= 8.10**. Inside the **frontend** folder, just run
    `npm install && npm start`

## How to run the backend tests
`mvn clean test`

## How to run the frontend tests
`npm tests`

## Screenshot
![Screenshot](/screenshot.png)