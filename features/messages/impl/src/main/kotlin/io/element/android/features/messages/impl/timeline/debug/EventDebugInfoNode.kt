/*
 * Copyright (c) 2023 New Vector Ltd
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

package io.element.android.features.messages.impl.timeline.debug

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.plugin.Plugin
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.element.android.anvilannotations.ContributesNode
import io.element.android.libraries.architecture.NodeInputs
import io.element.android.libraries.architecture.inputs
import io.element.android.libraries.di.RoomScope
import io.element.android.libraries.matrix.api.core.EventId
import io.element.android.libraries.matrix.api.timeline.item.TimelineItemDebugInfo

@ContributesNode(RoomScope::class)
class EventDebugInfoNode @AssistedInject constructor(
    @Assisted buildContext: BuildContext,
    @Assisted plugins: List<Plugin>,
) : Node(buildContext, plugins = plugins) {

    data class Inputs(
        val eventId: EventId,
        val timelineItemDebugInfo: TimelineItemDebugInfo,
    ) : NodeInputs

    private val inputs = inputs<Inputs>()

    private fun onBackPressed() {
        navigateUp()
    }

    @Composable
    override fun View(modifier: Modifier) = with(inputs) {
        EventDebugInfoView(
            eventId = eventId,
            model = timelineItemDebugInfo.model,
            originalJson = timelineItemDebugInfo.originalJson,
            latestEditedJson = timelineItemDebugInfo.latestEditedJson,
            onBackPressed = ::onBackPressed
        )
    }
}
