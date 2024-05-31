package com.example.calculator

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable

class CalculatorViewModel : ViewModel() {

    private val _equationText = MutableLiveData("0")
    val equationText: MutableLiveData<String> = _equationText

    private val _resultText = MutableLiveData("0")
    val resultText: MutableLiveData<String> = _resultText

    fun onButtonClicked(button: String) {

        _equationText.value?.let{
            when (button) {
                "C" -> {
                    if (it.isNotEmpty()) {
                        _equationText.value = it.dropLast(1)
                    }
                }
                "AC" -> {
                    _equationText.value = ""
                    _resultText.value = "0"
                }
                "=" -> {
                    try {
                        val result = evaluateExpression(it)
                        _resultText.value = result
                        
                    } catch (e: Exception) {
                        Log.e("CalculatorViewModel", "Error evaluating expression", e)
                        _resultText.value = "Error"
                    }
                }
                else -> {
                    if (it == "0") {
                        _equationText.value = button
                    } else {
                        _equationText.value = it + button
                    }
                }
            }

            Log.d("CalculatorViewModel", "Equation: ${_equationText.value}")
        }
    }
}

fun evaluateExpression(expression: String): String {
    val context = Context.enter()
    context.optimizationLevel = -1
    val scriptable: Scriptable = context.initStandardObjects()
    val result = context.evaluateString(scriptable, expression, "javascript", 1, null)
    return Context.toString(result)
}