package com.ingenifi

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("RuleResource Tests")
class RuleResourceTest {

    @Nested
    @DisplayName("StringResource Tests")
    inner class StringResourceTests {

        @Test
        fun `valid rule content - not null`() {
            val stringResource = StringResource("rule \"Test\" when then end")
            assertNotNull(stringResource.resource)
        }

        @Test
        fun `valid rule content, custom target path generator - not null and target path matches custom one`() {
            val customTargetPathGenerator: (String) -> String = { _ -> "custom-rule.drl" }
            val stringResource = StringResource("rule \"Test\" when then end", customTargetPathGenerator)
            assertAll(
                { assertNotNull(stringResource.resource) },
                { assertEquals("custom-rule.drl", stringResource.resource.targetPath) }
            )
        }
    }

    @Nested
    @DisplayName("FileResource Tests")
    inner class FileResourceTests {

        @Test
        fun `valid file path - not null`() {
            val validFilePath = "src/test/resources/test.drl"
            val fileResource = FileResource(validFilePath)
            assertNotNull(fileResource.resource)
        }
    }

    @Nested
    @DisplayName("ClasspathResource Tests")
    inner class ClasspathResourceTests {

        @Test
        fun `valid classpath - not null`() {
            val validClasspath = "rules/test.drl"
            val classpathResource = ClasspathResource(validClasspath)
            assertNotNull(classpathResource.resource)
        }
    }
}
