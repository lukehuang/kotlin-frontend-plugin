package org.jetbrains.kotlin.gradle.frontend.karma

import org.gradle.api.logging.*
import org.gradle.api.tasks.*
import org.jetbrains.kotlin.gradle.frontend.util.*
import java.io.*

open class RunKarmaSingleTask : Exec() {

    @get:Input
    val sourceMaps: Boolean
        get() = project.frontendExtension.sourceMaps

    @get:Nested
    val extension: KarmaExtension by lazy { project.extensions.getByType(KarmaExtension::class.java) }

    @get:OutputFile
    @get:Optional
    val report: File by lazy {  project.buildDir.resolve("reports/karma.xml") }

    private fun prepare() {
        val nodePath = nodePath(project, "node").first().absolutePath
        val karmaBinPath = project.buildDir.resolve("node_modules/karma/bin/karma").absolutePath
        val logLevel = when (project.logging.level) {
            LogLevel.DEBUG -> "debug"
            LogLevel.INFO -> "info"
            LogLevel.WARN -> "warn"
            LogLevel.ERROR -> "error"
            LogLevel.LIFECYCLE -> "error"
            LogLevel.QUIET -> "disable"
            null -> "info"
        }

        executable(nodePath)
        args(karmaBinPath, "start", "--single-run", "--log-level", logLevel)
        workingDir(project.buildDir)
    }

    @TaskAction
    override fun exec() {
        prepare()
        super.exec()
    }
}
