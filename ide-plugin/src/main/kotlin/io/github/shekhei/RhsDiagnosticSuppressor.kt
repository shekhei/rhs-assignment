package io.github.shekhei

import org.jetbrains.kotlin.diagnostics.Diagnostic
import org.jetbrains.kotlin.resolve.diagnostics.DiagnosticSuppressor

class RhsDiagnosticSuppressor: DiagnosticSuppressor {
    override fun isSuppressed(diagnostic: Diagnostic): Boolean {
        return false
    }
}