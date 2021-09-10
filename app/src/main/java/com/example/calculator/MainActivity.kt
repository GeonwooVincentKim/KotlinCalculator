package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private val expressionTextView: TextView by lazy{
        findViewById<TextView>(R.id.expressionTextView)
    }

    private val resultTextView: TextView by lazy{
        findViewById<TextView>(R.id.resultTextView)
    }

    private var isOperator: Boolean = false
    private var hasOperator: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun buttonClicked(view: android.view.View) {
        when (view.id) {
            R.id.number0Button -> numberButtonClicked("0")
            R.id.number1Button -> numberButtonClicked("1")
            R.id.number2Button -> numberButtonClicked("2")
            R.id.number3Button -> numberButtonClicked("3")
            R.id.number4Button -> numberButtonClicked("4")
            R.id.number5Button -> numberButtonClicked("5")
            R.id.number6Button -> numberButtonClicked("6")
            R.id.number7Button -> numberButtonClicked("7")
            R.id.number8Button -> numberButtonClicked("8")
            R.id.number9Button -> numberButtonClicked("9")

            R.id.plusButton -> operatorButtonClicked("+")
            R.id.minusButton -> operatorButtonClicked("-")
            R.id.multiplyButton -> operatorButtonClicked("*")
            R.id.divideButton -> operatorButtonClicked("/")
            R.id.remainderButton -> operatorButtonClicked("%")
        }
    }

    private fun numberButtonClicked(number: String): Unit {
        if(isOperator){
            expressionTextView.append(" ")
        }

        isOperator = false

        val splitValue = expressionTextView.text.split(" ")

        if (splitValue.isNotEmpty() && splitValue.last().length >= 15){
            Toast.makeText(this, "15자리 까지만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return
        } else if (splitValue.last().isEmpty() && number == "0"){
            Toast.makeText(this, "0은 제일 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        expressionTextView.append(number)

        // TODO Function for put the calculated result real time into resultTextView
    }

    private fun operatorButtonClicked(operator: String): Unit {

    }

    fun clearButtonClicked(view: android.view.View) {

    }

    fun historyButtonClicked(view: android.view.View) {

    }

    fun resultButtonClicked(view: android.view.View) {

    }
}