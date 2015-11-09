# Key Signature Identifying Neural Network in Java
A neural network that can learn identify the key signature of a given melody.

## How to run it

Simply run the `MusicTheoryNets.java` main.

## How it works

#### For each training session
1. The `Song` class creates a *song* which is nothing more than a random series of notes.
2. The song is then preprocessed in the `NetsPreprocessing` class to output an array
containing the number of occurrences of each note – C through B.  
  * Examples: 
    * Note C -> `array[0] = 29` means that the note C occurred a total of 29 times in the song.
    * Note C# -> `array[1] = 0` means that the note C# did not occur in the song. 
    * Note B -> `array[11] = 3` means that the note B only occured a total of 3 times in the song.
3. The preprocessed song is fed into the neural network in the `MusicTheoryNets` class for a single training session. The neural net has 12 input nodes and 12 output nodes that correspond to the 12 notes in a scale. (It also has 1 layer of 12 hidden nodes.) Each index in the preprocessed array containing the total note counts are given to each corresponding
input node. The 12 output nodes of the neural net are also set up in this same fashion.
4. The output node with the highest value is the neural net's *guess* as to which note is the root note of the scale.
Simple post processing is done to find the highest assigned value.
5. For back propogation, the network is trainied to output a binary answer. By this, it means that if the key signature is
C, the expected output from the network would be `[ 1 0 0 0 0 0 0 0 0 0 0 0 ]` because index 0 maps to C. So the net 
is then given the *correct answer* of `[ 1 0 0 0 0 0 0 0 0 0 0 0 ]` to learn on for whenever the answer is C.
6. The network adjusts its weights according to the learning rate in preperation for the next training session.
7. Repeat.

## Output
* Output is sent to standard out.
* Output contains:
  * Neural network's guess
  * The correct answer
  * The standard error
  * A `+` sign if the network was correct

## Notes
* **This project has not been tested on real data. Results from testing with real data will come soon.**
* The default learning rate is `.18` and the default number of training sessions is `10,000,000`.  
This can be changed in the global variables in `MusicTheoryNets.java`.
