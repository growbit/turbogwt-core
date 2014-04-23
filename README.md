Turbo GWT (*TurboG*) Core
==
**Turbo GWT** is a suite of libs intended to speed up development of GWT applications. It aims to promote a fluent and enjoyable programming.

**Turbo GWT Core** contains JavaScriptObject extensions for performing common operations faster in production. These classes are reused across other TurboG modules. Also it offers a HTTP module, nicknamed *Requestor*, with a convenient API for making requests.

## Highlights

### Native Utilities
* [Overlays](https://github.com/growbit/turbogwt-core/blob/master/src/main/java/org/turbogwt/core/js/Overlays.java) - Easy boxing and unboxing JS native values and much more.

### Native Collections
* [JsArrayList](https://github.com/growbit/turbogwt-core/blob/master/src/main/java/org/turbogwt/core/js/collections/JsArrayList.java) - List implementation wrapping native JS array. Take your json array from request and wrap it directly into a List. No iterations. Simple as <code>JsArrayList.of(jsArray)</code>. It works with any object type (not only JavaScriptObjects)!
* [JsMap](https://github.com/growbit/turbogwt-core/blob/master/src/main/java/org/turbogwt/core/js/collections/JsMap.java) - Fast map from String to Objects.

### Misc
* [Registration](https://github.com/growbit/turbogwt-core/blob/master/src/main/java/org/turbogwt/core/util/Registration.java) - Inheriting from HandlerRegistration, it aims to represent the result of any registration (bind) operation (not only for events).
* Analogous to the ProvidesKey, there is a generic ProvidesValue and pre-defined value providers for Boolean, Date, Number and Text.

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
    <version>0.2.0</version>
</dependency>
```

## Thanks to
* [Thomas Broyer](https://plus.google.com/u/0/+ThomasBroyer) for contributing with [JsCollections](http://code.google.com/p/gwt-in-the-air/source/browse/#svn%2Ftrunk%2Fsrc%2Fnet%2Fltgt%2Fgwt%2Fjscollections%2Fclient%253Fstate%253Dclosed).

## License
Turbo GWT Core is freely distributable under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html)
