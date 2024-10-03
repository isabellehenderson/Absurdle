# Absurdle

Here's a README and a brief description for the Absurdle project:

## Absurdle

Absurdle is a variant of the popular word-guessing game Wordle, implemented in Java. Unlike traditional Wordle, Absurdle dynamically adjusts its target word based on the player's guesses, making it more challenging and unpredictable.

### Description

Absurdle is a Java implementation of an adversarial word-guessing game that utilizes a dictionary-based approach to dynamically prune potential target words. The game employs a strategy of maintaining the largest possible set of candidate words after each guess, maximizing the difficulty for the player to deduce the final target word[1].

### Features

- Customizable dictionary input
- Adjustable word length
- Dynamic target word selection
- Emoji-based feedback (🟩, 🟨, ⬜)
- Infinite guesses allowed

### How to Play

1. Run the `Absurdle` class
2. Choose a dictionary file
3. Specify the desired word length
4. Start guessing words
5. Receive feedback after each guess
6. Continue until you guess the target word

### Code Structure

The `Absurdle` class contains the following key methods:

- `main`: Handles game setup and user interaction
- `pruneDictionary`: Filters the dictionary based on word length
- `record`: Processes each guess and updates the word set
- `patternFor`: Generates the feedback pattern for a guess
- `markGreen`, `markYellow`, `markGray`: Helper methods for pattern generation

### Requirements

- Java Development Kit (JDK)
- A text file containing dictionary words

### How to Run in Terminal

1. Compile the Java file:
   ```
   javac Absurdle.java
   ```
2. Run the compiled class:
   ```
   java Absurdle
   ```
3. Follow the on-screen prompts to play the game

Enjoy playing Absurdle!

Citations:
[1] https://ppl-ai-file-upload.s3.amazonaws.com/web/direct-files/32303081/63dbc45c-cb8b-4663-a9e8-5b9fef711e32/Absurdle.java