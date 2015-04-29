# factorization_algorithm
Java implementation of algorithm for Factorization of Birational Maps

This program creates a visual representation of the algorithm for finding the common refinement of two sequences of subdivisions of a 3-dimensional cone. One of these sequences is of length 1, and in order to avoid trivial subdivisions, both initial subdivisions are fixed.

This program takes the remainder (all but the first) of the longer sequence of subdivisions as input, either selecting them graphically via an interactive pane, or through a text based description of the generating sets. Any input can be exported and saved as a text based description. Controls are also available for adding and removing subdivisions from the sequence during execution.

The program then creates a visual representation of the grid that produces the common refinement, color coded based on how the refinement of the two subdivisions was used. If the two subdivisions were equal, no divisions are needed to commute them, and the area inside the square is green. If the two subdivision were in the same 3 dimensional cone, then the square is red. Otherwise the square is blue in the representation. This visualization can also be saved as an image.

This representation is interactive as well, with any click opening up a window with a 2D representation of the fan at the nearest point in the grid. A few statistics accompany the image, which can also be saved. Zoom controls are also available on the main panel, which can be scrolled if necessary.

The program can also be run with an argument, which must be a path to a text file. The first line of the text file must be one of three command strings: "given", "exhaustive", or "random." If the command is "given," the following lines must give the 2-dimensional cones to divide on, in the same format as used before. The output is text file with a summary of a few different statistics about the algorithm on the sequence specified.

If the command is "exhaustive", the following line must contain a single number. The output is then a text file with a summary of the same statistics about the algorithm. These statistics are the maximum over all sequences with length equal to the given number.

Finally, if the command is "random", the following line must also contain a single number. The output is then a text file with a summary of the same statistics about the algorithm when run on a  fan divided on a random sequence of length equal to the given number.

To run this program, simply download the .jar file in this repository to wherever you choose. Then, in your system's command line, navigate to the directory containing the program. If you have Java 6 or higher installed (check by typing "java --version"), the program can be run with the command "java Factorization\_Algorithm.jar". The path to the input file, if any, is given to the program following the previous command. Note that any files created by this program will save into the current directory.
