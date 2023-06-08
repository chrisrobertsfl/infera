package com.ingenifi

import com.ingenifi.RuleResource.*
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.slf4j.LoggerFactory
import java.util.stream.Stream


class EngineTest {

    val logger = LoggerFactory.getLogger(EngineTest::class.java)

    @ParameterizedTest(name = "{index} - RuleResource: {0}, Expected Greeting: {1}")
    @ArgumentsSource(RuleResourceProvider::class)
    fun `RuleResource should contain expected Greeting`(ruleResource: RuleResource, name: String) {
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
    fun `Initialized engine should contain inserted Greeting fact`() {
        val engine = Engine(facts = listOf(Greeting("inserted")))
        val facts = engine.retrieveFacts()
        assertAll(
            { facts.size shouldBe 1 },
            { facts[0] shouldBe Greeting("inserted") }
        )
    }

    @Test
    fun `Engine should contain inserted Greeting fact during rule execution`() {
        val engine = Engine()
        val facts = engine.executeRules(listOf(Greeting("inserted"))).retrieveFacts()
        assertAll(
            { facts.size shouldBe 1 },
            { facts[0] shouldBe Greeting("inserted") }
        )
    }

    @Test
    fun `Engine should contain no facts during initialization`() {
        Engine().retrieveFacts().shouldBeEmpty()
    }

    @Test
    fun `Engine should contain no facts during rule execution`() {
        Engine().executeRules().retrieveFacts().shouldBeEmpty()
    }

    @Test
    fun `Engine should have single fired rule`() {
        val engine = Engine(
            ruleResources = listOf(StringResource("rule \"fired\" when then end")),
            facts = listOf(Greeting("show fact")),
            options = listOf(Option.TRACK_RULES, Option.SHOW_FACTS)
        )
        engine.executeRules().firedRules().count() shouldBe 1
    }

}

data class Greeting(val name: String)
