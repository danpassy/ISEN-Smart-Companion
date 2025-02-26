/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.provider;

import org.gradle.api.Incubating;
import org.gradle.api.provider.Provider;

import java.util.Map;

public interface MapPropertyInternal<K, V> {
    /**
     * Adds a map entry to the property value.
     *
     * <p>
     * When invoked on a property with no value, this method first sets the value
     * of the property to its current convention value, if set, or an empty map.
     * </p>
     *
     * @param key the key
     * @param value the value
     */
    @Incubating
    void insert(K key, V value);

    /**
     * Adds a map entry to the property value.
     *
     * <p>The given provider will be queried when the value of this property is queried.
     * <p>
     * When invoked on a property with no value, this method first sets the value
     * of the property to its current convention value, if set, or an empty map.
     * </p>
     * <p>Even if the given provider has no value, after this method is invoked,
     * the actual value of this property is guaranteed to be present.</p>
     *
     * @param key the key
     * @param providerOfValue the provider of the value
     */
    @Incubating
    void insert(K key, Provider<? extends V> providerOfValue);

    /**
     * Adds all entries from another {@link Map} to the property value.
     *
     * <p>
     * When invoked on a property with no value, this method first sets the value
     * of the property to its current convention value, if set, or an empty map.
     * </p>
     *
     * @param entries a {@link Map} containing the entries to add
     */
    @Incubating
    void insertAll(Map<? extends K, ? extends V> entries);

    /**
     * Adds all entries from another {@link Map} to the property value.
     *
     * <p>The given provider will be queried when the value of this property is queried.
     *
     * <p>
     * When invoked on a property with no value, this method first sets the value
     * of the property to its current convention value, if set, or an empty map.
     * </p>
     * <p>Even if the given provider has no value, after this method is invoked,
     * the actual value of this property is guaranteed to be present.</p>
     *
     * @param provider the provider of the entries
     */
    @Incubating
    void insertAll(Provider<? extends Map<? extends K, ? extends V>> provider);
}
