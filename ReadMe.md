# Mail Order Pharmacy

## Backend

### Installation

- Unzip the project folder.
- Import all the four services present in the folder named "Backend" into your Eclipse/STS.
- Do Maven Update on all four services.

### Usage

Run the services one by one as Spring Boot project.

## Frontend

### Installation

Run

```bash
npm install
```

### Usage

Run

```bash
ng serve
```

## Some Useful References

Below are some useful links for accessing Swagger API documentation, h2-console and Angular home page, etc.

### Membar Web Portal (Angular)

Link: http://localhost:4200/

### Drugs Microservice

- H2 Console: http://localhost:8081/drugdetailapp/h2-console
- spring.datasource.url: jdbc:h2:mem:drugsdb
- username: sa
- Swagger: http://localhost:8081/drugdetailapp/swagger-ui.html

### Subscription Microservice

- H2 Console: http://localhost:8082/subscriptionapp/h2-console
- spring.datasource.url: jdbc:h2:mem:subscriptiondb
- username: sa
- Swagger: http://localhost:8082/subscriptionapp/swagger-ui.html

### Refill Microservice

- H2 Console: http://localhost:8454/refillapp/h2-console
- spring.datasource.url: jdbc:h2:mem:refilldb
- username: sa
- Swagger: http://localhost:8454/refillapp/swagger-ui.html

### Authorization Microservice

- H2 Console: http://localhost:8090/authapp/h2-console
- spring.datasource.url: jdbc:h2:mem:authdb
- username: sa
- Swagger: http://localhost:8090/authapp/swagger-ui.html
