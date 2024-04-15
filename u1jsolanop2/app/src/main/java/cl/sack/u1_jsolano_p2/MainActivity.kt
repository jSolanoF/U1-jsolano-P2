package cl.sack.u1_jsolano_p2

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cl.sack.u1_jsolano_p2.modelo.CuentaMesa
import cl.sack.u1_jsolano_p2.modelo.ItemMenu
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var cuentaMesa: CuentaMesa
    private var etCantidadPastel: EditText? = null
    private var etCantidadCazuela: EditText? = null
    private var tvPrecioPastel: TextView? = null
    private var tvPrecioCazuela: TextView? = null
    private var swPropina: Switch? = null
    private var tvValTotal: TextView? = null
    private var tvValPropina: TextView? = null
    private var tvValComida: TextView? = null

    val formatoMonedaChilena = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    val pastelChoclo = "Pastel de Choclo"
    val valorPastel = "12000"
    val cazuela = "Cazuela"
    val valorcazuela = "10000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialización de las vistas
        cuentaMesa = CuentaMesa(1)
        etCantidadPastel = findViewById<EditText>(R.id.etCantPastelChoclo)
        etCantidadCazuela = findViewById<EditText>(R.id.etCantCazuela)
        tvPrecioPastel = findViewById<TextView>(R.id.tvPrecioPastel)
        tvPrecioCazuela = findViewById<TextView>(R.id.tvPrecioCazuela)
        swPropina = findViewById<Switch>(R.id.swPropina)
        tvValTotal = findViewById<TextView>(R.id.tvValTotal)
        tvValPropina = findViewById<TextView>(R.id.tvValPropina)
        tvValComida = findViewById<TextView>(R.id.tvValComida)
        actualizarTotales()


        swPropina?.setOnCheckedChangeListener { _, isChecked ->
            cuentaMesa.aceptaPropina  = isChecked
            actualizarTotales()
        }

        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                actualizarTotales()
            }
        }
        etCantidadPastel?.addTextChangedListener(textWatcher)
        etCantidadCazuela?.addTextChangedListener(textWatcher)
    }

    fun actualizarTotales() {   // Función para actualizar todos los totales
        cuentaMesa.limpiarItems()
        // Obtener las cantidades ingresadas para pastel de choclo y cazuela
        val cantPastelDeChoclo = etCantidadPastel?.text.toString().toIntOrNull() ?: 0
        val cantCazuela = etCantidadCazuela?.text.toString().toIntOrNull() ?: 0
        // Agregar los productos a la cuenta de mesa
        cuentaMesa.agregarItem(ItemMenu(pastelChoclo,  valorPastel), cantPastelDeChoclo)
        cuentaMesa.agregarItem(ItemMenu(cazuela,  valorcazuela), cantCazuela)
        // Actualizar los diferentes totales en la interfaz de usuario
        subTotal()
        propina()
        precioTotal()
        precioPorProducto()
    }

    // Función para actualizar la visualización de la propina
    fun propina() {
        if (swPropina?.isChecked == true) { // Verificar si el switch de propina está activado
            val propina = cuentaMesa.calcularPropina()  // Calcular y mostrar la propina
                                                       // solo si está activado
            tvValPropina?.text = formatoMonedaChilena.format(propina)
        } else {
            tvValPropina?.text = formatoMonedaChilena.format(0)// Si el switch está
                                                                      // desactivado, mostrar
                                                                     // la propina como cero
        }
    }

    fun precioTotal(){  // Función para actualizar el precio total en la interfaz de usuario
        val total = cuentaMesa.calcularTotalConPropina()
        tvValTotal?.setText(total.toString())
        tvValTotal?.text = formatoMonedaChilena.format(total)
    }

    // Función para actualizar el subtotal (total sin propina) en la interfaz de usuario
    fun subTotal(){
        val valorComida = cuentaMesa.calcularTotalSinPropina()
        tvValComida?.setText(valorComida.toString())
        tvValComida?.text = formatoMonedaChilena.format(valorComida)
    }

    // Función para actualizar el precio de cada producto en la interfaz de usuario
    fun precioPorProducto(){
        val cantPastelDeChoclo = etCantidadPastel?.text.toString().toIntOrNull() ?: 0
        val cantCazuela = etCantidadCazuela?.text.toString().toIntOrNull() ?: 0
        val totalValorPastel = cantPastelDeChoclo *  valorPastel.toInt()
        val totalValorCazuela = cantCazuela *  valorcazuela.toInt()
        tvPrecioPastel?.setText(totalValorPastel.toString())
        tvPrecioCazuela?.setText(totalValorCazuela.toString())
        tvPrecioPastel?.text = formatoMonedaChilena.format(totalValorPastel)
        tvPrecioCazuela?.text = formatoMonedaChilena.format(totalValorCazuela)

    }
}



