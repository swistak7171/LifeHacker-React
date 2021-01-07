package model

import kotlinx.serialization.*

@Serializable
abstract class Model {
    @SerialName("id")
    val id: Long = 0

//    @SerialName("creation_date")
//    val creationDate: LocalDateTime = LocalDateTime.now()
//
//    @SerialName("modification_date")
//    val modificationDate: LocalDateTime = LocalDateTime.now()
}