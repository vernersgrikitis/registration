# Registration Application

## Getting Started

- You must have JDK 17 , Apache Maven and Docker installed on your PC to run it!

- You can use a commandline move to directory you want to download this project and hit the command 
git pull https://github.com/vernersgrikitis/registration.git
then move to directory registration and
hit "mvn clean package"
when .jar file is created
hit "docker-compose up --build"

- Now you can use unsecured localhost:8080/registration or localhost:8080/login to login
and get a JWT token so you are able to check if u pass the security with
localhost:8080/user/secure-endpoint
with a token, you can upload image localhost:8080/user/add-image where key:image and value:image.jpg <- example
You can get an image by passing your JWT token localhost:8080/user/get-image and
localhost:8080/user/delete-image to delete image
localhost:8080/user/delete-user to delete user, user will be deleted and you will not be able to login and your token will be annulated!

- If you have IDE installed on your PC or MAC , you can run custom StompClient.java which inside of this project, you can login in it with your registred account and observe real time events via websocket connection of changing or adding image or delating user.

- I use Postman to send requests to server, you can freely use software you prefer

![endpointi](https://github.com/vernersgrikitis/registration/assets/127933614/4f082de1-120d-4631-8f4f-638cc185b2d3)


### Technology used: 

- Spring Boot
- Lombok
- Docker
- MongoDB
- Websocket
- Stomp
- Swagger documentation
- JWT Security
- Postman
- Unit tests

#### You can:

- Register(with firstname, lastname, email, password).
- Login (with email and password)
- Authorize with a token which you can get by Register or Login
- Add image 
- Get an image
- Delete image
- Delete your profile
- You can run a custom StompClient, connect to a server and get notice messages about changes you make. 
 


