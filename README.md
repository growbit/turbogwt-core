Turbo GWT (*TurboG*) Core
==
**Turbo GWT** is a suite of libs intended to speed up development of GWT applications. It aims to promote a fluent and enjoyable programming.

**Turbo GWT Core** contains JavaScriptObject extensions for performing common operations faster in production. These classes are reused across other TurboG modules. Also it offers a HTTP module, nicknamed *Requestory*, with a convenient API for making requests.

## Highlights

### Native Utilities
* [JsArrayList](https://github.com/growbit/turbogwt-core/blob/master/src/main/java/org/turbogwt/core/js/collections/client/JsArrayList.java) - List implementation wrapping native JS array. Take your json array from request and wrap it directly into a List. No iterations. Simple as <code>JsArrayList.of(jsArray)</code>. It works with any object type (not only JavaScriptObjects)!
* [Overlays](https://github.com/growbit/turbogwt-core/blob/master/src/main/java/org/turbogwt/core/js/client/Overlays.java) - Easy boxing and unboxing JS native values and much more.
* [JsMap](https://github.com/growbit/turbogwt-core/blob/master/src/main/java/org/turbogwt/core/js/collections/client/JsMap.java) - Fast map from String to Objects.
 
### *REQUESTORY* - TurboG HTTP module
TurboG proposes a new fluent way of making http requests. It fits better the REST style communication. 
Just look how simple you can get a book from server:

```java
requestory.request(Void.class, Book.class)
        .path("server").segment("books").segment(1)
        .get(new AsyncCallback<Book>() {
            @Override
            public void onFailure(Throwable caught) {
        
            }
        
            @Override
            public void onSuccess(Book result) {
                Window.alert("My book title: " + result.getTitle());
            }
});
```

For serializing/deserializing this object you just need to create this simple SerDes.

```java 
public class BookSerdes extends JsonObjectSerdes<Book> {

    @Override
    public Book readJson(JsonRecordReader reader, DeserializationContext context) {
        return new Book(reader.readInteger("id"),
                reader.readString("title"),
                reader.readString("author"));
    }

    @Override
    public void writeJson(Book book, JsonRecordWriter writer, SerializationContext context) {
        writer.writeInt("id", book.getId())
                .writeString("title", book.getTitle())
                .writeString("author", book.getAuthor());
    }
}
```

One more configuration step: just remember to register your SerDes in the Requestory.
<br />
If you are using *Overlays*, then you don't need any SerDes, *serialization/deserialization is automatic*!

Doing a POST is as simple as:

```java 
requestory.request(Book.class, Void.class).path("server").segment("books")
        .post(new Book(1, "My Title", "My Author"), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(Void result) {
                Window.alert("POST done!");
            }
        });
```

If you are too lazy, Requestory provides **shortcut methods** to performing requests with only one method call. 
The above could be done like this:

```java 
requestory.post("/server/books", Book.class, new Book(1, "My Title", "My Author"), Void.class, 
        new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(Void result) {
                Window.alert("POST done!");
            }
        });
```

With FluentRequests you can also set callbacks for specific responses, with specificity priority.

```java 
requestory.request().path(uri)
        .on(20, new SingleCallback() {
            @Override
            public void onResponseReceived(Request request, Response response) {
                Window.alert("200, 201, ..., 209");
            }
        })
        .on(2, new SingleCallback() {
            @Override
            public void onResponseReceived(Request request, Response response) {
                Window.alert("210, 211, ..., 299");
                // 200 - 209 responses won't reach here because you set a callback for the 20 dozen.
            }
        })
        .get(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Some bad thing happened!");
            }
            @Override
            public void onSuccess(Void result) {
                // Won't reach here. 
                // Only 200-299 responses call onSuccess, and you have already set callbacks for those.
            }
        });
```

#### Extensible design
All Requests are created by an underlying abstraction called Server. This Server interface is analogous to the JDBC Datasource and provides a new ServerConnection by calling getConnection(). This design allows you to determine how you want to communicate with your Server over all your application.

E.g., suppose you are creating a mobile application and want to prevent data loss by poor connection. You can create a new implementation of Server that stores the data on the browser's phone if no internet connection is availble, and sync the data when the signal is back.

The default implementation of Server (ServerImpl) creates the ServerConnectionImpl (default implementation of ServerConnection), which performs the communication by directly creating a request using RequestBuilder and sending it. The binding is done via DefferedBinding. 

#### Finally
Take a look at the [tests](https://github.com/growbit/turbogwt-core/tree/master/src/test/java/org/turbogwt/core/http/client) for more examples and read the [docs](http://growbit.github.io/turbogwt-core/javadoc/apidocs/index.html)!

## Documentation
* [Javadocs](http://growbit.github.io/turbogwt-core/javadoc/apidocs/index.html)

## Community
* [Turbo GWT Google Group](http://groups.google.com/d/forum/turbogwt) - Share ideas and ask for help.

## Downloads
Turbo GWT Core is currently available at maven central.

### Maven
```
<dependency>
    <groupId>org.turbogwt.core</groupId>
    <artifactId>turbogwt-core</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Thanks to
* [Thomas Broyer](https://plus.google.com/u/0/+ThomasBroyer) for contributing with [JsCollections](http://code.google.com/p/gwt-in-the-air/source/browse/#svn%2Ftrunk%2Fsrc%2Fnet%2Fltgt%2Fgwt%2Fjscollections%2Fclient%253Fstate%253Dclosed).

## License
Turbo GWT Core is freely distributable under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html)
