package com.ingenifi.engine

import org.kie.api.event.rule.*

open class TrackingAgendaEventListener(var trackedRules: List<TrackedRule> = mutableListOf()) : AgendaEventListener {

    override fun matchCreated(event: MatchCreatedEvent?) {
    }

    override fun matchCancelled(event: MatchCancelledEvent?) {
        TODO("Not yet implemented")
    }

    override fun beforeMatchFired(event: BeforeMatchFiredEvent?) {
    }

    override fun afterMatchFired(event: AfterMatchFiredEvent?) {
        trackedRules += TrackedRule(trackedRules.size + 1, event?.match?.rule?.name.orEmpty())
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

    fun clear() {
        trackedRules = mutableListOf()
    }
}

class DoNothingAgendaEventListener(numberOfRulesFired: Int = 0, trackedRules: List<TrackedRule> = mutableListOf<TrackedRule>()) : TrackingAgendaEventListener() {
    override fun afterMatchFired(event: AfterMatchFiredEvent?) {
    }
}