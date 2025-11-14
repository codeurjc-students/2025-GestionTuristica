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

| Functionality                                                | Unregistered User | Registered User | Admin User | 
|--------------------------------------------------------------|-------------------|-----------------|------------| 
| Filter hotels by location, rating, users' opinion            | Yes               | Yes             | Yes        |
| Get bill in PDF format when reservation is made              | -                 | Yes             | -          |
| Get an email when reservation is made, modified or cancelled | -                 | Yes             | -          |

--------------------------------------------------------------------------------------------------------------------------------
