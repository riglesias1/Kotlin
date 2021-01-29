import java.text.DecimalFormat

fun main(){
    // Indicaciones de datos
    print("Ingrese su salario mensual bruto: $")
    val salarioBruto= readLine()!!.toDouble()

    print("Impuesto mensual (porcentaje): %")
    val impuesto= readLine()!!.toDouble()

    // Calculo de impuesto mensual y anual
    val impuestoMensual= (impuesto * salarioBruto) / 100
    val impuestoAnual= impuestoMensual * 12

    // Calculo de salario mensual y anual
    val salarioMensual= salarioBruto - impuestoMensual
    val salarioAnual= salarioMensual * 12

    val formato= "#.##" // Formato decimal a imprimir

    print("""${"\n"}
        
        *** Datos de Salida ***
        - Salario mensual neto: $${DecimalFormat(formato).format(salarioMensual)}
        - Impuestos a pagar por mes: $${DecimalFormat(formato).format(impuestoMensual)}
        
        - Salario anual neto: $${DecimalFormat(formato).format(salarioAnual)}
        - Impuestos a pagar por año: $${DecimalFormat(formato).format(impuestoAnual)}
        
    """.trimIndent())
}
