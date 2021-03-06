package com.example.calculator

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.room.Room
import com.example.calculator.model.History
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

    private val historyLinearLayout: LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.historyLinearLayout)
    }

    lateinit var db: AppDataBase

    private var isOperator: Boolean = false
    private var hasOperator: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize `DB` Object when create application
        db = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java,
            "historyDB"
        ).build()
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
            R.id.multiplyButton -> operatorButtonClicked("??")
            R.id.divideButton -> operatorButtonClicked("??")
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
            Toast.makeText(this, "15?????? ????????? ????????? ??? ????????????.", Toast.LENGTH_SHORT).show()
            return
        } else if (splitValue.last().isEmpty() && number == "0") {
            Toast.makeText(this, "0??? ?????? ?????? ??? ??? ????????????.", Toast.LENGTH_SHORT).show()
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
                makeTextShort("???????????? ??? ?????? ????????? ??? ????????????.")
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

    @SuppressLint("SetTextI18n")
    fun historyButtonClicked(view: android.view.View) {
        historyLayout.isVisible = true
        historyLinearLayout.removeAllViews()

        Thread(Runnable{
            db.historyDao().getAll().reversed().forEach{
                runOnUiThread{
                    val historyView = LayoutInflater.from(this).inflate(R.layout.history_row, null,false)
                    historyView.findViewById<TextView>(R.id.expressionTextView).text = it.expression
                    historyView.findViewById<TextView>(R.id.resultTextView).text = "= ${it.result}"

                    historyLinearLayout.addView(historyView)
                }
            }
        }).start()

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
            "??" -> (exp1 * exp2).toString()
            "??" -> (exp1 / exp2).toString()
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
            makeTextShort("?????? ???????????? ?????? ???????????????")
            return
        }

        if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            makeTextShort("????????? ??????????????????")
            return
        }

        val expressionText = expressionTextView.text.toString()
        val resultText = calculateExpression()

        // TODO -> Insert Data into DataBase
        Thread(Runnable {
            db.historyDao().insertHistory(History(null, expressionText, resultText))
        }).start()

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
        historyLinearLayout.removeAllViews()

        // TODO - Delete all of History from all of DataBase
        // TODO - Delete all of History from all of View
        Thread(Runnable{
            db.historyDao().deleteAll()
        }).start()
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
