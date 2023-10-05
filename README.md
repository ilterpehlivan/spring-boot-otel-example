# spring-boot-otel-example
Simple spring boot application with open telemetry


# Test

1. Create user
```bash
echo -n '{"name": "ilter","email":"example@com"}' | http localhost:8080/users 
```
2. Create movie

```bash
echo -n '{"title": "test movie","director":"test director","genre":"dram"}' | http localhost:8081/movies
```
3. Add movie to user's list

```bash
http PUT localhost:8080/users/{userId}/movie/{movieId} 
```
> userId from step 1 and movieId from step 2

4. Get user details

```bash
http localhost:8080/users/{userId}
```