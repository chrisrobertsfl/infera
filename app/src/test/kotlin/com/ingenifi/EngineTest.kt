package com.ingenifi

import com.ingenifi.RuleResource.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

@DisplayName("Engine rule execution and fact retrieval tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EngineTest {

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
        private val STRING_DRL = """
            package com.ingenifi
            
            rule "Greeting"
            when
            then
                insert( new Greeting("string") );
            end
        """
        private val FILE_DRL = "src/test/resources/file.drl"
        private val CLASSPATH_DRL = "classpath.drl"
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(StringResource(STRING_DRL), "string"),
                Arguments.of(FileResource(FILE_DRL), "file"),
                Arguments.of(ClasspathResource(CLASSPATH_DRL), "classpath")
            )
        }
    }

    @Test
    @DisplayName("Should insert a single fact successfully during initialization")
    fun `test successful fact insertion during initialization`() {
        val engine = Engine(facts = listOf(Greeting("inserted")))
        val facts = engine.retrieveFacts()
        assertAll(
            { assertEquals(1, facts.size, "There should only be one fact inserted") },
            { assertEquals(Greeting("inserted"), facts[0], "Inserted fact should match the expected fact") }
        )
    }

    @Test
    @DisplayName("Should insert a single fact successfully during rule execution")
    fun `test successful fact insertion during rule execution`() {
        val engine = Engine()
        val facts = engine.executeRules(listOf(Greeting("inserted"))).retrieveFacts()
        assertAll(
            { assertEquals(1, facts.size, "There should only be one fact inserted") },
            { assertEquals(Greeting("inserted"), facts[0], "Inserted fact should match the expected fact") }
        )
    }

    @Test
    @DisplayName("Should handle no facts being inserted during initialization")
    fun `test no fact insertion during initialization`() {
        val engine = Engine()
        val facts = engine.retrieveFacts()
        assertTrue(facts.isEmpty(), "No facts should have been inserted")
    }

    @Test
    @DisplayName("Should handle no facts being inserted during rule execution")
    fun `test no fact insertion during rule execution`() {
        val engine = Engine()
        val facts = engine.executeRules().retrieveFacts()
        assertTrue(facts.isEmpty(), "No facts should have been inserted")
    }
}

data class Greeting(val name: String)
