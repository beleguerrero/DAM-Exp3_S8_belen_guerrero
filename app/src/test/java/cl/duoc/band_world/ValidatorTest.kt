package cl.duoc.band_world

import cl.duoc.band_world.utils.Validator
import org.junit.Assert.assertTrue
import  org.junit.Assert.assertFalse
import org.junit.Test


class ValidatorTest {

    @Test
    fun validarCorreoCorrecto() {
        val validator = Validator()
        val res = validator.isEmail("prueba@mail.com")

        assertTrue(res)
    }

    @Test
    fun validarCorreoIncorrecto() {
        val validator = Validator()
        val res = validator.isEmail("prueba")

        assertFalse(res)
    }

    @Test
    fun validarPassCorredto() {
        val validator = Validator()
        val res = validator.isPassValid("pass123")

        assertTrue(res)
    }

    @Test
    fun validarPassIncorrecto(){
        val validator = Validator()
        val res = validator.isPassValid("pass")

        assertFalse(res)
    }
}
