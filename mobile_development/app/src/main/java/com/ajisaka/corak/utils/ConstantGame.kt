package com.ajisaka.corak.utils

import android.content.Context
import android.content.res.Resources
import android.provider.Settings.Global.getString
import com.ajisaka.corak.R
import com.ajisaka.corak.ui.games.gtn.Question

object ConstantGame {

    // TODO (STEP 1: Create a constant variables which we required in the result screen.)
    // START
    const val USER_NAME: String = "user_name"
    const val TOTAL_QUESTIONS: String = "total_questions"
    const val CORRECT_ANSWERS: String = "correct_answers"
    const val TOTAL_SCORE: Int = 0
    // END
    
    fun getQuestions(context: Context): ArrayList<Question> {
        val questionsList = ArrayList<Question>()

        // 1
        val question1: String = context.getString(R.string.question)
        val que1 = Question(
            1, question1,
            R.drawable.kawung,
            "Batik Pala", "Batik Parang",
            "Batik Kawung", "Batik Poleng", 3,100
        )

        questionsList.add(que1)

        // 2
        val que2 = Question(
            2, question1,
            R.drawable.parang,
                "Batik Kawung", "Batik Pala",
                "Batik Poleng", "Batik Parang", 4,100
        )

        questionsList.add(que2)

        // 3
        val que3 = Question(
            3, question1,
            R.drawable.poleng,
                "Batik Megamendung", "Batik Parang",
                "Batik Poleng", "Batik Bali", 3,100
        )

        questionsList.add(que3)

        // 4
        val que4 = Question(
            4, question1,
            R.drawable.pala,
            "Batik Parang", "Batik Pala",
            "Batik Poleng", "Batik Lasem", 2,100
        )

        questionsList.add(que4)

        // 5
        val que5 = Question(
            5, question1,
            R.drawable.lasem,
            "Batik Lasem", "Batik Celup",
            "Batik Poleng", "Batik Betawi", 1, 100
        )

        questionsList.add(que5)

        // 6
        val que6 = Question(
            6, question1,
            R.drawable.ikat_celup,
            "Batik Poleng", "Batik Parang",
            "Batik Bali", "Batik Celup", 4, 100
        )

        questionsList.add(que6)

        // 7
        val que7 = Question(
            7, question1,
            R.drawable.betawi,
            "Batik Betawi", "Batik Poleng",
            "Batik Tambal", "Batik Bali", 1, 100
        )

        questionsList.add(que7)

        // 8
        val que8 = Question(
            8, question1,
            R.drawable.tambal,
            "Batik Parang", "Batik Sekar Jagad",
            "Batik Insang", "Batik Tambal", 4,100
        )

        questionsList.add(que8)

        // 9
        val que9 = Question(
            9, question1,
            R.drawable.insang,
            "Batik Dayak", "Batik Insang",
            "Batik Geblek Renteng", "Batik Cendrawasih", 2,100
        )

        questionsList.add(que9)

        // 10
        val que10 = Question(
            10, question1,
            R.drawable.cendrawasih,
                "Batik Geblek Renteng", "Batik Dayak",
                "Batik Insang", "Batik Cendrawasih", 4,100
        )

        questionsList.add(que10)
        return questionsList
    }
}