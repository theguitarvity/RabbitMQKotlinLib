package br.com.rabbitmqkotlinlib.rabbitmq

import com.fasterxml.jackson.databind.ObjectMapper
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Produces

@ApplicationScoped
class CustomObjectMapper {
    private lateinit var objectMapper: ObjectMapper

    @Produces
    fun objectMapper(): ObjectMapper{
        objectMapper?. let{
            objectMapper = ObjectMapper()
        }
        return objectMapper
    }
}