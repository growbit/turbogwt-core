/*
 * Copyright 2014 Grow Bit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.turbogwt.core.http.books;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.xml.client.impl.DOMParseException;

import java.util.Collection;

import org.turbogwt.core.http.serialization.DeserializationContext;
import org.turbogwt.core.http.serialization.Serdes;
import org.turbogwt.core.http.serialization.SerializationContext;
import org.turbogwt.core.http.serialization.UnableToDeserializeException;

/**
 * @author Danilo Reinert
 */
public class BookXmlSerdes implements Serdes<Book> {

    public static final String[] CONTENT_TYPE_PATTERNS = new String[]{"*/xml", "*/*+xml", "*/xml+*"};

    /**
     * Method for accessing type of Objects this deserializer can handle.
     *
     * @return The class which this deserializer can deserialize
     */
    @Override
    public Class<Book> handledType() {
        return Book.class;
    }

    /**
     * Informs the content type this serializer serializes.
     *
     * @return The content type serialized.
     */
    @Override
    public String[] contentType() {
        return CONTENT_TYPE_PATTERNS;
    }

    /**
     * Serialize T to plain text.
     *
     * @param book    The object to be serialized
     * @param context Context of the serialization
     *
     * @return The object serialized.
     */
    @Override
    public String serialize(Book book, SerializationContext context) {
        StringBuilder xmlBuilder = buildXml(book);
        return xmlBuilder.toString();
    }

    /**
     * Serialize a collection of T to plain text.
     *
     * @param c       The collection of the object to be serialized
     * @param context Context of the serialization
     *
     * @return The object serialized.
     */
    @Override
    public String serializeFromCollection(Collection<Book> c, SerializationContext context) {
        StringBuilder xmlBuilder = new StringBuilder("<books type=\"array\">");
        for (Book book : c) {
            xmlBuilder.append(buildXml(book));
        }
        xmlBuilder.append("</books>");
        return xmlBuilder.toString();
    }

    /**
     * Informs the content type this serializer handle.
     *
     * @return The content type handled by this serializer.
     */
    @Override
    public String[] accept() {
        return CONTENT_TYPE_PATTERNS;
    }

    /**
     * Deserialize the plain text into an object of type T.
     *
     * @param response Http response body content
     * @param context  Context of deserialization
     *
     * @return The object deserialized
     */
    @Override
    public Book deserialize(String response, DeserializationContext context) {
        Document xml;
        try {
            xml = XMLParser.parse(response);
        } catch (DOMParseException e) {
            throw new UnableToDeserializeException("Could not read response as xml.", e);
        }

        return parseXmlDocumentAsBook(xml);
    }

    /**
     * Deserialize the plain text into an object of type T.
     *
     * @param collectionType The class of the collection
     * @param response       Http response body content
     * @param context        Context of deserialization
     *
     * @return The object deserialized
     */
    @Override
    public <C extends Collection<Book>> C deserializeAsCollection(Class<C> collectionType, String response,
                                                                  DeserializationContext context) {
        C col;
        try {
            col = context.getContainerFactoryManager().getFactory(collectionType).get();
        } catch (Exception e) {
            throw new UnableToDeserializeException("There's not factory registered for type *" + collectionType + "*.");
        }

        Document xml;
        try {
            xml = XMLParser.parse(response);
        } catch (DOMParseException e) {
            throw new UnableToDeserializeException("Could not read response as xml.", e);
        }

        NodeList nodes = xml.getElementsByTagName("book");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            col.add(parseXmlDocumentAsBook(node.getOwnerDocument()));
        }

        return col;
    }

    private StringBuilder buildXml(Book book) {
        StringBuilder xmlBuilder = new StringBuilder("<book>");
        xmlBuilder.append("<id>").append(book.getId()).append("</id>");
        xmlBuilder.append("<title>").append(book.getTitle()).append("</title>");
        xmlBuilder.append("<author>").append(book.getAuthor()).append("</author>");
        xmlBuilder.append("</book>");
        return xmlBuilder;
    }

    private Book parseXmlDocumentAsBook(Document xml) {
        String id = xml.getElementsByTagName("id").item(0).getNodeValue();
        String title = xml.getElementsByTagName("title").item(0).getNodeValue();
        String author = xml.getElementsByTagName("author").item(0).getNodeValue();
        return new Book(Integer.valueOf(id), title, author);
    }
}
