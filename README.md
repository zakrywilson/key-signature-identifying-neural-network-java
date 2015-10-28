# key-signature-identifying-neural-network-java
A neural network that can identify the key signature of any melody.

To run this project, simply run `MusicTheoryNets.java`.

## How it works

* For each training session...
* The `Song` class creates a *song* which is nothing more than a random series of notes.
* The *song* is then preprocessed in the `NetsPreprocessing` class to output an array
containing the number of occurrences of each note C through B: e.g., note C -> `array[0] = 11` occurrences,
note C# -> `array[1] = 0` occurrences, etc. 
* The preprocessed song is fed into the neural network in the `MusicTheoryNets` class for a single training session.

###### Note: This project has not been tested on real data. Results from testing with real data will come soon.
