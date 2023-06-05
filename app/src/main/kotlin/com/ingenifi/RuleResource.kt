package com.ingenifi

import org.kie.api.io.Resource
import org.kie.internal.io.ResourceFactory
import java.io.StringReader
import java.util.*

sealed interface RuleResource {
    val resource: Resource
}

data class StringResource(
    val string: String,
    val targetPathGenerator: (String) -> String = DefaultTargetPathGenerator.GENERATE_TARGET_PATH
) : RuleResource {
    override val resource: Resource = ResourceFactory.newReaderResource(StringReader(string)).apply {
        targetPath = targetPathGenerator(string)
    }
}

data class FileResource(val filepath: String) : RuleResource {
    override val resource: Resource = ResourceFactory.newFileResource(filepath)
}

data class ClasspathResource(val classpath: String) : RuleResource {
    override val resource: Resource = ResourceFactory.newClassPathResource(classpath)
}

object DefaultTargetPathGenerator {
    val GENERATE_TARGET_PATH: (String) -> String = { _ -> "rule-${UUID.randomUUID()}.drl" }
}
