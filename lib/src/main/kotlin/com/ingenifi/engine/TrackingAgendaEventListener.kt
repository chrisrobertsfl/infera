package com.ingenifi.engine

import org.kie.api.event.rule.*

open class TrackingAgendaEventListener(
    var numberOfRulesFired: Int = 0,
    var trackedRules: List<TrackedRule> = mutableListOf<TrackedRule>()
) : AgendaEventListener {

    override fun matchCreated(event: MatchCreatedEvent?) {
    }

    override fun matchCancelled(event: MatchCancelledEvent?) {
        TODO("Not yet implemented")
    }

    override fun beforeMatchFired(event: BeforeMatchFiredEvent?) {
    }

    override fun afterMatchFired(event: AfterMatchFiredEvent?) {
        numberOfRulesFired += 1
        val name = event?.match?.rule?.name.orEmpty()
        trackedRules += TrackedRule(numberOfRulesFired, name)
    }

    override fun agendaGroupPopped(event: AgendaGroupPoppedEvent?) {
        TODO("Not yet implemented")
    }

    override fun agendaGroupPushed(event: AgendaGroupPushedEvent?) {
        TODO("Not yet implemented")
    }

    override fun beforeRuleFlowGroupActivated(event: RuleFlowGroupActivatedEvent?) {
        TODO("Not yet implemented")
    }

    override fun afterRuleFlowGroupActivated(event: RuleFlowGroupActivatedEvent?) {
        TODO("Not yet implemented")
    }

    override fun beforeRuleFlowGroupDeactivated(event: RuleFlowGroupDeactivatedEvent?) {
        TODO("Not yet implemented")
    }

    override fun afterRuleFlowGroupDeactivated(event: RuleFlowGroupDeactivatedEvent?) {
        TODO("Not yet implemented")
    }
}

class DoNothingAgendaEventListener(numberOfRulesFired: Int = 0, trackedRules: List<TrackedRule> = mutableListOf<TrackedRule>()) : TrackingAgendaEventListener() {
    override fun afterMatchFired(event: AfterMatchFiredEvent?) {
    }
}