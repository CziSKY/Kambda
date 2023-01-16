package ink.kirraobj.kambda

import java.util.*


interface Expr {

    fun reduce(): Expr

    fun apply(v: Val, expr: Expr): Expr
}

class Val(private val x: String, val uuid: UUID = UUID.randomUUID()) : Expr {

    override fun reduce() = this

    override fun apply(v: Val, expr: Expr): Expr {
        if (this == v) {
            return expr
        }
        return this
    }

    override fun toString() = x

    override fun equals(other: Any?): Boolean {
        if (other is Val) {
            return uuid == other.uuid
        }
        return false
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + uuid.hashCode()
        return result
    }
}

class Fun(val x: Val, val expr: Expr) : Expr {

    override fun reduce() = this

    override fun apply(v: Val, expr: Expr): Expr {
        if (v == x) {
            return this
        }
        return Fun(x, expr.apply(v, expr))
    }

    constructor(x: String, expr: Expr) : this(Val(x), expr)

    override fun toString() = "(λ$x. $expr)"
}

class Apply(private val f: Expr, private val x: Expr) : Expr {

    override fun reduce(): Expr {
        val reduce = f.reduce()
        if (reduce is Fun) {
            return reduce.expr.apply(reduce.x, x).reduce()
        }
        return Apply(reduce, x)
    }

    override fun apply(v: Val, expr: Expr): Expr {
        return Apply(f.apply(v, expr), x.apply(v, expr))
    }

    override fun toString() = "($f $x)"
}