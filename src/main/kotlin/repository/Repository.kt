package repository

abstract class Repository(baseEndpoint: String) {
    init {
        js("process.env.REACT_APP_LIFEHACKER_URL")
    }
}