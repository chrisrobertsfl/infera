package com.ingenifi

import com.ingenifi.RuleResource.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.kie.api.KieServices
import org.kie.api.builder.KieBuilder
import org.kie.api.builder.KieFileSystem
import org.kie.api.builder.KieRepository
import org.kie.api.runtime.KieContainer
import org.kie.api.runtime.KieSession
import java.util.stream.Stream

@DisplayName("Tests for verifying Engine's rule execution with different RuleResources")
class EngineTest {
    companion object {
        const val STRING_DRL = """
            package com.ingenifi
            
            rule "Greeting"
            when
            then
                insert( new Greeting("string") );
            end
        """
        const val FILE_DRL = "src/test/resources/file.drl"
        const val CLASSPATH_DRL = "classpath.drl"
    }

    @DisplayName("Given a RuleResource, when engine runs, then it should contain the expected Greeting")
    @ParameterizedTest
    @ArgumentsSource(RuleResourceProvider::class)
    fun `verify greeting from rule resources`(ruleResource: RuleResource, name: String) {
        val engine = Engine(ruleResources = listOf(ruleResource))
        engine.executeRules()
        assertTrue(engine.retrieveFacts { it is Greeting }
            .map { it as Greeting }
            .any { it.name == name })
    }

    @DisplayName("Provider for different types of RuleResources")
    class RuleResourceProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(StringResource(STRING_DRL), "string"),
                Arguments.of(FileResource(FILE_DRL), "file"),
                Arguments.of(ClasspathResource(CLASSPATH_DRL), "classpath")
            )
        }
    }
}

class Engine(private val ruleResources: List<RuleResource>) {
    private lateinit var session: KieSession

    fun retrieveFacts(predicate: (Any) -> Boolean): List<Any> = session.objects.filter(predicate)

    fun executeRules() {
        val services: KieServices = KieServices.Factory.get()
        val fileSystem: KieFileSystem = services.newKieFileSystem().apply {
            ruleResources.map { it.resource }
                .forEach { write(it) }
        }
        val repository: KieRepository = services.repository
        repository.addKieModule { repository.defaultReleaseId }
        val builder: KieBuilder = services.newKieBuilder(fileSystem)
        builder.buildAll()
        val module = builder.kieModule
        val container: KieContainer = services.newKieContainer(module.releaseId)
        session = container.newKieSession()
        session.fireAllRules()
    }
}

data class Greeting(val name: String)
