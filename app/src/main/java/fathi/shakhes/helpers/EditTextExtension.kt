package fathi.shakhes.helpers

import android.widget.EditText

/**
 * Returns the {@see EditText} text as Long if conversion succeed; otherwise 0
 */
val EditText.asLong
    get() = try {
        if (!this.hasText) 0L else this.text.toString().toLong()
    } catch (e: NumberFormatException) {
        0L
    }

/**
 * Returns the {@see EditText} text as Int if conversion succeed; otherwise 0
 */
val EditText.asInt
    get() = try {
        if (!this.hasText) 0 else this.text.toString().toInt()
    } catch (e: NumberFormatException) {
        0
    }

/**
 * Returns the {@see EditText} text as Double if conversion succeed; otherwise 0
 */
val EditText.asDouble
    get() = try {
        if (!this.hasText) 0.0 else this.text.toString().toDouble()
    } catch (e: NumberFormatException) {
        0.0
    }


/**
 * Returns the {@see EditText} text as Float if conversion succeed; otherwise 0
 */
val EditText.asFloat
    get() = try {
        if (!this.hasText) 0F else this.text.toString().toFloat()
    } catch (e: NumberFormatException) {
        0F
    }

/**
 * Returns the {@see EditText} text as String
 */
val EditText.asString
    get() = this.text.toString()

val EditText.hasText
    get() = this.asString.isNotBlank()
