package br.com.rabbitmqkotlinlib.rabbitmq.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

@JsonIgnoreProperties(ignoreUnknown = true)
class Message(
    val id: String,
    val body: String,
){
    companion object {
        fun parse(message: String): Message {
            var mapper = jacksonObjectMapper()
            lateinit var messageObj: Message
            try {
                messageObj = mapper.readValue(message)
            }
            catch (err: JsonProcessingException){
                println("Error on parsing message to object")
            }
            return messageObj
        }
    }

}