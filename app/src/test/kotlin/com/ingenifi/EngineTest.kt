package com.ingenifi

import com.ingenifi.RuleResource.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

class EngineTest {

    @ParameterizedTest(name = "{index} - Given a RuleResource: {0}, when the engine runs, then it should contain the expected Greeting: {1}")
    @ArgumentsSource(RuleResourceProvider::class)
    fun `Given a RuleResource, when the engine runs, then it should contain the expected Greeting`(ruleResource: RuleResource, name: String) {
        val engine = Engine(ruleResources = listOf(ruleResource))
        engine.executeRules()
        assertTrue(engine.retrieveFacts { it is Greeting }
            .map { it as Greeting }
            .any { it.name == name })
    }


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
    fun `Given a Greeting fact during initialization, when the engine is created, then it should contain the inserted fact`() {
        val engine = Engine(facts = listOf(Greeting("inserted")))
        val facts = engine.retrieveFacts()
        assertAll(
            { assertEquals(1, facts.size, "There should only be one fact inserted") },
            { assertEquals(Greeting("inserted"), facts[0], "Inserted fact should match the expected fact") }
        )
    }

    @Test
    fun `Given a Greeting fact during rule execution, when the engine runs, then it should contain the inserted fact`() {
        val engine = Engine()
        val facts = engine.executeRules(listOf(Greeting("inserted"))).retrieveFacts()
        assertAll(
            { assertEquals(1, facts.size, "There should only be one fact inserted") },
            { assertEquals(Greeting("inserted"), facts[0], "Inserted fact should match the expected fact") }
        )
    }

    @Test
    fun `Given no facts during initialization, when the engine is created, then it should contain no facts`() {
        val engine = Engine()
        val facts = engine.retrieveFacts()
        assertTrue(facts.isEmpty(), "No facts should have been inserted")
    }

    @Test
    fun `Given no facts during rule execution, when the engine runs, then it should contain no facts`() {
        val engine = Engine()
        val facts = engine.executeRules().retrieveFacts()
        assertTrue(facts.isEmpty(), "No facts should have been inserted")
    }
}

data class Greeting(val name: String)
