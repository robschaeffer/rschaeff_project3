package com.example.rschaeff_project3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Basic Calculator Class
 *
 * This class acts as a simple calculator in which it takes a left operand, right operand,
 * and an operator, and can perform a calculation based on those values.
 *
 * @property leftOperand This is the value on the left side of the current operation.
 * @property rightOperand This is the value on the left side of the current operation.
 * @property operation This character represents the operation to be performed.
 */
class Calc (leftOperand: Double, rightOperand: Double, operation: Char? = null) {

    private val op = operation
    private val left = leftOperand
    private val right = rightOperand

    /**
     * This is the only public member function in the calculator class, and can be called
     * by the operator buttons on the user end.
     *
     * @return the computed value after the operation
     */
    fun calculate():String {
        var value:String = ""
        when(op) {
            '+'-> { value = add(left,right) }
            '-'-> { value = sub(left,right) }
            '*'-> { value = mul(left,right) }
            '/'-> { value = div(left,right) }
        }
        return value
    }


    /**
     * The following private functions are called appropriately by the calculate()
     * function and perform the operation.
     *
     * @return the result of the calculation as a string
     */
    private fun add(leftOp: Double = left, rightOp: Double = right):String {
        val result:Double = leftOp + rightOp
        return result.toString()
    }

    private fun sub(leftOp: Double, rightOp: Double):String {
        val result:Double = leftOp - rightOp
        return result.toString()
    }

    private fun mul(leftOp: Double, rightOp: Double):String {
        val result:Double = leftOp * rightOp
        return result.toString()
    }

    private fun div(leftOp: Double, rightOp: Double):String {
        val result:Double = leftOp / rightOp
        return result.toString()
    }

}

/**
 * Main Activity
 *
 * This is the main activity for the app. It has all the functionality for
 * input, contacting the calculator class, and reporting errors.
 */
class MainActivity : AppCompatActivity() {

    private var currentValStr:String = ""
    private var resultValStr:String = ""
    private var leftValStr:String = ""
    private var rightValStr:String = ""
    private var currentOperator:Char? = null

    /**
     * Resets all value strings back to empty strings, and the operator to null.
     */
    private fun clear() {
        currentValStr = ""
        resultValStr = ""
        rightValStr = ""
        leftValStr = ""
        currentOperator = null
    }

    /**
     * Updates the textview strings to match the appropriate current values.
     * Since this calculator works with doubles, an even number will be represented
     * with a ".0" at the end, so we remove that so numbers appear more simple for the user.
     */
    private fun updateDisplay(){
        display_current.text = currentValStr
        if (resultValStr != "" && resultValStr.last() == '0' && resultValStr[resultValStr.length-2] == '.') {
            display_result.text = resultValStr.dropLast(2)
        }
        else { display_result.text = resultValStr }
    }

    /**
     * If an error is found, this function is called and the current strings are
     * set to ERROR. The calculator will function normally again once the user
     * clears the values.
     */
    private fun error(){
        currentValStr = "ERROR"
        resultValStr = "ERROR"
    }

    /**
     * Overriding the onCreate() function an setting up listeners for the buttons,
     * and giving them functionality.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * This is the listener for all of our whole number and '.' buttons.
         * When one of these buttons is clicked, the correct character will be added to
         * the currentValStr (string of the current value being entered by the user).
         */
        val listenerNumButtons = View.OnClickListener {
            when(it.id){
                R.id.button_0->{ currentValStr += getString(R.string.button_0) }
                R.id.button_1->{ currentValStr += getString(R.string.button_1) }
                R.id.button_2->{ currentValStr += getString(R.string.button_2) }
                R.id.button_3->{ currentValStr += getString(R.string.button_3) }
                R.id.button_4->{ currentValStr += getString(R.string.button_4) }
                R.id.button_5->{ currentValStr += getString(R.string.button_5) }
                R.id.button_6->{ currentValStr += getString(R.string.button_6) }
                R.id.button_7->{ currentValStr += getString(R.string.button_7) }
                R.id.button_8->{ currentValStr += getString(R.string.button_8) }
                R.id.button_9->{ currentValStr += getString(R.string.button_9) }
                R.id.button_dot->{
                    //If current value already contains a '.', this is an error.
                    if(!currentValStr!!.contains('.')) {
                        currentValStr += getString(R.string.button_dot)
                    }
                    else{ error() }
                }
            }

            if (currentValStr.length > 15){ error() }

            updateDisplay()
        }

        //Set listeners for buttons 0-9 and '.'
        button_0.setOnClickListener(listenerNumButtons)
        button_1.setOnClickListener(listenerNumButtons)
        button_2.setOnClickListener(listenerNumButtons)
        button_3.setOnClickListener(listenerNumButtons)
        button_4.setOnClickListener(listenerNumButtons)
        button_5.setOnClickListener(listenerNumButtons)
        button_6.setOnClickListener(listenerNumButtons)
        button_7.setOnClickListener(listenerNumButtons)
        button_8.setOnClickListener(listenerNumButtons)
        button_9.setOnClickListener(listenerNumButtons)
        button_dot.setOnClickListener(listenerNumButtons)

        /**
         * This is the listener for all of the operator buttons, including '='.
         * When one of these buttons are clicked, if there is a current operation to
         * be performed, it will access the calculator class and get the result,
         * and the current operator and string values will be updated.
         */
        val listenerOpButtons = View.OnClickListener {

            var operatorError:Boolean = false

            //If there is an operation to be performed, we check to see if all formatting is
            //appropriate and we will perform the operation.
            if (currentOperator != null) {
                if (currentValStr == "" || currentValStr == "." || currentValStr == "-" || resultValStr == "") {
                    error()
                    operatorError = true
                }
                else {
                    leftValStr = resultValStr
                    rightValStr = currentValStr
                    val calculation: Calc =
                        Calc(leftValStr.toDouble(), rightValStr.toDouble(), currentOperator)

                    if (currentOperator == '/' && rightValStr.toDouble() == 0.0) {
                        error()
                    } else { resultValStr = calculation.calculate() }
                }
            }
            else{ resultValStr = currentValStr }

            //If there is no operator error above, we will reset the current value,
            //and update the current operator.
            if (!operatorError) {
                when (it.id) {

                    R.id.button_add -> {
                        currentValStr = ""
                        currentOperator = '+'
                    }

                    R.id.button_sub -> {
                        currentValStr = ""
                        currentOperator = '-'
                    }

                    R.id.button_mul -> {
                        currentValStr = ""
                        currentOperator = '*'
                    }

                    R.id.button_div -> {
                        currentValStr = ""
                        currentOperator = '/'
                    }

                    R.id.button_equal -> {
                        currentValStr = ""
                        currentOperator = null
                    }

                }
            }

            updateDisplay()
        }

        button_add.setOnClickListener(listenerOpButtons)
        button_sub.setOnClickListener(listenerOpButtons)
        button_mul.setOnClickListener(listenerOpButtons)
        button_div.setOnClickListener(listenerOpButtons)
        button_equal.setOnClickListener(listenerOpButtons)

        /**
         * This is the listener for the negation and clear buttons (the "other" buttons).
         */
        val listenerOtherButtons = View.OnClickListener {

            when(it.id) {

                //Negates the current value being inputed by the user.
                R.id.button_neg-> { currentValStr = "-$currentValStr" }

                R.id.button_clr-> { clear() }
            }

            updateDisplay()
        }

        button_neg.setOnClickListener(listenerOtherButtons)
        button_clr.setOnClickListener(listenerOtherButtons)
    }
}