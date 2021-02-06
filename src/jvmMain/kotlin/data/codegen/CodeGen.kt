package data.codegen

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import data.codegen.generators.ItemGen
import data.codegen.generators.ItemSetGen
import data.codegen.generators.SocketBonusGen

object CodeGen {
    private val mapper = ObjectMapper().registerKotlinModule()

    val outPath: String = "${System.getProperty("user.dir")}/src/commonMain/kotlin/"

    fun <T> load(file: String, type: TypeReference<List<T>>): List<T> {
        // Load
        val data = CodeGen::class.java.getResourceAsStream(file).readAllBytes().decodeToString()
        return mapper.readValue(data, type)
    }

    fun generate() {
        SocketBonusGen.generate()
        ItemSetGen.generate()
        ItemGen.generate()
    }
}
