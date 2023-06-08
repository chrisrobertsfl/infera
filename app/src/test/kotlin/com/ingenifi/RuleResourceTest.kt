package com.ingenifi

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

@DisplayName("RuleResource Unit Tests")
class RuleResourceTest {

    class RuleResourceProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(StringResource("rule \"Test\" when then end")),
                Arguments.of(FileResource("src/test/resources/test.drl")),
                Arguments.of(ClasspathResource("rules/test.drl"))
            )
        }
    }

    @ParameterizedTest(name = "Should not be null for valid rule's resource: {0}")
    @ArgumentsSource(RuleResourceProvider::class)
    fun `should not be null for valid rule's resource`(ruleResource: RuleResource) {
        ruleResource.resource shouldNotBe null
    }

    @Test
    fun `should not be null and target path should match custom one for valid rule content with custom target path generator`() {
        val customTargetPathGenerator: (String) -> String = { _ -> "custom-rule.drl" }
        val stringResource = StringResource("rule \"Test\" when then end", customTargetPathGenerator)
        assertAll(
            { stringResource.resource.shouldNotBeNull() },
            { stringResource.resource.targetPath shouldBe "custom-rule.drl" }
        )
    }
}
