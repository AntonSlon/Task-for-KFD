import kotlin.NumberFormatException
import kotlin.random.Random


object Terminal{
    private var coefficientsMap: MutableMap<String, Float> = mutableMapOf("RUB/USD" to 90f, "RUB/EUR" to 100f, "USD/EUR" to 1.1f,
        "USD/USDT" to 1f, "USD/BTC" to 1800000f)
    private var currencyMap: MutableMap<String, Float> = mutableMapOf("RUB" to 10_000f, "USD" to 1000f,
        "EUR" to 1000f, "USDT" to 1000f, "BTC" to 1.5f)
    private val changerMap = mapOf(1 to "RUB/USD", 2 to "RUB/EUR", 3 to "USD/EUR",
        4 to "USD/USDT", 5 to "USD/BTC")

    private fun coefficientsChanger(pairs: Int){
        val mapSign = mapOf(0 to "+", 1 to "-")
        val coefficient =  Random.nextInt(0,5)
        coefficientsMap[changerMap[pairs]!!] = coefficientsMap[changerMap[pairs]]!! + (mapSign[Random.nextInt(0, 1)].toString() + coefficient.toString()).toFloat()
        println("${(mapSign[Random.nextInt(0, 2)].toString() + coefficient.toString()).toFloat()} - изменение коэффициента")
    }


    fun operation(pairs: Int, amountOfMoney: Float, currency: String) {
        val value = changerMap[pairs]!!
        var currency1 = ""
        var currency2 = ""
        var flag = false

        for (i in value){
            if ((!flag) and (i != '/')) {currency1+=i}
            else if (i == '/') {flag = true}
            else {currency2+=i}
        }

        when (currency){
            currency1 -> {
                if (MyWallet.money[currency1]!! - amountOfMoney < 0){throw ValueException("Недостаточно средств на балансе")}
                if (currencyMap[currency1]!! - amountOfMoney * coefficientsMap[value]!! < 0){throw ValueException("Недостаточно средств на счете банка")}
                MyWallet.money[currency2] = MyWallet.money[currency2]!! - amountOfMoney
                MyWallet.money[currency1] = MyWallet.money[currency1]!! + amountOfMoney * coefficientsMap[value]!!
                currencyMap[currency1] = currencyMap[currency1]!! - amountOfMoney * coefficientsMap[value]!!
                coefficientsChanger(pairs)
            }
            currency2 -> {
                if (MyWallet.money[currency1]!! - amountOfMoney < 0){throw ValueException("Недостаточно средств на балансе")}
                if (currencyMap[currency2]!! - amountOfMoney / coefficientsMap[value]!! < 0){throw ValueException("Недостаточно средств на счете банка")}
                MyWallet.money[currency1] = MyWallet.money[currency1]!! - amountOfMoney
                MyWallet.money[currency2] = MyWallet.money[currency2]!! + amountOfMoney / coefficientsMap[value]!!
                currencyMap[currency2] = currencyMap[currency2]!! - amountOfMoney / coefficientsMap[value]!!
                coefficientsChanger(pairs)
            }
        }
    }

    fun getBalance(){
        println(currencyMap)
    }
}


class ValueException(textOfException: String) : Exception(){
    init {
        println(textOfException)
        MyWallet.getBalance()
        Terminal.getBalance()
    }
}


object MyWallet{
    var money: MutableMap<String, Float> = mutableMapOf("RUB" to 1_000_000f, "USD" to 0f, "EUR" to 0f, "USDT" to 0f, "BTC" to 0f)

    fun getBalance(){
        println(money)
    }
}


fun inputCurrency(){
    println("""|Валютные пары:
            |1 - RUB / USD
            |2 - RUB / EUR
            |3 - USD / EUR
            |4 - USD / USDT
            |5 - USD / BTC""".trimMargin())
    try{
        val pairs = readln().toInt()
        println("Выберите валюту, которую бы вы хотели получить")
        val currency = readln().uppercase()
        println("Введите сумму, которую вы хотите перевести")
        val amountOfMoney = readln().toFloat()
        try {
            Terminal.operation(pairs, amountOfMoney, currency)
        }catch (e: ValueException){
            return
        }
    }catch (e: NumberFormatException){
        println("Неверный формат ввода")
        return
    }

    Terminal.getBalance()
    MyWallet.getBalance()
}


fun main() {
    while (true) {
        inputCurrency()
    }
}