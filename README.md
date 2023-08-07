# keycloak-nginx-openidc-spring

This sample project demonstrates how to secure a Spring Boot application using Keycloak, an open-source identity and access management solution. Nginx, configured with lua-resty-openidc library, acts as a reverse proxy that authenticates users through OpenID Connect (OIDC) before forwarding their requests to the Spring Boot application.

## Key Points

Here are the key points demonstrated in this project:

**Keycloak Setup:**
This involves setting up a new realm, creating clients, setting up roles and users in the Keycloak Admin Console.

**Nginx Configuration:**
The Nginx server is configured to authenticate users through OIDC using the lua-resty-openidc library before forwarding the requests to the Spring Boot backend. Nginx communicates with Keycloak to authenticate users, receive and validate the tokens.

**Spring Boot Application Security:**
The Spring Boot application is secured using Spring Security. It is configured to validate the JWT tokens received in the request headers and map the roles embedded in the tokens to authorities that are used for authorization.

**JWT to Granted Authorities Conversion:**
A custom `JwtAuthenticationConverter` is used to map the roles embedded in the JWT under the `realm_access.roles` claim to `SimpleGrantedAuthority` objects, which are used by Spring Security for access control decisions.

## Starting Docker Containers

Before setting up Keycloak, start the necessary Docker containers:

```shell
docker-compose up
```

The `docker-compose.yml` file is located in the root of this repository. This command should be run in the same directory as that file.

## Keycloak Configuration

You first need to configure Keycloak. Follow these steps in the Keycloak admin console.

### Access the Keycloak admin console

1. Open a web browser and navigate to `http://localhost:8081`.
2. Click on the `Administration Console` link.
3. The Keycloak Admin Console login page will appear.
4. Login with the username `admin` and password `admin` (These credentials are set in the docker-compose file under the environment variables `KEYCLOAK_ADMIN` and `KEYCLOAK_ADMIN_PASSWORD` respectively).

### Create a new Realm

1. Click "Create Realm" from the dropdown menu on the top left corner,.
2. Enter the name for your new realm (e.g. "example") and click "Create".

### Create a new Client

1. In the created realm, navigate to "Clients" on the left menu and click "Create Client" at the right side.
2. For the first step:
   - **Client Type:** Choose "OpenID Connect"
   - **Client ID:** The ID of your client (e.g. "nginx-client")
   - Click "Next".
3. For the second step:
   - **Client Authentication:** Set to "ON", which means it is set to confidential access type. 
   - **Authorization:** Leave "OFF" unless you need fine-grained authorization support. 
   - **Authentication Flow:** Leave it as "Standard Flow". 
   - Click "Next".
4. For the third step:
   - **Root URL:** The base URL of your application (e.g. "http://localhost:8080")
   - **Valid redirect URIs:** Enter your valid URI (e.g. "http://localhost:8080/*"). This means after successful authentication, the user can be redirected to any path on localhost at port 8080.
     - **Note:** This setting is suitable for development or testing purposes. In a production environment, it's recommended to avoid this broad setting and instead specify a concrete and minimal set of redirect URIs to mitigate the risk of open redirect attacks.
   - **Web Origins:** Enter `+`  to permit all origins of Valid Redirect URIs for CORS.
     - **Note:** While this setting may be suitable for development or testing environments, it is recommended to explicitly list the origins that should be allowed in a production environment to prevent potential security risks associated with Cross-Origin Resource Sharing (CORS).
   - Click "Save".

Remember to note the "Client ID" and "Client Secret" which can be found in the "Credentials" tab of the client settings. You will need these for your nginx configuration later.

### Create a new Role

1. In the created realm, navigate to "Roles" on the left menu and click "Add role" at the right side.
2. Enter a name for the new role (e.g. "user") and click on "Save".

### Create a new User

1. In the created realm, navigate to "Users" on the left menu and click "Add user" at the right side.
2. Enter the details for the new user (username, email, etc.) and click on "Create".
3. Open the "Credentials" tab. Set a password, turn the "Temporary" switch "Off", and click "Save".
4. Open the "Role mappings" tab. Click "Assign Roles", select your created role, and click "Assign".

## Nginx Configuration

You need to configure Nginx to act as a reverse proxy and perform the authentication process. Follow these steps:

### Edit the nginx.conf file

Open the nginx.conf file located in the root directory of this repository.

Replace the "client_id" and "client_secret" with the Client ID and Client Secret created in Keycloak.

```
client_id = "client_id",  // replace with the client id created in Keycloak
client_secret = "client_secret", // replace with the client secret noted from Keycloak
```
### Restart Nginx

Once you have updated the `nginx.conf` file, save the changes and restart Nginx using the following command in your terminal:

```shell
docker-compose down && docker-compose up
```

## Testing the Application

1. After completing the setup and starting the application, open a web browser and navigate to `http://localhost:8080`.
2. You should be redirected to the Keycloak login page. Enter the credentials of the user you created in Keycloak.
3. After successful authentication, you should be redirected back to the application which now shows the information of the authenticated user.

## Application Structure

This Spring Boot application has the following key classes:

- `Application.kt`: The entry point for the Spring Boot application. The application starts here.
- `SecurityConfig.kt`: This is where the Spring Security configuration is set up. It includes the Keycloak integration and access control settings.
- `KeycloakJwtAuthenticationConverter.kt`: This class is responsible for converting the JWT into an Authentication object. It extracts the username and roles from the JWT issued by Keycloak.
- `KeycloakRealmRolesGrantedAuthoritiesConverter.kt`: This class converts the roles embedded in the JWT under "realm_access.roles" into a collection of GrantedAuthority. The roles are prefixed by the `authorityPrefix` (default to "") to be used by Spring Security for authorization checks.
- `RestController.kt`: Provides the endpoints for the REST API. This example includes an API that returns the information of the authenticated user.


---

Congratulations on securing your Spring Boot application with Keycloak and Nginx!

Now you can focus on building amazing features with the peace of mind that your application is secure and scalable. Happy coding!
