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
class Engine(ruleResources: List<RuleResource> = emptyList(), facts: List<Any> = emptyList()) {
    private val logger = LoggerFactory.getLogger(Engine::class.java)
    private var session: KieSession

    init {
        session = initializeKieSession(ruleResources)
        insertFacts(facts)
    }

    fun retrieveFacts(predicate: (Any) -> Boolean = { true }): List<Any> = session.objects.filter(predicate)

    fun executeRules(facts: List<Any> = emptyList()): Engine {
        logger.info("Executing rules")
        insertFacts(facts)
        session.fireAllRules()
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