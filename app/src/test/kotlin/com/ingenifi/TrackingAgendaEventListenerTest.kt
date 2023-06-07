package com.ingenifi

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kie.api.event.rule.AfterMatchFiredEvent

class TrackingAgendaEventListenerTest {

    @Test
    fun `Should add TrackedRule entry`() {
        var listener = TrackingAgendaEventListener()
        val event: AfterMatchFiredEvent = mockk()
        every { event.match.rule.name } returns "hello"
        listener.afterMatchFired(event)
        assertAll(
            { assertEquals(1, listener.numberOfRulesFired) },
            { assertEquals(listOf(TrackedRule(1, "hello")), listener.trackedRules) }
        )
    }
}

