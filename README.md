# Key Signature Identifying Neural Network
A neural network that can learn identify the key signature of a given melody.

## How it works

#### For each training session
1. The `Song` class creates a *song* which is nothing more than a random series 
of notes.
2. The song is then preprocessed to output an array containing the number of 
occurrences of each note – C through B.  
  * Examples: 
    * Note C -> `array[0] = 29` means that the note C occurred a total of 29 
    times in the song.
    * Note C# -> `array[1] = 0` means that the note C# did not occur in the song. 
    * Note B -> `array[11] = 3` means that the note B only occurred a total of 
    3 times in the song.
3. The preprocessed song is fed into the neural network in the `NeuralNet` 
class for a single training session. The neural net has 12 input nodes and 12 
output nodes that correspond to the 12 notes in a scale. (It also has 1 layer of 
12 hidden nodes.) Each index in the preprocessed array containing the 
total note counts are given to each corresponding input node. The 12 output 
nodes of the neural net are also set up in this same fashion.
4. The output node with the highest value is the neural net's *guess* as to 
which note is the root note of the scale. Simple post processing is done to find 
the highest assigned value.
5. For back propagation, the network is trained to output a binary answer. By 
this, it means that if the key signature is C, the expected output from the 
network would be `[ 1 0 0 0 0 0 0 0 0 0 0 0 ]` because index 0 maps to C. So 
the net is then given the *correct answer* of `[ 1 0 0 0 0 0 0 0 0 0 0 0 ]` to 
learn on for whenever the answer is C.
6. The network adjusts its weights according to the learning rate in preparation 
for the next training session.
7. Repeat.

## Output options

Unless specified to be verbose, program defaults to using *normal output*.

### Normal output
* Output over 10 thousand iterations of 10 million is displayed to standard out.
* Output contains:
  * The iteration number
  * The percept correct over each 10 thousand iterations

### Verbose output
* Each iteration's output is sent to standard out.
* Output contains:
  * The neural network's guess
  * The correct answer
  * The standard error
  * And a `+` sign if the network was correct
  * [Here](http://i.imgur.com/2QhDqRl.png) is an example of the output after 10 
  million training sessions with a learning rate of 0.18.
  
### End of all training sessions

At the end of all training sessions, the trained neural network will be tested
on all key signatures (C through B) and the results will be sent to standard out.
  
## How to run

Compile and run manually:

`> cd ~/path/to/key-signature-identifying-neural-network-java`

`> javac *java`

`> java Manager [arguments]`

Or run shell script: 

`> ./run.sh [arguments]`

### Optional arguments

* `--help ...... Help`
* `--verbose ... Verbose output`
* `-v .......... Verbose output`

## Notes
* **This project has not been tested on real data. Results from testing with 
real data will come.**
* More command line options to come.
* The default learning rate is `0.18` and the default number of training 
sessions is `10,000,000`. These settings can be changed in `Manager.java`.