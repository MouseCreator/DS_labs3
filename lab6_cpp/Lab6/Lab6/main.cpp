#include "fox.h"
#include "tape.h"
//https://github.com/microsoft/Microsoft-MPI/blob/master/src/include/mpi.h

void main(int argc, char* argv[]) {
	Tape::runTapeMultiplication(argc, argv);
}