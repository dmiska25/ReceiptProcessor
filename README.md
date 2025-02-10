# Receipt Processor

This is a sample project that processes receipts by applying a set of business rules to determine the number of reward 
points earned. The service is built using Spring Boot and an H2 in-memory database to handle receipt storage while the application is running.

## Running with docker

To run the project with docker, you can use the included `Dockerfile`. This will build
the project with gradle and run the jar file in a docker container. The h2 database I mentioned
is started silently in memory with the application so you don't need to worry about setting it up.
    
```bash
docker build -t receiptprocessor:v1 .
docker run -d --name receipt-server -p 8080:8080 receiptprocessor:v1
```

This will start the server on port 8080. You can access the server by visiting `http://localhost:8080`.

## Usage

### Documentation

The project uses generated swagger documentation to show the available endpoints. You can access the swagger documentation
by visiting `/swagger-ui/index.html#/` after starting the server.

### Endpoints

The project has the following endpoints:

#### Endpoint: Process Receipts

* Path: `/receipts/process`
* Method: `POST`
* Payload: Receipt JSON
* Response: JSON containing an id for the receipt.

Description:

Takes in a JSON receipt and returns a JSON object with an ID generated.

Example Response:
```json
{ "id": "7fb1377b-b223-49d9-a31a-5a02701dd310" }
```

#### Endpoint: Get Points

* Path: `/receipts/{id}/points`
* Method: `GET`
* Response: A JSON object containing the number of points awarded.

A simple Getter endpoint that looks up the receipt by the ID and returns an object specifying the points awarded.

Example Response:
```json
{ "points": 32 }
```

### Testing

The project has unit tests and integration tests. You can run the tests using the following command if you have gradle installed.

```bash
./gradlew test
```

## Trade-offs and discussion

### Language and Framework

I spent a bit of time deciding between using Spring Boot/Kotlin and Go for this project. I decided with Spring Boot/Kotlin
primarily just because its what i'm familiar with and I didn't see significant downsides with using it unless this were to
scale up to a very large number of requests. Though for a personal exercise I looked into what the equivalent would look
like in go and noticed some things that would probably be different from my experience.
1. A lot of the boilerplate code would be gone in go. Which sounds nice, though I think the separation of concerns is nice.
2. I'd probably have to think more about some functions I take for granted in kotlin/java. I'm used to just pulling an off the shelf function for most things.
3. Debugging would probably be a bit easier in go. I ran into a few issues with this solution that I'll mention below.

### Other tools

I switched out the default database management tool Hibernate for JOOQ instead. I made this decision because for a application
that needs to run at scale I think it would be much more beneficial to have direct control over queries that are executed.
Hibernate can be sometimes optimized but it would require much more time and debugging than most likely desirable or necessary.

### Challenges

I ran into a couple issues with this solution. The first was with persisting the parent/children. You'll notice I added
a `receipt` field to the `ItemEntity` class. Hibernate requires that in order to persist the parent/children. This was problematic
because it can cause issues like in the `ReceiptRepositoryIntegrationTest` where I had to deliberately avoid comparing the
items. This is because doing so actually causes a stack overflow error since it triggers a circular reference comparison.
The second issue was with validation. Typically `@Valid` recursively triggers validation but for a reason unknown the
items were not triggering validation so I had to add `@field:Valid` to the `items` field on the `ProcessReceiptRequest`.

### When to calculate the points

I spent some time thinking about weather to calculate the points on the fly or to store them in the database. The main
consideration I had with this is if they might(or probably would) change in the future. I think this is something i'd
typically bring up with the product team what the desired behavior is. For this, I thought it'd probably be best to store
since its probably better to treat them as a point in time value. I'd suspect that in the real world this type of service
would also be used alongside a balance service and if they were calculated on the fly it might be inconsistent with a balance
if that wasn't updated as well.
