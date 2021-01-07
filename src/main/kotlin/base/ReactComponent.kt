package base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import react.RComponent
import react.RProps
import react.RState

abstract class ReactComponent<P : RProps, S : RState>(props: P) : RComponent<P, S>(props) {
    private val job: Job = Job()
    protected val componentScope: CoroutineScope = CoroutineScope(job)

    override fun componentWillUnmount() {
        componentScope.cancel()
    }
}