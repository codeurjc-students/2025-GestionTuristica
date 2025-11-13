# +HOTEL

## Summary

This hotel booking application is designed to be a functional app that can be used by both clients and agencies to 
book and manage hotel reservations.

This application provides the tools for clients to search for hotels, reserve rooms, modify the reservation, cancel it 
and rate the hotels where they stayed. And also the necessary tools for a travel agency to manage the ones made by the clients.

Only technical and functional objectives have been defined so far. Implementation has not been started yet.

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

- Develop frontend using Angular with TypeScript
- Develop backend in Java using Spring Boot, structured into controllers, services and middleware, adding security with
Spring Security
- Use MySQL as database service
- Connect frontend and backend through API REST
- Document the API REST services with Spring doc and Swagger


## Methodology

----------------------------------------------------------------------------------------------------------

| Phase   | Description                              | Objective date | Initial date | Finish date  |
|---------|------------------------------------------|----------------|--------------|--------------|  
| Phase 1 | Functionality definition and prototyping | October 15th   | October 1st  | October 23rd | 
| Phase 2 | Repository setup, CI/CD, Sonar           | November 7th   | October 23rd | November 13th|
| Phase 3 | Full functionality with Docker setup     | December 22nd  | -            | -            |
| Phase 4 | Memory                                   | January 15th   | -            | -            | 
| Phase 5 | Presentation and defence                 | June           | -            | -            | 
----------------------------------------------------------------------------------------------------------

## Project Planning and Schedule

### Gantt diagram

The project was scheduled following an agile methodology. The following Gantt diagram shows how the tasks are distributed:

<img width="2775" height="281" alt="gantt_diagram_5" src="https://github.com/user-attachments/assets/f16012d7-4e19-44ac-903c-fb47a681232d" />

### Main project phases

- *Phase 1* - Initial analysis and functionality design: Identify and define the functionality of the web and the design 
and interactions. Differentiating the functionality of each user type.
- *Phase 2* - Repository, testing and CI: Create the git repository, both client and server project and implement the 
minimal functionality to connect the projects and database. Implement basic automatic tests and the CI system.
- *Phase 3* - Version 1.0 - Advanced functionality: The development of the application will be finished and the Version 
1.0 will be released.
- *Phase 4* - Report: Make the first draft of the first report (application).
- *Phase 5* - Docker compose with backend replicas: Implementation of a backend with multiple instances of backend
- *Phase 6* - Simple deployment with AWS: App will be launched in EC2, database will be launched in an RDS.
- *Phase 7* - Advanced deployment with AWS: App will also have scalability (Autoscaling groups), load balancer and load testing
- *Phase 8* - Infrastructure as code and continuous deployment: Create a CouldFormation and continuous deployment.
- *Phase 9* - Report 2: Make the first draft of the second report (cloud deployment).
- *Phase 10* - Defence: Presentation of the final degree projects.

## System Functionalities

Functionalities have been sorted into three categories: 
- Basic: Basic functionalities that are not very complex and fundamental for the application.
- Intermediate: Functionalities with a little more complexity, less vital to the app, but still necessary for the main
  services provided by the application.
- Advanced: The most complex functionalities of the app.

### Basic functionalities

-----------------------------------------------------------------------------------------------------------------------------------------------

| Functionality                                 | Unregistered User | Registered User      | Admin User      | 
|-----------------------------------------------|-------------------|----------------------|-----------------| 
| View existing hotels, detailed view and rooms | Yes               | Yes                  | Yes             |
| Manage hotels and their rooms                 | No                | No                   | Yes             |
| Make reservations                             | No                | Yes                  | No              |
| View reservations                             | No                | Yes, but only theirs | Yes, any user's |
| Modify and cancel reservations                | No                | Yes, but only theirs | Yes, any user's |
-----------------------------------------------------------------------------------------------------------------------------------------------

### Intermediate functionalities

-------------------------------------------------------------------------------------------------------------------------------------

| Functionality                 | Unregistered User | Registered User                                    | Admin User              | 
|-------------------------------|-------------------|----------------------------------------------------|-------------------------| 
| Create an user                | Yes               | -                                                  | -                       |
| Log in                        | Yes               | -                                                  | -                       |
| Leave reviews in hotels       | No                | Yes, but only in those where they had reservations | No, but can manage them |
| Create and delete admin users | No                | No                                                 | Yes                     | 
| Filter reservations           | No                | Only theirs                                        | Yes, in the admin page  |
-------------------------------------------------------------------------------------------------------------------------------------

### Advanced functionalities

--------------------------------------------------------------------------------------------------------------------------------

| Functionality                                                | Unregistered User | Registered User | Admin User             | 
|--------------------------------------------------------------|-------------------|-----------------|------------------------| 
| Filter hotels by location, rating, users' opinion            | Yes               | Yes             | Yes                    |
| Get bill in PDF format when reservation is made              | -                 | Yes             | -                      |
| Get an email when reservation is made, modified or cancelled | -                 | Yes             | -                      |
--------------------------------------------------------------------------------------------------------------------------------


## Initial Analysis

### Images

- Room images: Each room has an image which is shown in the rooms reservation page of the hotel they belong to
- Hotel images: Hotels can have multiple images, one will be shown in the hotel searching page and the rest can be 
seen in the hotel detail page

### Graphs

- Hotels with the most reservations: Graphic in the hotels page where it'll show how reservations distribute among the 
hotels of the app

### Complimentary technologies

- Email notifications: User will be contacted by email when a reservation is made, modified or cancelled
- PDF generation: A PDF will be generated with the info of a reservation once it's made
- Maps location: In the hotel detail page, a Google Maps component will show where the hotel is located

### Algorithms and advanced queries

- Hotel search: User will be able to search for hotels that meet one or multiple requirements, such as number of stars, price,
city... 

### Pages

The pages present in the app will be:

- **Main page**: page where users will land at first

<img width="740" height="418" alt="img" src="https://github.com/user-attachments/assets/e0a813c2-f2f2-49c8-b6f3-6b0488e8d7bd" />

- **Hotel search page**: page where hotels will show and can be filtered by different criteria

<img width="740" height="415" alt="img_1" src="https://github.com/user-attachments/assets/36e5d4cd-d95c-41f8-8932-7b40c1f79951" />

- **Hotel detail page**: page where users can check the hotel details

<img width="740" height="416" alt="img_2" src="https://github.com/user-attachments/assets/23657e1a-71df-491b-864c-340e6bbe8d91" />

- **Hotel room reservation page**: page where users will be able to see the available rooms in the hotel with the option to book them

<img width="738" height="412" alt="img_3" src="https://github.com/user-attachments/assets/8972c7d5-be35-495b-936a-c8eeaf33ed61" />

- **Reservations page**: page where the agency will be able to see the reservations made by the users, each user will have 
a personalized page where they'll be able to check their own reservations

<img width="745" height="411" alt="img_4" src="https://github.com/user-attachments/assets/2b854540-dfba-42bc-a9b8-ac1d6dbd4a2a" />

- **Reservation detail page**: page where users or the admins will be able to check the details of a reservation, cancel it
or modify it

<img width="735" height="414" alt="img_5" src="https://github.com/user-attachments/assets/fa9e2bf0-57b6-4296-9816-e55f8bbd1ba6" />

- **Log in page**: page where users will enter their credentials to log in

<img width="748" height="421" alt="img_10" src="https://github.com/user-attachments/assets/016d81e3-8763-413c-a03f-4a81633fe277" />

- **Sign in page**: page where users will enter credentials to create a new user

<img width="748" height="421" alt="img_11" src="https://github.com/user-attachments/assets/28a686a4-7fa1-42b9-a2cd-aacffdfa2218" />

- **Hotel addition page**: page where admins will be able to add new hotels to the application

<img width="938" height="834" alt="hotel_creation_page" src="https://github.com/user-attachments/assets/4857832f-bde9-431e-a962-43113001287a" />

- **Room addition page**: page where admins will be able to add a new room to a hotel

<img width="935" height="528" alt="room_creation_page" src="https://github.com/user-attachments/assets/0ad62518-9573-4771-9319-db38a47c2947" />

### Entities

The app will have 4 main entities as shown in the shown diagram: User, Hotel, Room and Reservation

<img width="845" height="687" alt="img_6" src="https://github.com/user-attachments/assets/81ffa32d-e55b-4524-9276-cc91ee6c21e9" />
