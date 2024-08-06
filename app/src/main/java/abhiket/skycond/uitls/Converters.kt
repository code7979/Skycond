package abhiket.skycond.uitls

import java.util.Locale

/**
 *   Celsius to Fahrenheit: ° F = 9/5 (° C) + 32
 *   Kelvin to Celsius: ° C = K - 273.15
 *   Fahrenheit to Kelvin: K = 5/9 (° F - 32) + 273.15
 **/

// Kelvin to Celsius
fun Double.toCelsius(): String {
    return String.format("%d°C", Math.round(this - 273.15))
}

// m/sec to km/h
fun Double.toKmh(): String {
    return String.format(Locale.US, "%.2f Km/h", (this * 3.6))
}