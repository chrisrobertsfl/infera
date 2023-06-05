package com.ingenifi

import org.kie.api.KieServices
import org.kie.api.builder.KieBuilder
import org.kie.api.builder.KieFileSystem
import org.kie.api.builder.KieRepository
import org.kie.api.runtime.KieSession

/**
 * Engine class responsible for executing rules and retrieving facts.
 */
class Engine(ruleResources: List<RuleResource> = emptyList(), facts: List<Any> = emptyList()) {
    private var session: KieSession

    init {
        session = initializeKieSession(ruleResources)
        insertFacts(facts)
    }

    fun retrieveFacts(predicate: (Any) -> Boolean = { true }): List<Any> = session.objects.filter(predicate)

    fun executeRules(facts: List<Any> = emptyList()): Engine {
        insertFacts(facts)
        return this
    }

    private fun insertFacts(facts: List<Any>) {
        facts.forEach { session.insert(it) }
        session.fireAllRules()
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