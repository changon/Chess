# Chess
Metis utilizes the minimax algorithm (along with pruning) to get evaluations from the search tree. 
The evaluations can either be trivial (on rough centrality or material cost), or trained with a neural network. 
'Train.java' reads in data in .pgn format. In particular from http://www.ficsgames.org/download.html. 
Currently one player games can be played with the trivial heuristic. 
However, check/checkmate along with castling is still incomplete.

# Dependencies
Java: -JBLAS Linear Algebra Library	

# Notes
When running, the java classpaths must be edited to its current directory, along with the jar of the dependency(ies).	
