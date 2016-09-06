# Chess
The evaluations can either be trivial (on rough centrality or material cost), or trained with a neural network. 
'Train.java' reads in data in .pgn format. In particular from http://www.ficsgames.org/download.html. 
Currently one player games can be played with the trivial heuristic. 
However, check/checkmate along with castling and En Passant is still incomplete.

# Dependencies
Java: -JBLAS Linear Algebra Library	

# Notes
When running, the java classpaths must be edited to its current directory, along with the jar of the dependency(ies).

#Meta
This is the initial version. The following versions updated with GA training options are defunct and not uploaded yet. Attempt #1 ANN.
'crap code high school. java v.'
