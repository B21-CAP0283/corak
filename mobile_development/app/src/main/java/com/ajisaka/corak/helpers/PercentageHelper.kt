package com.ajisaka.corak.helpers

object PercentageHelper {

    //without decimal digits
    fun toPercentage(n: Float): String {
        return String.format("%.0f", n * 100) + "%"
    }

    //accept a param to determine the numbers of decimal digits
    fun toPercentage(n: Float, digits: Int): String {
        return String.format("%." + digits + "f", n * 100) + "%"
    }
}