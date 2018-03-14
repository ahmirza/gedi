/**
 * 
 *    Copyright 2017 Florian Erhard
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 */
/*
 * Copyright 2003,2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.cglib.proxy;

import java.lang.reflect.Method;

/**
 * Map methods of subclasses generated by {@link Enhancer} to a particular
 * callback. The type of the callbacks chosen for each method affects
 * the bytecode generated for that method in the subclass, and cannot
 * change for the life of the class.
 */
public interface CallbackFilter {
    /**
     * Map a method to a callback.
     * @param method the intercepted method
     * @return the index into the array of callbacks (as specified by {@link Enhancer#setCallbacks}) to use for the method, 
     */
    int accept(Method method);

    /**
     * The <code>CallbackFilter</code> in use affects which cached class
     * the <code>Enhancer</code> will use, so this is a reminder that
     * you should correctly implement <code>equals</code> and
     * <code>hashCode</code> for custom <code>CallbackFilter</code>
     * implementations in order to improve performance.
    */
    boolean equals(Object o);
}
