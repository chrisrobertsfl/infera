package com.ingenifi

import org.kie.api.KieServices
import org.kie.api.builder.KieBuilder
import org.kie.api.builder.KieFileSystem
import org.kie.api.builder.KieRepository
import org.kie.api.runtime.KieSession
import org.slf4j.LoggerFactory

/**
 * Engine class responsible for executing rules and retrieving facts.
 */
class Engine(
    ruleResources: List<RuleResource> = emptyList(),
    facts: List<Any> = emptyList(),
    val options: List<Option> = emptyList()
) {
    private var trackingAgendaEventListener: TrackingAgendaEventListener
    private var session: KieSession
    private val logger = LoggerFactory.getLogger(Engine::class.java)

    init {
        session = initializeKieSession(ruleResources)
        trackingAgendaEventListener = if (options.contains(Option.TRACK_RULES))
            TrackingAgendaEventListener()
        else
            DoNothingAgendaEventListener()
        session.addEventListener(trackingAgendaEventListener)
        insertFacts(facts)
    }

    fun retrieveFacts(predicate: (Any) -> Boolean = { true }): List<Any> = session.objects.filter(predicate)

    fun executeRules(facts: List<Any> = emptyList()): Engine {
        logger.debug("Executing rules")
        trackingAgendaEventListener.let { session.addEventListener(it) }
        insertFacts(facts)
        session.fireAllRules()
        if (options.contains(Option.TRACK_RULES)) {
            logger.info("Rules fired:")
            trackingAgendaEventListener.trackedRules.forEach { it -> logger.info("{}. {}", it.position, it.name) }
        }
        if (options.contains(Option.SHOW_FACTS)) {
            logger.info("Facts:")
            retrieveFacts().forEach { it -> logger.info(it.toString()) }
        }
        return this
    }

    private fun insertFacts(facts: List<Any>): Engine {
        facts.forEach { session.insert(it) }
        return this
    }

    private fun initializeKieSession(ruleResources: List<RuleResource>): KieSession {
        val kieServices: KieServices = KieServices.Factory.get()
        val kieFileSystem: KieFileSystem = kieServices.newKieFileSystem().apply {
            ruleResources.map { it.resource }.forEach { write(it) }
        }
        val kieRepository: KieRepository = kieServices.repository
        kieRepository.addKieModule { kieRepository.defaultReleaseId }
        val kieBuilder: KieBuilder = kieServices.newKieBuilder(kieFileSystem).apply { buildAll() }
        return kieServices.newKieContainer(kieBuilder.kieModule.releaseId).newKieSession()
    }

}