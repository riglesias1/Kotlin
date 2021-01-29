import java.text.DecimalFormat

val PI = 3.14159265 // Variable global con el valor pi
val formato= "#.##" // Formato decimal a imprimir

fun main() {
    // Indicaciones de datos al usuario
    print("Ingrese el radio del circulo en centímetros: ")
    val radio = readLine()!!.toDouble()

    print("""
        
        *** Centímetros ***
        Área            | ${area(radio)}
        Circunferencia   | ${circunferencia(radio)}
                
        *** Pulgadas ***
        Área            | ${area(radio, 2.54)}
        Circunferencia   | ${circunferencia(radio, 0.155)}
        
    """.trimIndent())

}

// Función de Area
fun area(radio: Double, pulgada: Double= 1.00): String {
    return DecimalFormat(formato).format((radio*radio*PI) / pulgada)
}

// Función de Circunferencia
fun circunferencia(radio: Double, pulgada: Double= 1.00): String {
    return DecimalFormat(formato).format((2*PI*radio) * pulgada)
}