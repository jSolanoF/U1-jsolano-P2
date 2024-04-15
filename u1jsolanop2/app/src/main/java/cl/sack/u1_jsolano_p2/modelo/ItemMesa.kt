package cl.sack.u1_jsolano_p2.modelo

class ItemMesa (val itemMenu: ItemMenu, var cantidad: Int) {
    fun calcularSubtotal(): Int {
        return itemMenu.precio.toInt() * cantidad
    }
}
