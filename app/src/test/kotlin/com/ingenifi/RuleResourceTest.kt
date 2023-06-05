package com.ingenifi

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Tests for verifying RuleResource implementations")
class RuleResourceTest {

    @Test
    @DisplayName("Should create StringResource successfully")
    fun `test StringResource creation`() {
        val stringResource = StringResource("rule \"Test\" when then end")
        assertNotNull(stringResource.resource, "StringResource should not be null")
    }

    @Test
    @DisplayName("Should create StringResource with custom target path generator successfully")
    fun `test StringResource creation with custom target path generator`() {
        val customTargetPathGenerator: (String) -> String = { _ -> "custom-rule.drl" }
        val stringResource = StringResource("rule \"Test\" when then end", customTargetPathGenerator)
        assertAll(
            { assertNotNull(stringResource.resource, "StringResource should not be null") },
            { assertEquals("custom-rule.drl", stringResource.resource.targetPath, "Target path should match the custom one provided") }
        )
    }


    @Test
    @DisplayName("Should create FileResource successfully when valid file path is provided")
    fun `test FileResource creation with valid file path`() {
        val validFilePath = "src/test/resources/test.drl"
        val fileResource = FileResource(validFilePath)
        assertNotNull(fileResource.resource, "FileResource should not be null")
    }

    @Test
    @DisplayName("Should create ClasspathResource successfully when valid classpath is provided")
    fun `test ClasspathResource creation with valid classpath`() {
        val validClasspath = "rules/test.drl"
        val classpathResource = ClasspathResource(validClasspath)
        assertNotNull(classpathResource.resource, "ClasspathResource should not be null")
    }
}
