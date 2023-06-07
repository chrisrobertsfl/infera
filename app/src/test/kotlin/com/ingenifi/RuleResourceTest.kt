package com.ingenifi

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RuleResourceTest {

    @Test
    fun `Given a valid rule content, when creating a StringResource, then the resource should not be null`() {
        val stringResource = StringResource("rule \"Test\" when then end")
        assertNotNull(stringResource.resource, "StringResource should not be null")
    }

    @Test
    fun `Given a valid rule content and a custom target path generator, when creating a StringResource, then the resource should not be null and the target path should match the custom one provided`() {
        val customTargetPathGenerator: (String) -> String = { _ -> "custom-rule.drl" }
        val stringResource = StringResource("rule \"Test\" when then end", customTargetPathGenerator)
        assertAll(
            { assertNotNull(stringResource.resource, "StringResource should not be null") },
            { assertEquals("custom-rule.drl", stringResource.resource.targetPath, "Target path should match the custom one provided") }
        )
    }

    @Test
    fun `Given a valid file path, when creating a FileResource, then the resource should not be null`() {
        val validFilePath = "src/test/resources/test.drl"
        val fileResource = FileResource(validFilePath)
        assertNotNull(fileResource.resource, "FileResource should not be null")
    }

    @Test
    fun `Given a valid classpath, when creating a ClasspathResource, then the resource should not be null`() {
        val validClasspath = "rules/test.drl"
        val classpathResource = ClasspathResource(validClasspath)
        assertNotNull(classpathResource.resource, "ClasspathResource should not be null")
    }
}
