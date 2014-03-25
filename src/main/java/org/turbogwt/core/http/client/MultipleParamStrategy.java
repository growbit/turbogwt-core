package org.turbogwt.core.http.client;

import com.google.gwt.http.client.URL;

/**
 * Class that defines how multiple values will be appended to the URI along with its param name.
 */
public abstract class MultipleParamStrategy {

    public static MultipleParamStrategy REPEATED_PARAM = new RepeatedParamStrategy();
    public static MultipleParamStrategy COMMA_SEPARATED = new CommaSeparatedStrategy();

    /**
     * Construct URI part from gives values.
     *
     * @param separator the separator of parameters from current URI part
     * @param name      the parameter name
     * @param values    the parameter value(s), each object will be converted to a {@code String} using its {@code
     *                  toString()} method.
     *
     * @return URI part
     */
    public abstract String asUriPart(String separator, String name, Object... values);

    /**
     * Assert that the value is not null or empty.
     *
     * @param value   the value
     * @param message the message to include with any exceptions
     *
     * @throws IllegalArgumentException if value is null
     */
    protected void assertNotNullOrEmpty(String value, String message) throws IllegalArgumentException {
        if (value == null || value.length() == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static class RepeatedParamStrategy extends MultipleParamStrategy {

        /**
         * Construct encoded URI part from gives values.
         *
         * @param separator the separator of parameters from current URI part
         * @param name      the parameter name
         * @param values    the parameter value(s), each object will be converted to a {@code String} using its {@code
         *                  toString()} method.
         *
         * @return encoded URI part
         */
        @Override
        public String asUriPart(String separator, String name, Object... values) {
            assertNotNullOrEmpty(name, "Parameter name cannot be null or empty.");
            String uriPart = "";
            String sep = "";
            for (Object value : values) {
                String strValue = value.toString();
                assertNotNullOrEmpty(strValue, "Parameter value of *" + name
                        + "* null or empty. You must inform a valid value");

                uriPart += sep + URL.encodeQueryString(name) + "=" + URL.encodeQueryString(strValue);
                sep = separator;
            }
            return uriPart;
        }
    }

    public static class CommaSeparatedStrategy extends MultipleParamStrategy {

        /**
         * Construct encoded URI part from gives values.
         *
         * @param separator the separator of parameters from current URI part
         * @param name      the parameter name
         * @param values    the parameter value(s), each object will be converted to a {@code String} using its {@code
         *                  toString()} method.
         *
         * @return encoded URI part
         */
        @Override
        public String asUriPart(String separator, String name, Object... values) {
            assertNotNullOrEmpty(name, "Parameter name cannot be null or empty.");
            String uriPart = URL.encodeQueryString(name) + "=";
            String sep = "";
            for (Object value : values) {
                String strValue = value.toString();
                assertNotNullOrEmpty(strValue, "Parameter value of *" + name
                        + "* null or empty. You must inform a valid value");

                uriPart += sep + URL.encodeQueryString(strValue);
                sep = ",";
            }
            return uriPart;
        }
    }
}
