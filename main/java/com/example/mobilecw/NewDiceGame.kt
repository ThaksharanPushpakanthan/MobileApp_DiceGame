package com.example.mobilecw

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.app.AlertDialog
import kotlin.random.Random

class NewDiceGame : AppCompatActivity() {

    private lateinit var throwButton: Button
    private lateinit var backButton: Button
    private lateinit var scoreButton: Button
    private lateinit var reRollButton: Button
    private lateinit var scoreTextView: TextView
    private lateinit var playerScoreTextView: TextView
    private lateinit var computerScoreTextView: TextView
    private lateinit var targetScoreEditText: EditText
    private lateinit var playerDiceImages: Array<ImageView>
    private lateinit var computerDiceImages: Array<ImageView>

    private var playerScore = 0
    private var computerScore = 0
    private var playerDiceValue = 0
    private var numRerolls = 0
    private var selectedDice: MutableList<Int> = mutableListOf(0, 0, 0, 0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_game)

        throwButton = findViewById(R.id.throw_button)
        backButton = findViewById(R.id.back_button)
        scoreButton = findViewById(R.id.score_button)
        reRollButton = findViewById(R.id.reroll_button)
        scoreTextView = findViewById(R.id.score_text_view)
        playerScoreTextView = findViewById(R.id.player_score_text_view)
        computerScoreTextView = findViewById(R.id.computer_score_text_view)
        targetScoreEditText = findViewById(R.id.target_score_edit_text)

        //array consisting of dice images for the human player
        playerDiceImages = arrayOf(
            findViewById(R.id.player_dice_1),
            findViewById(R.id.player_dice_2),
            findViewById(R.id.player_dice_3),
            findViewById(R.id.player_dice_4),
            findViewById(R.id.player_dice_5)
        )
        //array consisting of dice images for the computer
        computerDiceImages = arrayOf(
            findViewById(R.id.computer_dice_1),
            findViewById(R.id.computer_dice_2),
            findViewById(R.id.computer_dice_3),
            findViewById(R.id.computer_dice_4),
            findViewById(R.id.computer_dice_5)
        )

        throwButton.setOnClickListener {
            playerThrowDice()
            computerThrowDice()
            throwButton.isEnabled = false
            backButton.isEnabled = false
            reRollButton.isEnabled = true

            for (i in 0 until 5) {
                playerDiceImages[i].setOnClickListener {
                    selectedDice[i] = if (selectedDice[i] == 0) 1 else 0
                }
            }

            for (i in 0 until 5) {
                playerDiceImages[i].setOnClickListener {
                    if (selectedDice[i] == 0) {
                        selectedDice[i] = 1
                        playerDiceImages[i].alpha = 0.5f // dim the selected dice
                    } else {
                        selectedDice[i] = 0
                        playerDiceImages[i].alpha = 1.0f // reset the selected dice alpha
                    }
                }
            }
        }

        scoreButton.setOnClickListener {
            playerScoreTextView.text = "Player Score : $playerScore"
            computerScoreTextView.text = "Computer Score : $computerScore"
            throwButton.isEnabled = true
            reRollButton.isEnabled = false

            var targetScore = targetScoreEditText.text.toString().toIntOrNull()
            if (targetScore == null) {
                targetScore = 101}

            if (playerScore >= targetScore && playerScore>computerScore) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Congratulations!")
                builder.setMessage("You have won!")
                builder.setPositiveButton("CLOSE") { dialog, which -> }
                val dialog = builder.create()
                dialog.show()
                throwButton.isEnabled = false
                scoreButton.isEnabled = false
                reRollButton.isEnabled = false
                backButton.isEnabled = true

            } else if (computerScore >= targetScore && computerScore>playerScore) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Oops")
                builder.setMessage("You have lost!")
                builder.setPositiveButton("CLOSE") { dialog, which ->
                }
                val dialog = builder.create()
                dialog.show()

                throwButton.isEnabled = false
                reRollButton.isEnabled = false
                scoreButton.isEnabled = false
                backButton.isEnabled = true

            //In the case of a tie the code section will be implemented
            } else if (computerScore == playerScore && computerScore >= targetScore) {
                scoreTextView.text = "It's a tie. Lets have one more round to decide the winner"
                playerScore = 0
                computerScore = 0
                throwButton.isEnabled = true
                playerThrowDice()
                computerThrowDice()
                reRollButton.isEnabled = false
                if (playerScore>computerScore){
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Congratulations!")
                    builder.setMessage("You have won!")
                    builder.setPositiveButton("CLOSE") { dialog, which -> }
                    val dialog = builder.create()
                    dialog.show()
                }else if (playerScore<computerScore){
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Oops")
                    builder.setMessage("You have lost!")
                    builder.setPositiveButton("CLOSE") { dialog, which ->
                    }
                    val dialog = builder.create()
                    dialog.show()
                }
            }
        }

        reRollButton.setOnClickListener {
            numRerolls++

            for (i in 0 until 5) {
                if (selectedDice[i] == 0) {
                    rollDice(playerDiceImages[i])      //We check which dices are selected and call the rollDice function to roll the other dices
                }
                playerDiceImages[i].alpha = 1.0f // reset the selected dice alpha
            }

            if (numRerolls == 3) {
                reRollButton.isEnabled = false
                return@setOnClickListener
            }
        }

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)}
    }

    private fun playerThrowDice() {
        for (i in 0 until 5) {
            playerDiceValue =  Random.nextInt(1,6)
            val drawableId = when (playerDiceValue) {
                1 -> R.drawable.dice_1
                2 -> R.drawable.dice_2
                3 -> R.drawable.dice_3
                4 -> R.drawable.dice_4
                5 -> R.drawable.dice_5
                else -> R.drawable.dice_6
            }
            playerDiceImages[i].setImageResource(drawableId)
            playerScore += playerDiceValue
        }
        throwButton.isEnabled = true
        backButton.isEnabled = false
    }

    private fun computerThrowDice() {
        for (j in 0 until 5) {
            val computerDiceValue =  Random.nextInt(1,6)
            val drawableId = when (computerDiceValue) {
                1 -> R.drawable.dice_1
                2 -> R.drawable.dice_2
                3 -> R.drawable.dice_3
                4 -> R.drawable.dice_4
                5 -> R.drawable.dice_5
                else -> R.drawable.dice_6
            }
            computerDiceImages[j].setImageResource(drawableId)
            computerScore += computerDiceValue
        }
        throwButton.isEnabled = true
        backButton.isEnabled = false
    }

    private fun rollDice(imageView: ImageView) {
        playerScore -= playerDiceValue
        val randomNumber = (1..6).random()
        val drawableResource = when (randomNumber) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        imageView.setImageResource(drawableResource)
        playerScore += randomNumber
        numRerolls = 0
    }
}
