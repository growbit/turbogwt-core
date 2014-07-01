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

package org.turbogwt.core.util.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Utility methods derived from browser's console.
 *
 * @author Danilo Reinert
 */
public final class Console {

    private Console() {
    }

    public static native void log(JavaScriptObject jso) /*-{
        $wnd.console.log(jso);
    }-*/;

    public static native void log(String text) /*-{
        $wnd.console.log(text);
    }-*/;
}
