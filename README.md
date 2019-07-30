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

## Technical stack used on this project
+ Backend
    * Java
    * SpringBoot
    * H2
+ Frontend
    * Javascript
    * React 16.9.0-alpha.0
    * React hooks
    * Axios
    * Fontawesome

## Features
* Register new file
* Edit file information
* Files search by title or description using
* Download of previously saved files
* Pagination
* Modal dialog boxes using react portals
* Full global application state management only using useContext and useReducer react hooks
* local component state management using useState hook
* Only React function/stateless based components

## How to run
+ Backend: inside the backend folder, just run
    `mvn clean spring-boot:run`
+ Frontend: inside the frontend folder, just run
    `npm install && npm start`


![Screenshot](/screenshot.png)