package com.ingenifi.engine

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.kie.api.event.rule.AfterMatchFiredEvent

@DisplayName("TrackingAgendaEventListener Unit Tests")
class TrackingAgendaEventListenerTest {

    @Test
    fun `should add TrackedRule entry`() {
        var listener = TrackingAgendaEventListener()
        val event: AfterMatchFiredEvent = mockk()
        every { event.match.rule.name } returns "hello"
        listener.afterMatchFired(event)
        assertAll(
            { assertEquals(1, listener.numberOfRulesFired) },
            { listener.numberOfRulesFired shouldBe 1},
            { listener.trackedRules shouldContainExactly listOf(TrackedRule(1, "hello")) }
        )
    }
}

