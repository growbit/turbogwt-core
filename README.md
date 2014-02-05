Turbo GWT (*TurboG*) Core
==

**Turbo GWT** is a suite of libs, inspired by famous Javascript MVVM frameworks like Knockout.js and Angular.js, intended to speed up development of GWT applications grounded on the MVP pattern.

**Turbo GWT Core** contains JavaScriptObject extensions intended to improve performance of any GWT application. These classes are reused across other TurboG modules.

Highlights
==

* [JsArrayList](https://github.com/growbit/turbogwt-core/blob/master/src/main/java/org/turbogwt/core/client/JsArrayList.java) - List implementation wrapping JsArray. Take your json array from request and wrap it directly into a List. No iterations. Simple as <code>new JsArrayList\<T\>(jsArray)</code>.
* [JavaScriptObjects](https://github.com/growbit/turbogwt-core/blob/master/src/main/java/org/turbogwt/core/client/JavaScriptObjects.java) - Easy boxing and unboxing JS native values and much more.
* [JsMap](https://github.com/growbit/turbogwt-core/blob/master/src/main/java/org/turbogwt/core/client/JsMap.java) - Fast map from String to Objects.

Latest Release
==

```
<dependency>
    <groupId>org.turbogwt.core</groupId>
    <artifactId>turbogwt-core</artifactId>
    <version>0.1.0</version>
</dependency>
```
