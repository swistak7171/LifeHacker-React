package configuration

external val process: Process

interface Process {
    val env: dynamic
}