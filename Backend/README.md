# Prime Alert

## Inspiration
The inspiration came from visiting a doctor office with my wife and we had to turn back because the type of medical I needed to do could not be walk-in. 
We came back next week after setting the appointment for 2pm which we arrived 15mins early but still had to wait until 3:30pm to see the doctor.

This allows patients to book appointments in advance via a website and they will be able to see a real time queue of appointments per day that is periodically refreshed.

## Introduction
This is a spring boot app that does the server side business logic for handling appointment scheduling via website

This allows patients to book appointments in advance via a website and they will be able to see a real time queue of 
appointments per day that is periodically refreshed.


## Language Version

- Java 17
- Maven 3.5.4
- Spring Boot 3.3.1

## How To Run Locally

1. Configure your IDE to use Java 17 and Maven 3.9.4
2. Change profile in application.properties to use either local or prod configs
3. Download pgAdmin 4(version 15) & Configure Postgres using the following link https://www.enterprisedb.com/downloads/postgres-postgresql-downloads

    - This installation process will give you the option to configure Postgres and pgAdmin 4 (version 15) at the same time(Recommended)
    - The installation process will ask you to select the following components
      . PostgreSQL Server (required)
      . pgAdmin 4 (required)
      . Stack Builder (optional)
      . Command Line Tools (required)

              OR

    - You can pull a docker image of Postgres using the command : docker pull postgres
    - Then configure it using the db properties outlined in the application.properties file
    - You will still need a Graphical User Interface(GUI) for your Database, use pgAdmin 4(Recommended) or any other GUI of your choice
    - Run the Dockerfile inside this project
    - azure-pipelines.yml and fly.toml can assist with deployment on azure and fly.io respectively 
     (NOTE environment variables are needed, reach out to Luigi Woodhouse if needed.)

4. Configure the AZURE_CLIENT_ID ,AZURE_CLIENT_SECRET, AZURE_TENANT_ID for the run configuration of the main class
   NB. for security reasons, the Azure environment variables were not included in this document, Reach out to Product Owner for them.

5. Live Urls of the Backend can be found at the following

Rest API

   1. Register a patient   https://prime-life.fly.dev/register/new/user
   2. Login                https://prime-life.fly.dev/login/authenticate
   3. Book appointment     https://prime-life.fly.dev/appointment/book
   4. View All appointment https://prime-life.fly.dev/view/all
   5. View patient by id   https://prime-life.fly.dev/view/view/patient/{id}
   6. Update Appointment   https://prime-life.fly.dev/appointment/modify/appointment/{appointmentId}
   7. Cancel Appointment   https://prime-life.fly.dev/appointment/cancel/{appointmentId}/{patientId}

Web Socket
   1. This endpoint is to view live queue of appointments

      wss://prime-life.fly.dev/ws/appointments

Live URLS will be available up until March 31, 2025 or until quota has been exceeded.