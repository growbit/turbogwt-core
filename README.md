Turbo GWT (*TurboG*) Core [![Build Status](https://travis-ci.org/growbit/turbogwt-core.svg?branch=master)](https://travis-ci.org/growbit/turbogwt-core)
==
[**Turbo GWT**](http://github.com/growbit/turbogwt) is a suite of libs intended to speed up development of GWT applications. It aims to promote a fluent and enjoyable programming.

**Turbo GWT Core** contains JavaScriptObject extensions for performing common operations faster in production. These classes are reused across other TurboG modules.

## Highlights

### Native Utilities
* [Overlays](http://growbit.github.io/turbogwt-core/javadoc/apidocs/org/turbogwt/core/js/Overlays.html) - Easy boxing and unboxing JS native values and more.
 
With Overlays, you can avoid using JSNI for most common operations:
```java
    class Person extends JavaScriptObject {

        protected Person() {
        }

        public Long getId() {
            return Overlays.boxPropertyAsLong(this, "id");
        }

        public String getName() {
            return Overlays.getPropertyAsString(this, "name");
        }

        public Integer getAge() {
            return Overlays.boxPropertyAsInteger(this, "age");
        }

        public Double getWeight() {
            return Overlays.boxPropertyAsDouble(this, "weight");
        }

        public Boolean isActive() {
            return Overlays.boxPropertyAsBoolean(this, "active");
        }

        public void setId(Long id) {
            Overlays.unboxValueToProperty(this, "id", id);
        }

        public void setName(String name) {
            Overlays.setValueToProperty(this, "name", name);
        }

        public void setAge(Integer age) {
            Overlays.unboxValueToProperty(this, "age", age);
        }

        public void setWeight(Double weight) {
            Overlays.unboxValueToProperty(this, "weight", weight);
        }

        public void setActive(Boolean active) {
            Overlays.unboxValueToProperty(this, "active", active);
        }
    }
```

### Native Collections
* [JsArrayList](https://github.com/growbit/turbogwt-core/blob/master/src/main/java/org/turbogwt/core/js/collections/JsArrayList.java) - List implementation wrapping native JS array. Take your json array from request and wrap it directly into a List. No iterations. Simple as <code>new JsArrayList(jsArray)</code>. It works with any object type (not only JavaScriptObjects)!
* [JsArrayIterator](http://growbit.github.io/turbogwt-core/javadoc/apidocs/org/turbogwt/core/js/collections/JsArrayIterator.html) - Iterator supporting JsArray.
* [JsArray](http://growbit.github.io/turbogwt-core/javadoc/apidocs/org/turbogwt/core/js/collections/JsArray.html) - New JsArray supporting any object type.
* [JsMap](http://growbit.github.io/turbogwt-core/javadoc/apidocs/org/turbogwt/core/js/collections/JsMap.html) - Fast map from String to Objects.
* [JsFastMap](http://growbit.github.io/turbogwt-core/javadoc/apidocs/org/turbogwt/core/js/collections/JsFastMap.html) - Fast Map<String,T> implementation on a simple javascript object.
* [JsFastSet](http://growbit.github.io/turbogwt-core/javadoc/apidocs/org/turbogwt/core/js/collections/JsFastSet.html) - Fast Set<T> implementation on a simple javascript object. Use it with caution! It assumes that t.toString().equals(otherT.toString()) is equivalent to t.equals(otherT).
* [JsHashSet](http://growbit.github.io/turbogwt-core/javadoc/apidocs/org/turbogwt/core/js/collections/JsHashSet.html) - Set<T> implementation on a ligthweight hash table (simple js object) using object properties to store the hash codes.

### Misc
* [Registration](http://growbit.github.io/turbogwt-core/javadoc/apidocs/org/turbogwt/core/util/Registration.html) - Inheriting from HandlerRegistration, it aims to represent the result of any registration (bind) operation (not only for events).
* [ProvidesValue](http://growbit.github.io/turbogwt-core/javadoc/apidocs/org/turbogwt/core/util/ProvidesValue.html) - Analogous to the ProvidesKey, there is a generic ProvidesValue and pre-defined value providers for Boolean, Date, Number and Text.

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
    <version>0.3.0</version>
</dependency>
```

## Thanks to
* [Thomas Broyer](https://plus.google.com/u/0/+ThomasBroyer) for contributing with [JsCollections](http://code.google.com/p/gwt-in-the-air/source/browse/#svn%2Ftrunk%2Fsrc%2Fnet%2Fltgt%2Fgwt%2Fjscollections%2Fclient%253Fstate%253Dclosed).

## License
Turbo GWT Core is freely distributable under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html)
