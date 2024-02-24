# Using Redis as Session Store - Best practices

This guide outlines essential strategies for session management using Spring and Redis, emphasizing simple, best practices.

The objective is to highlight the benefits of using sessions for analytics.

The repository is organized into branches, each providing functional code for specific scenarios. Branches are sequentially numbered, allowing for a logical progression that increases in _sophistication_.

This guide assumes you have foundational knowledge of the Spring framework and concepts such as Sessions and Session Store.

This guide is designed to focus more on Redis, offering insights and practical knowledge on its functionalities and best practices.
It does not specifically target the Spring framework, although Spring may be referenced in context.
The content aims to enhance understanding and skills in utilizing Redis effectively within various development scenarios.

## Stack
The technology stack utilized for this demonstration encompasses a range of tools and frameworks specifically chosen for their robustness and compatibility.

The components include:
- JDK 17, serving as the foundation with its extensive feature set and performance optimizations.
- Spring Boot 3.2.3, facilitating rapid application development.
  - Spring Security 6.2.2, offering comprehensive security features to protect application access and data.
  - Spring Data Redis 3.2.3, providing seamless integration with Redis for efficient data management and caching solutions.
- Redis Stack 7.2, delivering advanced capabilities for handling data structures (such as JSON), caching, streaming and message brokering with high performance.

## The Code

You can probably find the code used in this first demo everywhere on the internet.
The only thing worth mentioning is the application.yml file where Redis is set as Session store.

```yaml
spring:
  data:
    redis:
      port: 6379
      host: localhost
  session:
    store-type: redis
  security:
    user:
      password: password
      name: user
      roles: ["admin"]
```

The rest of the code is pretty standard for MVC and Security configuration in Spring.

An interesting part is the Security configuration where define which resources requires authentication and which not, as shown below:

```java
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((authorize) -> authorize
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((formLogin) -> formLogin
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout
                        .permitAll()
                )
                .build();
    }
```

The code is designed to be self-explanatory, particularly with its clear indentation:
1. All requests require authentication, except those requesting static resources and error handling.
2. Access to the login resource is permitted.
3. Access to the logout resource is also allowed.

## Action

First, Docker will be used to initiate Redis Stack.

```Bash
docker run -d --name=redis-spring-session -p 6379:6379 redis/redis-stack-server:7.2.0-v8
```

Then, the Spring Boot application can be launched either from your IDE or the command line, according to preference.
Upon completion, the following link should be accessed in a web browser:
- [http://localhost:8080/home](http://localhost:8080/home)

The login page will display as follows:

![Login page](images/login-page.png)

Enter `user` as the username and `password` as the password, then click the `Log in` button.

The next page will display a list of session attributes:

![Attributes list](images/attribute-list.png)


Up to this point, everything should be working correctly. Now, let's examine the data stored in Redis.

RedisInsight can be downloaded for free from the specified website:

- [https://redis.com/redis-enterprise/redis-insight/](https://redis.com/redis-enterprise/redis-insight/)

After installation, launch RedisInsight and connect it to the Redis instance using localhost and port 6379.

The view should resemble the following:

![Attributes list](images/redisinsight-session-view.png)

One key will be associated with the Spring Session ID, encompassing the session along with all its attributes.
Initially, the details may not be entirely clear.
It may be possible to infer the class of a specific attribute and some of its properties.
This lack of clarity is due to the attributes being stored in a binary serialization format, making them not fully readable.  

To identify the user associated with a session, a closer inspection of its values is necessary.
This task remains kind of manageable with a single session. However, with hundreds or thousands of sessions, locating a specific user's session becomes _challenging_.
The session data may require a more accessible format or improved serialization to facilitate better analytics.

Addressing this need for enhanced session data representation will be among the initial improvements in the next iteration or branch.
