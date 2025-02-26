/*
 * Copyright 2021 the original author or authors.
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

package org.gradle.internal.cc.impl

import org.gradle.internal.cc.impl.models.BuildTreeModel
import org.gradle.internal.buildtree.BuildTreeModelAction
import org.gradle.internal.buildtree.BuildTreeModelCreator


class ConfigurationCacheAwareBuildTreeModelCreator(
    private val delegate: BuildTreeModelCreator,
    private val cache: BuildTreeConfigurationCache
) : BuildTreeModelCreator {
    override fun <T : Any> beforeTasks(action: BuildTreeModelAction<out T>) {
        cache.maybePrepareModel {
            delegate.beforeTasks(action)
        }
    }

    override fun <T : Any?> fromBuildModel(action: BuildTreeModelAction<out T>): T? {
        return cache.loadOrCreateModel {
            val model = delegate.fromBuildModel(action)
            if (model == null) BuildTreeModel.NullModel else BuildTreeModel.Model(model)
        }.result()
    }
}
