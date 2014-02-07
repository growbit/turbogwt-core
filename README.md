Turbo GWT (*TurboG*) Core
==
**Turbo GWT** is a suite of libs, inspired by famous Javascript MVVM frameworks like Knockout.js and Angular.js, intended to speed up development of GWT applications grounded on the MVP pattern.

**Turbo GWT Core** contains JavaScriptObject extensions intended to improve performance of any GWT application. These classes are reused across other TurboG modules.

## Highlights
* [JsArrayList](http://growbit.github.io/turbogwt-core/javadoc/apidocs/org/turbogwt/core/client/JsArrayList.html) - List implementation wrapping JsArray. Take your json array from request and wrap it directly into a List. No iterations. Simple as <code>new JsArrayList\<T\>(jsArray)</code>.
* [JavaScriptObjects](http://growbit.github.io/turbogwt-core/javadoc/apidocs/org/turbogwt/core/client/JavaScriptObjects.html) - Easy boxing and unboxing JS native values and much more.
* [JsMap](http://growbit.github.io/turbogwt-core/javadoc/apidocs/index.html) - Fast map from String to Objects.

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
