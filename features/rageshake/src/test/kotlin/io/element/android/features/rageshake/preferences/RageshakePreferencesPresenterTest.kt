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

@file:OptIn(ExperimentalCoroutinesApi::class)

package io.element.android.features.rageshake.preferences

import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RageshakePreferencesPresenterTest {
    @Test
    fun `present - initial state available`() = runTest {
        val presenter = RageshakePreferencesPresenter(
            FakeRageShake(isAvailableValue = true),
            FakeRageshakeDataStore(isEnabled = true)
        )
        moleculeFlow(RecompositionClock.Immediate) {
            presenter.present()
        }.test {
            skipItems(1)
            val initialState = awaitItem()
            assertThat(initialState.isSupported).isTrue()
            assertThat(initialState.isEnabled).isTrue()
        }
    }

    @Test
    fun `present - initial state not available`() = runTest {
        val presenter = RageshakePreferencesPresenter(
            FakeRageShake(isAvailableValue = false),
            FakeRageshakeDataStore(isEnabled = true)
        )
        moleculeFlow(RecompositionClock.Immediate) {
            presenter.present()
        }.test {
            skipItems(1)
            val initialState = awaitItem()
            assertThat(initialState.isSupported).isFalse()
            assertThat(initialState.isEnabled).isTrue()
        }
    }

    @Test
    fun `present - enable and disable`() = runTest {
        val presenter = RageshakePreferencesPresenter(
            FakeRageShake(isAvailableValue = true),
            FakeRageshakeDataStore(isEnabled = true)
        )
        moleculeFlow(RecompositionClock.Immediate) {
            presenter.present()
        }.test {
            skipItems(1)
            val initialState = awaitItem()
            assertThat(initialState.isEnabled).isTrue()
            initialState.eventSink.invoke(RageshakePreferencesEvents.SetIsEnabled(false))
            assertThat(awaitItem().isEnabled).isFalse()
            initialState.eventSink.invoke(RageshakePreferencesEvents.SetIsEnabled(true))
            assertThat(awaitItem().isEnabled).isTrue()
        }
    }

    @Test
    fun `present - set sensitivity`() = runTest {
        val presenter = RageshakePreferencesPresenter(
            FakeRageShake(isAvailableValue = true),
            FakeRageshakeDataStore(isEnabled = true)
        )
        moleculeFlow(RecompositionClock.Immediate) {
            presenter.present()
        }.test {
            skipItems(1)
            val initialState = awaitItem()
            assertThat(initialState.sensitivity).isEqualTo(A_SENSITIVITY)
            initialState.eventSink.invoke(RageshakePreferencesEvents.SetSensitivity(A_SENSITIVITY + 1f))
            assertThat(awaitItem().sensitivity).isEqualTo(A_SENSITIVITY + 1f)
        }
    }
}
