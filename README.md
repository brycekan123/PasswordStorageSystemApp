## Password Storage System


### To run the code:

- git clone https://github.com/brycekan123/PasswordStorageSystemApp.git

- Navigate to this folder(demo/src/main/java/com/example/demo) and run PasswordStorageMain.java. 

### Description of Project:
Although not revolutionary, I wanted to showcase some of the topics I have learned as an MSCS student in my first semester at USC and explore new topics with my existing class knowledge.
Topics of this project: Spring Boot, RestAPIs, Object Oriented Design in Java.


This project uses the terminal and leverages RestAPIs and SpringBoot to make an interactive password storage system. The user will always be shown with a terminal command line prompt until they decide to quit the application. 

- I used REST API to allow for external Systems to interact with my application's backend logic via HTTP. This allows for a more efficient process and seamless process for individuals to access the passwords they desire at anytime.
For Example, if the user types in (http://localhost:8080/getAllPasswords) in their browser while application is running, they will be able to see all their passwords instead of loading through the terminal.
 - With the help of Spring Boots application, which simplifies the management of the application as it handles the required libraries for REST APIs and JSON processing. 
Additionally, Spring Boot has a built in Tomcat Server that allows the application to run locally and display the messages that the user desired on a local browswer. Specific HTTP endpoints were mapped to specific functions to entire that requests were unique.

### Examples of REST API Usage
- getPassword and getAllPasswords have anotations @getMapping from Spring Boot to map a unique http endpoint to ensure the desired message is displayed in the user's local broswer.
```ruby
@GetMapping("/getAPassword")
public ResponseEntity<String> getPassword(@RequestParam String website) {
```
```ruby
@GetMapping("/getAll")
public Map<String, String> getAllPasswords() {
    return passwordStore;
}
```
The passwords are stored as a map in a JSON file so the invidivual can access their saved passwords at anytime. 
