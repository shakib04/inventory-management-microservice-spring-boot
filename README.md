<h1 align="center">🌟 Spring-Boot-Microservices-Inventory-Management-Application 🌟</h1>
<h2>📋 Table of Contents</h2>

- [🔍 About](#-about)
- [🏛️ Architecture](#-architecture)
- [🚀 Microservices](#-microservices)
- [🚀 Getting Started](#-getting-started)
- [📖 Documentation](#-documentation)
- [⌚ Future Enhancement](#-future-enhancement)
- [🤝 Contribution](#-contribution)
- [📞 Contact Information](#-contact-information)

## 🔍 About
<p>
    The Inventory Application is built using a microservices architecture, incorporating the Spring Boot framework along with other Spring technologies such as Spring Data JPA, Spring Cloud, and Spring Security, alongside tools like Maven for dependency management. These technologies play a crucial role in establishing essential components like Service Registry, API Gateway, and more.<br><br>
    Moreover, they enable us to develop independent microservices such as the user service for user management, the product service for product management and other related functionalities, the order service for manage product orders, and the inventory service for stock management of products. These technologies not only streamline development but also enhance scalability and maintainability, ensuring a robust and efficient inventory management system.
</p>

## 🏛️ Architecture

- **Service Registry:** The microservices uses the discovery service for service registration and service discovery, this helps the microservices to discovery and communicate with other services, without needing to hardcode the endpoints while communicating with other microservices.

- **API Gateway:** This microservices uses the API gateway to centralize the API endpoint, where all the endpoints have common entry point to all the endpoints. The API Gateway also facilitates the Security inclusion where the Authorization and Authentication for the Application.

- **Database per Microservice:** Each of the microservice have there own dedicated database. Here for this application for all the microservices we are incorporating the Postgres database. This helps us to isolate each of the services from each other which facilitates each services to have their own data schemas and scale each of the database when required.

<h3>🛠️ Technologies </h3>
We have used below tools & technologies for this project-
- 🛢 Database: **PostgreSQL**
- 🚀 Caching: **Redis**
- 📢 Queue: **RabbitMQ**
- 🏛️ API Gateway: **Spring Cloud Gateway**
- 🔎 Service Discovery: **Eureka**
- 📝 API Documentation: **Swagger**
- 🔐 Authentication: **JWT Based Authentication**
- 🌐 Frontend-web: **Angular**

<h2>🚀 Microservices</h2>

- **👤 User Service:** The user microservice provides functionalities for user management. This includes user registration, updating user details, viewing user information, and accessing all accounts associated with the user. Additionally, this microservice handles user authentication and authorization processes.

- **💼 Product Service:** The product microservice manages product-related APIs. It enables users to modify product details, view all products.

- **💸 Inventory Service:** The inventory microservice facilitates product stock management-related functionalities. If new product has created, new inventory item will be created via RabbitMQ. 
                            User can update the stock details of inventory item using this service.

- **💳 Order Service:** The order service offers product order-related services. Users can create new order of product for sales. 
                        Inventory item stock will be updated if new order is created based on specific product.

<h2>🚀 Getting Started</h2>

To get started, follow these steps to run the application on your local application:

- Make sure you have Java 17 installed on your system. You can download it from the official Oracle website.
- Select an Integrated Development Environment (IDE) such as Eclipse, Spring Tool Suite, or IntelliJ IDEA. Configure the IDE according to your preferences.
- Clone the repository containing the microservices onto your local system using Git. Navigate to the directory where you have cloned the repository.
- Navigate to each microservice directory within the cloned repository and run the application. You can do this by using your IDE or running specific commands depending on the build tool used (e.g., Maven or Gradle).
- Some microservices and APIs may depend on others being up and running. Ensure that all necessary microservices and APIs are up and functioning correctly to avoid any issues in the application workflow.

<h2>📖 Documentation</h2>
<h3>📂 Microservices Documentation</h3>

For detailed information about each microservice, refer to their respective README files:

- [👤 User Service](./User-Service/README.md)
- [💼 Inventory Service](./Inventory-Service/README.md)
- [🏭 Product Service](./Product-Service/README.md)
- [🖱 Order Service](./Order-Service/README.md)

<h3>📖 API Documentation</h3>

We have used Swagger for API Documentation.
- [🔗 Swagger URL](http://localhost:8080/swagger-ui.html)