![image.png](./image.png)

[[_TOC_]]

# Where are you?

This is the client-side project for the Analysis and Development project.

## Sonar Badges
![client reliability rating](https://sonar.ti.howest.be/badges/project_badges/measure?project=2022.project-ii:mars-client-08&metric=reliability_rating)
![client bugs](https://sonar.ti.howest.be/badges/project_badges/measure?project=2022.project-ii:mars-client-08&metric=bugs)
![client code smells](https://sonar.ti.howest.be/badges/project_badges/measure?project=2022.project-ii:mars-client-08&metric=code_smells)
![client technical debt](https://sonar.ti.howest.be/badges/project_badges/measure?project=2022.project-ii:mars-client-08&metric=sqale_index)
![client alert status](https://sonar.ti.howest.be/badges/project_badges/measure?project=2022.project-ii:mars-client-08&metric=alert_status)
![client maintainability](https://sonar.ti.howest.be/badges/project_badges/measure?project=2022.project-ii:mars-client-08&metric=sqale_rating)
![client lines of code](https://sonar.ti.howest.be/badges/project_badges/measure?project=2022.project-ii:mars-client-08&metric=ncloc)
![client vulnerabilities](https://sonar.ti.howest.be/badges/project_badges/measure?project=2022.project-ii:mars-client-08&metric=vulnerabilities)
![client security rating](https://sonar.ti.howest.be/badges/project_badges/measure?project=2022.project-ii:mars-client-08&metric=security_rating)
![client coverage](https://sonar.ti.howest.be/badges/project_badges/measure?project=2022.project-ii:mars-client-08&metric=coverage)
![client duplicated lines density](https://sonar.ti.howest.be/badges/project_badges/measure?project=2022.project-ii:mars-client-08&metric=duplicated_lines_density)
## Important public urls  
* Web project: https://project-ii.ti.howest.be/mars-08/
* Sonar reports: https://sonar.ti.howest.be/dashboard?id=2022.project-ii%3Amars-client-08

## Please complete the following instructions before committing the **final version** on the project
Please **add** any **instructions** required to: 
* make your application work if applicable 
* be able to test the application (credentials, populated db, ...)

Also clarify
* If there are known **bugs**.
* If you haven't managed to finish certain required functionality.

## Instructions for Viewing the Wireframes

* The wireframes can be viewed on Figma [here](https://www.figma.com/file/7Mf6N7dbn6yqBFJBqWntAO/Garmar-Wireframes-Updated?node-id=0%3A1&t=wbuGC0q2asIaEFOC-3)

## Instructions for testing locally

* Run the mars-server with gradle run (through your IDE)
* Open the mars-client in phpstorm/webstorm
  * Navigate to the index.html
  * Click on a browser icon at the top right of your IDE to host the mars-client.
  
## Instruction for testing the web client locally with a deployed mars-server
* Open the mars-client in phpstorm
  * Copy the following settings to **config.json** 
```json
      {
        "host": "https://project-ii.ti.howest.be",
        "folder": "",
        "group": "mars-08"
      }
```
  * Navigate to the index.html
  * Click on a browser icon at the top right of your IDE to host the mars-client.
  * Make sure to undo the settings once you are done testing the remote server!

## Instructions for local quality checks

You can run the validators for html, CSS and JS rules locally. 

Make sure **npm** is installed.

There is no need to push to the server to check if you are compliant with our rules. 

In the interest of sparing the server, please result to local testing as often as possible. 

If everyone pushes to test, the remote will not last. 

Open a terminal in your IDE
  - Make sure you are in the root folder of the client project.
  - Execute `npm install` this step is only needed once.
  - Execute `npm run validate-local` for linux/mac users.
  - Execute `npm run validate-local-win` for Windows users. 
  - If there are errors, the program execution will halt and show the first error
  - If there are no errors, a report file will be generated in the `.scannerworks/` directory. 
    - You will find the link to the sonar report in this file 

Hint:

If you want to skip ci remotely, include `[ci skip]` in your commit message. 

This is convenient for when you want to quickly add a certain commit, but do not wish to trigger the whole CI sequence. 

## Known bugs
We have 200+ code smells in SonarQube within the client, these are because we have implemented Bootstrap and compile the SCSS files to one CSS file. This creates errors but we were advised by MR. Sys Kurt to ignore these code smells when asked. 

## Default files

### CSS 
The `reset.css` has already been supplied, but it's up to you and your team to add other styles. 

### JavaScript
A demonstration for connecting with the API has already been set up. 

We urge you to divide your JS files into many small JS files. 

## Garmar Garbage Collection as a Service

Welcome to Garmar Garbage Collection as a Service project! Our service aims to provide a convenient and environmentally-friendly solution for waste management.

### Features

- **Main Dashboard:** The main dashboard provides an overview of your disposed garbage per month.

- **Request Pickup:** Need to dispose of an item? Simply visit the request pickup page, fill out the form, and we'll take care of the rest!

- **Subscription Management:** Change your subscription plan or update your billing information on the subscription management page.

- **Recycled Molecules:** Our service also provides the option to purchase recycled molecules. These molecules are created from materials that have been properly recycled and repurposed.

- **Purchase History:** View a history of all the recycled molecules you have purchased on the purchase history page.

### Benefits

- Convenient: No need to worry about hauling your garbage to the curb. Simply put it in the recycle pipe or request a pickup and we'll take care of the rest.

- Environmentally-friendly: By using our service, you're helping to reduce waste and promote the use of recycled materials.

We hope you enjoy using Garmar Garbage Collection as a Service project! If you have any questions or feedback, please don't hesitate to reach out.
