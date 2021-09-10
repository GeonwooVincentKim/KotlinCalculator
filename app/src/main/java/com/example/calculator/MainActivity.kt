package com.example.calculator

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private val expressionTextView: TextView by lazy {
        findViewById<TextView>(R.id.expressionTextView)
    }

    private val resultTextView: TextView by lazy {
        findViewById<TextView>(R.id.resultTextView)
    }

    private val historyLayout: View by lazy {
        findViewById<View>(R.id.historyLayout)
    }

    private val historyLinearLayout: View by lazy {
        findViewById<View>(R.id.historyLinearLayout)
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
            R.id.multiplyButton -> operatorButtonClicked("×")
            R.id.divideButton -> operatorButtonClicked("÷")
            R.id.remainderButton -> operatorButtonClicked("%")
        }
    }

    private fun numberButtonClicked(number: String): Unit {
        if (isOperator) {
            expressionTextView.append(" ")
        }

        isOperator = false

        val splitValue = expressionTextView.text.split(" ")

        if (splitValue.isNotEmpty() && splitValue.last().length >= 15) {
            Toast.makeText(this, "15자리 까지만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return
        } else if (splitValue.last().isEmpty() && number == "0") {
            Toast.makeText(this, "0은 제일 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        expressionTextView.append(number)

        // TODO Function for put the calculated result real time into resultTextView
        resultTextView.text = calculateExpression()
    }

    @SuppressLint("SetTextI18n")
    private fun operatorButtonClicked(operator: String): Unit {
        if (expressionTextView.text.isEmpty()) {
            return
        }

        when {
            isOperator -> {
                val text = expressionTextView.text.toString()
                expressionTextView.text = text.dropLast(1) + operator
            }

            hasOperator -> {
                makeTextShort("연산자는 한 번만 사용할 수 있습니다.")
                return
            }

            else -> {
                expressionTextView.append(" $operator")
            }
        }

        val ssb = SpannableStringBuilder(expressionTextView.text)
        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.green)),
            expressionTextView.text.length - 1,
            expressionTextView.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        expressionTextView.text = ssb

        isOperator = true
        hasOperator = true
    }

    fun clearButtonClicked(view: android.view.View) {
        expressionTextView.text = ""
        resultTextView.text = ""

        isOperator = false
        hasOperator = false
    }

    fun historyButtonClicked(view: android.view.View) {
        historyLayout.isVisible = true

        // TODO - Bring all of histories from the DataBase
        // TODO - Allocates all of histories into View
    }

    private fun calculateExpression(): String {
        val expressionTexts = expressionTextView.text.split(" ")

        if (hasOperator.not() || expressionTexts.size != 3) {
            return ""
        } else if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            return ""
        }

        val exp1 = expressionTexts[0].toBigInteger()
        val exp2 = expressionTexts[2].toBigInteger()
        val op = expressionTexts[1]

        return when (op) {
            "+" -> (exp1 + exp2).toString()
            "-" -> (exp1 - exp2).toString()
            "×" -> (exp1 * exp2).toString()
            "÷" -> (exp1 / exp2).toString()
            "%" -> (exp1 % exp2).toString()
            else -> ""
        }
    }


    fun resultButtonClicked(view: android.view.View) {
        val expressionTexts = expressionTextView.text.split(" ")

        /* There is no more to operates values */
        if (expressionTextView.text.isEmpty() || expressionTexts.size == 1) {
            return
        }

        /* Input Number and operators and did not input last Number */
        if (expressionTexts.size != 3 && hasOperator) {
            makeTextShort("아직 완성되지 않은 수식입니다")
            return
        }

        if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            makeTextShort("오류가 발생했습니다")
            return
        }

        val expressionText = expressionTextView.text.toString()
        val resultText = calculateExpression()

        resultTextView.text = ""
        expressionTextView.text = resultText

        isOperator = false
        hasOperator = false
    }

    // Extracted function
    private fun makeTextShort(text: CharSequence): Unit {
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
    }

    fun closeHistoryButtonClicked(view: android.view.View) {
        historyLayout.isVisible = false
    }

    fun historyClearButtonClicked(view: android.view.View) {
        // TODO - Delete all of History from all of DataBase
        // TODO - Delete all of History from all of View
    }
}

fun String.isNumber(): Boolean {
    return try {
        this.toBigInteger()
        true
    } catch (e: NumberFormatException) {
        false
    }
}
