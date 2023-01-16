package ink.kirraobj.kambda

object PlayGround {

    @JvmStatic
    fun main(args: Array<String>) {
        // (λx. x (λx. x)) y
        val expr = Apply(Fun("x", Apply(Val("x"), Fun("x", Val("x")))), Val("y"))
        println(expr)
        println(expr.reduce())
    }
}