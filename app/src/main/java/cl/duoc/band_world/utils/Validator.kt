package cl.duoc.band_world.utils

class Validator {
    fun isEmail(email: String): Boolean {
        return Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(email)
    }

    fun isPassValid(pass: String): Boolean {
        val hasLetter = pass.any { it.isLetter() }
        val hasNum = pass.any { it.isDigit() }
        return hasLetter && hasNum
    }
}

