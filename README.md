# quotationsAPI
#### REST API application to manage Quotation

## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/kanthakdominik/quotationsAPI.git
```

**2. Run the app using maven**

```bash
mvn spring-boot:run
```
The app will start running at <http://localhost:8080>

<br /> 

**3. Run the tests using maven**

```bash
mvn -Dtest=QuoteControllerTest test
```

**4. Test the app using curl or Postman**

<br /> 

`Add quote`
```bash
curl -d '{"author": { "name": "Benjamin", "surname": "Franklin"}, "content": "Tell me and I forget. Teach me and I remember. Involve me and I learn." }' -H "Content-Type: application/json" -X POST 'localhost:8080/api/quotes'
```

<br /> 

`Get quote by id = 1`
```bash
curl 'localhost:8080/api/quotes/1'
```
<br /> 

`Delete quote by author`
```bash
curl -X "DELETE" 'localhost:8080/api/quotes/author?name=Benjamin&surname=Franklin'
```

<br /> 

## Explore Rest APIs

The app defines following CRUD API.

### Quotes

| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| GET    | /api/quotes | Get all quotes | |
| GET    | /api/quotes/{id} | Get quote by id | |
| POST   | /api/quotes | Add new quote | [JSON](#quoteCreate) |
| PUT    | /api/quotes/{id} | Update quote by id | [JSON](#quoteUpdate) |
| DELETE | /api/quotes/{id} | Delete quote by id | |
| DELETE | /api/quotes/author?name=name&surname=surname | Delete user by author | |

## Sample Valid JSON Request Bodys

##### <a id="quoteCreate">Add Quote -> /api/quotes</a>
```json
{
    "author": {
        "name": "Albert",
        "surname": "Einstein"
    },
    "content": "Two things are infinite: the universe and human stupidity, and I'm not sure about the universe."
}
```

##### <a id="quoteUpdate">Update Quote -> /api/quote/{id}</a>
```json
{
    "author": {
        "name": "Benjamin",
        "surname": "Franklin"
    },
    "content": "Tell me and I forget. Teach me and I remember. Involve me and I learn."
}
```
