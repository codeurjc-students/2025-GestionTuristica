# +HOTEL

## Summary

This hotel booking application is designed looking to make a functional app that can be used by both clients and agencies to 
book and manage hotel reservations.

This application provides the tools for clients to search for hotels, reserve rooms, modify the reservation, cancel it 
and rate the hotels where they stayed. And also the necessary tools for a travel agency to manage the ones made by the clients.

This app provides the function to search for hotels based on location, name, stars or other users ratings.

## Index

- [Objectives](#project-objectives)

- [Methodology](#methodology)

- [Planning and Schedule](#project-planning-and-schedule)

- [Functionalities](#system-functionalities)

- [Initial Analysis](#initial-analysis)


## Project Objectives

### Functional Objectives

- Provide the tools to make, modify and cancel room reservations
- Allow users to search and filter hotels
- Allow admins to manage reservations and hotels from an exclusive admin page
- Manage user authentication, with user roles that control which pages can be accessed and allow users to check and 
manage their own reservations
- Allow users to review the hotels where they stayed
- Provide tools to download your reservation bill and notify users if their reservations were cancelled or modified by 
an admin

### Technical Objectives

- Develop frontend with Angular with TypeScript
- Develop backend in Java with Spring Boot, structured into controllers, services and middleware, adding security with
Spring Security
- Use MySQL as database service
- Connect frontend and backend through API REST
- Document the API REST services with Spring doc and Swagger


## Methodology

----------------------------------------------------------------------------------------------------------

| Phase   | Description                              | Objective date | Initial date   | Finish date  |
|---------|------------------------------------------|----------------|----------------|--------------|  
| Phase 1 | Functionality definition and prototyping | September 15th | September 10th | October 15th | 
| Phase 2 | Repository setup, CI/CD, Sonar           | October 1st    | October 16th   | -            |
| Phase 3 | Basic functionality with testing         | November 1st   | -              | -            | 
| Phase 4 | Full functionality with Docker setup     | December 15th  | -              | -            |
| Phase 5 | Memory                                   | January 15th   | -              | -            | 
| Phase 6 | Presentation and defence                 | June           | -              | -            | 
----------------------------------------------------------------------------------------------------------

## Project Planning and Schedule

### Gantt diagram

The project was scheduled following an agile methodology. The following Gantt diagram shows how the tasks are distributed:

#### Main project phases

- Phase 1 - Initial analysis and functionality design: Identify and define the functionality of the web and the design 
and interactions. Differentiating the functionality of each user type.
- Phase 2 - Repository, testing and CI: Create the git repository, both client and server project and implement the 
minimal functionality to connect the projects and database. Implement basic automatic tests and the CI system.
- Phase 3 - Version 0.1 - Basic functionality and Docker: Add basic functionality (with automatic tests) and the Docker 
packaging. Capacity for continuous delivery will be added. Version 0.1 of the application will be released.
- Phase 4 - Version 0.2 - Intermediate functionality: Implement intermediate functionality (with automatic tests). 
Version 0.2 of the application will be released. The application will also be deployed in this phase.
- Phase 5 - Version 1.0 - Advanced functionality: The development of the application will be finished and the Version 
1.0 will be released.
- Phase 6 - Report: Make the first draft of the first report (application).
- Phase 7 - Docker compose with backend replicas: Implementation of a backend with multiple instances of backend
- Phase 8 - Simple deployment with AWS: App will be launched in EC2, database will be launched in a RDS.
- Phase 9 - Advanced deployment with AWS: App will also have scalability (Autoscaling groups), load balancer and load testing
- Phase 10 - Infrastructure as code and continuous deployment: Create a CouldFormation and continuous deployment.
- Phase 11 - Report 2: Make the first draft of the second report (cloud deployment).
- Phase 12 - Defense: Presentation of the final degree projects.

## System Functionalities

Functionalities have been sorted into three categories: 
- Basic: Basic functionalities that are not very complex and fundamental for the application.
- Intermediate: Functionalities with a little more complexity, less vital to the app, but still necessary for the basic
functionalities of the application.
- Advanced: The most complex functionalities of the app.

### Basic functionalities

- Do reservations, modify and cancel them
- Check reservations made
- Check hotels and rooms
- Show a room as not available if it's already reserved

### Intermediate functionalities

- Authentication and user creation
- Check reservations made by the user
- Create, modify and delete hotels from admin page
- Create and delete admin users
- Find reservations by identifier and being able to see them in admin page with modification and delete included

### Advanced functionalities

- Filter hotels by location, rating, users opinion...
- Filter reservations in admin page by user, hotel, date...
- Review and rate hotels where the user had reservations
- Get bill in PDF format to download when you make a reservation
- Get an email when an admin cancelled or modified your reservation

## Initial Analysis

### Pages

The pages present in the app will be:

Main page: page where users will land at first


Hotel search page: page where hotels will show and can be filtered by different criteria


Hotel detail page: page where users can check the hotel details


Hotel room reservation page: page where users will be able to see the available rooms in the hotel with the option to book them


Reservations page: page where the agency will be able to see the reservations made by the users, each user will have 
a personalized page where they'll be able to check their own reservations


Reservation detail page: page where users or the admins will be able to check the detail of a reservation, cancel it
or modify it


Log in page: page where users will enter their credentials to log in


Sign in page: page where users will enter credentials to create a new user



Hotel addition page: page where admins will be able to add new hotels to the application



### Entities

The app will have 4 main entities: User, Hotel, Room and Reservation






